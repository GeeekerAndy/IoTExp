package com.example.andy.iotexp.Scenes;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.example.andy.iotexp.clientSocketMCPH.*;

/**
 * using EXP_MFS sensor
 */

public class FamilyNanny {
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[5];
    private final String TAG = "McphActivity";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[]) (msg.obj);
            if (data[0] == 0) {
                if(data[1] == 0x01) {
                    //dark ????
                } else {
                    //light ???
                }
                if(data[2] == 0x01) {
                    //Big noise. ????
                } else {
                    //Quiet. ????
                }
            }
        }
    };
    public FamilyNanny() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                clientSocketThread = ClientSocketThread.getClientSocket(
                        clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG,clientSocketTools.byte2hex(message, message_len) + "     len = " + message_len);
                        if (message_len == 5) {
                            System.arraycopy(message, 0, data, 0, 5);
                            handler.sendMessage(handler.obtainMessage(100, data));
                        }

                    }
                });
            }
        });
        t.start();
    }
}
