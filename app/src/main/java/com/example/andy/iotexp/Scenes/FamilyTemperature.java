package com.example.andy.iotexp.Scenes;

/**
 * using EXP_SHT11 sensor
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.andy.iotexp.clientSocketSHT11.*;

public class FamilyTemperature {
    private static final String TAG = "EX01_SHT11Activity";
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
            String temperature = Float.toString(temp).substring(0, 4) + "?C";
            String humidity = Float.toString(humi).substring(0, 4) + "%";
            tv_temperature.setText(temperature);
            tv_humidity.setText(humidity);
            if(humi > 40) {
                Toast.makeText(context, "?????", Toast.LENGTH_SHORT).show();
            }
            if(temp > 30.0) {
                Toast.makeText(context, "??????", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public FamilyTemperature(Context context, TextView tv_temperature, TextView tv_humidity) {
        this.context = context;
        this.tv_temperature = tv_temperature;
        this.tv_humidity = tv_humidity;
        Thread t = new Thread(new Runnable() {
            public void run() {
                clientSocketThread = ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), 6008);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        Log.e(TAG,clientSocketTools.byte2hex(message, message_len) + "     len = " + message_len);
                        if (message_len == 11) {
                            System.arraycopy(message, 1, data, 0, 8);
                            handler.sendMessage(handler.obtainMessage(100, data));
                        }

                    }
                });
            }
        });
        t.start();
    }
}
