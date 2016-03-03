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
public class TipActivity extends SlidingFragmentActivity {

    public Context mContext;
    Button btnBack;
    TextView title2, versiontext, mailname, versiontext2, mailname2, versiontext3, mailname3;
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

        setContentView(R.layout.tip_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.tip_frame, mContent).commit();


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
        title2.setText("Information");

        versiontext = (TextView) findViewById(R.id.versiontext);
        versiontext.setText("검색은 내용과 작성 날짜 모두 초성으로도 가능합니다.");

        mailname = (TextView) findViewById(R.id.mailname);
        mailname.setText("ex) ㅇㄴ > 안녕,  2015.11 > 2015.11월에 작성된 모든 글");

        versiontext2 = (TextView) findViewById(R.id.versiontext2);
        versiontext2.setText("MemKey는 해킹의 위협에 노출되지 않습니다..");

        mailname2 = (TextView) findViewById(R.id.mailname2);
        mailname2.setText("어떠한 개인정보도 수집하지 않고 인터넷도 사용하지 않기 때문에 보안이 철저합니다. 이메일은 전송용이지 따로 수집하지 않습니다.");

        versiontext3 = (TextView) findViewById(R.id.versiontext3);
        versiontext3.setText("글과 카테고리의 삭제는 길게 누르고 있으면 할 수 있습니다.");

        mailname3 = (TextView) findViewById(R.id.mailname3);
        mailname3.setText("소중한 추억이 쉽게 지워지지 않게 여러 메모를 한번에 삭제하는 기능은 없습니다.");


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
