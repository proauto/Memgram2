package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends Activity {

    SharedPreferences setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setting = getSharedPreferences("setting", 0);


        if (setting.getBoolean("onoff", false)) {
            startActivity(new Intent(this, PasswordSplash.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
