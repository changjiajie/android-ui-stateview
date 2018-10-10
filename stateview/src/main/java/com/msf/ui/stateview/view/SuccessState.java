package com.msf.ui.stateview.view;


import android.content.Context;
import android.view.View;

public class SuccessState
        extends BaseStateControl {
    public SuccessState(View view, Context context, BaseStateControl.OnRefreshListener onRefreshListener) {
        super(view, context, onRefreshListener);
    }

    protected int onCreateView() {
        return 0;
    }

    public void hide() {
        obtainRootView().setVisibility(View.INVISIBLE);
    }

    public void show() {
        obtainRootView().setVisibility(View.VISIBLE);
    }

    public void showWithStateView(boolean successVisible) {
        obtainRootView().setVisibility(successVisible ? View.VISIBLE : View.INVISIBLE);
    }
}

