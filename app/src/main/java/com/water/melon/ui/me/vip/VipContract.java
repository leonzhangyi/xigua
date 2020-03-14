package com.water.melon.ui.me.vip;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.CreateCodeBean;

import java.util.List;

public interface VipContract {
    interface Present extends BasePresenter {
        void getVipDate();

        void doPay(VipBean item);

        void doBdVip(CreateCodeBean.UserCodeBean userCodeBean);
    }

    interface View extends BaseView<Present> {
        void setVipDate(List<VipBean> vipBeans);

        void setPayMethod(VipBean vipBean);

        void updata();
    }

}
