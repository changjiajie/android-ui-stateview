package com.msf.model.mvvm.stateview;

import com.tqzhang.stateview.stateview.BaseStateControl;

import trecyclerview.com.mvvm.R;

public class LoadingState extends BaseStateControl {
    @Override
    protected int onCreateView() {
        return R.layout.loading_view;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

}
