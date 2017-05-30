package com.example.andy.iotexp.Scenes;

/**
 * using EXP_Irst and EXP_LedBuzzer sensors
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.andy.iotexp.MainActivity;
import com.example.andy.iotexp.clientSocketIrst.*;
import com.example.andy.iotexp.clientSocketLEDBuzzer.*;

import java.io.IOException;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class FamilySafe {
    private ClientSocketThreadIrst clientSocketThreadIrstIrst;
    private ClientSocketThreadLEDBuzzer clientSocketThreadLEDBuzzer;
    private byte[] data = new byte[4];
    private Handler handlerIrst = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] data = (byte[])(msg.obj);
            if(data[0] == 0)
            {

                if(data[1] == 0x01) {
                    //add LED and buzzer logic here
                    //此处添加触发警报和LED逻辑
                    clientSocketThreadLEDBuzzer.wBuffer[7] = 0x01;
                    clientSocketThreadLEDBuzzer.wBuffer[6] = 0x01;
                    Log.e("FamilySafe", "infrared stopped");
                } else {
                    clientSocketThreadLEDBuzzer.wBuffer[7] = 0x00;
                    clientSocketThreadLEDBuzzer.wBuffer[6] = 0x00;
                    Log.e("FamilySafe", "infrared beginning");
                }
                clientSocketThreadLEDBuzzer.isNewStatus = true;
            }
        }
    };

    //construction method
    public FamilySafe() {
        Thread threadLEDBuzzer = new Thread(new Runnable() {
            public void run() {
                try {
                    clientSocketThreadLEDBuzzer = ClientSocketThreadLEDBuzzer.getClientSocket(
                            clientSocketToolsLEDBuzzer.getLocalIpAddress(), 6008);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadLEDBuzzer.start();
        Thread threadIrst = new Thread(new Runnable() {
            public void run() {
                clientSocketThreadIrstIrst = ClientSocketThreadIrst.getClientSocket(clientSocketToolsIrst.getLocalIpAddress(), 6008);
                clientSocketThreadIrstIrst.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        //Log.e(TAG,clientSocketToolsIrst.byte2hex(message, message_len) + "	len = " + message_len);
                        if (message_len == 4) {
                            System.arraycopy(message, 0, data, 0, 4);
                            handlerIrst.sendMessage(handlerIrst.obtainMessage(100, data));
                        }
                    }
                });
            }
        });
        threadIrst.start();
    }
}
