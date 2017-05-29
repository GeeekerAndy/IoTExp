package com.example.andy.iotexp.clientSocketMCPH;
/** message listener */
public interface MessageListener {
	public void Message(byte[] message, int message_len);
}
