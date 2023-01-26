package com.thkmon.socket.ssl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SSLServerSocketCtrl {
	
	
	public static void main(String[] args) {
		SSLServerSocketCtrl sslServerSocketCtrl = new SSLServerSocketCtrl();
		sslServerSocketCtrl.run();
	}
	
	
	public void run() {
		// SSL������ ��뿩��
		boolean useSSLCertificate = false;
		
		String socketIp = "127.0.0.1";
		int socketPort  = 7100;
		SSLServerSocket sslServerSocket = null;
		
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			// ����
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			sslServerSocket = (SSLServerSocket) ssf.createServerSocket(socketPort, 50, InetAddress.getByName(socketIp));
			
			if (useSSLCertificate) {
				sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
			} else {
				// SSL�������� ������� �ʱ� ���� anon cipher suite ���
				setAnonCipherArray(sslServerSocket);	
			}
			
			
			int clientIndex = 0;
			
			while (true) {
				System.out.println("SSLServerSocketCtrl : �����û ���");
				Socket socket = sslServerSocket.accept();
				clientIndex = clientIndex + 1;
				System.out.println("SSLServerSocketCtrl : �����. " + clientIndex + "��° Ŭ���̾�Ʈ�� �����߽��ϴ�.");
				
				
				// ������ ������
				System.out.println("SSLServerSocketCtrl : ������ ������ ����");
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
				System.out.println("SSLServerSocketCtrl : ������ ������ ��");
				
				
				// ������ �ޱ�
				System.out.println("SSLServerSocketCtrl : ������ �ޱ� ����");
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
				System.out.println("SSLServerSocketCtrl : ������ �ޱ� ��");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(dataOutputStream);
			close(outputStream);
			
			close(dataInputStream);
			close(inputStream);
			
			close(sslServerSocket);
		}
		
		System.out.println("SSLServerSocketCtrl : ����");
	}
	
	
	private void setAnonCipherArray(SSLServerSocket argSock) {
		List<String> anonChipherList = new ArrayList<String>();
		
		String[] supportedCipherSuites = argSock.getSupportedCipherSuites();
		if (supportedCipherSuites != null && supportedCipherSuites.length > 0) {
			for (int i = 0; i < supportedCipherSuites.length; i++) {
			    if (supportedCipherSuites[i].indexOf("_anon_") > -1) {
			    	anonChipherList.add(supportedCipherSuites[i]);
			    }
			}
		}
		
		String[] anonChipherArray = null;
		if (anonChipherList != null && anonChipherList.size() > 0) {
			anonChipherArray = new String[anonChipherList.size()];
			for (int i = 0; i < anonChipherList.size(); i++) {
				anonChipherArray[i] = anonChipherList.get(i);
			}
			
			if (anonChipherArray != null && anonChipherArray.length > 0) {
				argSock.setEnabledCipherSuites(anonChipherArray);
			}
		}
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
	
	
	public void close(SSLServerSocket obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
}