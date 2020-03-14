package com.water.melon.ui.home.h5;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.home.h5.utils.FragmentKeyDown;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class H5VideoActivity extends BaseActivity implements H5VideoConstract.View {

    private H5VideoPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_h5_activity;
    }

    private FragmentManager mFragmentManager;

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new H5VideoPresent(this, this);
        present.start();

    }

    private AgentWebFragment mAgentWebFragment;

    private void openFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//        mBundle.putString(AgentWebFragment.URL_KEY, "https://www.iqiyi.com");
        mBundle.putString(AgentWebFragment.URL_KEY, getIntent().getStringExtra(AgentWebFragment.URL_KEY));
        ft.commit();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void initView() {
        mFragmentManager = this.getSupportFragmentManager();
        openFragment();
        present.getRoads();
    }

    @Override
    public void setPresenter(H5VideoConstract.Present presenter) {
        present = (H5VideoPresent) presenter;
    }
}
