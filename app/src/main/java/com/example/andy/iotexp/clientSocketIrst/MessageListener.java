package com.example.andy.iotexp.clientSocketIrst;
/** message listener */
public interface MessageListener {
	public void Message(byte[] message, int message_len);
}
