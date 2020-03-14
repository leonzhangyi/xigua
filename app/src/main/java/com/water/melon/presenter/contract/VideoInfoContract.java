package com.water.melon.presenter.contract;


import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.ui.player.VlcVideoBean;

public interface VideoInfoContract {
    interface Presenter extends BasePresenter {
        void getVideoInfo(String videoId);

        void downLoadVideo(VlcVideoBean vlcVideoBean);

        void updateHistory(int time, VlcVideoBean vlcVideoBean);
    }

    interface View extends BaseView<Presenter> {
        void getVideoInfo(VideoPlayBean videoInfo);
    }
}
