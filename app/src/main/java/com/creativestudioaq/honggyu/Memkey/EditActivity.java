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
import android.util.Log;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by honggyu on 2015-10-04.
 */
public class EditActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
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
    int n = 0, m = 0, login = 0;
    ProgressDialog dialog;
    private Dialog d;
    Intent intent2;
    String realpath;
    ;
    String photo2;
    String content2;

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
            mContent = new MenuOneFragment();

        setContentView(R.layout.edit_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_edit, mContent).commit();

        // 메뉴 설정
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setSlidingEnabled(false);

        TextView textTitleBar = (TextView) findViewById(R.id.textTitle);
        textTitleBar.setText("편집");

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

        //원래값 받아오기
        db = dbHelper.getReadableDatabase();
        sql = "SELECT * FROM Memo;";
        Cursor cs = db.rawQuery(sql, null);
        cs.moveToLast();
        int s = cs.getPosition();
        realdata = s - data;
        cs.moveToPosition(realdata);
        id2 = cs.getInt(0);
        String day2 = cs.getString(1);
        photo2 = cs.getString(2);
        content2 = cs.getString(3);
        category2 = cs.getString(4);
        Datepick.setText(day2);
        strDate = day2;

        cs.close();
        //텍스트 설정
        content3.setText(content2);
        Spinner();
        //이미지설정
        try {
            image_bitmap = BitmapFactory.decodeFile(photo2);
            if (image_bitmap != null) {

                simpleview2.setImageBitmap(image_bitmap);
            } else {
                simpleview2.setImageDrawable(null);
            }

        } catch (Exception e) {

        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (v.getId()) {
                    case R.id.btnBack2:


                        AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this);

                        alert.setTitle("편집을 취소하시겠습니까?");


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
                    case R.id.btnSave2:
                        btnSave2.setSelected(true);


                        CheckTypesTask task = new CheckTypesTask();
                        task.execute();

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


        dialog = new ProgressDialog(EditActivity.this);


    }


    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
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
            if (m == 0) {
                if (n == 0) {

                    intent2 = new Intent(EditActivity.this, MainActivity.class);

                    n++;
                    login++;
                } else {

                }

            } else {
                if (n == 0) {


                    File originalfile = new File(photo2);
                    originalfile.delete();

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
                        Toast.makeText(EditActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
                    }

                    intent2 = new Intent(EditActivity.this, MainActivity.class);

                    n++;
                    login++;
                } else {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            Log.v("오긴하냐", "" + login);
            if (m == 0) {
                if (login == 1) {

                    String contents = content3.getText().toString();

                    db = dbHelper.getWritableDatabase();
                    sql = "UPDATE Memo SET day='" + strDate + "',photo='" + photo2 + "',content='" + contents + "',category='" + category + "' WHERE _id=" + id2 + ";";
                    Log.v("ddd", sql + db);
                    db.execSQL(sql);

                    MainActivity aActivity = (MainActivity) MainActivity.AActivity;
                    aActivity.finish();

                    ContentActivity bActivity = (ContentActivity) ContentActivity.BActivity;
                    bActivity.finish();
                    startActivity(intent2);

                    if (image_bitmap != null) {
                        dialog.dismiss();
                    }
                    Toast.makeText(EditActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                }


            } else {
                if (login == 1) {

                    String contents = content3.getText().toString();

                    db = dbHelper.getWritableDatabase();
                    sql = "UPDATE Memo SET day='" + strDate + "',photo='" + realpath + "',content='" + contents + "',category='" + category + "' WHERE _id=" + id2 + ";";
                    Log.v("ddd", sql + db);
                    db.execSQL(sql);

                    MainActivity aActivity = (MainActivity) MainActivity.AActivity;
                    aActivity.finish();

                    ContentActivity bActivity = (ContentActivity) ContentActivity.BActivity;
                    bActivity.finish();
                    startActivity(intent2);

                    Toast.makeText(EditActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    if (image_bitmap != null) {
                        dialog.dismiss();
                    }
                    finish();
                } else {
                }

            }


        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub


        AlertDialog.Builder alert = new AlertDialog.Builder(EditActivity.this);

        alert.setTitle("편집을 취소하시겠습니까?");


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


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (image_bitmap != null) {
            image_bitmap.recycle();
        }
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
        if (requestCode == REQUEST_CODE_PICKALBUM2) {
            if (resultCode == RESULT_OK) {
                try {
                    // 앨범인 경우
                    Uri mImageUri = data.getData();
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


                    //imageset
                    simpleview2.setImageBitmap(image_bitmap);

                    m++;

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


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    SQLiteDatabase db;
    String sql;

    private void Spinner() {
        spinnerlist2 = new ArrayList<String>();
        spinnerlist2.add("전체보기");
        db = dbHelper2.getReadableDatabase();
        sql = "SELECT * FROM category;";
        int i = 0;
        int selected = 0;
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {


                    String s = cursor.getString(1);
                    if (s.equals("전체보기")) {
                        selected = 0;

                    } else if (s.equals(category2)) {
                        selected = i + 1;
                    }
                    spinnerlist2.add(s);

                    i++;
                }
            } else {

            }
        } finally {
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
