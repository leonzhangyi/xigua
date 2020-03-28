package com.water.melon.ui.me.share;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.ShareBean;

public class ShareContract {
    interface Present extends BasePresenter {
        void getShareDate();
    }

    interface View extends BaseView<Present> {
        void setShareDate(ShareBean shareBean);
    }
}
