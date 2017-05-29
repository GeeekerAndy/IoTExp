package com.example.andy.iotexp.clientSocketSHT11;
/** message listener */
public interface MessageListener {
	public void Message(byte[] message, int message_len);
}
