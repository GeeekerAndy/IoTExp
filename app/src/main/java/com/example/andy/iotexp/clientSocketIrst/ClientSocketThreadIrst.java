package com.example.andy.iotexp.clientSocketIrst;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketThreadIrst extends Thread {
	private Socket socket = null;
	private final String TAG = "Irst";
	private static ClientSocketThreadIrst clientSocket = null;
	private MessageListener listener;
	private final int buffer_size = 256;
	public byte[] sendBuffer = new byte[] { (byte) 0xFE, (byte) 0xE0, 0x09,
			0x58, 0x71, 0x00, 0x40, 0x00, 0x0A };

	/** get client socket thread instance */
	public static ClientSocketThreadIrst getClientSocket(String ip, int port) {
		if (clientSocket == null) {
			clientSocket = new ClientSocketThreadIrst(ip, port);
			clientSocket.start();
		}
		return clientSocket;
	}

	/** constructor */
	private ClientSocketThreadIrst(String ip, int port) {
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
		int len = buffer_size;
		int message_len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (!interrupted()) {
			try {
				this.getOutputStream().write(sendBuffer);
				while (len == buffer_size) {
					len = this.getInputStream().read(buffer, 0, buffer_size);
					baos.write(buffer);
					message_len += len;
				}
				if (len < buffer_size) {
					FrameFilter(baos.toByteArray(), message_len);
					len = buffer_size;
					message_len = 0;
					baos.reset();
				}

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
			try {
				sleep(1000);
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
				if (ch == (byte) 0xE0)
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
	 * get inputstream from the ClientSocketThreadIrst
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
	 * get outputstream from ClientSocketThreadIrst
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException {
		return clientSocket.socket.getOutputStream();
	}
}
