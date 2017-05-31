package com.example.andy.iotexp.Scenes;

/**
 * Zigbee: using EXP_Irst and EXP_LedBuzzer sensors
 */

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;
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
    private String TAG = "FamilySafe";
    private byte[] data = new byte[4];
    private TextView tv_infrared_stat;
    private Handler handlerIrst = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] data = (byte[]) (msg.obj);
            if (data[0] == 0) {

                if (data[1] == 0x01) {
                    //add LED and buzzer logic here
                    //此处添加触发警报和LED逻辑
                    clientSocketThreadLEDBuzzer.wBuffer[7] = 0x01;
                    clientSocketThreadLEDBuzzer.wBuffer[6] = 0x01;
                    tv_infrared_stat.setText("Is safe? No");
                    Log.e("FamilySafe", "infrared stopped");
                } else {
                    clientSocketThreadLEDBuzzer.wBuffer[7] = 0x00;
                    clientSocketThreadLEDBuzzer.wBuffer[6] = 0x00;
                    tv_infrared_stat.setText("Is safe? Yes");
                    Log.e("FamilySafe", "infrared beginning");
                }
                clientSocketThreadLEDBuzzer.isNewStatus = true;
            }
        }
    };

    //construction method
    public FamilySafe(TextView tv_infrared_stat) {
        this.tv_infrared_stat = tv_infrared_stat;
//        Thread threadLEDBuzzer = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    clientSocketThreadLEDBuzzer = ClientSocketThreadLEDBuzzer.getClientSocket(
//                            clientSocketToolsLEDBuzzer.getLocalIpAddress(), 6008);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        threadLEDBuzzer.start();
        Thread threadIrst = new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                try {
                    clientSocketThreadLEDBuzzer = ClientSocketThreadLEDBuzzer.getClientSocket(
                            clientSocketToolsLEDBuzzer.getLocalIpAddress(), 6008);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientSocketThreadIrstIrst = ClientSocketThreadIrst.getClientSocket(clientSocketToolsIrst.getLocalIpAddress(), 6008);
                clientSocketThreadIrstIrst.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
//                        if(MainActivity.isAvailable()) {
//                            MainActivity.isAvailable = false;
                        Log.e(TAG, clientSocketToolsIrst.byte2hex(message, message_len) + "	len = " + message_len);
                        if (message_len == 4) {
                            try {
                                sleep(1000);
                                System.arraycopy(message, 0, data, 0, 4);
                                handlerIrst.sendMessage(handlerIrst.obtainMessage(100, data));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
//                            MainActivity.isAvailable = true;
//                        }
                    }
                });
            }
        });
        threadIrst.start();
    }
}
