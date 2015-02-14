package tool;

public class Check {

	/**
	 * ��֤����
	 * 
	 * @param pass
	 * @return �Ƿ�Ϸ�
	 */
	public static boolean checkPassword(String pass) {
		boolean f = false;
		String regex = "^([0-9]|[a-zA-Z]|_){3,16}$";// \\w--������ĸ�»���
		if (pass != null) {
			if (pass.matches(regex)) {
				f = true;
			}
		}

		return f;
	}

	/**
	 * ��֤����
	 * 
	 * @param sname
	 * @return �Ƿ�Ϸ�
	 */
	public static boolean checkSname(String sname) {
		boolean f = false;
		String regex = "^[һ-��]{2,10}$";// ȫ���Ļ�ȫӢ��
		if (sname != null) {
			if (sname.matches(regex)) {
				f = true;
			}
		}

		return f;
	}

	/**
	 * ��֤����
	 * 
	 * @param nage
	 * @return �Ƿ�Ϸ�
	 */
	public static boolean checkSage(String age) {
		boolean f = false;
		String regex = "^[1-9][0-9]{1,2}$";
		if (age != null) {
			age = age.trim();
			if (age.matches(regex)) {
				int i = Integer.parseInt(age);
				if (20 <= i && i <= 150) {
					f = true;
				}
			}
		}
		return f;
	}

	/**
	 * ��֤��ַ �����������������(�Ա��������ı��ָ�ʹ��)
	 * 
	 * @param saddress
	 * @return �Ƿ�Ϸ�
	 */
	public static boolean checkSaddress(String saddress) {
		boolean f = false;
		String regex = "[^,��]{0,100}";// ���Բ����뵫�ǲ��������붺��
		if (saddress != null) {
			if (saddress.matches(regex)) {
				f = true;
			}
		}
		return f;
	}

}
