package com.example.andy.iotexp.Scenes;

/**
 * using EXP_RainSnow sensor
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.andy.iotexp.clientSocketRainSnow.*;

public class FamilyWeather {
    private final String TAG = "EXP_RainSnowActivity";
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[4];
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[]) (msg.obj);
            if (data[0] == 0) {
                if (data[1] == 0x00) {
                    //set weather sunny.????
                } else if (data[1] == 0x01) {
                    //set weather rainny.????
                }
            }
        }
    };

    public FamilyWeather() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                clientSocketThread = ClientSocketThread.getClientSocket(
                        clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG,
                                clientSocketTools
                                        .byte2hex(message, message_len)
                                        + "     len = " + message_len);
                        if (message_len == 4) {
                            System.arraycopy(message, 0, data, 0, 4);
                            handler.sendMessage(handler
                                    .obtainMessage(100, data));
                        }

                    }
                });
            }
        });
        t.start();
    }
}
