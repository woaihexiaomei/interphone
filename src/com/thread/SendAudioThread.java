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

public class SendAudioThread extends Thread {

	OutputStream oos;

	AudioRecord recorder;

	public SendAudioThread(OutputStream oos) {
		// TODO Auto-generated constructor stub

		this.oos = oos;

	}

	byte[] processedData = new byte[1024];
	short[] rawdata = new short[1024];

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		StrictMode.setThreadPolicy(
				new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																											// 就包括了磁盘读写和网络I/O
						.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
						.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		try {

			// 获得录音缓冲区大小
			int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			Log.e("", "录音缓冲区大小" + bufferSize);

			// 获得录音机对象
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10);

			recorder.startRecording();// 开始录音
			short[] readBuffer = new short[1024];// 录音缓冲区

			int length = 0;

			while (Contants.isPlaying) {
				length = recorder.read(readBuffer, 0, readBuffer.length);// 从mic读取音频数据
				if (length > 0 && length % 2 == 0) {

					System.arraycopy(readBuffer, 0, rawdata, 0, length);

					int bufferLen = Contants.speex.encode(rawdata, 0, processedData, length);

					oos.write(processedData, 0, bufferLen);// 写入到输出流，把音频数据通过网络发送给对方
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
