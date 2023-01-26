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
		// SSL인증서 사용여부
		boolean useSSLCertificate = false;
		
		String socketIp = "127.0.0.1";
		int socketPort  = 7100;
		SSLServerSocket sslServerSocket = null;
		
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			// 연결
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			sslServerSocket = (SSLServerSocket) ssf.createServerSocket(socketPort, 50, InetAddress.getByName(socketIp));
			
			if (useSSLCertificate) {
				sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
			} else {
				// SSL인증서를 사용하지 않기 위해 anon cipher suite 사용
				setAnonCipherArray(sslServerSocket);	
			}
			
			
			int clientIndex = 0;
			
			while (true) {
				System.out.println("SSLServerSocketCtrl : 연결요청 대기");
				Socket socket = sslServerSocket.accept();
				clientIndex = clientIndex + 1;
				System.out.println("SSLServerSocketCtrl : 연결됨. " + clientIndex + "번째 클라이언트가 입장했습니다.");
				
				
				// 데이터 보내기
				System.out.println("SSLServerSocketCtrl : 데이터 보내기 시작");
				outputStream = socket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);
				
				ArrayList<String> writeList = new ArrayList<String>();
				writeList.add("server to client. 12345");
				writeList.add("server to client. abcde");
				writeList.add("server to client. 서버에서 클라이언트로 보냅니다.");
				writeList.add("EOF");
				
				String writeLine = "";
				for (int i=0; i<writeList.size(); i++) {
					writeLine = writeList.get(i);
					dataOutputStream.writeUTF(writeLine);
					System.out.println("보낸 데이터 : " + writeLine);
				}
				System.out.println("SSLServerSocketCtrl : 데이터 보내기 끝");
				
				
				// 데이터 받기
				System.out.println("SSLServerSocketCtrl : 데이터 받기 시작");
				inputStream = socket.getInputStream();
				dataInputStream = new DataInputStream(inputStream);
				
				String readLine = "";
				while (true) {
		            try {
		            	readLine = dataInputStream.readUTF();
		            	System.out.println("받은 데이터 : " + readLine);
		            	if (readLine != null && readLine.equals("EOF")) {
		            		break;
		            	}
		            	
		            } catch (EOFException eof) {
		            	break;
		            }
				}
				System.out.println("SSLServerSocketCtrl : 데이터 받기 끝");
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
		
		System.out.println("SSLServerSocketCtrl : 종료");
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