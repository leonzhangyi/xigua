package com.water.melon.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.constant.XGConstant;
import com.water.melon.presenter.MainPresent;
import com.water.melon.presenter.contract.MainContract;
import com.water.melon.utils.HandleBackUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.update.CheckAppVersionUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class MainActivity extends BaseActivity implements MainContract.View {
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.bottom_group)
    RadioGroup bottomGroup;

    private int position = 0;
    private MainPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        if (!XGConstant.hasInitP2p) {  //TODO  p2p初始化
            XGConstant.hasInitP2p = true;
            //闪退后重新初始化
            MyApplication.getp2p().InitP2PServer();
        }
        new MainPresent(this, this);
        present.start();//初始化升级
//        present.getUserInfo();//放到个人中心获取用户信息
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @OnCheckedChanged({R.id.bottom_radio_1, R.id.bottom_radio_2, R.id.bottom_radio_3, R.id.bottom_radio_4})
    public void onRadioCheckChage(CompoundButton view, boolean ischanged) {
        switch (view.getId()) {
            case R.id.bottom_radio_1:
                position = 0;
                break;
            case R.id.bottom_radio_2:
                position = 1;
                break;
            case R.id.bottom_radio_3:
                position = 2;
                break;
            case R.id.bottom_radio_4:
                position = 3;
                break;
        }
        if (ischanged) {
            present.selectTab(position, bottomGroup);
        }
    }

    @Override
    public void initView() {
        bottomGroup.getChildAt(position).performClick();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckAppVersionUtil.checkApp(true);
            }
        }, 1500);
    }

    @Override
    public void setPresenter(MainContract.Present presenter) {
        this.present = (MainPresent) presenter;
    }

    @Override
    public void selectTab(Fragment newFragment, Fragment oldFragment) {
        //功能页面切换
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (oldFragment == null) {
            fragmentManager.beginTransaction().add(R.id.content, newFragment, newFragment.getClass().getSimpleName()).commitAllowingStateLoss();
        } else {
            //先隐藏之前的Fragment
            //新Fragment如果不存在则添加，存在直接显示
            if (null != fragmentManager.findFragmentByTag(newFragment.getClass().getSimpleName())) {
                fragmentManager.beginTransaction().hide(oldFragment).show(newFragment).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().hide(oldFragment)
                        .add(R.id.content, newFragment, newFragment.getClass().getSimpleName()).commitAllowingStateLoss();
            }
        }
    }


    private long exitTime;//记录退出的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (HandleBackUtil.handleBackPress(this)) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                ToastUtil.showToastShort(getStringByResId(R.string.exit_again));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
