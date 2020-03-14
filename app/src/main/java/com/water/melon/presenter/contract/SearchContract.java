package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.ui.netresource.NetResoutVideoInfo;

import java.util.List;

public interface SearchContract {
    interface Present extends BasePresenter {
        void getListData(GetVideosRequest request);

        void setPage();
    }

    interface View extends BaseView<Present> {
        void getListData(List<NetResoutVideoInfo> datas, boolean serviceError, boolean newData);
    }
}
