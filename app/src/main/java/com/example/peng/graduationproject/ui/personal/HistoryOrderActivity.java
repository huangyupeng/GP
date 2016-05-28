package com.example.peng.graduationproject.ui.personal;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.peng.graduationproject.R;
import com.example.peng.graduationproject.common.BaseActivity;
import com.example.peng.graduationproject.ui.order.OrderFragment;

/**
 * Created by peng on 2016/5/27.
 */
public class HistoryOrderActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentLayout(R.layout.activity_historyorder);
        setTitleText(R.string.historyorder);

        getBtn_back().setVisibility(View.VISIBLE);

        initView();
        setDefaultValues();
        bindEvents();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setDefaultValues() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        OrderFragment orderFragment = new OrderFragment();
        ft.add(R.id.fragment_contaioner, orderFragment);

        ft.commit();


    }

    @Override
    protected void bindEvents() {

    }
}
