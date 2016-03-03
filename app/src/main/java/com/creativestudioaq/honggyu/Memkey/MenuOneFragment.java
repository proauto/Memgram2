package com.creativestudioaq.honggyu.Memkey;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MenuOneFragment extends SherlockFragment {

	TextView textTitleBar;
	String realcategory;

	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.menuone, container, false);



		return v;
	}

}