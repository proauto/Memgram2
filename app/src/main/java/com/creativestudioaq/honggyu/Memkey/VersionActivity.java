package com.creativestudioaq.honggyu.Memkey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by honggyu on 2015-10-27.
 */
public class VersionActivity extends SlidingFragmentActivity {

    public Context mContext;
    Button btnBack;
    TextView title2, versiontext;
    private Fragment mContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        // 메인 페이지 설정
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new MenuOneFragment();

        setContentView(R.layout.version_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.version_frame, mContent).commit();


        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);

        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_version);

        getSupportActionBar().show();


        btnBack = (Button) findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                finish();
            }
        });

        title2 = (TextView) findViewById((R.id.textTitle4));
        title2.setText("Version");

        versiontext = (TextView) findViewById(R.id.versiontext);
        versiontext.setText("version : 1.0\n\n " +
                        "개발자 : H\n\n" +
                        "디자이너 : M\n\n" +
                        "Creativestudio AQ\n\n"
        );

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
