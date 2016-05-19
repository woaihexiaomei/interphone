package com.qiyu.ui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import com.ipmsg.model.User;
import com.ipmsg.service.IPMsgService;
import com.ipmsg.util.Contants;
import com.ipmsg.util.IntentConfig;
import com.ipmsg.util.Speex;
import com.qiyu.interphone.R;
import com.qiyu.ui.adapter.UserAdapter;
import com.thread.AudioSendAndRecieveThread;
import com.thread.RecieveAudioThread;
import com.thread.SendAudioThread;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ipmsg.IPMComEvent;
import ipmsg.IPMPack;

public class MainActivity extends Activity {

	Button btn1, btn2;
	EditText edit_name;
	ListView listView;

	UserAdapter userAdapter;
	Context sInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);

		sInstance = this;

		Contants.speex = new Speex();
		Contants.speex.init();
		register();

		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		edit_name = (EditText) findViewById(R.id.nameEd);
		listView = (ListView) findViewById(R.id.listview);

		userAdapter = new UserAdapter(this, new ArrayList<User>());

		listView.setAdapter(userAdapter);

		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name = edit_name.getText().toString();

				if (null != name && name.length() > 0) {

					Contants.name = name;

					Intent intent = new Intent(sInstance, IPMsgService.class);
					startService(intent);
					
					
					
					new RecieveAudioThread().start();

				}

			}
		});

		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				IPMsgService.ipmsg.refreshList();

				System.out.println();

			}
		});

		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				
				
				
				User user = userAdapter.users.get(position);
				
				new AudioSendAndRecieveThread(user).start();
				
				
				
				
				
			}
			
			
			
			
		});
	
	
	}

	private void register() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IntentConfig.Action_User_Refresh);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (IntentConfig.Action_User_Refresh.equals(action)) {

				ArrayList<User> userlist = transferUsers(IPMsgService.ipmsg.getUserlist());

				userAdapter.users = userlist;

				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						userAdapter.notifyDataSetChanged();
						;
					}
				});

			}
		}

	};

	public ArrayList<User> transferUsers(Map<String, Object> userMap) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			for (Map.Entry entry : userMap.entrySet()) {
				IPMComEvent ipme = (IPMComEvent) entry.getValue();
				IPMPack ipm = ipme.getPack();
				String name = ipm.getUser();

				User user = new User();
				user.name = name;
				user.ipAddress = ipme.getIPMAddress();
				user.ip = (ipme.getIPMAddress().getInetAddress().getHostAddress());
				users.add(user);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return users;
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
		};

	};

	protected void onDestroy() {
		super.onDestroy();
		unregister();
		Contants.isPlaying = false;

	};

	private void unregister() {
		unregisterReceiver(mBroadcastReceiver);
	}
}
