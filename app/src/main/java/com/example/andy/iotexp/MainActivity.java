/**
 * Package "clientSocket***" are tools to connect to the server.
 * Package "Scenes" includes several scenes in family.
 * Main activity to control all the work flow.
 */

package com.example.andy.iotexp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andy.iotexp.Scenes.FamilyLight;
import com.example.andy.iotexp.Scenes.FamilyNanny;
import com.example.andy.iotexp.Scenes.FamilySafe;
import com.example.andy.iotexp.Scenes.FamilyTemperature;
import com.example.andy.iotexp.Scenes.FamilyWeather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FamilySafe();
        new FamilyLight();
        new FamilyWeather();
        new FamilyTemperature();
        new FamilyNanny();
    }
}
