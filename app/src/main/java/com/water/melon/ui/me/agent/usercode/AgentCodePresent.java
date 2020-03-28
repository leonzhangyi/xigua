package com.water.melon.ui.me.agent.usercode;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;

public class AgentCodePresent extends BasePresenterParent implements AgentCodeContract.Present {
    private AgentCodeContract.View mView;

    public AgentCodePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentCodeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }
}
