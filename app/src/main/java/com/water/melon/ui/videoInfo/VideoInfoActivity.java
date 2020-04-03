package com.water.melon.ui.videoInfo;

import android.Manifest;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.sunfusheng.progress.GlideApp;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.view.touping.ToupingListen;
import com.tencent.liteav.demo.play.view.touping.ToupingView;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.constant.XGConstant;
import com.water.melon.evenbus.EvenBusEven;
import com.water.melon.presenter.VideoInfoPresenter;
import com.water.melon.presenter.contract.VideoInfoContract;
import com.water.melon.ui.netresource.ItemNetResouceItemAdapter1;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.ui.player.MyOrientationEventListener;
import com.water.melon.ui.player.VlcVideoBean;
import com.water.melon.ui.player.adapter.TvAnthologyCountAdapter;
import com.water.melon.ui.player.adapter.VideoInfoAdapter;
import com.water.melon.utils.ClickTooQucik;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.NumberUtils;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.MessageButtonDialog;
import com.water.melon.views.VideoDetailTvAnthologyDialog;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VideoInfoActivity extends BaseActivity implements VideoInfoAdapter.AdapterListen, SuperPlayerView.OnSuperPlayerViewCallback, ToupingListen
        , VideoInfoContract.View, TvAnthologyCountAdapter.AdapterLsiten {
    @BindView(R.id.video_info_name)
    TextView videoInfoName;
    @BindView(R.id.video_info_simple)
    TextView videoInfoSimple;
    @BindView(R.id.video_info_blurd)
    TextView videoInfoBlurd;
    @BindView(R.id.video_info_check_more_inof)
    TextView videoInfoCheckMoreInof;
    @BindView(R.id.card_lay)
    LinearLayout cardLay;
    @BindView(R.id.video_info_directors)
    TextView videoInfoDirectors;
    @BindView(R.id.video_info_actors)
    TextView videoInfoActors;
    @BindView(R.id.video_info_many_resous_layout)
    LinearLayout videoInfoManyResousLayout;
    @BindView(R.id.video_info_download)
    TextView videoInfoDownload;
    @BindView(R.id.video_info_resours_rv)
    RecyclerView videoInfoResoursRv;
    @BindView(R.id.video_info_about_tab)
    TextView videoInfoAboutTab;

    @BindView(R.id.video_info_about_tab_lin)
    ImageView video_info_about_tab_lin;
    @BindView(R.id.video_info_resours_tab_lin)
    ImageView video_info_resours_tab_lin;


    @BindView(R.id.video_info_resours_tab)
    TextView videoInfoResoursTab;
    @BindView(R.id.video_view)
    SuperPlayerView mView;
    @BindView(R.id.player_view_buffer_info)
    TextView bufferInfo;
    @BindView(R.id.player_view_buffer_info_layout)
    RelativeLayout bufferInfoLayout;
    @BindView(R.id.touping_vs)
    ViewStub toupingVs;
    @BindView(R.id.info_layout)
    NestedScrollView infoLayout;
    @BindView(R.id.player_view_error_image)
    ImageView playerViewErrorImage;
    @BindView(R.id.player_view_error_layout)
    RelativeLayout playerViewErrorLayout;
    @BindView(R.id.more_resours_image)
    ImageView moreResoursImage;
    @BindView(R.id.more_resours_layout)
    RelativeLayout moreResoursLayout;
    @BindView(R.id.more_resours_play_position)
    TextView moreResoursPlayPosition;
    @BindView(R.id.status_bar_view)
    View status_bar_view;

    private VideoInfoPresenter mPresenter;
    private VideoPlayBean VideoPlayBean;
    private VideoInfoAdapter mAdapter;
    private ToupingView mToupingView;
    //停止Rx循环
    private Disposable mDisposable;
    //加载速度
    private long loadKb;
    //视频资源播放不出自动上报
    private Disposable feedBackDisposable;
    //========  电视剧相关字段 ===================
    //记录连续剧的播放信息，提示用户是否要继续观看上次播放的技术和位置
    //电视剧选集弹窗
    private VideoDetailTvAnthologyDialog tvAnthologyDialog;
    private Vector<VlcVideoBean> tvLastPlayInfo;
    private VlcVideoBean tvLastPlay;
    //电视剧上次播放第几集
    private HashMap<String, Integer> tvLastPlayAnthology;
    //跳转过指定时间
    private boolean hasSeekToTv;
    //电视剧当前是第几集
    private int tvPlayPositon = 0;
    private int tvListCount = 0;//电视剧一共多少集
    private TvAnthologyCountAdapter tvAnthologyCountAdapter;
    //========电视剧相关字段 End ===================
    //当前播放时下载完的
    private boolean isDone = false;
    //旋转监听
    MyOrientationEventListener myOrientationEventListener;
    //用户锁屏
    private boolean userClockLock = false;
    private boolean inPlay = true;//进来就播放
    private VlcVideoBean vlcVideoBean;
    private boolean isPaus = false;//当前app是否回退到桌面或者被挡住了
    private boolean isFull = false;
    private boolean isOtherVideo = false;//非服务器资源，都是强制全屏

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_video_info;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setFullQiLiuHai();
        if (getBundle().getSerializable(XGConstant.KEY_DATA) instanceof VideoPlayBean) {
            VideoPlayBean = (VideoPlayBean) getBundle().getSerializable(XGConstant.KEY_DATA);
        } else if (getBundle().getSerializable(XGConstant.KEY_DATA) instanceof VlcVideoBean) {
            vlcVideoBean = (VlcVideoBean) getBundle().getSerializable(XGConstant.KEY_DATA);
        }
        if (VideoPlayBean == null && null == vlcVideoBean) {
            return;
        }
        new VideoInfoPresenter(this, this);
        mPresenter.start();
    }

    @Override
    public void initView() {
        mView.setScreen_Width_Heigh(XGConstant.Screen_Width, XGConstant.Screen_Height);
        mView.setPlayerViewCallback(this);
        //旋转监听
        myOrientationEventListener = new MyOrientationEventListener(new SoftReference<>(this), new MyOrientationEventListener.OrientationListener() {
            @Override
            public void OrientationEventListener(int orientation) {
                //播放视频的时候可以随着屏幕旋转
                //是否开启自动旋转设置 1 开启 0 关闭
                int screenchange = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                if (screenchange == 0 || null == mView || !mView.getmVLCVideoView().isPlaying() || isOtherVideo || userClockLock) {
                    return;
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mView) {
                            if (orientation == 1) {
                                mView.autoFullScreen(true);
                            } else {
                                mView.autoFullScreen(false);
                            }
                        }
                        if (!userClockLock) {
                            myOrientationEventListener.enable();
                        }
                    }
                }, 500);
            }
        });
        myOrientationEventListener.enable();
        if (null != VideoPlayBean) {
            mPresenter.getVideoInfo(VideoPlayBean.getId());
        } else {
            if (TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
                getLastPlayRedord(vlcVideoBean.getVideoHttpUrl());
                playVideo();
            } else {
                mPresenter.getVideoInfo(vlcVideoBean.getVideoId());
            }
        }
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public void itemClick(VideoPlayBean.Zh item, int position) {
        //剧集点击
        tvPlayPositon = position;
        bufferInfoLayout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(item.getUrl())) {
            vlcVideoBean.setVideoImageUrl(VideoPlayBean.getPoster());
            vlcVideoBean.setVideoHttpUrl(item.getUrl());
            vlcVideoBean.setVideoId(VideoPlayBean.getId());
            playVideo();
        } else {
            ToastUtil.showToastLong("暂无播放源");
        }
    }

    @Override
    protected void onClickTitleBack() {
        goBackBySlowly();
    }

    @Override
    protected void onClickTitleRight() {

    }


    @OnClick({R.id.video_info_check_more_inof, R.id.video_info_download
            , R.id.video_info_about_tab, R.id.video_info_resours_tab, R.id.player_view_error_back
            , R.id.more_resours_back, R.id.more_resours_play, R.id.more_resours_play_position, R.id.more_resours_layout})
    public void onViewClicked(View view) {
        if (ClickTooQucik.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.player_view_error_back:
            case R.id.more_resours_back:
                goBackByQuick();
                break;
            case R.id.video_info_check_more_inof:
                new AlertDialog.Builder(this)
                        .setMessage(VideoPlayBean.getSynopsis())
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.video_info_download:
//                // 获取系统剪贴板
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
//                ClipData clipData = ClipData.newPlainText(null, VideoPlayBean.getBtbo_downlist().get(0).getUrl());
//                // 把数据集设置（复制）到剪贴板
//                clipboard.setPrimaryClip(clipData);
//                new MessageButtonDialog<String>(view.getContext(), getStringByResId(R.string.message_dialog_title),
//                        getStringByResId(R.string.video_detail_Pc_download), true, null).show();
                break;
            case R.id.video_info_about_tab:
                videoInfoAboutTab.setTextColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
                videoInfoResoursTab.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
                cardLay.setVisibility(View.VISIBLE);
                video_info_about_tab_lin.setVisibility(View.VISIBLE);
                video_info_resours_tab_lin.setVisibility(View.GONE);
                videoInfoResoursRv.setVisibility(View.GONE);
                break;
            case R.id.video_info_resours_tab:
                videoInfoAboutTab.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
                videoInfoResoursTab.setTextColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
                cardLay.setVisibility(View.GONE);
                video_info_about_tab_lin.setVisibility(View.GONE);
                video_info_resours_tab_lin.setVisibility(View.VISIBLE);
                videoInfoResoursRv.setVisibility(View.VISIBLE);
                break;
            case R.id.more_resours_play:
                if (tvPlayPositon > -1 && null != mAdapter && tvPlayPositon < mAdapter.getDatas().size()) {
                    mAdapter.changeItemColor(tvPlayPositon);
                    playVideo();
                }
                break;
            case R.id.more_resours_play_position:
                vlcVideoBean = tvLastPlay;
                if (null != mAdapter) {
                    mAdapter.changeItemColor(tvPlayPositon);
                }
                playVideo();
                break;
        }
    }

    //=================================
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(XGConstant.KEY_DATA, vlcVideoBean);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent.getExtras()) {
            if (initBrfore(intent.getExtras())) return;
            initView();
        }
    }

    public boolean initBrfore(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            vlcVideoBean = (VlcVideoBean) savedInstanceState.getSerializable(XGConstant.KEY_DATA);
        } else {
            vlcVideoBean = (VlcVideoBean) getBundle().getSerializable(XGConstant.KEY_DATA);
        }
        if (null == vlcVideoBean || TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
            ToastUtil.showToastLong("播放地址不能为空");
            return true;
        }
        return false;
    }

    /**
     * 适配齐刘海
     */
    public void setFullQiLiuHai() {
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaus = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_black, null));
        }
        if (null != mView && null != mView.getmVLCVideoView() && !mView.getmVLCVideoView().isPlaying()
                && null != vlcVideoBean && !TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
            if (null == mToupingView || mToupingView.getVisibility() == View.GONE) {
                //不是投屏的时候才继续播放
                mView.onResume();
                if (isFull) {
                    mView.autoFullScreen(true);
                } else {
                    mView.autoFullScreen(false);
                }
                myOrientationEventListener.enable();
            }
            //屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPaus = true;
        if (null != myOrientationEventListener) {
            myOrientationEventListener.disable();
        }
        try {
            if (null != mView && null != vlcVideoBean) {
                if (null != mView.getmVLCVideoView() && mView.getmVLCVideoView().isPlaying()) {
                    if (mView.getmCurrentTime() > 3) {
                        mView.TakeSnapshot(mView.getScreenPath());//保留最后播放的界面
                    }
                    mView.onPause();
                    if (mView.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                        mView.autoFullScreen(false);
                        isFull = true;
                    } else {
                        mView.autoFullScreen(true);
                        isFull = false;
                    }
                }
            }
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            LogUtil.e("ssssssssssss", "截屏失败");
        }
    }

    @Override
    public void getVideoInfo(VideoPlayBean data) {
        if (null == data) {
            findViewById(R.id.more_resours_play).setVisibility(View.GONE);
            bufferInfoLayout.setVisibility(View.GONE);
            if (null != vlcVideoBean && vlcVideoBean.isDone()) {
                playVideo();
            }
            return;
        }
        VideoPlayBean = data;
        showLoadingDialog(false);
        videoInfoName.setText(data.getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        if (null != data.getYear()) {
            stringBuilder.append(data.getYear());
            stringBuilder.append("·");
        }
        if (!TextUtils.isEmpty(data.getRuntime())) {
            stringBuilder.append(data.getRuntime());
            stringBuilder.append("·");
        }
        if (null != data.getReleased()) {
            stringBuilder.append(data.getReleased());
            stringBuilder.append("·");
        }
        if (!TextUtils.isEmpty(data.getScore())) {
            stringBuilder.append(data.getScore());
        }
        videoInfoSimple.setText(stringBuilder.toString());


        videoInfoBlurd.setText(ItemNetResouceItemAdapter1.trim1(data.getSynopsis()));

        if (null != data.getDirectors()) {
//            StringBuilder directors = new StringBuilder();
//            for (VideoPlayBean.PeopleName director : data.getDirectors()) {
//                directors.append(director.getName());
//                directors.append("，");
//            }
            videoInfoDirectors.setText(data.getDirectors());
        }

        if (null != data.getActors()) {
            videoInfoActors.setText(data.getActors());
        }
        if (null == vlcVideoBean) {
            vlcVideoBean = new VlcVideoBean();
            vlcVideoBean.setVideoImageUrl(VideoPlayBean.getPoster());
            vlcVideoBean.setVideoId(VideoPlayBean.getId());
        }
        String playUrl = "";
        if (VideoPlayBean.getTorrents().getZh().size() > 1) {
            //连续剧、多集
            inPlay = false;
            GlideApp.with(moreResoursImage)
                    .load(VideoPlayBean.getPoster())
                    .placeholder(R.mipmap.bg_video_plact_vertical)
                    .into(moreResoursImage);
            tvListCount = VideoPlayBean.getTorrents().getZh().size();
            moreResoursLayout.setVisibility(View.VISIBLE);
            videoInfoResoursRv.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new VideoInfoAdapter(VideoPlayBean.getTorrents().getZh(), this);
            videoInfoResoursRv.setAdapter(mAdapter);

            videoInfoManyResousLayout.setVisibility(View.VISIBLE);
            videoInfoDownload.setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) cardLay.getLayoutParams()).setMargins(0, 0, 0, 0);

            String json = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Tv_PLAY_RECORD_POSITION, "");
            if (!TextUtils.isEmpty(json)) {
                tvLastPlayAnthology = GsonUtil.toClass(json, new TypeToken<HashMap<String, Integer>>() {
                }.getType());
                if (null != tvLastPlayAnthology && tvLastPlayAnthology.get(VideoPlayBean.getId()) != null) {
                    if (TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
                        playUrl = VideoPlayBean.getTorrents().getZh().get(tvLastPlayAnthology.get(VideoPlayBean.getId())).getUrl();
                    } else {
                        playUrl = vlcVideoBean.getVideoHttpUrl();
                    }
                    getLastPlayRedord(playUrl);
                } else {
                    playUrl = VideoPlayBean.getTorrents().getZh().get(0).getUrl();
                }
            } else {
                playUrl = VideoPlayBean.getTorrents().getZh().get(0).getUrl();
            }
            //获取当前是第几集
            if (null != VideoPlayBean.getTorrents() && null != VideoPlayBean.getTorrents().getZh() && VideoPlayBean.getTorrents().getZh().size() > 0) {
                List<VideoPlayBean.Zh> tvItems = VideoPlayBean.getTorrents().getZh();
                int size = tvItems.size();
                for (int i = 0; i < size; i++) {
                    if (tvItems.get(i).getUrl().equals(playUrl)) {
                        tvPlayPositon = i;
                        break;
                    }
                }
            }

        } else {
            playUrl = VideoPlayBean.getTorrents().getZh().get(0).getUrl();
        }
        if (!TextUtils.isEmpty(vlcVideoBean.getVideoPlayUrl())) {
            vlcVideoBean.setVideoHttpUrl(playUrl);
            if (inPlay) {
                mView.setPlayNextSourAndXuanjiButtonGone(View.GONE);
            } else {
                mView.setPlayNextSourAndXuanjiButtonGone(View.VISIBLE);
            }
            if (vlcVideoBean.getTvPosition() >= 0 && mAdapter != null && vlcVideoBean.getTvPosition() < mAdapter.getDatas().size()) {
                mAdapter.changeItemColor(vlcVideoBean.getTvPosition());
            }
//            TODO 播放地址
            StarPlay();
            return;
        }
        if (VideoPlayBean.getTorrents().getZh().size() < 1) {
            ToastUtil.showToastLong(getStringByResId(R.string.video_detail_play_resour_empty));
            vlcVideoBean.setVideoHttpUrl("");
        } else {
            vlcVideoBean.setVideoHttpUrl(playUrl);
        }
        //开始播放
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            //本地或者外网的
                            if (inPlay) {
                                mView.setPlayNextSourAndXuanjiButtonGone(View.GONE);
                                playVideo();
                            } else {
                                mView.setPlayNextSourAndXuanjiButtonGone(View.VISIBLE);
                            }
                        } else {
                            ToastUtil.showToastLong(getStringByResId(R.string.permission_sd));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(getStringByResId(R.string.permission_sd));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setPlayAdv(boolean hasAdv) {
        playVideo();
    }

    @Override
    public void setPresenter(VideoInfoContract.Presenter presenter) {
        mPresenter = (VideoInfoPresenter) presenter;
    }

    @Override
    protected void onStop() {
        //保存当前电视剧视频播放位置信息记录
        savePlayPotionRecord();
        super.onStop();
        downloadAndHistory();
        EventBus.getDefault().post(new EvenBusEven.OffLineDownEven(1));//下载
        EventBus.getDefault().post(new EvenBusEven.DownLoadDoneEven());//下载完成
        EventBus.getDefault().post(new EvenBusEven.PlayRecordEven());//播放历史
        //关闭屏幕常亮
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setFullScreenOnly(false);
    }

    /**
     * 保存当前电视剧视频播放位置信息记录
     */
    private void savePlayPotionRecord() {
        if (null != vlcVideoBean && null != tvLastPlayInfo) {
            boolean hasAdd = false;//是否有记录过
            for (VlcVideoBean videoBean : tvLastPlayInfo) {
                if (videoBean.getVideoHttpUrl().equals(vlcVideoBean.getVideoHttpUrl())) {
                    //已添加过
                    hasAdd = true;
                    if ((mView.getmCurrentTime() * 1000) < (mView.getmVLCVideoView().getDuration() - 3000)) {
                        videoBean.setLastPlayTime(mView.getmCurrentTime() * 1000 + "");
                    } else {
                        //最后三秒不计
                        videoBean.setLastPlayTime("0");
                    }
//                    videoBean.setTvPosition(tvPlayPositon);
                    break;
                }
            }
            if (!hasAdd) {
                if ((mView.getmCurrentTime() * 1000) < (mView.getmVLCVideoView().getDuration() - 3000)) {
                    vlcVideoBean.setLastPlayTime(mView.getmCurrentTime() * 1000 + "");
                } else {
                    //最后三秒不计
                    vlcVideoBean.setLastPlayTime("0");
                }
                vlcVideoBean.setTvPosition(tvPlayPositon);
                tvLastPlayInfo.add(0, vlcVideoBean);
            }
            if (tvLastPlayInfo.size() > 50) {
                List<VlcVideoBean> newVlcVideoBeans = new ArrayList<>();
                for (int i = 0; i < 20; i--) {
                    newVlcVideoBeans.add(tvLastPlayInfo.get(i));
                }
                tvLastPlayInfo.clear();
                tvLastPlayInfo.addAll(newVlcVideoBeans);
                LogUtil.e("播放界面", "播放记录大小" + tvLastPlayInfo.size());
            }
            LogUtil.e("播放界面", "添加播放时间记录===" + mView.getmCurrentTime());
            LogUtil.e("播放界面", "添加播放记录===" + tvLastPlayInfo.toString());
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_PLAY_RECORD_INFO, tvLastPlayInfo.toString());

            if (!inPlay) {
                //记录电视剧上次播放到第几集
                if (null == tvLastPlayAnthology) {
                    tvLastPlayAnthology = new HashMap<>();
                }
                tvLastPlayAnthology.put(vlcVideoBean.getVideoId(), tvPlayPositon);
                if (tvLastPlayAnthology.size() > 50) {
                    HashMap<String, Integer> newData = new HashMap<>();
                    int size = 0;
                    for (Map.Entry<String, Integer> entry : tvLastPlayAnthology.entrySet()) {
                        newData.put(entry.getKey(), entry.getValue());
                        size++;
                        if (size == 30) {
                            break;
                        }
                    }
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Tv_PLAY_RECORD_POSITION, newData.toString());
                } else {
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Tv_PLAY_RECORD_POSITION, tvLastPlayAnthology.toString());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        exitplayer();
        super.onDestroy();
    }

    private void playVideo() {
        if (TextUtils.isEmpty(vlcVideoBean.getVideoHttpUrl())) {
            //没有播放地址
            mView.setVisibility(View.INVISIBLE);
            bufferInfoLayout.setVisibility(View.GONE);
            playerViewErrorLayout.setVisibility(View.VISIBLE);
            GlideApp.with(playerViewErrorImage)
                    .load(vlcVideoBean.getVideoImageUrl())
                    .into(playerViewErrorImage);
            ToastUtil.showToastLong("暂不支持该播放地址");
            return;
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (vlcVideoBean.getVideoHttpUrl().contains("file://") || TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
            //本地视频或链接下载的
//            mView.setmPlayMode(SuperPlayerConst.PLAYMODE_FULLSCREEN);
            isOtherVideo = true;
            isFull = true;
            mView.setLargeQualityVisibility(View.INVISIBLE);
            setFullScreenOnly(true);
            mView.fullScreen(true);
            infoLayout.setVisibility(View.GONE);
            StarPlay();
        } else {
            mView.fullScreen(false);
            if (vlcVideoBean.getVideoHttpUrl().indexOf("xg") != 0 && vlcVideoBean.getVideoHttpUrl().indexOf("http") != 0
                    && vlcVideoBean.getVideoHttpUrl().indexOf("ftp") != 0) {
                ToastUtil.showToastLong("暂不支持该播放地址");
                return;
            }
            String url = vlcVideoBean.getVideoHttpUrl();
//            url = "https://vo.zhuqc.com//20191225//vQ7nBOLn//index.m3u8";
//            url = "https://www.8090g.cn/jiexi/?url=https://www.iqiyi.com/v_19rrc1di2c.html#vfrm=19-9-0-1";
            Log.e("MainActivity", "url = " + url);
            getLastPlayRedord(url);
            XGUtil.getPlayUrl(url, new XGUtil.GetVlcVideoBean() {
                @Override
                public void getVlcVideoBean(VlcVideoBean vlcBean) {
                    if (null != vlcBean) {
                        vlcBean.setVideoId(vlcVideoBean.getVideoId());
                        vlcBean.setVideoImageUrl(vlcVideoBean.getVideoImageUrl());
                        vlcVideoBean = vlcBean;
                        StarPlay();
                    } else {
                        ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_error));
                    }
                }
            });
        }
    }

    public void StarPlay() {
        downloadAndHistory();
        if (moreResoursLayout.getVisibility() == View.VISIBLE) {
            moreResoursLayout.setVisibility(View.GONE);
        }
        isDone = false;
        //播放视频
        SuperPlayerModel model = new SuperPlayerModel();
        model.url = vlcVideoBean.getVideoPlayUrl();
        model.title = vlcVideoBean.getVideoName();
        mView.setScreenPath(FileUtil.ScreenPathCache, vlcVideoBean.getVideoName());
        onBufferInfo(0, false);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mView.playWithModel(model);
        showLoadingDialog(false);
        //隐藏顶部栏
        if (mView.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onStartFullScreenPlay() {
        infoLayout.setVisibility(View.GONE);
        //TODO 友盟点击统计
//        XGConstant.AddYOuMengs(XGConstant.quan_screen);
        if (null != mToupingView) {
            mToupingView.fullScreen(true);
        }
    }

    @Override
    public void onStopFullScreenPlay() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_black, null));
        }
        if (!TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
            infoLayout.setVisibility(View.VISIBLE);
        }
        if (null != mToupingView) {
            mToupingView.fullScreen(false);
        }
    }

    @Override
    public void onClickFloatCloseBtn() {

    }

    @Override
    public void onTvAnthologyItemClick(VideoPlayBean.Zh tv, int position) {
        //电视剧选集点击播放
        if ((position + 1) == tvListCount) {
            //没有下一集
            mView.setPlayNextSourGone(View.GONE, false);
        } else {
            mView.setPlayNextSourGone(View.VISIBLE, false);
        }
        isDone = false;
        tvPlayPositon = position;
//        vlcPlayVideoAnthologyRv.scrollToPosition(position);
        mAdapter.changeItemColor(position);
        //显示挡板
        mView.setmVLCVideoViewBaffleShow(true);
        mView.onPause();
        XGUtil.getPlayUrl(tv.getUrl(), new XGUtil.GetVlcVideoBean() {
            @Override
            public void getVlcVideoBean(VlcVideoBean vlcBean) {
                if (vlcBean != null) {
                    try {
                        vlcVideoBean = vlcBean;
                        vlcVideoBean.setVideoId(VideoPlayBean.getId());
                        vlcVideoBean.setVideoImageUrl(VideoPlayBean.getPoster());
                        vlcVideoBean.setTvPosition(tvPlayPositon);
                        mView.setXuanjiCheckPosition(tvPlayPositon);
                        if (null != mToupingView && mToupingView.getVisibility() == View.VISIBLE) {
                            //投屏播放
                            playOnTouPing(vlcVideoBean.getVideoName(), vlcVideoBean.getVideoId(), "", 10000
                                    , mView.getmVLCVideoView().getDuration(), vlcVideoBean.getVideoPlayUrl(), mView.getmCurrentTime(), true);
                        } else {
                            bufferInfo.setText(MyApplication.getStringByResId(R.string.video_detail_buffinfo_tip) + "\n" + vlcVideoBean.getVideoName());
                            bufferInfoLayout.setVisibility(View.VISIBLE);
                            playVideo();
                        }
                        if (null != tvAnthologyDialog && tvAnthologyDialog.isVisible()) {
                            tvAnthologyDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_error));
                }
            }
        });
    }

    @Override
    public void playFinish() {
        //播放完毕
        if (ClickTooQucik.isFastClick(2000)) {
            return;
        }
        int nextPosition = 0;
        if (null != mAdapter) {
            nextPosition = mAdapter.getOldCheckPosition() + 1;
            if ((nextPosition + 1) == tvListCount) {
                //没有下一集
                mView.setPlayNextSourGone(View.GONE, false);
            } else if ((nextPosition + 1) < tvListCount) {
                mView.setPlayNextSourGone(View.VISIBLE, false);
            } else {
                mView.setPlayNextSourGone(View.GONE, true);
            }
        } else {
            mView.setPlayNextSourGone(View.GONE, true);
        }
        if (!inPlay && nextPosition < tvListCount) {
            //电视剧自动播放下一集
            LogUtil.e("ddddddddd", "playNextSour22222");
            tvPlayPositon = nextPosition;
            VideoPlayBean.Zh tv = mAdapter.getDatas().get(tvPlayPositon);
            mView.setmVLCVideoViewBaffleShow(true);
            XGUtil.getPlayUrl(tv.getUrl(), new XGUtil.GetVlcVideoBean() {
                @Override
                public void getVlcVideoBean(VlcVideoBean vlcBean) {
                    if (null != vlcBean) {
                        vlcVideoBean = vlcBean;
                        vlcVideoBean.setVideoId(VideoPlayBean.getId());
                        vlcVideoBean.setVideoImageUrl(VideoPlayBean.getPoster());
                        vlcVideoBean.setTvPosition(tvPlayPositon);
                        LogUtil.e("sssssssss", tvPlayPositon + "下一集====" + vlcVideoBean.toString());
                        XGUtil.getPlayUrl(vlcVideoBean.getVideoHttpUrl(), new XGUtil.GetVlcVideoBean() {
                            @Override
                            public void getVlcVideoBean(VlcVideoBean vlcBean) {
                                if (null != vlcBean) {
                                    vlcBean.setVideoId(vlcVideoBean.getVideoId());
                                    vlcBean.setVideoImageUrl(vlcVideoBean.getVideoImageUrl());
                                    vlcVideoBean = vlcBean;
                                    StarPlay();
                                } else {
                                    ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_error));
                                }
                            }
                        });
                        mAdapter.changeItemColor(tvPlayPositon);
                        mView.setXuanjiCheckPosition(tvPlayPositon);
                        bufferInfo.setText(MyApplication.getStringByResId(R.string.video_detail_buffinfo_tip) + "\n" + vlcVideoBean.getVideoName());
                        bufferInfoLayout.setVisibility(View.VISIBLE);
                        if (null != mToupingView && mToupingView.getVisibility() == View.VISIBLE) {
                            //投屏播放
                            playOnTouPing(vlcVideoBean.getVideoName(), vlcVideoBean.getVideoId(), "", 10000
                                    , mView.getmVLCVideoView().getDuration(), vlcVideoBean.getVideoPlayUrl(), mView.getmCurrentTime(), true);
                        }
//                        if (null != tvAnthologyDialog && tvAnthologyDialog.isVisible()) {
//                            tvAnthologyDialog.dismiss();
//                        }
                    } else {
                        ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_error));
                    }
                }
            });
        } else {
            mView.onPause();
        }
    }

    @Override
    public void onClickSmallReturnBtn() {
        //退出
        if (null == mView || vlcVideoBean == null) {
            goBackBySlowly();
        }
        if (mView.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW || (!TextUtils.isEmpty(vlcVideoBean.getVideoPlayUrl()) && vlcVideoBean.getVideoPlayUrl().indexOf("file://") == 0)
                || TextUtils.isEmpty(vlcVideoBean.getVideoId())) {
            //小屏，本地相册视频，手动添加视频
            goBackBySlowly();
        } else if (!TextUtils.isEmpty(vlcVideoBean.getVideoId()) && !TextUtils.isEmpty(vlcVideoBean.getVideoPlayUrl()) && vlcVideoBean.getVideoPlayUrl().indexOf("file://") != 0) {
            //全屏变小屏
            mView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
        }
    }


    @Override
    public void onBufferInfo(float buffer, boolean show) {
        //加载进度
        int p2pSpeed = MyApplication.getp2p().P2Pgetpercent();
        LogUtil.e("缓存=========", "缓存:" + NumberUtils.setScale(new BigDecimal(buffer + ""), 0) + " ====p2pSpeed= " + p2pSpeed);
        if (buffer == 0) {
            //获取来自P2P的速度
            LogUtil.e("缓存=========", "获取来自P2P的速度");
            if (null == mDisposable) {
                mDisposable = Observable.interval(2, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                showSpeed(MyApplication.getp2p().P2Pgetpercent(), show);
                            }
                        }).subscribe();
            }
        } else {
            //获取来自Vlc的速度
            if (null != mDisposable) {
                //释放Rx循环获取P2P的速度
                mDisposable.dispose();
                mDisposable = null;
            }
            showSpeed(buffer, show);
        }

    }

    /**
     * 显示加载进度
     *
     * @param showSpeed
     */
    private void showSpeed(float showSpeed, boolean show) {
        LogUtil.e("缓存=========", "缓存:" + showSpeed);
        if (isDone) {
            if (null != tvLastPlay && !TextUtils.isEmpty(tvLastPlay.getLastPlayTime())
                    && !"0".equals(tvLastPlay.getLastPlayTime()) && !hasSeekToTv) {
                //跳过片头或者调到上一次播放的位置
                hasSeekToTv = true;
                mView.seekToTime(Float.valueOf(tvLastPlay.getLastPlayTime()).longValue());
                ToastUtil.showToastShort("已为您跳转到上次播放的位置");
                LogUtil.e("aaaaaaaaaaaaaa", "添加播放记录===" + Float.valueOf(tvLastPlay.getLastPlayTime()).longValue());
            }
            bufferInfoLayout.setVisibility(View.GONE);
            //关闭挡板
            mView.setmVLCVideoViewBaffleShow(false);
            return;
        }
        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
                int tid = -1;
                if (null != vlcVideoBean) {
                    tid = vlcVideoBean.getTid();
                }
                long kb = MyApplication.getp2p().P2Pgetspeed(tid) / 1024;
                LogUtil.e("aaaaaaaaaaaaaa", "速度===" + kb + "====" + tid);
                emitter.onNext(kb);
            }
        }, BackpressureStrategy.LATEST).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Long kb) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(MyApplication.getStringByResId(R.string.video_detail_buffinfo_load))
                                .append(NumberUtils.setScale(new BigDecimal(showSpeed + ""), 0))
                                .append("%  ");
                        if (loadKb < kb) {
                            loadKb = kb;
                        }
                        stringBuilder.append(kb).append("/kb");
                        stringBuilder.append("\n" + vlcVideoBean.getVideoName());
                        bufferInfo.setText(stringBuilder);
                        if (showSpeed == 100) {
                            if (isPaus) {
                                mView.onPause();
                            }
                            bufferInfo.setText(MyApplication.getStringByResId(R.string.video_detail_buffinfo_tip));
                            bufferInfoLayout.setVisibility(View.GONE);
                            //关闭挡板
                            mView.setmVLCVideoViewBaffleShow(false);
                            if (null != tvLastPlay && !TextUtils.isEmpty(tvLastPlay.getLastPlayTime())
                                    && !"0".equals(tvLastPlay.getLastPlayTime()) && !hasSeekToTv) {
                                //跳过片头或者调到上一次播放的位置
                                hasSeekToTv = true;
                                mView.seekToTime(Float.valueOf(tvLastPlay.getLastPlayTime()).longValue());
                                LogUtil.e("aaaaaaaaaaaaaa", "跳转位置===" + Float.valueOf(tvLastPlay.getLastPlayTime()).longValue());
                                ToastUtil.showToastShort(MyApplication.getStringByResId(R.string.video_detail_play_old_position));
                            }
                            if (null != mDisposable) {
                                //释放Rx循环获取P2P的速度
                                mDisposable.dispose();
                                mDisposable = null;
                            }
                        }
                        if (showSpeed < 100 && bufferInfoLayout.getVisibility() == View.GONE && ((null != mView.getmVLCVideoView() && !mView.getmVLCVideoView().isPlaying()) || show)) {
                            bufferInfoLayout.setVisibility(View.VISIBLE);
                            LogUtil.e("aaaaaaaaaaaaaa", "显示加载布局" + "=====" + show);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onStartFloatWindowPlay() {

    }


    //退出播放器
    public void exitplayer() {
        if (null != mView) {
            mView.onDestory();
            mView = null;
        }
        if (null != mToupingView) {
            mToupingView.onDestory();
        }
        MyApplication.isLoad = true;
        MyApplication.flag = true;
        if (null != mDisposable) {
            mDisposable.dispose();
            mDisposable = null;
        }
        if (null != feedBackDisposable) {
            feedBackDisposable.dispose();
            feedBackDisposable = null;
        }
        LogUtil.e("播放界面", "退出播放==============================");
    }

    /**
     * 横竖屏切换
     *
     * @param fullScreenOnly
     */
    public void setFullScreenOnly(boolean fullScreenOnly) {
        if (fullScreenOnly) {
            //全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            //竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_VOLUME_DOWN == keyCode || KeyEvent.KEYCODE_VOLUME_UP == keyCode) {
            //显示声音进度条
            AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            if (null != audioManager) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
            }
        } else if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (mView.getPlayMode() != SuperPlayerConst.PLAYMODE_WINDOW && !TextUtils.isEmpty(vlcVideoBean.getVideoId())
                    && vlcVideoBean.getVideoPlayUrl().indexOf("file://") != 0) {
                //全屏变小屏
                mView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTouPing() {
        //投屏
        if (ClickTooQucik.isFastClick(1500)) {
            return;
        }
        if (null == mToupingView) {
            mToupingView = (ToupingView) toupingVs.inflate();
            mToupingView.setToupingListen(this);
            mToupingView.setScreen_Width(XGConstant.Screen_Width);
        }
        //TODO 友盟统计
//        XGConstant.AddYOuMengs(XGConstant.Tou_Ping);
        playOnTouPing(vlcVideoBean.getVideoName(), vlcVideoBean.getVideoId(), "", 10000
                , mView.getmVLCVideoView().getDuration(), vlcVideoBean.getVideoPlayUrl(), mView.getmCurrentTime(), false);
        mToupingView.setVisibility(View.VISIBLE);
    }

    private void playOnTouPing(String name, String vid, String creator, long size, long dunation, String url, long cerrentDution, boolean newUrl) {
        if (mView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAY) {
            mView.onPause();
        }
        mToupingView.setData(name, vid, creator, size
                , dunation, url, cerrentDution, newUrl);
    }

    @Override
    public void exitTouPing(long dunation, boolean newUrl) {
        if (null != mView) {
            if (mView.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE && !newUrl) {
                mView.onResume();
            } else {
                playVideo();
            }
            mView.seekToTime(dunation * 1000);
            mToupingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void playError(long time) {
        //播放出错
        XGUtil.getPlayUrl(vlcVideoBean.getVideoHttpUrl(), new XGUtil.GetVlcVideoBean() {
            @Override
            public void getVlcVideoBean(VlcVideoBean vlcBean) {
                if (null != vlcBean) {
                    if (null != tvLastPlay) {
                        tvLastPlay.setLastPlayTime(time + "");
                    }
                    playVideo();
                    if (!mView.getmVLCVideoView().isPlaying()) {
                        mView.onResume();
                    }
                } else {
                    ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_error));
                }
            }
        });
    }

    @Override
    public void lockScreen(boolean isLock) {
        //锁屏
        if (null != myOrientationEventListener) {
            userClockLock = isLock;
            if (isLock) {
                myOrientationEventListener.disable();
            } else {
                myOrientationEventListener.enable();
            }
        }
    }

    @Override
    public void inputDanmuByFullScreenPlay(String content) {

    }

    @Override
    public void playNextSour() {
        int nextSour = tvPlayPositon + 1;
        if ((nextSour + 1) == tvListCount) {
            //没有下一集
            mView.setPlayNextSourGone(View.GONE, false);
        } else {
            mView.setPlayNextSourGone(View.VISIBLE, false);
        }
        if (!inPlay && nextSour < tvListCount) {
            playFinish();
        } else {
            ToastUtil.showToastShort(getStringByResId(R.string.video_detail_play_no_next));
        }
    }

    /**
     * 上次播放位置
     */
    public String getLastPlayRedord(String videoHttpUrl) {
        if (null == tvLastPlay && !TextUtils.isEmpty(videoHttpUrl)) {
            String json = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_PLAY_RECORD_INFO, "");
            if (!TextUtils.isEmpty(json)) {
                tvLastPlayInfo = GsonUtil.toClass(json, new TypeToken<Vector<VlcVideoBean>>() {
                }.getType());
                if (null != tvLastPlayInfo) {
                    for (VlcVideoBean videoBean : tvLastPlayInfo) {
                        if (videoHttpUrl.equals(videoBean.getVideoHttpUrl())) {
                            tvPlayPositon = videoBean.getTvPosition();
                            vlcVideoBean.setTvPosition(tvPlayPositon);
                            tvLastPlay = videoBean;
                            if (tvPlayPositon > 0) {
                                moreResoursPlayPosition.setText("上次播放至 " + mAdapter.getDatas().get(tvPlayPositon).getTitle());
                            }
                            LogUtil.e("sssssssssssss", tvPlayPositon + "==================");
                            return videoBean.getVideoHttpUrl();
                        }
                    }
                }
            } else {
                tvLastPlayInfo = new Vector<>();
            }
        }
        return "";
    }

    /**
     * 添加下载和播放记录
     */
    private void downloadAndHistory() {
        if (null != vlcVideoBean) {
            if (null != mView) {
                mPresenter.updateHistory((int) mView.getmCurrentTime(), vlcVideoBean);
            }
            if (vlcVideoBean.getVideoPlayUrl() != null && !vlcVideoBean.getVideoPlayUrl().contains("file://")) {
                //本地视频不能加入下载界面
                mPresenter.downLoadVideo(vlcVideoBean);
                XGConstant.userSeeVideoId = vlcVideoBean.getVideoName();
            }
        }
    }


    @Override
    public void initImmersionBar() {
//        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarView(status_bar_view)
                .init();
    }

}
