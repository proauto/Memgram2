package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by honggyu on 2015-10-28.
 */

public class PasswordSplash extends Activity {

    TextView gotopassword, hinttopassword, emailgogo;
    EditText insertpassword;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String password1, hint1;
    String id;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_password);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        id = setting.getString("ID", "");

        gotopassword = (TextView) findViewById(R.id.gotopassword);
        gotopassword.setText("비밀번호");

        insertpassword = (EditText) findViewById(R.id.insertpassword);
        insertpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        password1 = insertpassword.getText().toString();

                        if (password1.equals(setting.getString("ID", ""))) {
                            Intent intent = new Intent(PasswordSplash.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PasswordSplash.this, "비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });

        hinttopassword = (TextView) findViewById(R.id.hinttopassword);
        hint1 = setting.getString("Hint", "");
        hinttopassword.setText(hint1);

        dialog = new ProgressDialog(PasswordSplash.this);
        emailgogo = (TextView) findViewById(R.id.emailgogo);

        emailgogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setting.getString("Email", "").equals("")) {
                    new ProcessEmailTask().execute(null, null, null);




                }
            }

        });
    }


    class ProcessEmailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("전송 중입니다. 잠시 기다려주세요.");
                dialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            GMailSender sender = new GMailSender("creativestudioaq@gmail.com", "alsdk9401");
            try {
                sender.sendMail(
                        "[ MemKey 비밀번호 ]",
                        "\n\n안녕하세요 MemKey 비밀번호 잊으셔서 많이 놀라셨죠 ? \n\n당신의 비밀번호 입니다. [" + setting.getString("ID", "") + "]",
                        "hg1771@gmail.com",
                        setting.getString("Email", "")
                );
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(PasswordSplash.this, "Email 전송되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
