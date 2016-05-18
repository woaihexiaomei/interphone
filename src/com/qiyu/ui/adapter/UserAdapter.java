package com.qiyu.ui.adapter;

import java.util.ArrayList;

import com.ipmsg.model.User;import com.qiyu.interphone.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter{

	public ArrayList<User> users;
	Context mContext;
	LayoutInflater layoutInflater;
	
	public UserAdapter(Context mContext,ArrayList<User> users) {
		// TODO Auto-generated constructor stub
		
		this.users = users;
		this.mContext = mContext;
		layoutInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		if (null == convertView) {
			
			convertView = layoutInflater.inflate(R.layout.user_adapter_item_layout, null);
			
			
			convertView.setTag(new ViewHolder((TextView)convertView.findViewById(R.id.tx1)));
			
		}
		
		
		
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		
		viewHolder.tx1.setText(users.get(position).name+"=="+users.get(position).ip);;
		
		
		
		
		
		
		
		return convertView;
	}

	class ViewHolder{
		
		public TextView tx1;
		
		public ViewHolder(TextView tx1){
			
			this.tx1 = tx1;
		}
		
		
	}
	
	
}
