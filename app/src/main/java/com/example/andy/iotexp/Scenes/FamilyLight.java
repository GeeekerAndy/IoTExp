package com.example.andy.iotexp.Scenes;

/**
 * using EXP_Bh1750 sensor
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.andy.iotexp.clientSocketBh1750.*;

public class FamilyLight {
    private final String TAG = "EX09_Bh1750Activity";
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[5];
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[])(msg.obj);
            if(data[0] == 0)
            {
                //Show this value on screen. ??????????
                int value =clientSocketTools.getInt(data, 1);
            }
        }
    };
    public FamilyLight() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                clientSocketThread = ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG,clientSocketTools.byte2hex(message, message_len) + "     len = " + message_len);
                        if (message_len == 5) {
                            System.arraycopy(message, 0, data, 0, 4);
                            handler.sendMessage(handler.obtainMessage(100, data));
                        }

                    }
                });
            }
        });
        t.start();
    }
}
