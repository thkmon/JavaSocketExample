package com.thkmon.socket.ssl;

import java.util.Arrays;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SSLCipherSuitesPrinter {

	public static void main(String[] args) {

		try {
			ServerSocketFactory sf = SSLServerSocketFactory.getDefault();
			SSLServerSocket serverSocket = (SSLServerSocket) sf.createServerSocket(7100); // 임의의 포트번호

			System.out.println("SSLServerSocket SupportedCipherSuites : " + Arrays.toString(serverSocket.getSupportedCipherSuites()));
			serverSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}