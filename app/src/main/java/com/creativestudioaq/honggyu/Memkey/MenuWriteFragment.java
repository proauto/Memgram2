package com.creativestudioaq.honggyu.Memkey;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MenuWriteFragment extends SherlockFragment {

    TextView textTitleBar;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        textTitleBar = (TextView) getSherlockActivity().findViewById(R.id.textTitle);
        textTitleBar.setText("글쓰기");
        return v;
    }

}