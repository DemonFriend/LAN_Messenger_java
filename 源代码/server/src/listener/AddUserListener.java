package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import panel.AddUserPanel;
import panel.UserPanel;
import tool.Check;

public class AddUserListener implements ActionListener {
	AddUserPanel addUserPanel;

	public AddUserListener(AddUserPanel addUserPanel) {
		this.addUserPanel = addUserPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == addUserPanel.getButton1()) {
			String cPassword = String.valueOf(addUserPanel.getTextField2()
					.getPassword());

			if (!Check.checkPassword(cPassword)) {
				addUserPanel.getjLabelMessage().setText(
						"��������볤������ 3~16 ֮��(ֻ���������֣���ĸ , _) ");
				return;
			} else {

				addUserPanel.getUser().setPassword(cPassword);
			}

			String cName = addUserPanel.getTextField3().getText();

			if (!Check.checkSname(cName)) {
				addUserPanel.getjLabelMessage().setText(
						"�������ʵ�������ǳ����� 2~10 ֮�������");
				return;
			} else {

				addUserPanel.getUser().setName(cName);
			}

			String cAge = addUserPanel.getTextField4().getText();

			if (!Check.checkSage(cAge)) {
				addUserPanel.getjLabelMessage().setText("�������������20~150֮�������");
				return;
			} else {

				addUserPanel.getUser().setAge(cAge);
			}

			String cAddress = String.valueOf(addUserPanel.getTextField6()
					.getText());

			if (!Check.checkSaddress(cAddress)) {
				addUserPanel.getjLabelMessage().setText("����ĵ�ַ������Ϊ0~100");
				return;
			} else {

				addUserPanel.getUser().setAddress(cAddress);
			}

			addUserPanel.getUser().setSex(
					String.valueOf(addUserPanel.getTextField5()
							.getSelectedItem()));

			addUserPanel.getUserimp().addUser(addUserPanel.getUser());

			addUserPanel.getUserDialog().dispose();
			UserPanel.refresh(null, null);

		} else if (e.getSource() == addUserPanel.getButton2()) {
			addUserPanel.getUserDialog().dispose();
		}

	}

}
