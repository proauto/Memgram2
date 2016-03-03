package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends BaseActivity {

    private Fragment mContent;
    private ArrayList<List> _lists = null;
    public static Activity AActivity;
    public Context mContext;
    final static String dbName = "Memo.db";
    final static int dbVersion = 1;
    int selectedPos = -1;
    static ListAdapter adapter;
    SQLiteDatabase db;
    String sql;
    Button btnWrite;
    com.creativestudioaq.honggyu.Memkey.DBHelper dbHelper;
    String Tag;
    private com.creativestudioaq.honggyu.Memkey.BackPressCloseHandler backPressCloseHandler;
    SharedPreferences setting;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        // 메인 페이지 설정
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new com.creativestudioaq.honggyu.Memkey.MenuFirstFragment();

        setContentView(com.creativestudioaq.honggyu.Memkey.R.layout.content_frame);

        getSupportFragmentManager().beginTransaction().replace(com.creativestudioaq.honggyu.Memkey.R.id.content_frame, mContent, Tag).commit();

        //뒤로 가기 버튼 구현
        backPressCloseHandler = new com.creativestudioaq.honggyu.Memkey.BackPressCloseHandler(this);

        //dbHelper 호출 및 데이터 List 안에 저장
        dbHelper = new com.creativestudioaq.honggyu.Memkey.DBHelper(this, dbName, null, dbVersion);
        AActivity = MainActivity.this;
        setting = getSharedPreferences("setting", 0);

        _lists = new ArrayList<List>();

        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo;";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    List s = new List(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

                    _lists.add(s);


                }
            } else {

            }
        } finally {
            cursor.close();
        }
        Collections.reverse(_lists);

        //Listview 구현
        int duration = 800;
        int pixelsToMove = 0;

        adapter = new ListAdapter(this, com.creativestudioaq.honggyu.Memkey.R.layout.menuone_sub, _lists);

        ListView listView = (ListView) findViewById(com.creativestudioaq.honggyu.Memkey.R.id.list);
        listView.smoothScrollBy(pixelsToMove, duration);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewItemClickListener());
        listView.setOnItemLongClickListener(new ListViewItemLongClickListener());


        // 메뉴 설정
        setBehindContentView(com.creativestudioaq.honggyu.Memkey.R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(com.creativestudioaq.honggyu.Memkey.R.id.menu_frame, new com.creativestudioaq.honggyu.Memkey.MenuFragment()).commit();

        // 슬라이딩 메뉴 커스터마이징
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(com.creativestudioaq.honggyu.Memkey.R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //글쓰기 버튼 구현
        btnWrite = (Button) findViewById(com.creativestudioaq.honggyu.Memkey.R.id.btnWrite);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, com.creativestudioaq.honggyu.Memkey.WriteActivity.class);
                startActivity(intent);

            }
        });
/*
        //background 설정
        if(setting.getString("background", "").equals("MemKey기본")){
            findViewById(R.id.content_frame).setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.background)));
        }else  if(setting.getString("background", "").equals("SimpleWhite")){
            findViewById(R.id.content_frame).setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.whitebackground)));
        }else  if(setting.getString("background", "").equals("SimpleBlack")) {
            findViewById(R.id.content_frame).setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.blackbackground)));
        }else  if(setting.getString("background", "").equals("SimpleBlue")){
            findViewById(R.id.content_frame).setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.bluebackground)));
        }else{
            findViewById(R.id.content_frame).setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.background)));
        }
*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleView(findViewById(R.id.content_frame));
    }

    private void recycleView(View view) {
        if(view != null) {
            Drawable bg = view.getBackground();
            if(bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable)bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
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
    //ListView 클릭 시 작업

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent3 = new Intent(MainActivity.this, com.creativestudioaq.honggyu.Memkey.ContentActivity.class);
            intent3.putExtra("value", position);
            startActivity(intent3);

        }

    }
    //ListView 길게 클릭 시 작업
    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


            adapter.notifyDataSetChanged();
            //item 위치 얻기
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);
            AlertDialog alert = alertDlg.create();
            //listview의 아이템들을 오래 된 순서대로 밑에서 부터 출력하기 위해 list를 뒤집는다
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
                    String realphoto = cs.getString(2);
                    File originalfile = new File(realphoto);
                    originalfile.delete();
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

    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        }
    }


}
//List Class
class List {
    private String _day;
    private String _photo;
    private String _memo;
    private String _category;

    public String getday() {
        return _day;
    }

    public String getPhoto() {
        return _photo;
    }

    public String getMemo() {
        return _memo;
    }

    public String getCategory() {
        return _category;
    }

    public List(String day, String photo, String memo, String category) {
        _day = day;
        _photo = photo;
        _memo = memo;
        _category = category;
    }
}

//List Adapter Class
class ListAdapter extends BaseAdapter {


    private LayoutInflater _inflater;
    private static ArrayList<List> _lists;
    private int _layout;
    private static Context m_ctx;
    private AsyncTask<Void, Void, Bitmap> mTask;


    public ListAdapter(Context context, int layout, ArrayList<List> lists) {
        _inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        _lists = lists;
        _layout = layout;
        m_ctx = context;

    }

    @Override
    public int getCount() {
        return _lists.size();
    }

    @Override
    public String getItem(int position) {
        return _lists.get(position).getday();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = _inflater.inflate(_layout, parent, false);

            holder = new ViewHolder();
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.memo = (TextView) convertView.findViewById(R.id.memo);
            holder.voidbar1 = (TextView) convertView.findViewById(R.id.voidbar1);
            holder.voidbar2 = (TextView) convertView.findViewById(R.id.voidbar2);
            holder.voidbar3 = (TextView) convertView.findViewById(R.id.voidbar3);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.position = position;
        mTask = new ThumbnailTask(position, holder, m_ctx);
        mTask.execute();


        return convertView;
    }


    private static class ThumbnailTask extends AsyncTask<Void, Void, Bitmap> {
        private int mPosition;
        private ViewHolder mHolder;
        private List list;
        int width;


        public ThumbnailTask(int position, ViewHolder holder, Context context) {
            mPosition = position;
            mHolder = holder;
            width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            list = _lists.get(mPosition);

            mHolder.photo.setVisibility(View.INVISIBLE);
            mHolder.day.setVisibility(View.INVISIBLE);
            mHolder.memo.setVisibility(View.INVISIBLE);
            mHolder.voidbar1.setVisibility(View.INVISIBLE);
            mHolder.voidbar2.setVisibility(View.INVISIBLE);
            mHolder.voidbar3.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Bitmap doInBackground(Void... arg0) {

            Bitmap bm = null;
            String imagePath = list.getPhoto();

            if (list.getPhoto().equals("null")) {

            } else {

                try {
                    Log.v("try",""+imagePath);
                    bm = BitmapFactory.decodeFile(imagePath);
                } catch (Exception e) {
                    Log.v("catch",""+imagePath);
                    e.printStackTrace();
                }



            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bm) {

            if (mHolder.position == mPosition) {

                mHolder.photo.setVisibility(View.VISIBLE);
                mHolder.day.setVisibility(View.VISIBLE);
                mHolder.memo.setVisibility(View.VISIBLE);
                mHolder.voidbar1.setVisibility(View.VISIBLE);
                mHolder.voidbar2.setVisibility(View.VISIBLE);
                mHolder.voidbar3.setVisibility(View.VISIBLE);

                if(list.getday()!=null) {
                    mHolder.day.setText(list.getday());
                }else {
                    mHolder.day.setText(null);
                }

                if(list.getMemo()!=null) {
                    mHolder.memo.setText(list.getMemo());
                }else{
                    mHolder.memo.setText(null);
                }

                if (isCancelled()) {
                    bm = null;
                }



                if (bm != null) {
                    mHolder.photo.setImageBitmap(bm);

                } else {
                    mHolder.photo.setImageDrawable(null);
                }

            }

        }


    }

    private static class ViewHolder {
        public ImageView photo;
        public TextView day, voidbar1, voidbar2, voidbar3;
        public TextView memo;
        public int position;

    }


}

