package com.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.ipmsg.model.User;
import com.ipmsg.util.Contants;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.StrictMode;
import android.util.Log;

public class SendAudioThread extends Thread{

	OutputStream oos;
	
	AudioRecord recorder;
	
	
	public SendAudioThread(OutputStream oos) {
		// TODO Auto-generated constructor stub
		
		this.oos = oos; 
	
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	
	try{	
		
		 // 获得录音缓冲区大小  
        int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,  
                AudioFormat.ENCODING_PCM_16BIT);  
        Log.e("", "录音缓冲区大小"+bufferSize);  

        // 获得录音机对象  
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,  
                AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10);  

        recorder.startRecording();// 开始录音  
        byte[] readBuffer = new byte[640];// 录音缓冲区  

        int length = 0;  

        while (!Contants.isPlaying) {  
            length = recorder.read(readBuffer, 0, 640);// 从mic读取音频数据  
            if (length > 0 && length % 2 == 0) {  
                oos.write(readBuffer, 0, length);// 写入到输出流，把音频数据通过网络发送给对方  
            }  
        }  
        recorder.stop();  
        recorder.release();  
        recorder = null;  
        oos.close();  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
		
		
			
			
			
		
		
		
	
	}
	
	
}
