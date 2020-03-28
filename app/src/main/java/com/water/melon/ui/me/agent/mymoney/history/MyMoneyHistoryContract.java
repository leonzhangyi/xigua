package com.water.melon.ui.me.agent.mymoney.history;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.MyMoneyBean;

import java.util.List;

public interface MyMoneyHistoryContract {
    interface Present extends BasePresenter {
        void getHistoryData(MyMoneyBean request);

    }

    interface View extends BaseView<Present> {
        void setUserDate(List<MyMoneyBean.MyMoneyHistory> historyDate, boolean err);
    }
}
