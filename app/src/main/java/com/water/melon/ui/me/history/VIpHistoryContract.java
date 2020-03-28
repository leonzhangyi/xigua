package com.water.melon.ui.me.history;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.VideoHistoryBean;

import java.util.List;

public interface VIpHistoryContract {
    interface Present extends BasePresenter {
        void getDate(int page);
    }

    interface View extends BaseView<Present> {
        void setDate(List<VideoHistoryBean> videoHistoryBeans, boolean isSuc);
    }
}
