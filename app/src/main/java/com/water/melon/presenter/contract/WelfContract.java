package com.water.melon.presenter.contract;

import android.content.Context;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.ui.home.HomeBean;

import java.util.ArrayList;
import java.util.List;

public interface WelfContract {
    interface present extends BasePresenter {
        void getWelfBeans();

        void getWelTopAdv();

        void getMidAdv();

        void getBtnBeans(int page);

        void downloadAPK(String downLoadUrl, Context context, String name);
    }

    interface View extends BaseView<WelfContract.present> {
        void getwelfBeans(List<AdvBean> advs);

        void setWelTopAdv(List<AdvBean> advs);

        void setMidAdv(List<AdvBean> advs);

        void setBtnBeans(List<AdvBean> advs,int type);
    }

}
