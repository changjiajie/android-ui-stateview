package com.msf.model.mvvm.base;


import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class AbsRepository {

    private CompositeSubscription mCompositeSubscription;


    public AbsRepository() {

    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }
}
