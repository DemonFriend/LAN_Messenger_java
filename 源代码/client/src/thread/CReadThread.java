package thread;

import java.io.ObjectInputStream;
import java.util.Vector;

import javax.swing.JOptionPane;

import pub.PackType;
import pub.QQPackage;
import tool.StaticTool;

import frame.LaterLoadFrame;

public class CReadThread extends Thread {
	ObjectInputStream inputStream;
	LaterLoadFrame clientFrame;

	public CReadThread(ObjectInputStream inputStream, LaterLoadFrame clientFrame) {
		this.clientFrame = clientFrame;
		this.inputStream = inputStream;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (!this.isInterrupted()) {

			try {
				QQPackage qqPackage = (QQPackage) clientFrame
						.getObjectInputStream().readObject();
				// �����û��б�ˢ��
				if (qqPackage.getPackType().equals(PackType.onlineuser)) {
					clientFrame.getLaterLoadPanel().refresh(
							(Vector<String>) qqPackage.getData());
				} else if (qqPackage.getPackType().equals(PackType.privateChat)) {
					// ˽��
					String message = qqPackage.getFrom().substring(6) + "  "
							+ StaticTool.SystemTime() + "  �� �� ˵��" + "\n"
							+ (String) qqPackage.getData() + "\n";
					clientFrame.getLaterLoadPanel().getTextArea1()
							.append(message);
					clientFrame.getLaterLoadPanel().writeLog(message,
							clientFrame.getTitle().substring(0, 5));
				} else if (qqPackage.getPackType().equals(PackType.publicChat)) {
					// Ⱥ��
					String messageAll = qqPackage.getFrom().substring(6) + "  "
							+ StaticTool.SystemTime() + "  �� ������ ˵��" + "\n"
							+ (String) qqPackage.getData() + "\n";
					clientFrame.getLaterLoadPanel().getTextArea1()
							.append(messageAll);
					clientFrame.getLaterLoadPanel().writeLog(messageAll,
							clientFrame.getTitle().substring(0, 5));
				} else if (qqPackage.getPackType().equals(
						PackType.updatePassword)) {
					// �޸�����
					String string = (String) qqPackage.getData();
					if (string.equals("�������")) {
						JOptionPane.showMessageDialog(clientFrame
								.getLaterLoadPanel().getLaterListener()
								.getPasswordFrame(), "ԭ�������");
					} else if (string.equals("�����޸ĳɹ�")) {
						JOptionPane.showMessageDialog(clientFrame
								.getLaterLoadPanel().getLaterListener()
								.getPasswordFrame(), "������ĳɹ���");
						clientFrame.getLaterLoadPanel().getLaterListener()
								.getPasswordFrame().dispose();
					}
				} else if (qqPackage.getPackType().equals(PackType.post)) {
					// ����
					String notice = (String) qqPackage.getData();
					clientFrame.getLaterLoadPanel().getTextArea3()
							.setText(notice);

				} else if (qqPackage.getPackType().equals(PackType.stopServer)) {
					// �������ر�
					interrupt();
					JOptionPane.showMessageDialog(clientFrame, "�������ر�");
					System.exit(0);
					clientFrame.dispose();
					break;
				} else if (qqPackage.getPackType().equals(PackType.enforceDown)) {
					// ������ǿ������
					interrupt();
					JOptionPane.showMessageDialog(clientFrame, "������ǿ��������");
					System.exit(0);
					clientFrame.dispose();
					break;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(clientFrame, "������������ж�");
				System.exit(0);
				break;
			}
		}
	}
}
