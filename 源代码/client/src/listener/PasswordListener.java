package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import panel.PasswordPanel;
import pub.PackType;
import pub.QQPackage;

import frame.PasswordFrame;

public class PasswordListener implements ActionListener {
	private PasswordFrame passwordFrame;
	private PasswordPanel passwordPanel;

	public PasswordListener(PasswordFrame passwordFrame,
			PasswordPanel passwordPanel) {
		this.passwordFrame = passwordFrame;
		this.passwordPanel = passwordPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// �޸����룺
		// a����֤�����ʽ�Ƿ���ȷ
		// b����֤�����������Ƿ���ͬ
		// c�������޸���������������ͣ��û��������룬�����룩
		//
		// 2���˳���¼
		// a����ʾ�Ƿ�
		// b�������û����߰����������ͣ��û���
		// c���ر�io��
		// d���ر�socket
		// e���˳�ϵͳ
		String oldPsw = String.valueOf(passwordPanel.getTextField1()
				.getPassword());
		String newPsw = String.valueOf(passwordPanel.getTextField2()
				.getPassword());
		String affirmPsw = String.valueOf(passwordPanel.getTextField3()
				.getPassword());
		if (e.getSource() == passwordPanel.getButton1()) {
			Boolean rightOrNot1 = oldPsw(oldPsw);
			if (rightOrNot1 == false) {
				passwordPanel.getTextField1().setText("");
				JOptionPane.showMessageDialog(passwordPanel,
						"���벻��Ϊ�գ��ҳ����� 3~16 ֮�� ��ֻ���������֡���ĸ �������»���");
				return;
			}
			Boolean rightOrNot2 = newPsw(newPsw);
			if (rightOrNot2 == false) {
				passwordPanel.getTextField2().setText("");
				JOptionPane.showMessageDialog(passwordFrame,
						"���벻��Ϊ�գ��ҳ����� 3~16 ֮�� ��ֻ���������֡���ĸ �������»���");
				return;
			}
			Boolean rightOrNot3 = affirmPsw(affirmPsw);
			if (rightOrNot3 == false) {
				passwordPanel.getTextField3().setText("");
				JOptionPane.showMessageDialog(passwordFrame,
						"���벻��Ϊ�գ��ҳ����� 3~16 ֮�� ��ֻ���������֡���ĸ �������»���");
				return;
			}
			if (!newPsw.equals(affirmPsw)) {
				JOptionPane.showMessageDialog(passwordFrame, "�������ȷ�����벻һ��");
			} else if (newPsw.equals(affirmPsw)) {
				try {
					ObjectOutputStream objectOutputStream = passwordFrame
							.getLaterListener().getLaterLoadFrame()
							.getObjectOutputStream();
					QQPackage qqPackage = new QQPackage();
					qqPackage.setPackType(PackType.updatePassword);
					qqPackage.setFrom(passwordFrame.getLaterListener()
							.getLaterLoadFrame().getTitle().substring(0, 5));
					String[] password = { oldPsw, newPsw };
					qqPackage.setData(password);
					objectOutputStream.writeObject(qqPackage);
					objectOutputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		} else if (e.getSource() == passwordPanel.getButton2()) {
			passwordFrame.dispose();
		}
	}

	private boolean oldPsw(String oldPsw) {
		Pattern pattern = Pattern.compile("^([0-9]|[a-zA-Z]|_){3,16}$");
		Matcher matcher = pattern.matcher(oldPsw);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	private boolean newPsw(String newPsw) {
		Pattern pattern = Pattern.compile("^([0-9]|[a-zA-Z]|_){3,16}$");
		Matcher matcher = pattern.matcher(newPsw);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	private boolean affirmPsw(String affirmPsw) {
		Pattern pattern = Pattern.compile("^([0-9]|[a-zA-Z]|_){3,16}$");
		Matcher matcher = pattern.matcher(affirmPsw);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

}
