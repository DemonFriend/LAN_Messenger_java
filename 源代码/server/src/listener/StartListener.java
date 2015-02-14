package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import panel.StartPanel;

import frame.ServerFrame;

public class StartListener implements ActionListener {
	StartPanel startPanel;
	private Connection connection;

	public StartListener(StartPanel startPanel) {
		this.startPanel = startPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == startPanel.getButton1()) {
			// ����ֵ

			// �ӽ����ı���ֱ��ȡ���ݿ����������Ϣ
			String cUrl = startPanel.getTextField2().getText();
			String cUser = startPanel.getTextField3().getText();
			String cPassword = String.valueOf(startPanel.getTextField4()
					.getPassword());
			String cDriver = startPanel.getTextField5().getText();
			String cPort = startPanel.getTextField6().getText();

			/**
			 * ����url���û��������롢���ݿ����������� �������ݿ������Ƿ���������������Ӧ��ʾ������ʧ����ʾ�������ã����²��ԣ� ��
			 * */
			try {
				Class.forName(cDriver);
			} catch (ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(startPanel.getStartFrame(),
						"��������ʧ�ܣ���������������������");
				return;
			}
			try {
				connection = DriverManager
						.getConnection(cUrl, cUser, cPassword);
			} catch (SQLException e2) {
				JOptionPane.showMessageDialog(startPanel.getStartFrame(),
						"url���û����������������������ò�����");
				return;
			}

			// ������ݿ������������ӽ���˿ںſ��ö˿ںţ����Զ˿ں��Ƿ�ռ�ã���ʾ�����Ƿ�ɹ������ɹ�˵���˿ںű�ռ�ã���ʾ�û��޸Ķ˿ںţ����²���

			try {

				String regex = "^[0-9]{4,5}$";
				if (cPort.matches(regex)) {

					int port = Integer.parseInt(cPort);
					if (1024 >= port || port >= 65535) {
						System.out.println(port);
						JOptionPane.showMessageDialog(
								startPanel.getStartFrame(),
								"�˿ںŸ�ʽ�������޸Ķ˿ںŲ����²���");

						return;
					}
				} else {
					JOptionPane.showMessageDialog(startPanel.getStartFrame(),
							"�˿ںŸ�ʽ�������޸Ķ˿ںŲ����²���");
					return;
				}

				new ServerSocket(Integer.parseInt(cPort));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(startPanel.getStartFrame(),
						"�˿ںű�ռ�ã����޸Ķ˿ںŲ����²���");
				return;
			}

			// �����ݿ�Ͷ˿ڶ�����ͨ���ˣ���������Ϣ����������ļ���ʾ�����ɹ����
			startPanel.getConfig().setValue("����url", cUrl);
			startPanel.getConfig().setValue("�û���", cUser);
			startPanel.getConfig().setValue("����", cPassword);
			startPanel.getConfig().setValue("����", cDriver);
			startPanel.getConfig().setValue("�˿ں�", cPort);
			// ����
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			startPanel.getConfig().save();
			JOptionPane
					.showMessageDialog(startPanel.getStartFrame(), "���Բ�����ɹ�");

			// �����������ť����ʹ�ã������Ե����
			startPanel.getButton2().setEnabled(true);

		} else if (e.getSource() == startPanel.getButton2()) {
			startPanel.getStartFrame().dispose();
			new ServerFrame();
		}

	}

}
