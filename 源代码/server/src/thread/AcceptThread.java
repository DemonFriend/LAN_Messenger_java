package thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import listener.ServerListener;

import dao.bean.User;
import dao.imf.Userimp;

import pub.PackType;
import pub.QQPackage;
import tool.StaticTool;

import frame.ServerFrame;

public class AcceptThread extends Thread {
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	ServerSocket serverSocket;
	ServerFrame frame;
	private Userimp userimp;
	private User bean;

	public AcceptThread(ServerSocket serverSocket, ServerFrame frame) {
		this.serverSocket = serverSocket;
		this.frame = frame;
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			// 2-2.�򿪼���
			try {
				Socket socket = serverSocket.accept();
				// ��ȡio��
				objectInputStream = new ObjectInputStream(
						socket.getInputStream());

				QQPackage qqPackage = (QQPackage) objectInputStream
						.readObject();
				String gId = qqPackage.getFrom();
				String gPassword = (String) qqPackage.getData();
				userimp = new Userimp();
				bean = userimp.getUserBySidAndPsw(gId, gPassword);
				objectOutputStream = new ObjectOutputStream(
						socket.getOutputStream());

				// a�����ӳɹ��������ͻ��˷������İ� ����sid��ѯ���ݿ⣬�жϱ�Ŵ��ڣ�
				// ������ȷ������״̬Ϊ����
				if (bean != null) {
					if (gPassword.equals(bean.getPassword())) {
						if (bean.getIsonline().equals("����")) {
							frame.getLogPanel().writeLog(
									bean.getName() + "(" + bean.getId() + ")"
											+ "��¼ʧ��!");
							QQPackage package1 = new QQPackage();
							package1.setPackType(PackType.loginFail);
							packageSend(package1, objectOutputStream);
						} else {
							bean.setIsonline("����");
							userimp.updateOnline(bean.getId());
							frame.getServerPanel()
									.getTextArea4()
									.append("��" + StaticTool.SystemTime() + "��"
											+ bean.getId() + "��¼�ɹ�\n");
							frame.getLogPanel().writeLog(
									bean.getName() + "(" + bean.getId() + ")"
											+ "��¼�ɹ�!");
							/**
							 * ���͵�¼�ɹ���
							 */

							QQPackage package1 = new QQPackage();
							String name = bean.getId() + " " + bean.getName();
							package1.setPackType(PackType.loginSuccess);
							package1.setData(name);
							packageSend(package1, objectOutputStream);
							/**
							 * ���͹���
							 */
							package1 = new QQPackage();
							package1.setData("��ӭ��½QQ");
							package1.setPackType(PackType.post);
							packageSend(package1, objectOutputStream);
							/**
							 * ˢ���û����б�
							 */
							ServerListener.sRefresh(null, null, "����");// ˢ���б�

							/**
							 * �����־
							 */
							frame.getLogPanel().writeLog(
									bean.getName() + "(" + bean.getId() + ")"
											+ "������!");
							/**
							 * �����߳�
							 */
							SReadThread sReadThread = new SReadThread(frame,
									name, socket, objectInputStream,
									objectOutputStream);

							frame.getReadThreadMap().put(name, sReadThread);
							// Ⱥ�������û��б�
							StaticTool.sendOnLineUsers(frame);
							sReadThread.start();
						}
					} else {
						QQPackage package1 = new QQPackage();
						package1.setPackType(PackType.loginFail);
						this.packageSend(package1, objectOutputStream);
					}

				} else {
					QQPackage package1 = new QQPackage();
					package1.setPackType(PackType.loginFail);
					this.packageSend(package1, objectOutputStream);
				}

			} catch (IOException e) {
				System.out.println("�����ѹر�");
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void packageSend(QQPackage qqPackage,
			ObjectOutputStream objectOutputStream) {
		try {
			objectOutputStream.writeObject(qqPackage);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	public void setObjectInputStream(ObjectInputStream objectInputStream) {
		this.objectInputStream = objectInputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
		this.objectOutputStream = objectOutputStream;
	}

}
