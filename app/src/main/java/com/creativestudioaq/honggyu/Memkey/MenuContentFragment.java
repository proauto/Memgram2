package com.creativestudioaq.honggyu.Memkey;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MenuContentFragment extends SherlockFragment {


    TextView textTitleBar2;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        textTitleBar2 = (TextView) getSherlockActivity().findViewById(R.id.textTitle2);
        textTitleBar2.setText("내가 남긴 추억");
        return v;
    }

}