package com.msf.ui.stateview.core;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.msf.ui.stateview.util.LoadUtil;
import com.msf.ui.stateview.view.BaseStateControl;
import com.msf.ui.stateview.view.SuccessState;

import java.util.List;

public class LoadManager
{
    private LoadLayout loadLayout;

    public static class Builder
    {
        private LoadManager.StateViewParams stateViewParams;
        private BaseStateControl.OnRefreshListener onRefreshListener;

        public Builder setListener(BaseStateControl.OnRefreshListener listener)
        {
            this.onRefreshListener = listener;
            return this;
        }

        public Builder setViewParams(Object stateView)
        {
            this.stateViewParams = LoadUtil.getViewParams(stateView);
            return this;
        }

        public LoadManager build()
        {
            return new LoadManager(this.stateViewParams, this.onRefreshListener, LoadState.newInstance().getBuilder());
        }
    }

    public LoadManager(StateViewParams stateViewParams, BaseStateControl.OnRefreshListener onRefreshListener, LoadState.Builder builder)
    {
        Context context = stateViewParams.context;
        View rootView = stateViewParams.rootView;
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        this.loadLayout = new LoadLayout(context, onRefreshListener);
        this.loadLayout.setSuccessLayout(new SuccessState(rootView, context, onRefreshListener));
        if (stateViewParams.parentView != null) {
            stateViewParams.parentView.addView(this.loadLayout, stateViewParams.childIndex, layoutParams);
        }
        initStateViews(builder);
    }

    private void initStateViews(LoadState.Builder builder)
    {
        if (builder == null) {
            throw new IllegalArgumentException("The builder must be  set stateview");
        }
        List<BaseStateControl> stateViews = builder.getStateViews();

        Class<? extends BaseStateControl> defalutStateView = builder.getDefaultStateView();
        if ((stateViews != null) && (stateViews.size() > 0)) {
            for (BaseStateControl stateView : stateViews) {
                this.loadLayout.setStateView(stateView);
            }
        }
        if (defalutStateView != null) {
            this.loadLayout.showStateView(defalutStateView);
        }
    }

    public void showSuccess()
    {
        this.loadLayout.showStateView(SuccessState.class);
    }

    public void showStateView(Class<? extends BaseStateControl> stateView)
    {
        this.loadLayout.showStateView(stateView);
    }

    public void showStateView(Class<? extends BaseStateControl> stateView, Object tag)
    {
        this.loadLayout.showStateView(stateView, tag);
    }

    public LoadLayout getLoadLayout()
    {
        return this.loadLayout;
    }

    public Class<? extends BaseStateControl> getCurrentStateView()
    {
        return this.loadLayout.getCurrentStateView();
    }

    public static class StateViewParams
    {
        public Context context;
        public ViewGroup parentView;
        public View rootView;
        public int childIndex;

        public StateViewParams(Context context, ViewGroup parentView, View rootView, int childIndex)
        {
            this.context = context;
            this.parentView = parentView;
            this.rootView = rootView;
            this.childIndex = childIndex;
        }
    }
}
