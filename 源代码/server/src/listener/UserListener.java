package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import panel.UserPanel;

import frame.AddUserDialog;
import frame.ServerFrame;
import frame.UpdateUserDialog;

public class UserListener implements ActionListener {
	ServerFrame serverFrame;
	UserPanel userPanel;

	public UserListener(ServerFrame serverFrame, UserPanel panel) {
		this.userPanel = panel;
		this.serverFrame = serverFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == userPanel.getButton()) {
			userPanel.setSid(userPanel.getTextFieldNum().getText());
			userPanel.setSname(userPanel.getTextFieldName().getText());

			UserPanel.refresh(userPanel.getSid(), userPanel.getSname());
			userPanel.getjLabelCount().setText("�ܼ�¼����" + UserPanel.getCounts());
		} else if (e.getSource() == userPanel.getButtonAdd()) {
			new AddUserDialog(serverFrame);
			userPanel.getjLabelCount().setText("�ܼ�¼����" + UserPanel.getCounts());
		} else if (e.getSource() == userPanel.getButtonDelete()) {
			String dId = (String) userPanel.getjTable().getValueAt(
					userPanel.getjTable().getSelectedRow(), 0);

			int isDelete = JOptionPane.showConfirmDialog(serverFrame, "ȷ��ɾ��"
					+ dId + "�û���", "ɾ���û�", 0);
			if (isDelete == 0) {
				UserPanel.getUserimp().delUser(dId);
				UserPanel.refresh(null, null);
				userPanel.getjLabelCount().setText(
						"�ܼ�¼����" + UserPanel.getCounts());
			}
		} else if (e.getSource() == userPanel.getButtonAlter()) {
			new UpdateUserDialog(serverFrame);
		} else if (e.getSource() == userPanel.getButtonClear()) {
			String cId = (String) userPanel.getjTable().getValueAt(
					userPanel.getjTable().getSelectedRow(), 0);

			int isDelete = JOptionPane.showConfirmDialog(serverFrame, "ȷ������"
					+ cId + "�û���������", "�����û�����", 0);
			if (isDelete == 0) {
				UserPanel.getUserimp().clearUser(cId);
				UserPanel.refresh(null, null);
				JOptionPane.showMessageDialog(serverFrame, "�������óɹ�!");
			}
		} else if (e.getSource() == userPanel.getButtonClearAll()) {
			int isDelete = JOptionPane.showConfirmDialog(serverFrame,
					"ȷ�����������û���������", "�����û�����", 0);
			if (isDelete == 0) {
				UserPanel.getUserimp().clearUser(null);
				UserPanel.refresh(null, null);
				JOptionPane.showMessageDialog(serverFrame, "�������óɹ�!");
			}

		}
	}

}
