package com.msf.ui.stateview.view;


import android.content.Context;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class BaseStateControl
        implements Serializable
{
    private View rootView;
    private Context context;
    private OnRefreshListener onRefreshListener;
    private boolean isVisible;

    public BaseStateControl() {}

    BaseStateControl(View view, Context context, OnRefreshListener onRefreshListener)
    {
        this.rootView = view;
        this.context = context;
        this.onRefreshListener = onRefreshListener;
    }

    public BaseStateControl setStateView(View view, Context context, OnRefreshListener onRefreshListener)
    {
        this.rootView = view;
        this.context = context;
        this.onRefreshListener = onRefreshListener;
        return this;
    }

    public View getRootView(Object tag)
    {
        int resId = onCreateView();
        if ((resId == 0) && (this.rootView != null)) {
            return this.rootView;
        }
        if (onBuildView(this.context) != null) {
            this.rootView = onBuildView(this.context);
        }
        if (this.rootView == null) {
            this.rootView = View.inflate(this.context, onCreateView(), null);
        }
        this.rootView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (BaseStateControl.this.onReloadEvent(BaseStateControl.this.context, BaseStateControl.this.rootView)) {
                    return;
                }
                if (BaseStateControl.this.onRefreshListener != null) {
                    BaseStateControl.this.onRefreshListener.onRefresh(v);
                }
            }
        });
        if (tag != null)
        {
            this.rootView.setTag(null);
            this.rootView.setTag(tag);
        }
        onViewCreate(this.context, this.rootView);
        return this.rootView;
    }

    protected View onBuildView(Context context)
    {
        return null;
    }

    public boolean isVisible()
    {
        return this.isVisible;
    }

    void isVisible(boolean visible)
    {
        this.isVisible = visible;
    }

    protected boolean onRetry(Context context, View view)
    {
        return false;
    }

    protected boolean onReloadEvent(Context context, View view)
    {
        return false;
    }

    public BaseStateControl copy()
    {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        Object obj = null;
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(this);
            oos.close();
            ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (BaseStateControl)obj;
    }

    public View obtainRootView()
    {
        if (this.rootView == null) {
            this.rootView = View.inflate(this.context, onCreateView(), null);
        }
        return this.rootView;
    }

    protected abstract int onCreateView();

    protected void onViewCreate(Context context, View view) {}

    public void onAttach(Context context, View view) {}

    public void onDetach() {}

    public static abstract interface OnRefreshListener
            extends Serializable
    {
        public abstract void onRefresh(View paramView);
    }
}
