package com.thkmon.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketCtrl {

	public static void main(String[] args) {
		ServerSocketCtrl serverSocketCtrl = new ServerSocketCtrl();
		serverSocketCtrl.run();
	}
	
	
	public void run() {
		String socketIp = "127.0.0.1";
		int socketPort  = 7100;
		ServerSocket serverSocket = null;
		
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			// ����
			serverSocket = new ServerSocket(socketPort, 50, InetAddress.getByName(socketIp));
			
			int clientIndex = 0;
			
			while (true) {
				System.out.println("ServerSocketCtrl : �����û ���");
				Socket socket = serverSocket.accept();
				clientIndex = clientIndex + 1;
				System.out.println("ServerSocketCtrl : �����. " + clientIndex + "��° Ŭ���̾�Ʈ�� �����߽��ϴ�.");
				
				
				// ������ ������
				System.out.println("ServerSocketCtrl : ������ ������ ����");
				outputStream = socket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				
				ArrayList<String> writeList = new ArrayList<String>();
				writeList.add("server to client. 12345");
				writeList.add("server to client. abcde");
				writeList.add("server to client. �������� Ŭ���̾�Ʈ�� �����ϴ�.");
				writeList.add("EOF");
				
				String writeLine = "";
				for (int i=0; i<writeList.size(); i++) {
					writeLine = writeList.get(i);
					dataOutputStream.writeUTF(writeLine);
					System.out.println("���� ������ : " + writeLine);
				}
				System.out.println("ServerSocketCtrl : ������ ������ ��");
				
				
				// ������ �ޱ�
				System.out.println("ServerSocketCtrl : ������ �ޱ� ����");
				inputStream = socket.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				
				String readLine = "";
				while (true) {
		            try {
		            	readLine = dataInputStream.readUTF();
		            	System.out.println("���� ������ : " + readLine);
		            	if (readLine != null && readLine.equals("EOF")) {
		            		break;
		            	}
		            	
		            } catch (EOFException eof) {
		            	break;
		            }
				}
				System.out.println("ServerSocketCtrl : ������ �ޱ� ��");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(dataOutputStream);
			close(outputStream);
			
			close(dataInputStream);
			close(inputStream);
			
			close(serverSocket);
		}
		
		System.out.println("ServerSocketCtrl : ����");
	}
	
	
	public void close(OutputStream obj) {
		try {
			if (obj != null) {
				obj.flush();
			}
		} catch (Exception e) {}
		
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
	
	
	public void close(InputStream obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
	
	
	public void close(ServerSocket obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
}