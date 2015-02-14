package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import panel.UpdateUserPanel;
import panel.UserPanel;
import tool.Check;

public class UpdateListener implements ActionListener {
	UpdateUserPanel updateUserPanel;

	public UpdateListener(UpdateUserPanel updateUserPanel) {
		this.updateUserPanel = updateUserPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateUserPanel.getButton1()) {
			String cName = updateUserPanel.getTextField3().getText();

			if (!Check.checkSname(cName)) {
				updateUserPanel.getjLabelMessage().setText(
						"�������ʵ�������ǳ����� 2~10 ֮�������");
				return;
			} else {

				updateUserPanel.getUser().setName(cName);
			}

			String cAge = updateUserPanel.getTextField4().getText();

			if (!Check.checkSage(cAge)) {
				updateUserPanel.getjLabelMessage()
						.setText("�������������20~150֮�������");
				return;
			} else {

				updateUserPanel.getUser().setAge(cAge);
			}

			String cAddress = String.valueOf(updateUserPanel.getTextField6()
					.getText());

			if (!Check.checkSaddress(cAddress)) {
				updateUserPanel.getjLabelMessage().setText("����ĵ�ַ������Ϊ0~100");
				return;
			} else {

				updateUserPanel.getUser().setAddress(cAddress);
			}

			updateUserPanel.getUser().setSex(
					String.valueOf(updateUserPanel.getTextField5()
							.getSelectedItem()));

			updateUserPanel.getUserPanel();
			updateUserPanel.getUserimp().updateUser(updateUserPanel.getUser(),
					UserPanel.getuId());
			UserPanel.refresh(null, null);

			JOptionPane.showMessageDialog(
					updateUserPanel.getUpdateUserDialog(), "�޸ĳɹ�");
			updateUserPanel.getUpdateUserDialog().dispose();
		} else if (e.getSource() == updateUserPanel.getButton2()) {
			updateUserPanel.getUpdateUserDialog().dispose();
		}

	}

}
