package com.water.melon.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.constant.XGConstant;
import com.p2p.P2PClass;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.ui.player.VlcVideoBean;
import com.water.melon.ui.videoInfo.VideoInfoActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class XGUtil {
    public static Disposable disposable;

    /**
     * 加载电影列表
     */
    public static List<LocalVideoInfo> loadList() {
        try {
            String doneJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Dowanload_off, "");
            if (TextUtils.isEmpty(doneJson)) {
                return new ArrayList<>();
            } else {
                ArrayList<LocalVideoInfo> newList = GsonUtil.toClass(doneJson, new TypeToken<ArrayList<LocalVideoInfo>>() {
                }.getType());
                if (newList == null) {
                    return new ArrayList<>();
                } else {
                    return newList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 保存电影列表
     */
    public static void saveList(List<LocalVideoInfo> list) {
        try {
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Dowanload_off, list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载已完成电影列表
     */
    public static ArrayList<LocalVideoInfo> loadCacheList() {
        try {
            String doneJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Dowanload_done, "");
            if (TextUtils.isEmpty(doneJson)) {
                return new ArrayList<>();
            } else {
                ArrayList<LocalVideoInfo> newList = GsonUtil.toClass(doneJson, new TypeToken<ArrayList<LocalVideoInfo>>() {
                }.getType());
                if (newList == null) {
                    return new ArrayList<>();
                } else {
                    return newList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 保存电影列表
     */
    public static void saveCacheList(List<LocalVideoInfo> list) {
        try {
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Dowanload_done, list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存历史记录列表
     */
    public static void saveHistoryList(List<LocalVideoInfo> list) {
        try {
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Play_Record, list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取历史记录列表
     */
    public static List<LocalVideoInfo> loadHistoryList() {
        try {
            String doneJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Play_Record, "");
            if (TextUtils.isEmpty(doneJson)) {
                return new ArrayList<>();
            } else {
                ArrayList<LocalVideoInfo> newList = GsonUtil.toClass(doneJson, new TypeToken<ArrayList<LocalVideoInfo>>() {
                }.getType());
                if (newList == null) {
                    return new ArrayList<>();
                } else {
                    return newList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 停止下载
     *
     * @param url        除了当前播放地址
     * @param isNetVideo ture是从网络资源点播过来的下载列表全部停止,也可以用来停止所有下载
     */
    public static void stopAll(String url, boolean isNetVideo) {
        List<LocalVideoInfo> tmpData = loadList();
        if (!TextUtils.isEmpty(url)) {
            boolean isDownLoadDone = false;//是否是已经下载完成的，已下载完成的不影响正在下载的电影
            for (LocalVideoInfo m : loadCacheList()) {
                if (url.equals(m.getUrl())) {
                    isDownLoadDone = true;
                    break;
                }
            }
            if (isDownLoadDone) {
                //已下载完的视频不需要暂停其他下载的电影
                LogUtil.e("XGUtil", "stopAll==" + url);
                return;
            }
        }
        LogUtil.e("XGUtil", "stopAll===" + url + "====" + isNetVideo);
        MyApplication.downTaskPositionList.clear();
        for (LocalVideoInfo m : tmpData) {
            if (url.equals(m.getUrl())) {
                //每次播放都会暂停所有下载，所有在这校验如果是播放的视频则继续下载
                if (!MyApplication.downTaskPositionList.contains(m)) {
                    MyApplication.downTaskPositionList.add(m);
                }
                continue;
            }
            LogUtil.e("XGUtil", "stopAll........" + m.getUrl());
            try {
                MyApplication.getp2p().P2Pdoxpause(m.getUrl().getBytes("GBK"));
                int tid = Integer.valueOf(m.getTid());
                long dsize = MyApplication.getp2p().P2Pgetdownsize(tid);
                long fsize = MyApplication.getp2p().P2Pgetfilesize(tid);
                if (fsize == 0) {
                    fsize = 1;
                }
                m.setRunning(LocalVideoInfo.running_Stop);
                m.setSpeed_info(MyApplication.getStringByResId(R.string.download_manage_stop));
//                m.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
//                m.setInfo(FileUtil.getSize(fsize));
                m.setPercent((int) (1000 * dsize / fsize) + "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        saveList(tmpData);// 保存下载列表
    }

    //网络地址播放，不是本地服务器的资源，所以没有评论之类的东西
    public static void playXG(LocalVideoInfo info, final Activity activity) {
        playXG(info, activity, false);
    }

    //网络地址播放，不是本地服务器的资源，所以没有评论之类的东西
    public static void playXG(LocalVideoInfo info, final Activity activity, boolean isClose) {
//        if (null != disposable && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//        Observable.create(new ObservableOnSubscribe<Bundle>() {
//            @Override
//            public void subscribe(ObservableEmitter<Bundle> emitter) throws Exception {
        try {
            XGUtil.stopAll(info.getUrl(), false);

            // frag_ie.wvSaveState();
            String allurl = java.net.URLDecoder.decode(info.getUrl(), "UTF-8");
            String adurl = XGConstant.AdUrl;
            String referurl = "xghome://home";
            String[] urls = allurl.split("\\|");
            if (urls.length == 3) {
                adurl = urls[1];
                referurl = urls[2];
            }

            String newurl = urls[0].replace("xg://", "ftp://");
            String[] newurls = newurl.split("/");
            String title = newurls[newurls.length - 1];


            //:TJ:添加的修改的：兼容UC的Intent的JS
            if (newurl.contains("intent://")) {
                String[] newurls2 = newurl.split("#");
                newurls2 = newurls2[0].split("//");
//                newurls2= newurl3.split("//");
                newurl = "ftp://" + newurls2[1];


            }
            //:9.4号添加 兼容 新的 xgplay://开头的视频的兼容
            if (newurl.contains("xgplay://")) {
                newurl = urls[0].replace("xgplay://", "ftp://");

            }
            boolean isDone = false;
            for (LocalVideoInfo localVideoInfo : loadCacheList()) {
                if (localVideoInfo.getUrl().equals(newurl)) {
                    isDone = true;
                }
            }
            if (!info.getUrl().contains("file://") && !isDone) {
                MyApplication.tid = MyApplication.getp2p().P2Pdoxstart(newurl.getBytes("GBK"));
            } else {
                MyApplication.tid = Integer.parseInt(info.getTid());
                MyApplication.getp2p().P2Pdoxstart(newurl.getBytes("GBK"));
            }


            String currentUri = "http://127.0.0.1:" + P2PClass.port + "/" + URLEncoder.encode(Uri.parse(newurl).getLastPathSegment(), "GBK");

            Bundle mBundle = new Bundle();
            VlcVideoBean vlcVideoBean = new VlcVideoBean();
            if (info.getUrl().contains("file://")) {
                //本地视频
                vlcVideoBean.setVideoPlayUrl(info.getUrl());
            } else {
                vlcVideoBean.setVideoPlayUrl(currentUri);
            }
            vlcVideoBean.setDone(info.isDone());
            vlcVideoBean.setVideoHttpUrl(info.getUrl());
            vlcVideoBean.setVideoName(title);
            vlcVideoBean.setTid(MyApplication.tid);
            vlcVideoBean.setVideoId(info.getVod_id());//从资源下载下来的可以看到详情信息
            vlcVideoBean.setTvPosition(info.getTvPosition());
            mBundle.putSerializable(XGConstant.KEY_DATA, vlcVideoBean);
            Intent vintent = new Intent(activity, VideoInfoActivity.class);
            vintent.putExtras(mBundle);
            vintent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            activity.startActivity(vintent);
            if (isClose) {
                activity.finish();
            }
//                    emitter.onNext(mBundle);
        } catch (Exception e) {
            e.printStackTrace();
//                    emitter.onNext(null);
        }
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Bundle>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        disposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Bundle mBundle) {
//                        if (mBundle != null) {
//                            Intent vintent = new Intent(activity, VlcPlayActivity.class);
//                            vintent.putExtras(mBundle);
//                            vintent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                            activity.startActivity(vintent);
//                            if (isClose) {
//                                activity.finish();
//                            }
//                        } else {
//                            ToastUtil.showToastLong("加载视频失败，请稍后重试");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (!disposable.isDisposed()) {
//                            disposable.dispose();
//                        }
//                    }
//                });

    }

    /**
     * 网络资源跳转过来的
     *
     * @param vlcVideoBean
     * @param activity
     * @param hasSeekToLastPlay 是否要跳转到电视剧上次播放的集数和位置
     */
    public static void playXG(VlcVideoBean vlcVideoBean, final Activity activity, boolean hasSeekToLastPlay) {
        Intent vintent = new Intent(activity, VideoInfoActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(XGConstant.KEY_DATA, vlcVideoBean);
        if (hasSeekToLastPlay) {
            mBundle.putBoolean(XGConstant.KEY_DATA_2, true);
        }
        vintent.putExtras(mBundle);
        vintent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        activity.startActivity(vintent);
    }

    /**
     * 网络资源->播放界面->服务器获取的地址
     *
     * @param urlByService
     */
    public static void getPlayUrl(String urlByService, GetVlcVideoBean getVlcVideoBean) {
//        if (null != disposable && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//        Observable.create(new ObservableOnSubscribe<VlcVideoBean>() {
//            @Override
//            public void subscribe(ObservableEmitter<VlcVideoBean> emitter) throws Exception {
        //暂停所有下载
        try {
//            XGUtil.stopAll(urlByService, true);
            XGUtil.stopAll(urlByService, true);
            String allurl = java.net.URLDecoder.decode(urlByService, "UTF-8");
            String adurl = XGConstant.AdUrl;
            String referurl = "xghome://home";
            String[] urls = allurl.split("\\|");
            if (urls.length == 3) {
                adurl = urls[1];
                referurl = urls[2];
            }

            String newurl = urls[0].replace("xg://", "ftp://");
            String[] newurls = newurl.split("/");
            String title = newurls[newurls.length - 1];


            //:TJ:添加的修改的：兼容UC的Intent的JS
            if (newurl.contains("intent://")) {
                String[] newurls2 = newurl.split("#");
                newurls2 = newurls2[0].split("//");
//                newurls2= newurl3.split("//");
                newurl = "ftp://" + newurls2[1];


            }
            //:9.4号添加 兼容 新的 xgplay://开头的视频的兼容
            if (newurl.contains("xgplay://")) {
                newurl = urls[0].replace("xgplay://", "ftp://");

            }
            String currentUri = "http://127.0.0.1:" + P2PClass.port + "/" + URLEncoder.encode(Uri.parse(newurl).getLastPathSegment(), "GBK");
            VlcVideoBean vlcVideoBean = new VlcVideoBean();
            vlcVideoBean.setVideoPlayUrl(currentUri);
            vlcVideoBean.setVideoHttpUrl(urlByService);
            vlcVideoBean.setVideoName(title);
            int tid = 0;
            for (LocalVideoInfo localVideoInfo : loadCacheList()) {
                if (localVideoInfo.getUrl().equals(newurl)) {
                    tid = -1;
                }
            }
            if (tid == 0) {
                MyApplication.tid = MyApplication.getp2p().P2Pdoxstart(newurl.getBytes("GBK"));
                MyApplication.getp2p().P2Pdoxadd(urlByService.getBytes("GBK"));
//                MyApplication.getp2p().P2Pdoxdownload(urlByService.getBytes("GBK"));
            } else {
                MyApplication.tid = tid;
                MyApplication.getp2p().P2Pdoxstart(newurl.getBytes("GBK"));
            }
            vlcVideoBean.setTid(MyApplication.tid);
            getVlcVideoBean.getVlcVideoBean(vlcVideoBean);
//                    emitter.onNext(vlcVideoBean);
        } catch (Exception e) {
            e.printStackTrace();
//                    emitter.onNext(null);
        }
//    }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VlcVideoBean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable = d;
//            }
//
//            @Override
//            public void onNext(VlcVideoBean vlcVideoBean) {

//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                if (!disposable.isDisposed()) {
//                    disposable.dispose();
//                }
//            }
//        });
    }

    public interface GetVlcVideoBean {
        void getVlcVideoBean(VlcVideoBean vlcVideoBean);
    }

}
