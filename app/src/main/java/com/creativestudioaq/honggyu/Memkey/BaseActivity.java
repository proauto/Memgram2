package com.creativestudioaq.honggyu.Memkey;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity  {
	
	Button btnToggle;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 액션바 설정
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(com.creativestudioaq.honggyu.Memkey.R.layout.action_bar_title);

		getSupportActionBar().show();
		
		// 메뉴 토글 버튼 추가
		btnToggle = (Button) findViewById(com.creativestudioaq.honggyu.Memkey.R.id.btnToggle);
		btnToggle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				toggle();
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
