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
	            byte[] audio = new byte[160];// ��Ƶ��ȡ����  
	            int length = 0;  
	  
	            while (!isStopTalk) {  
	                length = is.read(audio);// �������ȡ��Ƶ����  
	                byte[] temp = audio.clone();  
	                if (length > 0 && length % 2 == 0) {  
	                    // for(int  
	                    // i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//��Ƶ�Ŵ�1��  
	                    player.write(audio, 0, temp.length);// ������Ƶ����  
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
