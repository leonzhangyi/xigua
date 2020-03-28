package com.water.melon.ui.welfare;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.home.HomeBean;
import com.water.melon.ui.home.h5.H5VideoPresent;
import com.water.melon.ui.home.h5.utils.FragmentKeyDown;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WelfH5Activity extends BaseActivity {

    private String name;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_h5_activity;
    }

    private FragmentManager mFragmentManager;

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {

        name = getIntent().getStringExtra(WelfWebFragment.URL_POSTER_NAME);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment();
    }

    private WelfWebFragment mWelfWebFragment;

    private void openFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.container_framelayout, mWelfWebFragment = WelfWebFragment.getInstance(mBundle = new Bundle()), WelfWebFragment.class.getName());
        mBundle.putString(WelfWebFragment.URL_KEY, getIntent().getStringExtra(WelfWebFragment.URL_KEY));
        ft.commit();

    }


    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    HomeBean bean = new HomeBean();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mWelfWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WelfWebFragment mWelfWebFragment = this.mWelfWebFragment;
        if (mWelfWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mWelfWebFragment;
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

}
