package com.mamt.plumel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.mamt.plumel.view.ConteinerActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 1500;
    private boolean IsLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                IsLogin = prefs.getBoolean("IsLogin", false); // get value of last login status




                if (IsLogin == true) {

                    // Start the next activity
                    Intent SignInIntent = new Intent().setClass(SplashActivity.this, ConteinerActivity.class);
                    startActivity(SignInIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                } else {

                    // Start the next activity
                    Intent SignInIntent = new Intent().setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(SignInIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }





            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }
}
