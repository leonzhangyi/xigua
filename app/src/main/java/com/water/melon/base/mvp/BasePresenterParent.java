package com.water.melon.base.mvp;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

public abstract class BasePresenterParent {
    public LifecycleProvider lifecycleProvider;

    public BasePresenterParent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    public LifecycleTransformer getLifecycleTransformerByStopToActivity() {
        return lifecycleProvider.bindUntilEvent(ActivityEvent.STOP);
    }
    public LifecycleTransformer getLifecycleTransformerByStopToFragment() {
        return lifecycleProvider.bindUntilEvent(FragmentEvent.STOP);
    }
}
