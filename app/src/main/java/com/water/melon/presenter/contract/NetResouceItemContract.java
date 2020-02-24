package com.water.melon.presenter.contract;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.ui.netresource.NetResoutVideoInfo;

import java.util.List;

public interface NetResouceItemContract {
    interface Presenter extends BasePresenter {
        void getListData(GetVideosRequest request);
    }

    interface View extends BaseView<Presenter> {
        void getListData(List<NetResoutVideoInfo> datas, boolean serviceError, boolean newData);
    }
}
