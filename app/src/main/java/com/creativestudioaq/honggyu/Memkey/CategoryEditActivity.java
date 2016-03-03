package com.creativestudioaq.honggyu.Memkey;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by honggyu on 2015-10-04.
 */
public class CategoryEditActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    public static final int REQUEST_CODE_PICKALBUM2 = 101;
    private Fragment mContent;
    private String mImgPath;
    final static String dbName = "Memo.db";
    final static String dbName2 = "category.db";
    final static int dbVersion = 1;
    ArrayList<String> spinnerlist2;
    String strDate;
    String datas2;
    EditText content3;
    TextView Datepick;
    Button btnBack2;
    Button btnSave2;
    Button btnPhoto2;
    DBHelper dbHelper;
    DBHelper2 dbHelper2;
    String category;
    ImageView simpleview2;
    int width;
    String category2;
    int realdata;
    int id2;
    String realcategory;
    Bitmap image_bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        width = getWindowManager().getDefaultDisplay().getWidth();
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        dbHelper2 = new DBHelper2(this, dbName2, null, dbVersion);

        // 액션바 설정
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_edit);

        getSupportActionBar().show();

        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new MenuWriteFragment();

        setContentView(R.layout.edit_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_edit, mContent).commit();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        strDate = dateFormat.format(date);

        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);

        //Main 넘어온 값 받기
        Intent intent = getIntent();
        int data = intent.getIntExtra("value", 1);
        realcategory = intent.getExtras().getString("category");


        btnBack2 = (Button) findViewById(R.id.btnBack2);
        btnSave2 = (Button) findViewById(R.id.btnSave2);
        btnPhoto2 = (Button) findViewById(R.id.photoselect2);
        simpleview2 = (ImageView) findViewById(R.id.SimpleView2);
        content3 = (EditText) findViewById(R.id.content2);
        Datepick = (TextView) findViewById(R.id.date2);
        Datepick.setText(strDate);
        //원래값 받아오기
        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo Where category='"+realcategory+"' ;";
        Cursor cs = db.rawQuery(sql, null);
        cs.moveToLast();
        int s = cs.getPosition();
        realdata = s-data;
        cs.moveToPosition(realdata);
         id2 = cs.getInt(0);
        String day2 = cs.getString(1);
        String photo2 = cs.getString(2);
        String content2 = cs.getString(3);
        category2 = cs.getString(4);

        cs.close();
        //텍스트 설정
        content3.setText(content2);
        Spinner();
        //이미지설정

        image_bitmap = BitmapFactory.decodeFile(photo2);
        simpleview2.setImageBitmap(image_bitmap);



        View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SQLiteDatabase db;
                    String sql;
                    switch (v.getId()) {
                        case R.id.btnBack2:


                            finish();

                            break;
                        case R.id.btnSave2:
                            btnSave2.setSelected(true);
                            Intent intent2 = new Intent(CategoryEditActivity.this, MainActivity.class);

                            String contents = content3.getText().toString();

                            db = dbHelper.getWritableDatabase();
                            sql = "UPDATE Memo SET day='" + strDate + "',photo='" + datas2 + "',content='" + contents + "',category='" + category + "' WHERE _id="+ id2 +";";
                            Log.v("ddd", sql + db);
                            db.execSQL(sql);
                            CategoryActivity cActivity = (CategoryActivity) CategoryActivity.CActivity;
                            cActivity.finish();

                            CategoryContentActivity dActivity = (CategoryContentActivity) CategoryContentActivity.DActivity;
                            dActivity.finish();
                            startActivity(intent2);
                            finish();
                            break;
                        case R.id.photoselect2:


                            Intent intent3 = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent3.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            startActivityForResult(Intent.createChooser(intent3, "앨범에서 불러오기"),
                                    REQUEST_CODE_PICKALBUM2);


                            break;
                    }

                }
            };
            btnBack2.setOnClickListener(listener);
            btnSave2.setOnClickListener(listener);
            btnPhoto2.setOnClickListener(listener);
            Log.v("알림", "제발좀4");

        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(image_bitmap!=null) {
            image_bitmap.recycle();
        }
    }

        public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_edit, fragment).commit();
        getSlidingMenu().showContent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKALBUM2) {
            if (resultCode == RESULT_OK) {
                try {
                    // 앨범인 경우
                    Uri mImageUri = data.getData();
                    String string = mImageUri.toString();


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize =2;
                    Bitmap image_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri), null, options);
                    int resizedWidth = width;
                    int resizedHeight = width * image_bitmap.getHeight() / image_bitmap.getWidth();
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, resizedWidth, resizedHeight, true);


                    ExifInterface exif = new ExifInterface(string);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    image_bitmap = rotate(image_bitmap, exifDegree);


                    // 이미지 Path 취득
                    mImgPath = getPath(mImageUri);
                    updateImageView();
//dbmaker로 데이터 줌 >string
                    datas2 = string;
                    //imageset
                    simpleview2.setImageBitmap(image_bitmap);
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
    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }
    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void updateImageView() {
        int degree = ImageUtil.GetExifOrientation(mImgPath);
        Bitmap resizeBitmap = ImageUtil.loadBackgroundBitmap(
                CategoryEditActivity.this, mImgPath);
        Bitmap rotateBitmap = ImageUtil.GetRotatedBitmap(resizeBitmap, degree);
        Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(rotateBitmap);


        resizeBitmap.recycle();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

SQLiteDatabase db;
String sql;

    private void Spinner(){
        spinnerlist2 = new ArrayList<String>();
        spinnerlist2.add("전체보기");
        db = dbHelper2.getReadableDatabase();
        sql = "SELECT * FROM category;";
        int i = 0;
        int selected=0;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {


                    String s = cursor.getString(1);
                    if(s.equals("전체보기")) {
                        selected=0;

                    }else if(s.equals(category2)) {
                        selected = i+1;
                    }
                    spinnerlist2.add(s);

                i++;
                }
            } else {

            }
        }finally {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_simple_dropdown_item_1line, spinnerlist2);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.spinner2);
        sp.setPrompt("골라봐"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setSelection(selected);
        sp.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        category = spinnerlist2.get(arg2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
