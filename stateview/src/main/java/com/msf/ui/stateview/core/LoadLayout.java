package com.msf.ui.stateview.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.msf.ui.stateview.util.LoadUtil;
import com.msf.ui.stateview.view.BaseStateControl;
import com.msf.ui.stateview.view.SuccessState;

import java.util.HashMap;
import java.util.Map;

public class LoadLayout extends FrameLayout {
    private Map<Class<? extends BaseStateControl>, BaseStateControl> stateViews = new HashMap();
    private Context context;
    private BaseStateControl.OnRefreshListener onRefreshListener;
    private Class<? extends BaseStateControl> preStateView;
    private Class<? extends BaseStateControl> curStateView;
    private static final int STATEVIEW_CUSTOM_INDEX = 1;

    public LoadLayout(@NonNull Context context) {
        super(context);
    }

    public LoadLayout(@NonNull Context context, BaseStateControl.OnRefreshListener onRefreshListener) {
        this(context);
        this.context = context;
        this.onRefreshListener = onRefreshListener;
    }

    public void setSuccessLayout(BaseStateControl callback) {
        addStateView(callback);
        View successView = callback.getRootView(null);
        successView.setVisibility(View.GONE);
        addView(successView);
        this.curStateView = SuccessState.class;
    }

    public void setStateView(BaseStateControl stateview) {
        BaseStateControl cloneStateView = stateview.copy();
        cloneStateView.setStateView(null, this.context, this.onRefreshListener);
        addStateView(cloneStateView);
    }

    public void addStateView(BaseStateControl stateview) {
        if (!this.stateViews.containsKey(stateview.getClass())) {
            this.stateViews.put(stateview.getClass(), stateview);
        }
    }

    public void showStateView(Class<? extends BaseStateControl> callback) {
        showStateView(callback, null);
    }

    public void showStateView(Class<? extends BaseStateControl> callback, Object tag) {
        checkStateViewExist(callback);
        if (LoadUtil.isMainThread()) {
            showStateViewView(callback, tag);
        } else {
            postMainThread(callback, tag);
        }
    }

    public Class<? extends BaseStateControl> getCurrentStateView() {
        return this.curStateView;
    }

    private void postMainThread(final Class<? extends BaseStateControl> status, final Object tag) {
        post(new Runnable() {
            public void run() {
                LoadLayout.this.showStateViewView(status, tag);
            }
        });
    }

    private void showStateViewView(Class<? extends BaseStateControl> status, Object tag) {
        if (this.preStateView != null) {
            if (this.preStateView == status) {
                return;
            }
            ((BaseStateControl) this.stateViews.get(this.preStateView)).onDetach();
        }
        if (getChildCount() > 1) {
            removeViewAt(1);
        }
        for (Class key : this.stateViews.keySet()) {
            if (key == status) {
                SuccessState successView = (SuccessState) this.stateViews.get(SuccessState.class);
                if (key == SuccessState.class) {
                    successView.show();
                } else {
                    successView.showWithStateView(((BaseStateControl) this.stateViews.get(key)).isVisible());
                    View rootView = ((BaseStateControl) this.stateViews.get(key)).getRootView(tag);
                    addView(rootView);
                    ((BaseStateControl) this.stateViews.get(key)).onAttach(this.context, rootView);
                }
                this.preStateView = status;
            }
        }
        this.curStateView = status;
    }

    private void checkStateViewExist(Class<? extends BaseStateControl> stateView) {
        if (!this.stateViews.containsKey(stateView)) {
            throw new IllegalArgumentException(String.format("The BaseStateControl (%s) is nonexistent.", new Object[]{stateView
                    .getSimpleName()}));
        }
    }
}

