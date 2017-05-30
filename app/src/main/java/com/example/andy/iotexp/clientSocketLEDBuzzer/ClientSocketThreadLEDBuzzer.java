package com.example.andy.iotexp.clientSocketLEDBuzzer;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketThreadLEDBuzzer extends Thread {
	private Socket socket = null;
	private final String TAG = "LEDBuzzer";
	private static ClientSocketThreadLEDBuzzer clientSocket = null;
	public Boolean isNewStatus = false;
	public byte wBuffer[] = new byte[] { (byte) 0xFE, (byte) 0xE0, 0x0B, 0x58,
			0x72, 0x00, 0x00, 0x00, 0x70, 0x00, 0x0A };

	/** get client socket thread instance 
	 * @throws IOException
	 * @throws UnknownHostException */
	public static ClientSocketThreadLEDBuzzer getClientSocket(String ip, int port) throws UnknownHostException, IOException {
		if (clientSocket == null) {
			clientSocket = new ClientSocketThreadLEDBuzzer(ip, port);
			clientSocket.start();
		}
		return clientSocket;
	}

	/** constructor 
	 * @throws IOException
	 * @throws UnknownHostException */
	private ClientSocketThreadLEDBuzzer(String ip, int port) throws UnknownHostException, IOException {
			socket = new Socket(ip, port);
	}

	@Override
	public void run() {
		while (!interrupted()) {
			if (isNewStatus) {
				try {
					this.getOutputStream().write(wBuffer);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
					e.printStackTrace();
				}
				isNewStatus = false;
			}
			try {
				sleep(10);
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * get outputstream from ClientSocketThreadLEDBuzzer
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException {
		return clientSocket.socket.getOutputStream();
	}
}
