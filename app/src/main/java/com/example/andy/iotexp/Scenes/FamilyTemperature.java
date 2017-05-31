package com.example.andy.iotexp.Scenes;

/**
 * Zigbee: using EXP_SHT11 sensor
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.andy.iotexp.MainActivity;
import com.example.andy.iotexp.clientSocketSHT11.*;

import static java.lang.Thread.sleep;

public class FamilyTemperature {
    private static final String TAG = "FamilyTemperature";
    private byte[] data = new byte[8];
    private ClientSocketThread clientSocketThread;
    private Context context;
    private TextView tv_temperature;
    private TextView tv_humidity;
    private Handler handler = new Handler() {
        float temp, humi;
        @Override
        public void handleMessage(Message msg) {
            humi = clientSocketTools.byte2float((byte[]) (msg.obj), 0);
            temp = clientSocketTools.byte2float((byte[]) (msg.obj), 4);
            String temperature = "Temperature: " + Float.toString(temp).substring(0, 4) + "ºC";
            String humidity = "Humidity:" + Float.toString(humi).substring(0, 4) + "%";
            tv_temperature.setText(temperature);
            tv_humidity.setText(humidity);
            if(humi > 40 || humi < 10) {
                Toast.makeText(context, "Dehumidifier launching...", Toast.LENGTH_SHORT).show();
            }
            if(temp > 30.0 || temp < 5.0) {
                Toast.makeText(context, "Air-conditioning launching...", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public FamilyTemperature(Context context, TextView tv_temperature, TextView tv_humidity) {
        this.context = context;
        this.tv_temperature = tv_temperature;
        this.tv_humidity = tv_humidity;
        Thread t = new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                clientSocketThread = ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG,clientSocketTools.byte2hex(message, message_len) + "     len = " + message_len);
                        if (message_len == 11) {
                            try {
                                sleep(1000);
                                System.arraycopy(message, 1, data, 0, 8);
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
