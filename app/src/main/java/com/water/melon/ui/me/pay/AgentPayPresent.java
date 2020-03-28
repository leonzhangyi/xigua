package com.water.melon.ui.me.pay;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;

public class AgentPayPresent extends BasePresenterParent implements AgentPayContract.Present {

    private AgentPayContract.View mView;

    public AgentPayPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentPayContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }
}
