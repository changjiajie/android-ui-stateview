package com.msf.ui.stateview.core;

import android.support.annotation.NonNull;

import com.msf.ui.stateview.view.BaseStateControl;

import java.util.ArrayList;
import java.util.List;

public class LoadState
{
    private static volatile LoadState loadState;
    private Builder builder;

    public static LoadState newInstance()
    {
        if (loadState == null) {
            synchronized (LoadState.class)
            {
                if (loadState == null) {
                    loadState = new LoadState();
                }
            }
        }
        return loadState;
    }

    public Builder getBuilder()
    {
        return this.builder;
    }

    private LoadState()
    {
        this.builder = new Builder();
    }

    public void setBuilder(@NonNull Builder builder)
    {
        this.builder = builder;
    }

    public static class Builder
    {
        private List<BaseStateControl> stateViews = new ArrayList();
        private Class<? extends BaseStateControl> defaultStateView;

        public Builder register(@NonNull BaseStateControl stateView)
        {
            this.stateViews.add(stateView);
            return this;
        }

        public Builder setDefaultCallback(@NonNull Class<? extends BaseStateControl> defaultStateView)
        {
            this.defaultStateView = defaultStateView;
            return this;
        }

        List<BaseStateControl> getStateViews()
        {
            return this.stateViews;
        }

        Class<? extends BaseStateControl> getDefaultStateView()
        {
            return this.defaultStateView;
        }

        public void build()
        {
            LoadState.newInstance().setBuilder(this);
        }
    }
}
