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
			// 연결
			serverSocket = new ServerSocket(socketPort, 50, InetAddress.getByName(socketIp));
			
			int clientIndex = 0;
			
			while (true) {
				System.out.println("ServerSocketCtrl : 연결요청 대기");
				Socket socket = serverSocket.accept();
				clientIndex = clientIndex + 1;
				System.out.println("ServerSocketCtrl : 연결됨. " + clientIndex + "번째 클라이언트가 입장했습니다.");
				
				
				// 데이터 보내기
				System.out.println("ServerSocketCtrl : 데이터 보내기 시작");
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
				System.out.println("ServerSocketCtrl : 데이터 보내기 끝");
				
				
				// 데이터 받기
				System.out.println("ServerSocketCtrl : 데이터 받기 시작");
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
				System.out.println("ServerSocketCtrl : 데이터 받기 끝");
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
		
		System.out.println("ServerSocketCtrl : 종료");
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