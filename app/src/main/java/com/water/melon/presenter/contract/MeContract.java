package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;

public interface MeContract {
    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter> {
    }

}
