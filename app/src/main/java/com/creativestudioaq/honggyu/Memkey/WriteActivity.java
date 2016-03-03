package com.creativestudioaq.honggyu.Memkey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by honggyu on 2015-09-27.
 */
public class WriteActivity extends SlidingFragmentActivity implements AdapterView.OnItemSelectedListener {
    public static final int REQUEST_CODE_PICKALBUM = 101;
    private Fragment mContent;
    private String mImgPath;
    final static String dbName = "Memo.db";
    final static String dbName2 = "category.db";
    final static int dbVersion = 1;
    ArrayList<String> spinnerlist;
    String strDate;
    EditText content;
    TextView Datepick;
    Button btnBack;
    Button btnSave;
    Button btnPhoto;
    com.creativestudioaq.honggyu.Memkey.DBHelper dbHelper;
    DBHelper2 dbHelper2;
    String category;
    ImageView simpleview;
    int width;
    String realcategory;
    Bitmap image_bitmap;
    int n = 0, login = 0;
    ProgressDialog dialog;
    private Dialog d;
    Intent intent2;
    String realpath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        width = getWindowManager().getDefaultDisplay().getWidth();
        dbHelper = new com.creativestudioaq.honggyu.Memkey.DBHelper(this, dbName, null, dbVersion);
        dbHelper2 = new DBHelper2(this, dbName2, null, dbVersion);

        Intent intent6 = getIntent();
        String test2 = getIntent().getStringExtra("category");
        if (test2 != null) {
            realcategory = intent6.getExtras().getString("category");
        } else {

        }


        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_write);

        getSupportActionBar().show();

        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new com.creativestudioaq.honggyu.Memkey.MenuWriteFragment();

        setContentView(R.layout.activity_write);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_write, mContent).commit();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        strDate = dateFormat.format(date);

        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        Spinner();

        // 슬라이딩 메뉴 커스터마이징
        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);
        // sm.setShadowWidthRes(R.dimen.shadow_width);
        // sm.setShadowDrawable(R.drawable.shadow);
        // sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //  sm.setFadeDegree(0.00f);
        //  sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 메뉴 토글 버튼 추가
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnPhoto = (Button) findViewById(R.id.photoselect);
        simpleview = (ImageView) findViewById(R.id.SimpleView);
        content = (EditText) findViewById(R.id.content);
        Datepick = (TextView) findViewById(R.id.date);
        Datepick.setText(strDate);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db;
                String sql;
                switch (v.getId()) {
                    case R.id.btnBack:


                        AlertDialog.Builder alert = new AlertDialog.Builder(WriteActivity.this);

                        alert.setTitle("글쓰기를 취소하시겠습니까?");


                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();

                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        });

                        alert.show();


                        break;
                    case R.id.btnSave:

                        String contents2 = content.getText().toString();
                        if (contents2.trim().length() != 0 || image_bitmap != null) {
                            btnSave.setSelected(true);

                            CheckTypesTask task = new CheckTypesTask();
                            task.execute();

                        } else {
                            Toast.makeText(WriteActivity.this, "아무것도 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.photoselect:


                        Intent intent3 = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent3.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(Intent.createChooser(intent3, "앨범에서 불러오기"),
                                REQUEST_CODE_PICKALBUM);


                        break;
                }

            }
        };
        btnBack.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnPhoto.setOnClickListener(listener);

        dialog = new ProgressDialog(WriteActivity.this);


        if (getIntent().getExtras() != null) {
            Uri uri = getIntent().getExtras().getParcelable(Intent.EXTRA_STREAM);
            sharefunction(uri);

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (image_bitmap != null) {
            image_bitmap.recycle();
        }
    }


    void sharefunction(Uri uri) {
        try {

            Uri mImageUri = uri;
            String realpaths = getRealImagePath(mImageUri);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            image_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
            int resizedWidth = width;
            int resizedHeight = width * image_bitmap.getHeight() / image_bitmap.getWidth();
            image_bitmap = Bitmap.createScaledBitmap(image_bitmap, resizedWidth, resizedHeight, true);

            ExifInterface exif = new ExifInterface(realpaths);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            image_bitmap = rotate(image_bitmap, exifDegree);

            // 이미지 Path 취득

            //imageset
            simpleview.setImageBitmap(image_bitmap);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            //dialog 띄우기
            if (image_bitmap != null) {
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("저장 중입니다. 잠시 기다려주세요.");
                // show dialog
                dialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //이미지 저장 작업 > n과 login은 버튼이 여러번 눌리는 것을 막기 위한 장치입니다.

            if (n == 0) {
                intent2 = new Intent(WriteActivity.this, com.creativestudioaq.honggyu.Memkey.MainActivity.class);


                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.memkey/MemKey";
                String mi = System.currentTimeMillis() + ".png";
                realpath = dirPath + "/" + mi;


                String str = Environment.getExternalStorageState();
                if (str.equals(Environment.MEDIA_MOUNTED)) {
                    OutputStream outputStream = null;


                    File file = new File(dirPath);


                    if (!file.exists()) {  // 원하는 경로에 폴더가 있는지 확인
                        file.mkdirs();
                    }

                    try {

                        outputStream = new FileOutputStream(dirPath + "/" + mi);

                        image_bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();


                    } catch (Exception e) {

                    }


                } else {
                    Toast.makeText(WriteActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
                }


                n++;
                login++;
            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);


            //db에 저장하는 작업
            if (login == 1) {
                com.creativestudioaq.honggyu.Memkey.MainActivity aActivity = (com.creativestudioaq.honggyu.Memkey.MainActivity) com.creativestudioaq.honggyu.Memkey.MainActivity.AActivity;
                com.creativestudioaq.honggyu.Memkey.CategoryActivity bActivity = (com.creativestudioaq.honggyu.Memkey.CategoryActivity) com.creativestudioaq.honggyu.Memkey.CategoryActivity.CActivity;


                String contents = content.getText().toString();
                contents = contents.replace("'", "''");

                db = dbHelper.getWritableDatabase();
                sql = String.format("INSERT INTO Memo VALUES(NULL,'%s','%s','%s','%s');", strDate, realpath, contents, category);
                db.execSQL(sql);

                if (aActivity != null) {
                    aActivity.finish();
                }

                if (bActivity != null) {
                    bActivity.finish();
                }


                startActivity(intent2);
                if (image_bitmap != null) {
                    dialog.dismiss();
                }
                Toast.makeText(WriteActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub


        AlertDialog.Builder alert = new AlertDialog.Builder(WriteActivity.this);

        alert.setTitle("글쓰기를 취소하시겠습니까?");


        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });

        alert.show();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_write, fragment).commit();
        getSlidingMenu().showContent();
    }

    protected String getRealImagePath(Uri uriPath) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = managedQuery(uriPath, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        c.moveToFirst();
        String fullPath = null;
        try {
            fullPath = c.getString(index);  // 파일의 실제 경로
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        startManagingCursor(c);
        return fullPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    // 앨범인 경우
                    Uri mImageUri = data.getData();
                    String realpaths = getRealImagePath(mImageUri);

                    //image option 설정 및 resizing
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    image_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
                    int resizedWidth = width;
                    int resizedHeight = width * image_bitmap.getHeight() / image_bitmap.getWidth();
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, resizedWidth, resizedHeight, true);

                    //이미지 회전
                    ExifInterface exif = new ExifInterface(realpaths);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    image_bitmap = rotate(image_bitmap, exifDegree);

                    //imageset
                    simpleview.setImageBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();


                }
            }
        }
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    SQLiteDatabase db;
    String sql;

    private void Spinner() {

        //spinner 아이템 list 선언 및 "전체보기" 추가
        spinnerlist = new ArrayList<String>();
        spinnerlist.add("전체보기");

        db = dbHelper2.getReadableDatabase();
        sql = "SELECT * FROM category;";

        //지정되어있는 값이 무엇인지 알고 이를 초기값으로 세팅하기 위한 변수
        int selected = 0;
        int i = 0;

        //spinner item 불러오기 & 지정되어있는 값 찾기
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    //s = 지정되어있는 값

                    String s = cursor.getString(1);
                    if (s.equals("전체보기")) {
                        selected = 0;

                    } else if (s.equals(realcategory)) {
                        selected = i + 1;
                    }
                    spinnerlist.add(s);

                    i++;
                }
            } else {

            }
        } finally {
            cursor.close();
        }

        //adapter 세팅
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_simple_dropdown_item_1line, spinnerlist);

        //스피너 속성 >> setSelection으로 초기값을 세팅하는데 이것은 반드시 setAdapter 보다 나중에 사용해야한다.
        Spinner sp = (Spinner) this.findViewById(R.id.spinner);
        sp.setPrompt("골라봐"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setSelection(selected);
        sp.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        category = spinnerlist.get(arg2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
