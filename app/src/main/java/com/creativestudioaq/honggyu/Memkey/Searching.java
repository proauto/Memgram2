package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Collections;

public class Searching extends BaseActivity {

    private Fragment mContent;
    private ArrayList<com.creativestudioaq.honggyu.Memkey.List> _lists = null;
    public static Activity AActivity;
    public Context mContext;
    final static String dbName = "Memo.db";
    final static int dbVersion = 1;
    int selectedPos = -1;
    static com.creativestudioaq.honggyu.Memkey.ListAdapter adapter;
    SQLiteDatabase db;
    String sql;
    DBHelper dbHelper;
    String Tag;
    Button btnBack5;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        // 메인 페이지 설정
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new MenuOneFragment();

        setContentView(R.layout.content_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent, Tag).commit();
        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_searching);


        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        AActivity = Searching.this;

        _lists = new ArrayList<com.creativestudioaq.honggyu.Memkey.List>();

        ArrayList<Integer> Intitems;

        Intitems = getIntent().getIntegerArrayListExtra("search");
        int n = Intitems.size();

        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo;";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    com.creativestudioaq.honggyu.Memkey.List s = new com.creativestudioaq.honggyu.Memkey.List(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));


                    for (int x = 0; x < n; x++) {

                        if (cursor.getInt(0) == Intitems.get(x)) {
                            _lists.add(s);
                            Log.v("", "" + Intitems.get(x));
                        }
                    }

                }
            } else {

            }
        } finally {
            cursor.close();
        }
        Collections.reverse(_lists);


        adapter = new com.creativestudioaq.honggyu.Memkey.ListAdapter(this, R.layout.menuone_sub, _lists);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewItemClickListener());
        listView.setOnItemLongClickListener(new ListViewItemLongClickListener());


        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

        // 슬라이딩 메뉴 커스터마이징
        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);


        TextView textTitleBar = (TextView) findViewById(R.id.textTitle);
        textTitleBar.setText("검색결과");


        btnBack5 = (Button) findViewById(R.id.btnBack5);
        btnBack5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent4 = new Intent(Searching.this, MainActivity.class);
                startActivity(intent4);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Intent intent8 = new Intent(Searching.this, MainActivity.class);
        startActivity(intent8);
        finish();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:


                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(1, 1000);

    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _lists.get(position);

            db = dbHelper.getReadableDatabase();
            sql = "SELECT * FROM Memo;";
            Cursor cursor = db.rawQuery(sql, null);
            try {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {


                        if ((cursor.getString(4).equals(_lists.get(position).getCategory())) && (cursor.getString(3).equals(_lists.get(position).getMemo())) && (cursor.getString(1).equals(_lists.get(position).getday())) && (cursor.getString(2).equals(_lists.get(position).getPhoto()))) {


                            Intent intent3 = new Intent(Searching.this, ContentActivity.class);
                            intent3.putExtra("searching", cursor.getInt(0));
                            startActivity(intent3);


                        }


                    }
                } else {

                }
            } finally {
                cursor.close();
            }


        }

    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            adapter.notifyDataSetChanged();
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);
            AlertDialog alert = alertDlg.create();
            Collections.reverse(_lists);
            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db = dbHelper.getReadableDatabase();
                    sql = "SELECT * FROM Memo;";
                    Cursor cs = db.rawQuery(sql, null);
                    int count = cs.getCount();
                    int realposition = count - selectedPos - 1;
                    cs.moveToPosition(realposition);
                    int realid = cs.getInt(0);
                    _lists.remove(selectedPos);


                    sql = "DELETE FROM Memo WHERE _id=" + realid + ";";
                    db.execSQL(sql);
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    adapter.notifyDataSetChanged();
                    cs.close();
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            // '아니오' 버튼이 클릭되면
            alertDlg.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });
            alertDlg.setMessage("글을 삭제 하시겠습니까?");
            alertDlg.show();
            Collections.reverse(_lists);


            return true;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);

    }


}

