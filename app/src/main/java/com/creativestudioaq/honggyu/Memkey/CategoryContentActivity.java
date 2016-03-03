package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by honggyu on 2015-10-04.
 */
public class CategoryContentActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    public static final int REQUEST_CODE_PICKALBUM2 = 101;
    private Fragment mContent;
    private String mImgPath;
    final static String dbName = "Memo.db";
    final static String dbName2 = "category.db";
    public static Activity DActivity;
    final static int dbVersion = 1;
    ArrayList<String> spinnerlist3;
    String strDate;
    String datas2;
    TextView words;
    TextView Datepick;
    Button btnBack3;
    Button btnEdit;
    DBHelper dbHelper;
    DBHelper2 dbHelper2;
    String category;
    ImageView realView;
    int width;
    String category2;
    int realdata;
    int id2;
    int data;
    String realcategory;
    Bitmap image_bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        width = getWindowManager().getDefaultDisplay().getWidth();
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        dbHelper2 = new DBHelper2(this, dbName2, null, dbVersion);

        DActivity = CategoryContentActivity.this;

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_content);

        getSupportActionBar().show();

        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        if (mContent == null)
            mContent = new MenuContentFragment();

        setContentView(R.layout.activity_content);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, mContent).commit();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date();
        strDate = dateFormat.format(date);

        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();
        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);


        //Main 넘어온 값 받기
        Intent intent = getIntent();
        data = intent.getIntExtra("value", 1);
        realcategory = intent.getExtras().getString("category");


        btnBack3 = (Button) findViewById(R.id.btnBack3);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        realView = (ImageView) findViewById(R.id.RealView);
        words = (TextView) findViewById(R.id.words);
        Datepick = (TextView) findViewById(R.id.date3);
        Datepick.setText(strDate);


        //원래값 받아오기
        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo Where category='" + realcategory + "' ;";
        Cursor cs = db.rawQuery(sql, null);
        cs.moveToLast();
        int s = cs.getPosition();
        realdata = s - data;
        cs.moveToPosition(realdata);
        id2 = cs.getInt(0);
        String photo2 = cs.getString(2);
        String content2 = cs.getString(3);
        category2 = cs.getString(4);

        cs.close();

        //텍스트 설정
        words.setText(content2);


        try {
            image_bitmap = BitmapFactory.decodeFile(photo2);
            if (image_bitmap != null) {

                realView.setImageBitmap(image_bitmap);
            } else {
                realView.setImageDrawable(null);
            }

        } catch (Exception e) {

        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btnBack3:


                        finish();

                        break;
                    case R.id.btnEdit:


                        Intent intent2 = new Intent(CategoryContentActivity.this, CategoryEditActivity.class);


                        intent2.putExtra("value", data);
                        intent2.putExtra("category", realcategory);


                        startActivity(intent2);

                        break;

                }

            }
        };
        btnBack3.setOnClickListener(listener);
        btnEdit.setOnClickListener(listener);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (image_bitmap != null) {
            image_bitmap.recycle();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
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
                    options.inSampleSize = 2;
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
                    realView.setImageBitmap(image_bitmap);
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

    private void updateImageView() {
        int degree = ImageUtil.GetExifOrientation(mImgPath);
        Bitmap resizeBitmap = ImageUtil.loadBackgroundBitmap(
                CategoryContentActivity.this, mImgPath);
        Bitmap rotateBitmap = ImageUtil.GetRotatedBitmap(resizeBitmap, degree);


        if (resizeBitmap != null) {
            resizeBitmap.recycle();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    SQLiteDatabase db;
    String sql;

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        category = spinnerlist3.get(arg2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
