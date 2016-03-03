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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class CategoryActivity extends BaseActivity {

    private Fragment mContent;
    private ArrayList<com.creativestudioaq.honggyu.Memkey.List> _lists2 = null;
    public static Activity CActivity;
    public Context mContext;
    final static String dbName = "Memo.db";
    final static int dbVersion = 1;
    int selectedPos = -1;
    static com.creativestudioaq.honggyu.Memkey.ListAdapter adapter;
    SQLiteDatabase db;
    String sql;
    Button btnWrite;
    DBHelper dbHelper;
    TextView text;
    String See;
    TextView textTitleBar;
    String realcategory;

    String Tag;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        // 메인 페이지 설정
        Intent intent = getIntent();
        realcategory = intent.getExtras().getString("category");

      if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null) {
            mContent = new MenuOneFragment();

        }

        setContentView(R.layout.content_frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent, Tag).commit();


        textTitleBar = (TextView) findViewById(R.id.textTitles);
        textTitleBar.setText(realcategory);


        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        CActivity = CategoryActivity.this;

        _lists2 = new ArrayList<com.creativestudioaq.honggyu.Memkey.List>();

        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo;";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    com.creativestudioaq.honggyu.Memkey.List s = new com.creativestudioaq.honggyu.Memkey.List(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                    if (realcategory.equals("전체보기")) {
                        _lists2.add(s);
                    } else if (cursor.getString(4).equals(realcategory)) {
                        _lists2.add(s);
                    }
                }
            } else {

            }
        } finally {

            cursor.close();
        }
        Collections.reverse(_lists2);


        adapter = new com.creativestudioaq.honggyu.Memkey.ListAdapter(this, R.layout.menuone_sub, _lists2);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewItemClickListener());
        listView.setOnItemLongClickListener(new ListViewItemLongClickListener());

        //new DataSetSync().execute();


        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

        // 슬라이딩 메뉴 커스터마이징
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        btnWrite = (Button) findViewById(R.id.btnWrite);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(CategoryActivity.this, WriteActivity.class);
                intent.putExtra("category", realcategory);
                startActivity(intent);

            }
        });


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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent5 = new Intent(CategoryActivity.this, MainActivity.class);
        startActivity(intent5);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(1, 1000);

    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent3 = new Intent(CategoryActivity.this, CategoryContentActivity.class);
            intent3.putExtra("value", position);
            intent3.putExtra("category", realcategory);
            startActivity(intent3);

        }

    }

    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            adapter.notifyDataSetChanged();
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);
            AlertDialog alert = alertDlg.create();
            Collections.reverse(_lists2);

            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db = dbHelper.getReadableDatabase();
                    sql = "SELECT * FROM Memo Where category='" + realcategory + "' ;";
                    Cursor cs = db.rawQuery(sql, null);

                    int count = cs.getCount();
                    int realposition = count - selectedPos - 1;
                    cs.moveToPosition(realposition);
                    int realid = cs.getInt(0);
                    String realphoto = cs.getString(2);
                    File originalfile = new File(realphoto);
                    originalfile.delete();
                    _lists2.remove(selectedPos);

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
            Collections.reverse(_lists2);


            return true;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        }
    }

}

