package com.creativestudioaq.honggyu.Memkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;

public class MenuFragment extends SherlockFragment {

	final static String dbName2 = "category.db";
	final static String dbName = "Memo.db";
	final static int dbVersion = 1;
	ListView listView2;
	ArrayList<String> alItems;
	ArrayAdapter<String> adapter2;
	ImageView plus;
	ImageView setting;
	TextView allsee;
	DBHelper dbHelper;
	DBHelper2 dbHelper2;
	SQLiteDatabase db;
	SQLiteDatabase db1;
	String sql;
	int selectedPos = -1;
	View v;
	EditText searching;
	int n = 0;
	boolean a = true;


	public MenuFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		alItems = new ArrayList<String>();
		dbHelper2 = new DBHelper2(getActivity(), dbName2, null, dbVersion);
		dbHelper = new DBHelper(getActivity(), dbName, null, dbVersion);

		db = dbHelper2.getReadableDatabase();
		db1 = dbHelper.getWritableDatabase();

		AsyncTask mTask = new ArraycontrolTask();
		Object[] param =null;
		mTask.execute(param);

		/*
		sql = "SELECT * FROM category;";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String s = cursor.getString(1);

					alItems.add(s);


				}
			} else {

			}
		} finally {
			cursor.close();
		}
*/
		View v = inflater.inflate(R.layout.menu_frame, container, false);

		// 기본 변수 선언


		listView2 = (ListView) v.findViewById(R.id.list);
		searching = (EditText) v.findViewById(R.id.searching);
		searching.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == event.ACTION_DOWN) {
					a = true;
				}
				if (keyCode == event.ACTION_UP) {
					a = false;
				}


				if (keyCode == event.KEYCODE_ENTER) {

					String A = searching.getText().toString();
					ArrayList<Integer> Intitems = new ArrayList<Integer>();

					sql = "SELECT * FROM Memo;";

					if (!A.equals("")) {
						Cursor cursor = db.rawQuery(sql, null);
						try {
							if (cursor.getCount() > 0) {
								while (cursor.moveToNext()) {
									String s = cursor.getString(3);
									String daysearch = cursor.getString(1);

									if (SoundSearcher.matchString(s, A)) {
										Intitems.add(cursor.getInt(0));
									}
									if (SoundSearcher.matchString(daysearch, A)) {
										Intitems.add(cursor.getInt(0));
									}

								}
							} else {

							}
						} finally {
							cursor.close();
						}
						if (!Intitems.isEmpty()) {
							if (n == 0) {

								Intent gotosearch = new Intent(getActivity(), Searching.class);
								gotosearch.putExtra("search", Intitems);
								startActivity(gotosearch);
								getActivity().finish();

								n++;
							}
						} else {
							Toast.makeText(getActivity(), "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), "입력어가 없습니다.", Toast.LENGTH_SHORT).show();
					}


				}
				return false;
			}
		});
		setting = (ImageView) v.findViewById(R.id.setting);
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				Intent intent7 = new Intent(getActivity(), SettingActivity.class);
				startActivity(intent7);
				getActivity().finish();
			}
		});

		//전체보기 버튼
		allsee = (TextView) v.findViewById(R.id.wholeshow);
		allsee.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				Intent intent5 = new Intent(getActivity(), MainActivity.class);
				startActivity(intent5);
				getActivity().finish();
			}
		});


		//추가버튼
		plus = (ImageView) v.findViewById(R.id.plus);
		plus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("카테고리 생성");

				final EditText name = new EditText(getActivity());
				name.setSingleLine();
				int maxLength = 15;
				InputFilter[] fArray = new InputFilter[1];
				fArray[0] = new InputFilter.LengthFilter(maxLength);
				name.setFilters(fArray);
				alert.setView(name);

				alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String category = name.getText().toString();
						category = category.replace("'", "''");

						if (category.equals("")) {
							Toast.makeText(getActivity(), "카테고리를 입력해주세요~!", Toast.LENGTH_SHORT).show();
						} else {

							db = dbHelper2.getWritableDatabase();
							sql = String.format("INSERT INTO category VALUES(NULL,'%s');", category);
							db.execSQL(sql);
							alItems.add(category);
							adapter2.notifyDataSetChanged();
						}
					}
				});
				alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Fragment newContent6;
						newContent6 = new MenuFirstFragment();


					}
				});

				alert.show();

			}
		});

		return v;
	}


	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

	}



	@Override
	public void onStart() {
		super.onStart();

	}

	private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			selectedPos = position;
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
			alertDlg.setTitle("질문");
			AlertDialog alert = alertDlg.create();
			// '예' 버튼이 클릭되면
			alertDlg.setNeutralButton(R.string.button_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {


					sql = "SELECT * FROM category;";
					Cursor cs = db.rawQuery(sql, null);

					cs.moveToPosition(selectedPos);
					int id = cs.getInt(0);

					alItems.remove(selectedPos);
					sql = "DELETE FROM category WHERE _id=" + id + ";";
					db.execSQL(sql);

					// 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
					adapter2.notifyDataSetChanged();
					cs.close();
					dialog.dismiss();  // AlertDialog를 닫는다.


				}
			});


			alertDlg.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();  // AlertDialog를 닫는다.
				}
			});
			// '아니오' 버튼이 클릭되면
			alertDlg.setPositiveButton(R.string.button_revise, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();  // AlertDialog를 닫는다.


					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
					alert.setTitle("카테고리 수정");

					final EditText name = new EditText(getActivity());
					name.setSingleLine();
					name.setText("" + alItems.get(selectedPos));
					int maxLength = 15;
					InputFilter[] fArray = new InputFilter[1];
					fArray[0] = new InputFilter.LengthFilter(maxLength);
					name.setFilters(fArray);
					alert.setView(name);

					alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String category = name.getText().toString();
							category = category.replace("'", "''");

							if (category.equals("")) {
								Toast.makeText(getActivity(), "카테고리를 수정해주세요~!", Toast.LENGTH_SHORT).show();
							} else {

								sql = "SELECT * FROM category;";
								Cursor cs = db.rawQuery(sql, null);
								cs.moveToPosition(selectedPos);
								int realid = cs.getInt(0);
								sql = "UPDATE category SET category='" + category+"' WHERE _id="+realid+";";
								db.execSQL(sql);

								sql = "UPDATE Memo SET category='"+category+"' WHERE category='"+alItems.get(selectedPos) + "';";
								db1.execSQL(sql);

								alItems.set(selectedPos, category);
								adapter2.notifyDataSetChanged();


								cs.close();
							}
						}
					});

					alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Fragment newContent6;
							newContent6 = new MenuFirstFragment();


						}
					});

					alert.show();


				}
			});
			alertDlg.setMessage(String.format(getString(R.string.alert_msg_delete),
					alItems.get(position)));
			alertDlg.show();


			return true;
		}

	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}




	class ArraycontrolTask extends AsyncTask<Void, Void, Void> {

		public void execute(){
			super.execute();
		}
		@Override
		protected void onPreExecute() {


			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			sql = "SELECT * FROM category;";
			Cursor cursor = db.rawQuery(sql, null);
			try {
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						String s = cursor.getString(1);

						alItems.add(s);


					}
				} else {

				}
			} finally {
				cursor.close();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.list_textview, alItems);
			listView2.setAdapter(adapter2);
			listView2.setOnItemLongClickListener(new ListViewItemLongClickListener());
			listView2.setOnItemClickListener(new ListView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> a, View vi, int pos, long id) {

					Intent intent4 = new Intent(getActivity(), CategoryActivity.class);
					intent4.putExtra("category", alItems.get(pos));
					startActivity(intent4);
					getActivity().finish();

				}
			});

			adapter2.notifyDataSetChanged();
		}
	}
}