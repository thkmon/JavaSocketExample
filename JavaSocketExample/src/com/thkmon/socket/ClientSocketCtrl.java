package com.thkmon.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocketCtrl {
	
	
	public static void main(String[] args) {
		ClientSocketCtrl clientSocketCtrl = new ClientSocketCtrl();
		clientSocketCtrl.run();
	}
	
	
	public void run() {
		String socketIp = "127.0.0.1";
		int socketPort  = 7100;
		Socket clientSocket = null;
		
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		
		try {
			// 연결
			clientSocket = new Socket(socketIp, socketPort);
			System.out.println("ClientSocketCtrl : 연결됨");
			
			
			// 데이터 보내기
			System.out.println("ClientSocketCtrl : 데이터 보내기 시작");
			outputStream = clientSocket.getOutputStream();
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
			System.out.println("ClientSocketCtrl : 데이터 보내기 끝");
			
			
			// 데이터 받기
			System.out.println("ClientSocketCtrl : 데이터 받기 시작");
			inputStream = clientSocket.getInputStream();
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
			System.out.println("ClientSocketCtrl : 데이터 받기 끝");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(dataOutputStream);
			close(outputStream);
			
			close(dataInputStream);
			close(inputStream);
			
			close(clientSocket);
		}
		
		System.out.println("ClientSocketCtrl : 종료");
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
	
	
	public void close(Socket obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {}
	}
}