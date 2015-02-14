package thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Vector;

import listener.ServerListener;

import dao.bean.User;
import dao.imf.Userimp;

import pub.PackType;
import pub.QQPackage;
import tool.StaticTool;

import frame.ServerFrame;

public class SReadThread extends Thread {
	ObjectInputStream inputStream;
	ServerFrame serverFrame;
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;
	private Userimp userimp = new Userimp();
	private Socket socket;
	String sidName;
	private SReadThread serverReadClientThread;

	public SReadThread(ServerFrame frame, String sidName, Socket socket,
			ObjectInputStream objectInputStream,
			ObjectOutputStream objectOutputStream) {
		this.serverFrame = frame;
		this.objectInputStream = objectInputStream;
		this.objectOutputStream = objectOutputStream;
		this.sidName = sidName;
		this.socket = socket;
	}

	@Override
	public void run() {

		while (!this.isInterrupted()) {
			try {
				QQPackage qqPackage = (QQPackage) objectInputStream
						.readObject();
				if (qqPackage.getPackType() == PackType.privateChat) {
					// ˽��
					SReadThread sReadThread = serverFrame.getReadThreadMap()
							.get(qqPackage.getTo());
					sReadThread.objectOutputStream.writeObject(qqPackage);
					sReadThread.objectOutputStream.flush();
				} else if (qqPackage.getPackType() == PackType.publicChat) {
					// Ⱥ��
					Collection<SReadThread> collection = serverFrame
							.getReadThreadMap().values();
					for (SReadThread serverReadClientThread : collection) {
						if (serverReadClientThread.equals(serverFrame
								.getReadThreadMap().get(qqPackage.getFrom()))) {
							continue;
						}
						serverReadClientThread.objectOutputStream
								.writeObject(qqPackage);
						serverReadClientThread.objectOutputStream.flush();
					}
				} else if (qqPackage.getPackType() == PackType.userDownLine) {
					// �û�����
					serverReadClientThread = serverFrame.getReadThreadMap()
							.get(qqPackage.getFrom());
					serverFrame.getReadThreadMap().remove(qqPackage.getFrom());
					Collection<String> collection = serverFrame
							.getReadThreadMap().keySet();
					Vector<String> onlineName = new Vector<String>();
					onlineName.removeAllElements();
					for (String string : collection) {
						onlineName.add(string);
					}
					Collection<SReadThread> collection2 = serverFrame
							.getReadThreadMap().values();
					QQPackage package1 = new QQPackage();
					package1.setPackType(PackType.onlineuser);
					package1.setData(onlineName);
					for (SReadThread serverReadClientThread2 : collection2) {
						serverReadClientThread2.objectOutputStream
								.writeObject(package1);
						objectOutputStream.flush();
					}
					String sid = qqPackage.getFrom().substring(0, 5);
					String sName = qqPackage.getFrom().substring(6);
					userimp.updateUserGo(sid);// �޸��û�Ϊ����
					ServerListener.sRefresh(null, null, "����");// ˢ���б�
					serverFrame
							.getServerPanel()
							.getTextArea4()
							.append("��" + StaticTool.SystemTime() + "��" + sid
									+ "�����ˣ�\n");
					serverFrame.getLogPanel().writeLog(
							sName + "(" + sid + ")" + "������!");

					serverReadClientThread.socket.close();
					serverReadClientThread.interrupt();// �ر������û����߳�

				} else if (qqPackage.getPackType() == PackType.updatePassword) {
					// �޸�����
					String sid = qqPackage.getFrom();
					String[] password = (String[]) qqPackage.getData();
					String oldPsw = password[0];
					String newPsw = password[1];
					User user = userimp.getUserPswBySid(sid);
					if (user.getPassword().equals(oldPsw)) {
						userimp.updatePassword(sid, newPsw);
						qqPackage.setPackType(PackType.updatePassword);
						qqPackage.setData("�����޸ĳɹ�");
						objectOutputStream.writeObject(qqPackage);
						objectOutputStream.flush();
						ServerListener.sRefresh(null, null, null);// ˢ���б�
					} else {
						qqPackage.setPackType(PackType.updatePassword);
						qqPackage.setData("�������");
						objectOutputStream.writeObject(qqPackage);
						objectOutputStream.flush();
					}
				}
			} catch (IOException e) {
				try {
					QQPackage qqPackage = (QQPackage) objectInputStream
							.readObject();
					String sid = qqPackage.getFrom().substring(0, 5);
					String sName = qqPackage.getFrom().substring(6);

					userimp.updateUserGo(sid);// �޸��û�Ϊ����
					ServerListener.sRefresh(null, null, "����");// ˢ���б�
					serverFrame
							.getServerPanel()
							.getTextArea4()
							.append("��" + StaticTool.SystemTime() + "��" + sid
									+ "�����ˣ�\n");
					serverFrame.getLogPanel().writeLog(
							sName + "(" + sid + ")" + "������!");
				} catch (IOException e2) {
					System.out.println("�����ѹر�");
				} catch (ClassNotFoundException e2) {
					e2.printStackTrace();
				}
				try {
					objectOutputStream.close();
					objectInputStream.close();
					socket.close();
				} catch (IOException e1) {
					System.out.println("�����ѹر�");
				}
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

}
