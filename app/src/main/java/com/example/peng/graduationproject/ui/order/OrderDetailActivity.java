package com.example.peng.graduationproject.ui.order;

import android.os.Bundle;

import com.example.peng.graduationproject.R;
import com.example.peng.graduationproject.common.BaseActivity;

/**
 * Created by peng on 2016/4/19.
 */
public class OrderDetailActivity extends BaseActivity{

    private int orderType;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_orderdetail);


    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setDefaultValues() {
        orderType = getIntent().getExtras().getInt("orderType");
        position = getIntent().getExtras().getInt("position");

    }

    @Override
    protected void bindEvents() {

    }
}
