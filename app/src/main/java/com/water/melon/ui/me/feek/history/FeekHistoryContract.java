package com.water.melon.ui.me.feek.history;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.FeedBean;
import com.water.melon.net.bean.VideoHistoryBean;

import java.util.List;

public interface FeekHistoryContract {
    interface Present extends BasePresenter {
        void getDate(int page);
    }

    interface View extends BaseView<Present> {
        void setDate(FeedBean feedBean, boolean isSuc);
    }
}
