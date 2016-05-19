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
				new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // ��������滻ΪdetectAll()
																											// �Ͱ����˴��̶�д������I/O
						.penaltyLog() // ��ӡlogcat����ȻҲ���Զ�λ��dropbox��ͨ���ļ�������Ӧ��log
						.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
				.penaltyLog() // ��ӡlogcat
				.penaltyDeath().build());
		try {

			// ���¼����������С
			int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			Log.e("", "¼����������С" + bufferSize);

			// ���¼��������
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize * 10);

			recorder.startRecording();// ��ʼ¼��
			short[] readBuffer = new short[1024];// ¼��������

			int length = 0;

			while (Contants.isPlaying) {
				length = recorder.read(readBuffer, 0, readBuffer.length);// ��mic��ȡ��Ƶ����
				if (length > 0 && length % 2 == 0) {

					System.arraycopy(readBuffer, 0, rawdata, 0, length);

					int bufferLen = Contants.speex.encode(rawdata, 0, processedData, length);

					oos.write(processedData, 0, bufferLen);// д�뵽�����������Ƶ����ͨ�����緢�͸��Է�
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
