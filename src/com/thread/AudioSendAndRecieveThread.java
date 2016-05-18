package com.thread;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.ipmsg.model.User;

import android.os.StrictMode;
import android.view.animation.ScaleAnimation;

public class AudioSendAndRecieveThread extends Thread{

	Socket socket;
	User user;
	
	OutputStream os;
	InputStream is;
	
	public AudioSendAndRecieveThread(User user) {
		// TODO Auto-generated constructor stub
		
		this.socket = socket;
	
		this.user = user;
	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		
		
		try {
			Socket socket = new Socket(user.ipAddress.getInetAddress(), 6060);
			
			os = socket.getOutputStream();
			is = socket.getInputStream();
			
			
			//send audio
			new SendAudioThread(os).start();
		
			//recive audio
			new PlayAudioThread(is).start();
			
			
		
	
	
	}catch(Exception ex){
		ex.printStackTrace();
		
		
		
	}
	}
	
	
}
