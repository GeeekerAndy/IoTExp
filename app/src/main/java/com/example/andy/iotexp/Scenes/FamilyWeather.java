package com.example.andy.iotexp.Scenes;

/**
 * Wifi: using EXP_RainSnow sensor
 */

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.iotexp.MainActivity;
import com.example.andy.iotexp.R;
import com.example.andy.iotexp.clientSocketRainSnow.*;

import static java.lang.Thread.sleep;

public class FamilyWeather {
    private final String TAG = "FamilyWeather";
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[4];
    private ImageView iv_sunny_rainy;
    private TextView tv_weather;
//    String uriSun = "@drawable/sun";
//    String uriRain = "@drawable/cloud_dark_rain";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[]) (msg.obj);
            if (data[0] == 0) {
                if (data[1] == 0x00) {
                    //set weather sunny.????
                    iv_sunny_rainy.setBackgroundResource(R.drawable.sun);
//                    iv_sunny_rainy.setImageDrawable(getClass().get);
                    tv_weather.setText("Weather: Sunny");
                } else if (data[1] == 0x01) {
                    //set weather rainny.????
                    iv_sunny_rainy.setBackgroundResource(R.drawable.cloud_dark_rain);
                    tv_weather.setText("Weather: Rainy");
                }
            }
        }
    };

    public FamilyWeather(ImageView iv_sunny_rainy, TextView tv_weather) {
        this.iv_sunny_rainy = iv_sunny_rainy;
        this.tv_weather = tv_weather;
        Thread t = new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                clientSocketThread = ClientSocketThread.getClientSocket(
                        clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG, clientSocketTools.byte2hex(message, message_len)
                                        + "     len = " + message_len);
                        if (message_len == 4) {
                            try {
                                sleep(1000);
                                System.arraycopy(message, 0, data, 0, 4);
                                handler.sendMessage(handler.obtainMessage(100, data));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

//                            if(MainActivity.isAvailable()) {
//                                MainActivity.isAvailable = false;

//                                MainActivity.isAvailable = true;
//                            } else {
//                                try {
//                                    sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
                        }

                    }
                });
            }
        });
        t.start();
    }
}
