package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import frame.ChatRecordFrame;
import frame.LaterLoadFrame;
import frame.PasswordFrame;

import panel.LaterLoadPanel;
import pub.PackType;
import pub.QQPackage;
import tool.StaticTool;

public class LaterListener implements ActionListener {
	LaterLoadPanel laterLoadPanel;
	LaterLoadFrame laterLoadFrame;
	private PasswordFrame passwordFrame;

	public LaterListener(LaterLoadPanel laterLoadPanel,
			LaterLoadFrame laterLoadFrame) {
		this.laterLoadPanel = laterLoadPanel;
		this.laterLoadFrame = laterLoadFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == laterLoadPanel.getButton()) {
			passwordFrame = new PasswordFrame(this);
		} else if (e.getSource() == laterLoadPanel.getButton1()) {

			// �����¼
			new ChatRecordFrame(laterLoadFrame);

		} else if (e.getSource() == laterLoadPanel.getButton2()) {

			// ������Ϣ
			String inputMessage = laterLoadPanel.getTextArea2().getText();
			int length = inputMessage.trim().length();
			if (laterLoadPanel.getTextArea2().getText() == null
					|| "".equals(laterLoadPanel.getTextArea2().getText())) {
				JOptionPane.showMessageDialog(
						laterLoadPanel.getLaterLoadFrame(), "���ܷ��Ϳ���Ϣ!");
				return;
			}
			if (laterLoadPanel.getName().equals(
					laterLoadPanel.getLaterLoadFrame().getTitle())) {
				JOptionPane.showMessageDialog(
						laterLoadPanel.getLaterLoadFrame(), "���ܸ��Լ�����Ϣ!");
				return;
			}
			if (length > 200) {
				JOptionPane.showMessageDialog(
						laterLoadPanel.getLaterLoadFrame(), "��Ϣ���Ȳ��ܳ���200���ַ�!");
				return;
			}
			try {
				ObjectOutputStream objectOutputStream = laterLoadPanel
						.getLaterLoadFrame().getObjectOutputStream();
				QQPackage qqPackage = new QQPackage();
				qqPackage
						.setFrom(laterLoadPanel.getLaterLoadFrame().getTitle());
				qqPackage.setTo(laterLoadPanel.getName());
				if (laterLoadPanel.getName().equals("������")) {
					qqPackage.setPackType(PackType.publicChat);
					String message = "��  " + StaticTool.SystemTime()
							+ "  �� ������ ˵:" + "\n"
							+ laterLoadPanel.getTextArea2().getText() + "\n";
					laterLoadPanel.getTextArea1().append(message);
					laterLoadPanel.writeLog(message, laterLoadPanel
							.getLaterLoadFrame().getTitle().substring(0, 5));

				} else {
					qqPackage.setPackType(PackType.privateChat);
					String message = "��  " + StaticTool.SystemTime() + "  �� "
							+ laterLoadPanel.getName().substring(6) + " ˵:"
							+ "\n" + laterLoadPanel.getTextArea2().getText()
							+ "\n";
					laterLoadPanel.getTextArea1().append(message);
					laterLoadPanel.writeLog(message, laterLoadPanel
							.getLaterLoadFrame().getTitle().substring(0, 5));
				}
				qqPackage.setData(laterLoadPanel.getTextArea2().getText());
				objectOutputStream.writeObject(qqPackage);
				objectOutputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			laterLoadPanel.getTextArea2().setText("");

		} else if (e.getSource() == laterLoadPanel.getButton3()) {

			// �ر�
			int i = JOptionPane.showConfirmDialog(
					laterLoadPanel.getLaterLoadFrame(), "ȷ��Ҫ�˳�QQ��");
			if (i == 0) {
				QQPackage qqPackage = new QQPackage();
				qqPackage.setPackType(PackType.userDownLine);
				qqPackage
						.setFrom(laterLoadPanel.getLaterLoadFrame().getTitle());
				try {
					ObjectOutputStream objectOutputStream = laterLoadPanel
							.getLaterLoadFrame().getObjectOutputStream();
					objectOutputStream.writeObject(qqPackage);
					objectOutputStream.flush();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	public PasswordFrame getPasswordFrame() {
		return passwordFrame;
	}

	public LaterLoadPanel getLaterLoadPanel() {
		return laterLoadPanel;
	}

	public LaterLoadFrame getLaterLoadFrame() {
		return laterLoadFrame;
	}

}
