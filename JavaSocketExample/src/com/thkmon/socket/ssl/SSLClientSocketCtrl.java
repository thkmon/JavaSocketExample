package com.thkmon.socket.ssl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClientSocketCtrl {
	
	
	public static void main(String[] args) {
		SSLClientSocketCtrl sslClientSocketCtrl = new SSLClientSocketCtrl();
		sslClientSocketCtrl.run();
	}
	
	
	public void run() {
		// SSL인증서 사용여부
		boolean useSSLCertificate = false;
		
		String socketIp = "127.0.0.1";
		int socketPort  = 7100;
		SSLSocket sslClientSocket = null;
		
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			// 연결
			sslClientSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(socketIp, socketPort);
			System.out.println("SSLClientSocketCtrl : 연결됨");
			
			if (useSSLCertificate) {
				sslClientSocket.setEnabledCipherSuites(sslClientSocket.getSupportedCipherSuites());
			} else {
				// SSL인증서를 사용하지 않기 위해 anon cipher suite 사용
				setAnonCipherArray(sslClientSocket);	
			}
			
			
			// 데이터 보내기
			System.out.println("SSLClientSocketCtrl : 데이터 보내기 시작");
			outputStream = sslClientSocket.getOutputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			
			ArrayList<String> writeList = new ArrayList<String>();
			writeList.add("client to server. 12345");
			writeList.add("client to server. abcde");
			writeList.add("client to server. 클라이언트에서 서버로 보냅니다.");
			writeList.add("EOF");
			
			String writeLine = "";
			for (int i=0; i<writeList.size(); i++) {
				writeLine = writeList.get(i);
				dataOutputStream.writeUTF(writeLine);
				System.out.println("보낸 데이터 : " + writeLine);
			}
			System.out.println("SSLClientSocketCtrl : 데이터 보내기 끝");
			
			
			// 데이터 받기
			System.out.println("SSLClientSocketCtrl : 데이터 받기 시작");
			inputStream = sslClientSocket.getInputStream();
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
			System.out.println("SSLClientSocketCtrl : 데이터 받기 끝");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(dataOutputStream);
			close(outputStream);
			
			close(dataInputStream);
			close(inputStream);
			
			close(sslClientSocket);
		}
		
		System.out.println("SSLClientSocketCtrl : 종료");
	}
	
	
	private void setAnonCipherArray(SSLSocket argSock) {
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
	
	
	public void close(SSLSocket obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
}