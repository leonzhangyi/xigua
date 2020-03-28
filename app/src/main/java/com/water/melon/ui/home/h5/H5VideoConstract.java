package com.water.melon.ui.home.h5;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.ui.home.HomeBean;

public class H5VideoConstract {
    interface Present extends BasePresenter {
        void getRoads();

        void doWrite(HomeBean bean);
    }

    interface View extends BaseView<Present> {

    }
}
