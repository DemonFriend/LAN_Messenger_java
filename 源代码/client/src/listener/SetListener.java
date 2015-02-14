package listener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import frame.LaterLoadFrame;
import frame.LoadFrame;

import panel.LoadPanel;
import pub.PackType;
import pub.QQPackage;
import thread.CReadThread;
import tool.CheckData;

public class SetListener implements ActionListener {
	LoadFrame loadFrame;
	LoadPanel loadPanel;
	LaterLoadFrame laterLoadFrame;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private Socket socket;
	private int j;
	private int i;

	public SetListener(LoadFrame loadFrame, LoadPanel loadPanel) {
		this.loadFrame = loadFrame;
		this.loadPanel = loadPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadPanel.getButton1()) {
			if (i == 0) {
				loadFrame.setSize(new Dimension(330, 340));
				i = 1;
			} else if (i == 1) {
				loadFrame.setSize(new Dimension(330, 250));
				i = 0;
			}
		} else if (e.getSource() == loadPanel.getButton2()) {

			// a����֤ip��port��userid��pwd�Ƿ�ϸ�

			String id = loadPanel.getTextField().getText();
			String ip = loadPanel.getTextField3().getText();
			String port = loadPanel.getTextField4().getText();
			String pass = String.valueOf(loadPanel.getTextField2()
					.getPassword());
			CheckData.isIp(ip);
			CheckData.isPort(port);

			if (loadPanel.getTextField().getText() == null
					|| loadPanel.getTextField().getText() == "") {
				JOptionPane.showMessageDialog(loadFrame, "�ʺŲ���Ϊ��");
				return;
			} else if (!CheckData.checkPassword(pass)) {
				JOptionPane.showMessageDialog(loadFrame,
						"��������볤������ 3~16 ֮��(ֻ���������֣���ĸ , _) ");
				return;
			} else if (!CheckData.isIp(ip)) {
				JOptionPane.showMessageDialog(loadFrame, "ip��ʽ����ȷ");

				return;
			} else if (!CheckData.isPort(port)) {
				JOptionPane.showMessageDialog(loadFrame, "�˿ںŸ�ʽ����ȷ");
				return;
			} else {
				// b����֤�ϸ������ӷ����������͵�¼�������
				
				try {
					socket = new Socket(ip, Integer.parseInt(port));
					objectOutputStream = new ObjectOutputStream(
							socket.getOutputStream());
					QQPackage qqPackage = new QQPackage();

					qqPackage.setPackType(PackType.loginApply);
					qqPackage.setFrom(id);
					qqPackage.setData(pass);
					objectOutputStream.writeObject(qqPackage);
					objectOutputStream.flush();

					/**
					 * ������֤�ɹ����
					 */

					/**
					 * ���յ���½ʧ�ܰ�ʱ��������Ӧ����ʾ�������Ϲر� socket��oos��ois(��¼ʧ�ܴ���3������
					 * ��½���棬�˳�Ӧ�ó���)
					 */

					objectInputStream = new ObjectInputStream(
							socket.getInputStream());
					QQPackage qqPackage2 = (QQPackage) objectInputStream
							.readObject();
					if (qqPackage2.getPackType().equals(PackType.loginSuccess)) {
						loadPanel.getLoadMainFrame().dispose();
						laterLoadFrame = new LaterLoadFrame(socket,
								objectOutputStream, objectInputStream);
						laterLoadFrame.setTitle((String) qqPackage2.getData());

					} else {
						j++;
						if (j == 3) {
							loadPanel.getLoadMainFrame().dispose();
							JOptionPane
									.showMessageDialog(
											loadPanel.getLoadMainFrame(),
											"���β��ɹ�������������");
							return;
						}
						JOptionPane.showMessageDialog(
								loadPanel.getLoadMainFrame(), "���벻�ɹ�");
						try {
							socket.close();
							return;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(loadPanel.getLoadMainFrame(),
							"����ʧ�ܣ���ȷ�Ϸ�������ַ�Ͷ˿ں��Ƿ���ȷ��");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(loadPanel.getLoadMainFrame(),
							"��������ʧ�ܣ�����ͷ���ϵ!");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				/**
				 * ���յ���½�ɹ���ʱ�����ٵ�½���棬�����������(�ڹ��� ����
				 * �����socket��oos��ois���ݹ�ȥ)��ͬʱ�����ͻ��˶�ȡ���� �߳�
				 */
				// ������Ϣ
				CReadThread readThread = new CReadThread(objectInputStream,
						laterLoadFrame);
				readThread.start();
			}

		}
	}

}
