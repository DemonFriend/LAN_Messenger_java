package listener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.imf.Userimp;

import panel.ServerPanel;
import panel.UserPanel;
import pub.PackType;
import pub.QQPackage;
import thread.AcceptThread;
import thread.SReadThread;
import tool.StaticTool;
import frame.ServerFrame;

public class ServerListener implements ActionListener {
	ServerFrame serverFrame;
	ServerPanel serverPanel;
	private AcceptThread acceptThread;
	private JTable jTable;
	private JPanel panel;
	private String uId;
	private JScrollPane jScrollPane;
	private ServerSocket serverSocket;
	private SReadThread serverReadClientThread;
	private String uName;
	private static Userimp userimp = new Userimp();
	private static DefaultTableModel defaultTableModel = new DefaultTableModel();

	public ServerListener(ServerPanel serverPanel, ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
		this.serverPanel = serverPanel;
	}

	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == serverPanel.getButtonStart()) {
			// 1��������������

			// a������serverSocket��

			try {
				serverSocket = new ServerSocket(8088);

				// f������acceptThread,�������߳�
				acceptThread = new AcceptThread(serverSocket, serverFrame);
				acceptThread.start();

			} catch (IOException e1) {
				System.out.println("�˿ںű�ռ��");
			}

			// b���İ�ť�ȿؼ��Ƿ����
			serverPanel.getButtonStart().setEnabled(false);
			serverPanel.getButtonStop().setEnabled(true);
			serverPanel.getButtonSend().setEnabled(true);
			// serverPanel.getButtonForce().setEnabled(true);

			// c��д��־
			serverFrame.getLogPanel().writeLog("�����������ɹ�");
			// d��дͨѶ��Ϣ��ʾ
			serverPanel.getTextArea4().append(
					"��" + StaticTool.SystemTime() + "�������������ɹ�\n");

			// e����ͼƬ
			serverPanel.getLabel().setIcon(StaticTool.startIcon);

			serverPanel.getPanel2().add(centerpanel());

			// ��ѡ������Ӽ���
			jTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
							if (e.getValueIsAdjusting()) {

								if (jTable.getSelectedRow() != -1) {

									uId = (String) jTable.getValueAt(
											jTable.getSelectedRow(), 0);
									uName = (String) jTable.getValueAt(
											jTable.getSelectedRow(), 1);
									serverPanel.getButtonForce().setEnabled(
											true);
								} else {
									serverPanel.getButtonForce().setEnabled(
											false);
								}
							}
						}
					});

		} else if (e.getSource() == serverPanel.getButtonStop()) {
			// ֹͣ��������
			// a��д��־
			serverFrame.getLogPanel().writeLog("�رշ������ɹ�");
			// b��дͨѶ��Ϣ��ʾ
			serverPanel.getTextArea4().append(
					"��" + StaticTool.SystemTime() + "����������ֹͣ\n");

			// c���޸�ͼƬ
			serverPanel.getLabel().setIcon(StaticTool.imageIcon);

			// d�����ð�ť������������
			serverPanel.getButtonStart().setEnabled(true);
			serverPanel.getButtonStop().setEnabled(false);
			serverPanel.getButtonSend().setEnabled(false);
			serverPanel.getButtonForce().setEnabled(false);

			// e���޸������û�Ϊ����״̬����ˢ�·���������������߱��

			userimp.isonline(null);
			serverPanel.getPanel2().removeAll();
			//

			// ע��ˢ�·��������ͬʱҪˢ���û��������

			serverFrame.getUserPanel().refresh(null, null);
			// f�������������û�����ֹͣ��������

			Vector<String> onlineName = new Vector<String>();
			onlineName.removeAllElements();

			Collection<SReadThread> collection2 = serverFrame
					.getReadThreadMap().values();
			QQPackage package1 = new QQPackage();
			package1.setPackType(PackType.stopServer);
			package1.setData(onlineName);
			for (SReadThread serverReadClientThread2 : collection2) {
				try {
					serverReadClientThread2.getObjectOutputStream()
							.writeObject(package1);
					serverReadClientThread2.getObjectOutputStream().flush();
					serverReadClientThread2.interrupt();

				} catch (IOException e1) {
					System.out.println("�������ѹر�");
				}

			}
			// g�����hashtable

			serverFrame.getReadThreadMap().clear();
			// h���ж��߳�

			// i���ر�serverSocket

			try {
				serverSocket.close();
			} catch (IOException e1) {
				System.out.println("�����ѹر�");
			}

		} else if (e.getSource() == serverPanel.getButtonForce()) {

			int isDelete = JOptionPane.showConfirmDialog(serverFrame, "ȷ��ǿ��"
					+ uId + "�û�������", "ǿ������", 0);

			if (isDelete == 0) {
				userimp.isonline(uId);
				sRefresh(null, null, "����");
				UserPanel.refresh(null, null);
				JOptionPane.showMessageDialog(serverFrame, "ǿ�����߳ɹ�!");

				// a��д��־
				serverFrame.getLogPanel().writeLog(
						uName + "(" + uId + ")" + "��ǿ������!");
				// b��дͨѶ��Ϣ��ʾ
				serverPanel.getTextArea4().append(
						"��" + StaticTool.SystemTime() + "��" + uId + "��ǿ������!\n");

				serverReadClientThread = serverFrame.getReadThreadMap().get(
						uId + " " + uName);
				serverFrame.getReadThreadMap().remove(uId + " " + uName);

				QQPackage package1 = new QQPackage();
				package1.setPackType(PackType.enforceDown);

				try {
					serverReadClientThread.getObjectOutputStream().writeObject(
							package1);
					serverReadClientThread.getObjectOutputStream().flush();
				} catch (IOException e1) {
					System.out.println("socket closed");
				}

				serverReadClientThread.interrupt();
				try {
					serverReadClientThread.getSocket().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				Collection<String> collection = serverFrame.getReadThreadMap()
						.keySet();
				Vector<String> onlineName = new Vector<String>();
				onlineName.removeAllElements();
				for (String string : collection) {
					onlineName.add(string);
				}
				Collection<SReadThread> collection2 = serverFrame
						.getReadThreadMap().values();
				QQPackage package2 = new QQPackage();
				package2.setPackType(PackType.onlineuser);
				package2.setData(onlineName);
				for (SReadThread serverReadClientThread2 : collection2) {
					try {
						serverReadClientThread2.getObjectOutputStream()
								.writeObject(package2);
						serverReadClientThread2.getObjectOutputStream().flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			}

		} else if (e.getSource() == serverPanel.getButtonSend()) {

			// ���͹���
			String message = serverPanel.getTextArea5().getText();
			QQPackage qqPackage = new QQPackage();
			qqPackage.setPackType(PackType.post);
			qqPackage.setData(message);

			Collection<SReadThread> collection = serverFrame.getReadThreadMap()
					.values();
			for (SReadThread sReadThread : collection) {
				try {

					ObjectOutputStream objectOutputStream = sReadThread
							.getObjectOutputStream();
					objectOutputStream.writeObject(qqPackage);

					objectOutputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			serverPanel.getTextArea5().setText("");
			JOptionPane.showMessageDialog(null, "�ѷ��͹���ɹ���");

		}

	}

	private JPanel centerpanel() {
		// ��ʾ�����û��б�
		panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(484, 180));

		// 3��������
		defaultTableModel.setDataVector(userimp.selectUser(null, null, "����"),
				userimp.head());

		// 4����ģ��
		jTable = new JTable(defaultTableModel) {

			private static final long serialVersionUID = 3008651421845101749L;

			// ���ñ�������ݲ��ɱ༭
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};

		// ���ñ�ͷ�����϶�
		jTable.getTableHeader().setReorderingAllowed(false);
		// �����е�ѡ
		jTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		// Ҫ��ʾ��ͷһ��Ҫ�ѱ����뵽�������
		jScrollPane = new JScrollPane(jTable);
		panel.add(jScrollPane);
		return panel;

	}

	/**
	 * ˢ�±��
	 */
	public static void sRefresh(String sId, String sName, String sIsOnline) {

		Vector<Vector<String>> vecData = userimp.selectUser(sId, sName,
				sIsOnline);
		defaultTableModel.setDataVector(vecData, userimp.head());
		defaultTableModel.fireTableDataChanged();

	}

}
