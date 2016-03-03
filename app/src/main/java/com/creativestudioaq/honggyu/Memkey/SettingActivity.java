package com.creativestudioaq.honggyu.Memkey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by honggyu on 2015-10-27.
 */
public class SettingActivity extends SlidingFragmentActivity {

    public Context mContext;
    Button btnBack, btnLove;
    TextView title;
    private Fragment mContent;
    TextView version,password,information,background;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        // 메인 페이지 설정
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if(mContent==null)
            mContent= new com.creativestudioaq.honggyu.Memkey.MenuOneFragment();

        setContentView(R.layout.setting_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_frame, mContent).commit();


        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new com.creativestudioaq.honggyu.Memkey.MenuFragment()).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);



        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_setting);

        getSupportActionBar().show();

        // 메뉴 토글 버튼 추가
        btnBack = (Button) findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, com.creativestudioaq.honggyu.Memkey.MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnLove = (Button) findViewById(R.id.btnLove);
        btnLove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Toast.makeText(SettingActivity.this, "사랑합니다 고객님~♥", Toast.LENGTH_LONG).show();
            }
        });

        title = (TextView)findViewById((R.id.textTitle4));
        title.setText("설정");

        version = (TextView)findViewById(R.id.version);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(SettingActivity.this, VersionActivity.class);
                startActivity(intent2);
            }
        });
        password = (TextView)findViewById(R.id.password);
        password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(SettingActivity.this, com.creativestudioaq.honggyu.Memkey.PasswordActivity.class);
                startActivity(intent3);
            }
        });

        information = (TextView)findViewById(R.id.information);
        information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent4 = new Intent(SettingActivity.this, TipActivity.class);
                startActivity(intent4);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent(SettingActivity.this, com.creativestudioaq.honggyu.Memkey.MainActivity.class);
        startActivity(intent4);
        super.onBackPressed();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        super.onActivityResult(requestCode, resultCode, Data);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}
