package com.creativestudioaq.honggyu.Memkey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by honggyu on 2015-10-27.
 */
public class PasswordActivity extends SlidingFragmentActivity {

    public Context mContext;
    Button btnBack, savedatareal;
    TextView title3, passwordtext, passwordinsert, passwordhint, hinttext, emailhint, emailtext;
    private Fragment mContent;
    EditText inputthings, inputhint, inputemail;
    Switch switch1;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String password, hint, email;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        // 메인 페이지 설정
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new MenuOneFragment();

        setContentView(R.layout.password_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.password_frame, mContent).commit();


        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();


        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);

        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_password);

        getSupportActionBar().show();


        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        switch1 = (Switch) findViewById(R.id.switch1);
        if (setting.getBoolean("onoff", false)) {
            switch1.setChecked((true));
        } else {
            switch1.setChecked(false);
        }

        title3 = (TextView) findViewById((R.id.textTitle4));
        title3.setText("Password");

        btnBack = (Button) findViewById(R.id.btnBack4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnBack.setSelected(true);
                finish();
            }
        });


        passwordinsert = (TextView) findViewById(R.id.passwordinsert);
        passwordinsert.setText("비밀번호");

        inputthings = (EditText) findViewById(R.id.inputthings);
        inputthings.setText(setting.getString("ID", ""));


        passwordtext = (TextView) findViewById(R.id.passwordtext);
        passwordtext.setText("비밀번호는 8자리 이하의 숫자로 설정해주세요.");

        passwordhint = (TextView) findViewById(R.id.passwordhint);
        passwordhint.setText("비밀번호 힌트");

        inputhint = (EditText) findViewById(R.id.inputhint);
        inputhint.setText(setting.getString("Hint", ""));


        hinttext = (TextView) findViewById(R.id.hinttext);
        hinttext.setText("비밀번호 힌트는 20자 이하로 설정해주세요.");


        emailhint = (TextView) findViewById(R.id.emailhint);
        emailhint.setText("이메일 입력");

        inputemail = (EditText) findViewById(R.id.inputemail);
        inputemail.setText(setting.getString("Email", ""));


        emailtext = (TextView) findViewById(R.id.emailtext);
        emailtext.setText("혹시 비밀번호를 잊을 수 도 있으니 이메일을 입력해주세요.");


        switch1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = String.valueOf(isChecked);

                editor.putBoolean("onoff", isChecked);
                password = inputthings.getText().toString();
                hint = inputhint.getText().toString();
                editor.putString("Hint", hint);
                editor.putString("ID", password);
                email = inputemail.getText().toString();
                editor.putString("Email", email);
                editor.commit();

            }

        });

        savedatareal = (Button) findViewById(R.id.btnSavereal);
        savedatareal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savedatareal.setSelected(true);
                password = inputthings.getText().toString();
                hint = inputhint.getText().toString();
                editor.putString("Hint", hint);
                editor.putString("ID", password);
                email = inputemail.getText().toString();
                editor.putString("Email", email);
                editor.commit();

                Toast.makeText(PasswordActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();


            }
        });


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
