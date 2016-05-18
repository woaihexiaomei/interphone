package com.ipmsg.service;

import ipmsg.IPMAddress;
import ipmsg.IPMComEvent;
import ipmsg.IPMEvent;
import ipmsg.IPMListener;
import ipmsg.IPMPack;
import ipmsg.IPMsg;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import com.ipmsg.util.Contants;
import com.ipmsg.util.IntentConfig;

import JP.digitune.util.Cp932;
import JP.digitune.util.SortVector;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.widget.Toast;


public class IPMsgService extends Service {

	private static MediaPlayer player;
	public static IPMsg ipmsg;
	private boolean refreshing = false, received = false;
	private Hashtable<String, IPMComEvent> NAMEtoINFO;
	private Hashtable ADDRtoINFO;
	private List memberlist;

	 
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		
		ipmsg = IPMsg.getInstance(Contants.name);
		ipmsg.setGroup("1");
		
		String host = null;
		
		try {
			host = ipmsg.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ipmsg.setHost(host);
		ipmsg.addIPMListener(new IPMListener() {
			public void eventOccured(IPMEvent ipme) {
				processEvent(ipme);
			}
		});
		
		ipmsg.entry();
		
		
		
		
	}


	synchronized void processEvent(IPMEvent ipme) {

		
		switch (ipme.getID()) {
		case IPMEvent.UPDATELIST_EVENT:
			
			System.out.println("接收到消息了");	
			
			handler.sendEmptyMessage(0);
			
			
			break;

		default:
			break;
		}
		
		
		
		
		
		
	}

	Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			
			int what = msg.what;
			
			switch (what) {
			case 0:
				
				sendBroadcast(new Intent(IntentConfig.Action_User_Refresh));
				
				
				break;

			default:
				break;
			}
			
			
			
			
			
		};
		
		
	};
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
