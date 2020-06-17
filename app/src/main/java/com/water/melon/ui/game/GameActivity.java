package com.water.melon.ui.game;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class GameActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager mFragmentManager;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {

        //去除标题栏
        return R.layout.game_layout;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        findViewById(R.id.back).setOnClickListener(this);

        mFragmentManager = this.getSupportFragmentManager();
        openFragment();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }


    private PlayGameFragment mPlayGameFragment;

    private void openFragment() {
        Log.e("GameActivity", "URL_KEY url = " + getIntent().getStringExtra(PlayGameFragment.URL_KEY));
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.container_framelayout, mPlayGameFragment = PlayGameFragment.getInstance(mBundle = new Bundle()), PlayGameFragment.class.getName());
        mBundle.putString(PlayGameFragment.URL_KEY, getIntent().getStringExtra(PlayGameFragment.URL_KEY));
        ft.commit();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            setResult(10002);
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(10002);
        }
        return super.onKeyDown(keyCode, event);
    }
}
