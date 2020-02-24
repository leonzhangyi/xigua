package com.water.melon.presenter;

import android.util.SparseArray;
import android.widget.RadioGroup;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.presenter.contract.MainContract;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.ui.me.MeFragment;
import com.water.melon.ui.netresource.NetResouceFragment;
import com.water.melon.ui.welfare.WelfareFragment;

import androidx.fragment.app.Fragment;

public class MainPresent extends BasePresenterParent implements MainContract.Present {
    private MainContract.View mView;
    private SparseArray<Fragment> fragments;
    private int oldPositon = -1;

    public MainPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        fragments = new SparseArray<>();
        mView = (MainContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void selectTab(int position, RadioGroup tablayout) {
        if (oldPositon == position) {
            return;
        }
        switch (position) {
            case 0:
                if (fragments.get(position) == null) {
                    fragments.put(position,new MainFragment());
                }
                break;
            case 1:
                if (fragments.get(position) == null) {
                    fragments.put(position,new NetResouceFragment());
                }
                break;
            case 2:
                if (fragments.get(position) == null) {
                    fragments.put(position,new WelfareFragment());
                }
                break;
            case 3:
                if (fragments.get(position) == null) {
                    fragments.put(position,new MeFragment());
                }
                break;
        }


        switchFragment(position);
        oldPositon = position;
    }

    private void switchFragment(int selectPosition) {
        //功能页面切换
        if (null == fragments.get(selectPosition)) {
            return;
        }
        Fragment oldFragment = null;
        if (oldPositon >= 0) {
            oldFragment = fragments.get(oldPositon);
        }
        mView.selectTab(fragments.get(selectPosition), oldFragment);
    }
}
