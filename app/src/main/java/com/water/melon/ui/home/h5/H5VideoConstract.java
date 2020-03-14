package com.water.melon.ui.home.h5;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;

public class H5VideoConstract {
    interface Present extends BasePresenter {
        void getRoads();

    }

    interface View extends BaseView<Present> {

    }
}
