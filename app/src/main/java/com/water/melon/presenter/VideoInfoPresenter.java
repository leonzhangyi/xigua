package com.water.melon.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.application.MyApplication;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.presenter.contract.VideoInfoContract;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.ui.player.VlcVideoBean;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import java.util.List;

public class VideoInfoPresenter extends BasePresenterParent implements VideoInfoContract.Presenter {
    private VideoInfoContract.View mView;

    public VideoInfoPresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        this.mView = (VideoInfoContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getVideoInfo(String videoId) {
        mView.showLoadingDialog(true);
        GetVideosRequest request = new GetVideosRequest();
        request.setVideoId(videoId);
        ApiImp.getInstance().getNetVideoInfo(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData<SearchVideoInfoBean>>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                mView.getVideoInfo(null);
                if (error.getCode() != 2) {
                    ToastUtil.showToastLong(error.getMessage());
                }
            }

            @Override
            public void onNext(BaseApiResultData<SearchVideoInfoBean> data) {
                if (null == data.getData() || TextUtils.isEmpty(data.getData().getId())) {
                    ToastUtil.showToastLong(data.getMessage());
                }
                mView.getVideoInfo(data.getData());
            }
        });
    }

    @Override
    public void downLoadVideo(VlcVideoBean vlcVideoBean) {
        try {
            if (TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
                return;
            }
            String videoUrl = vlcVideoBean.getVideoHttpUrl();
            List<LocalVideoInfo> mData = XGUtil.loadList();//下载列表
            boolean foundSame = false;
            for (int i = 0; i < mData.size(); i++) {
                LocalVideoInfo m = mData.get(i);
                //判断从网页跳转过来的电影是否已经在下载列表
                if (videoUrl.equals(m.getUrl())) {
                    foundSame = true;//在缓存的文件里面有此电影信息
                    m.setVideoScreenImage(vlcVideoBean.getVideoScreenImage());
                    break;
                }
            }
            if (!foundSame) {
                //下载完成列表
                List<LocalVideoInfo> mDataCache = XGUtil.loadCacheList();
                for (int i = 0; i < mDataCache.size(); i++) {
                    LocalVideoInfo m = mDataCache.get(i);
                    //判断从网页跳转过来的电影是否已经在下载完成列表
                    if (videoUrl.equals(m.getUrl())) {
                        foundSame = true;//在缓存的文件里面有此电影信息
                        m.setVideoScreenImage(vlcVideoBean.getVideoScreenImage());
                        break;
                    }
                }
            }
            if (!foundSame) {
                //当前电影没有缓存，加入下载列表
                LocalVideoInfo m = new LocalVideoInfo();
                long dsize = MyApplication.getp2p().P2Pgetdownsize(vlcVideoBean.getTid());
                long fsize = MyApplication.getp2p().P2Pgetfilesize(vlcVideoBean.getTid());

                if (fsize == 0) {
                    m.setPercent(0 + "");
                } else {
                    m.setPercent((int) ((1000 * dsize) / fsize) + "");
                }
                m.setUrl(vlcVideoBean.getVideoHttpUrl());
                m.setTitle(vlcVideoBean.getVideoName());
                m.setSpeed_info("0.0 KB");
                m.setInfo(FileUtil.getSize(fsize));
                //加入到下载列表中
                m.setRunning(LocalVideoInfo.running_Star);
                m.setTid(String.valueOf(vlcVideoBean.getTid()));
                m.setTvPosition(vlcVideoBean.getTvPosition());
                if (!TextUtils.isEmpty(vlcVideoBean.getVideoImageUrl())) {
                    m.setVideoImage(vlcVideoBean.getVideoImageUrl());
                }
                if (!TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
                    m.setVod_id(vlcVideoBean.getVideoId());
                }

                mData.add(0, m);
                if (null != XGConstant.userSeeVideoId) {
                    XGConstant.userSeeVideoId = m.getTitle();
                }
                XGUtil.saveList(mData);
                LogUtil.e("VlcPlayActivity", "下载列表没有该电影，加入下载列表" + vlcVideoBean.toString());
            } else {
                LogUtil.e("VlcPlayActivity", "已在下载列表");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHistory(int time, VlcVideoBean vlcVideoBean) {
        if (TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
            return;
        }
        String videoUrl = vlcVideoBean.getVideoHttpUrl();
        boolean foundSame = false;
        List<LocalVideoInfo> mData = XGUtil.loadHistoryList();
        for (LocalVideoInfo m : mData) {
            if (videoUrl.equals(m.getUrl())) {
                mData.remove(m);
                m.setDuration(time + "");
                if (!TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
                    m.setVod_id(vlcVideoBean.getVideoId());
                }
                mData.add(0, m);
                foundSame = true;
                break;
            }
        }
        if (!foundSame) {
            LocalVideoInfo mapData = new LocalVideoInfo();
            mapData.setUrl(videoUrl);
            mapData.setDuration(time + "");
            mapData.setTitle(vlcVideoBean.getVideoName());
            mapData.setTid(String.valueOf(vlcVideoBean.getTid()));
            mapData.setTvPosition(vlcVideoBean.getTvPosition());
            if (!TextUtils.isEmpty(vlcVideoBean.getVideoImageUrl())) {
                mapData.setVideoImage(vlcVideoBean.getVideoImageUrl());
            }
            if (!TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
                mapData.setVod_id(vlcVideoBean.getVideoId());
            }
            mData.add(0, mapData);
        }
        XGUtil.saveHistoryList(mData);
        LogUtil.e("VlcPlayActivity", "更新播放记录=======" + time + "=======" + videoUrl + "=====" + mData.get(0).getUrl());
    }
}
