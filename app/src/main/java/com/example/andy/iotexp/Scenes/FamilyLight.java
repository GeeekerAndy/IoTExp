package com.example.andy.iotexp.Scenes;

/**
 * using EXP_Bh1750 sensor
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andy.iotexp.MainActivity;
import com.example.andy.iotexp.R;
import com.example.andy.iotexp.clientSocketBh1750.*;

import static java.security.AccessController.getContext;

public class FamilyLight {
    private final String TAG = "EX09_Bh1750Activity";
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[5];
    private RelativeLayout rl_main_background;
    private Context context;
    private TextView tv_light_intensity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[])(msg.obj);
            if(data[0] == 0)
            {
                //Show this value on screen. ??????????
                int value =clientSocketTools.getInt(data, 1);
                tv_light_intensity.setText("?????" + value + "lux");
                if(value < 100) {
                    //Increase the screen brightness
                    rl_main_background.setBackgroundResource(R.drawable.interior_design_bright);

                } else {
                    //restore the screen brightness. ??????
                    rl_main_background.setBackgroundResource(R.drawable.interior_design_normal);
                }
            }
        }
    };
    public FamilyLight(Context context, RelativeLayout relativelayout, TextView tv_light_intensity) {
        this.context = context;
        this.rl_main_background = relativelayout;
        this.tv_light_intensity = tv_light_intensity;
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
