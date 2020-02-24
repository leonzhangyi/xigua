package com.water.melon.presenter.contract;

import android.widget.RadioGroup;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;

import androidx.fragment.app.Fragment;

public interface MainContract {
    interface Present extends BasePresenter {
        void selectTab(int position, RadioGroup tablayout);
    }

    interface View extends BaseView<Present> {
        void selectTab(Fragment newFragment, Fragment oldFragment);
    }

}
