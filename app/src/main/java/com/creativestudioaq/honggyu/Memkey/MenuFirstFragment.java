package com.creativestudioaq.honggyu.Memkey;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MenuFirstFragment extends SherlockFragment {

    TextView textTitleBar;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.menufirst, container, false);

        textTitleBar = (TextView) getSherlockActivity().findViewById(R.id.textTitles);
        textTitleBar.setText("전체보기");

        return v;
    }

}