package com.water.melon.ui.me.agent.mymoney;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.MyMoneyBean;

public interface MyMoneyContract {
    interface Present extends BasePresenter {
        void getMyMoney(BaseRequest<MyMoneyBean> baseRequest);
    }

    interface View extends BaseView<Present> {
        void setMoneyDate(MyMoneyBean.BeforeBean beforeBean);
        void setUserDate(MyMoneyBean.MyUserMoney beforeBean,boolean isErr);
    }
}