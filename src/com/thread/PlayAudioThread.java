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
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		 try {  
	            // 获得音频缓冲区大小  
	            int bufferSize = android.media.AudioTrack.getMinBufferSize(8000,  
	                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);  
	            Log.e("", "播放缓冲区大小"+bufferSize);  
	  
	            // 获得音轨对象  
	            AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,  
	                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,  
	                    AudioTrack.MODE_STREAM);  
	  
	            // 设置喇叭音量  
	            player.setStereoVolume(1.0f, 1.0f);  
	  
	            // 开始播放声音  
	            player.play();  
	            byte[] audio = new byte[1024];// 音频读取缓存
	            
	            byte[]  rawData= new byte[1024];
	            
	            int length = 0;  
	  
	            while (Contants.isPlaying) {  
	                length = is.read(audio);// 从网络读取音频数据  
	                
	                if (length > 0 && length % 2 == 0) {
	                	Log.e("innog", "开始播放ing");
	                    // for(int  
	                    // i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//音频放大1倍  
	                
	                	System.arraycopy(audio, 0, rawData, 0, length);
	                	
	                	int rawLen = Contants.speex.decode(rawData, rcvProcessedData, length);
	                	
	                	player.write(rcvProcessedData, 0, rawLen);// 播放音频数据  
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
