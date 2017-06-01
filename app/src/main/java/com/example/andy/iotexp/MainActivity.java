/**
 * Package "clientSocket***" are tools to connect to the server.
 * Package "Scenes" includes several scenes in family.
 * Main activity to control all the work flow.
 */

package com.example.andy.iotexp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andy.iotexp.Scenes.FamilyLight;
import com.example.andy.iotexp.Scenes.FamilyNanny;
import com.example.andy.iotexp.Scenes.FamilySafe;
import com.example.andy.iotexp.Scenes.FamilyTemperature;
import com.example.andy.iotexp.Scenes.FamilyWeather;

public class MainActivity extends AppCompatActivity {

//    public static volatile boolean isAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rl_main_background = (RelativeLayout)findViewById(R.id.rl_main_background);
        ImageView iv_sunny_rainy = (ImageView)findViewById(R.id.iv_sunny_rainy);
        ImageView iv_light_on_off = (ImageView)findViewById(R.id.iv_light_on_off);
        TextView tv_infrared_stat = (TextView)findViewById(R.id.tv_infrared_stat);
        TextView tv_weather = (TextView)findViewById(R.id.tv_weather);
        TextView tv_light_intensity = (TextView)findViewById(R.id.tv_light_intensity);
        TextView tv_temperature = (TextView)findViewById(R.id.tv_temperature);
        TextView tv_humidity = (TextView)findViewById(R.id.tv_humidity);
        TextView tv_light_stat = (TextView)findViewById(R.id.tv_light_stat);
        TextView tv_sound_stat = (TextView)findViewById(R.id.tv_sound_stat);

        new FamilySafe(tv_infrared_stat);
        new FamilyLight(this, rl_main_background, tv_light_intensity);
        new FamilyNanny(this, iv_light_on_off, tv_light_stat, tv_sound_stat);
        new FamilyWeather(iv_sunny_rainy, tv_weather);
        new FamilyTemperature(this, tv_temperature, tv_humidity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

//    public synchronized static boolean isAvailable() {
//        return isAvailable;
//    }

}
