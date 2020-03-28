package com.water.melon.ui.me.pay;

import android.os.Bundle;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;

public class AgentPayActivity extends BaseActivity implements AgentPayContract.View {

    private AgentPayPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_agent_pay;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new AgentPayPresent(this,this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setPresenter(AgentPayContract.Present presenter) {
        present = (AgentPayPresent) presenter;
    }
}
