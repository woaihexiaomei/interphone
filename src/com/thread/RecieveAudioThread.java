package com.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.ipmsg.util.Contants;

public class RecieveAudioThread extends Thread {

	ServerSocket serverSocket = null;

	Socket acceptSocket;


	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			serverSocket = new ServerSocket(6060);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		while (Contants.isPlaying) {

			try {

				acceptSocket = serverSocket.accept();

				OutputStream oos = acceptSocket.getOutputStream();
				InputStream is = acceptSocket.getInputStream();

				// send audio
				new SendAudioThread(oos).start();

				// recive audio
				new PlayAudioThread(is).start();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				if (null != serverSocket) {

					try {
						serverSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}

		}
	}

}
