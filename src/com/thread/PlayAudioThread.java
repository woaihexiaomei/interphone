package com.thread;

import java.io.IOException;
import java.io.InputStream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class PlayAudioThread extends Thread{

	InputStream is;	
	
	boolean isStopTalk = false;
	
	public PlayAudioThread(InputStream is) {
		// TODO Auto-generated constructor stub
		
		this.is = is;
	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
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
	            byte[] audio = new byte[160];// 音频读取缓存  
	            int length = 0;  
	  
	            while (!isStopTalk) {  
	                length = is.read(audio);// 从网络读取音频数据  
	                byte[] temp = audio.clone();  
	                if (length > 0 && length % 2 == 0) {  
	                    // for(int  
	                    // i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//音频放大1倍  
	                    player.write(audio, 0, temp.length);// 播放音频数据  
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
