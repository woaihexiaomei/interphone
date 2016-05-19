package com.thread;

import java.io.IOException;
import java.io.InputStream;

import com.ipmsg.util.Contants;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.StrictMode;
import android.util.Log;

public class PlayAudioThread extends Thread{

	InputStream is;	
	
	byte[] processedData = new byte[1024];
	short[] rcvProcessedData = new short[1024];
	public PlayAudioThread(InputStream is) {
		// TODO Auto-generated constructor stub
		
		this.is = is;
	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // ��������滻ΪdetectAll()
																		// �Ͱ����˴��̶�д������I/O
				.penaltyLog() // ��ӡlogcat����ȻҲ���Զ�λ��dropbox��ͨ���ļ�������Ӧ��log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
				.penaltyLog() // ��ӡlogcat
				.penaltyDeath().build());
		 try {  
	            // �����Ƶ��������С  
	            int bufferSize = android.media.AudioTrack.getMinBufferSize(8000,  
	                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);  
	            Log.e("", "���Ż�������С"+bufferSize);  
	  
	            // ����������  
	            AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,  
	                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,  
	                    AudioTrack.MODE_STREAM);  
	  
	            // ������������  
	            player.setStereoVolume(1.0f, 1.0f);  
	  
	            // ��ʼ��������  
	            player.play();  
	            byte[] audio = new byte[1024];// ��Ƶ��ȡ����
	            
	            byte[]  rawData= new byte[1024];
	            
	            int length = 0;  
	  
	            while (Contants.isPlaying) {  
	                length = is.read(audio);// �������ȡ��Ƶ����  
	                
	                if (length > 0 && length % 2 == 0) {
	                	Log.e("innog", "��ʼ����ing");
	                    // for(int  
	                    // i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//��Ƶ�Ŵ�1��  
	                
	                	System.arraycopy(audio, 0, rawData, 0, length);
	                	
	                	int rawLen = Contants.speex.decode(rawData, rcvProcessedData, length);
	                	
	                	player.write(rcvProcessedData, 0, rawLen);// ������Ƶ����  
	                }  
	            }  
	            player.stop();  
	            player.release();  
	            player = null;  
	            is.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
		
	
	
	}
	
}
