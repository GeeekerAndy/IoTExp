package com.example.andy.iotexp.Scenes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.iotexp.R;
import com.example.andy.iotexp.clientSocketMCPH.*;

/**
 * using EXP_MFS sensor
 */

public class FamilyNanny {
    private ClientSocketThread clientSocketThread;
    private byte[] data = new byte[5];
    private final String TAG = "McphActivity";
    private Context context;
    private ImageView iv_light_on_off;
    private TextView tv_light_stat;
    private TextView tv_sound_stat;
    final Ringtone r;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[]) (msg.obj);
            if (data[0] == 0) {
                if(data[1] == 0x01) {
                    //dark ????
                    iv_light_on_off.setBackgroundResource(R.drawable.lightbulb);
                    tv_light_stat.setText("??????");
                } else {
                    //light ???
                    iv_light_on_off.setBackgroundResource(R.drawable.darkbulb);
                    tv_light_stat.setText("??????");
                }
                if(data[2] == 0x01) {
                    //Big noise. ????
                    tv_sound_stat.setText("??????");
                    r.play();
                } else {
                    //Quiet. ????
                    tv_sound_stat.setText("??????");
                    r.stop();
                }
            }
        }
    };
    public FamilyNanny(Context context, ImageView iv_light_on_off, TextView tv_light_stat, TextView tv_sound_stat) {
        this.iv_light_on_off = iv_light_on_off;
        this.tv_light_stat = tv_light_stat;
        this.tv_sound_stat = tv_sound_stat;
        this.context = context;
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(context, notification);

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
