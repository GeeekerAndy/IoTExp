package com.example.andy.iotexp.clientSocketMCPH;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketThread extends Thread {
	private Socket socket = null;
	private final String TAG = "ClientSocketThread";
	private static ClientSocketThread clientSocket = null;
	private MessageListener listener;
	private final int buffer_size = 256;
	public byte[] sendBuffer = new byte[] { (byte) 0xFE, (byte) 0xE4, 0x09,
			0x58, 0x71, 0x00, 0x45, 0x00, 0x0A };

	/** get client socket thread instance */
	public static ClientSocketThread getClientSocket(String ip, int port) {
		if (clientSocket == null) {
			clientSocket = new ClientSocketThread(ip, port);
			clientSocket.start();
		}
		return clientSocket;
	}

	/** constructor */
	private ClientSocketThread(String ip, int port) {
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

	/** set a listener to report message received */
	public void setListener(MessageListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[buffer_size];
		int len = 0;
		while (!interrupted()) {
			try {
				this.getOutputStream().write(sendBuffer);
				len = this.getInputStream().read(buffer, 0, buffer_size);
				FrameFilter(buffer, len);
				//Log.e(TAG, clientSocketTools.byte2hex(buffer, len));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
			try {
				sleep(1500);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Frame filter
	 * 
	 * @param buffer
	 *            frame data
	 * @param len
	 *            frame lenth
	 */
	public void FrameFilter(byte[] buffer, int len) {
		int index = 0, frmlen = 0;
		byte ch;
		byte status = 0;
		byte[] sensordata = null;
		while ((len--) > 0) {
			ch = buffer[index++];
			switch (status) {
			case 0:
				if (ch == (byte) 0xFE)
					status = 1;
				break;
			case 1:
				if (ch == (byte) 0xE4)
					status = 2;
				else
					status = 0;
				break;
			case 2:
				frmlen = ch;
				if (frmlen <= 255) {
					frmlen -= 6;
					index++;
					index++;
					sensordata = new byte[frmlen];
					System.arraycopy(buffer, index, sensordata, 0, frmlen);
					index = index + frmlen;
					status = 3;
				} else
					status = 0;
				break;
			case 3:
				if (ch == 0x0A) {
					listener.Message(sensordata, frmlen);
				}
				status = 0;
				break;
			}
		}
	}

	/**
	 * get inputstream from the ClientSocketThread
	 * 
	 * @return InputStream
	 */
	private InputStream getInputStream() {
		try {
			return clientSocket.socket.getInputStream();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * get outputstream from ClientSocketThread
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException {
		return clientSocket.socket.getOutputStream();
	}
}
