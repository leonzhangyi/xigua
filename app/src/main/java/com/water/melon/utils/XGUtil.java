package com.water.melon.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.just.agentweb.LogUtils;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.constant.XGConstant;
import com.p2p.P2PClass;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.net.bean.RoadBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.ui.home.BannerActivity;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.agent.AgentActivity;
import com.water.melon.ui.me.share.ShareActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.ui.player.VlcVideoBean;
import com.water.melon.ui.videoInfo.VideoInfoActivity;
import com.water.melon.utils.glide.GlideHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class XGUtil {
    public static final String AGENT_PHONE = "agent_phone";
    public static final String AGENT_STATE = "agent_state";
    public static final String AGENT_ID = "agent_id";

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


    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    public static void copyText(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
        ToastUtil.showToastLong("复制成功！");
    }


    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
//注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

        }
        return i;
    }

    public static UserBean getMyUserInfo() {
        UserBean userBean = null;
        SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        String baseInfo = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        if (baseInfo != null && !baseInfo.trim().equals("") && !baseInfo.trim().equals("[]")) {
            userBean = (UserBean) GsonUtil.toClass(baseInfo, UserBean.class);
        }
        return userBean;
    }

    //    0游客,1,注册会员,2,代理,3,总代理
    public static String setVipDate(String groupID) {
        String text = "游客";
        int id = Integer.parseInt(groupID.trim());
        switch (id) {
            case 0:
                text = "游客";
                break;
            case 1:
                text = "注册会员";
                break;
            case 2:
                text = "代理";
                break;
            case 3:
                text = "总代理";
                break;
        }
        return text;
    }

    public static String getCurrentAppVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            String appVersionName = pm.getPackageInfo(context.getPackageName(), 0).versionName;
            return appVersionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "2.0.0";
    }

    public static void sharePic(Context context, String share_text, String url) {
        if (url != null && !url.trim().equals("")) {
//            if(url.contains("channelCode")) {
//                url = url+"&invitecode="+AppConfig.getUserInfo(context).getInvitation_code();
//            }else{
//                url = url+"?invitecode="+AppConfig.getUserInfo(context).getInvitation_code();
//            }

            if (share_text == null) {
                share_text = "各位觀眾";
            }
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_SUBJECT, share_text);
            textIntent.putExtra(Intent.EXTRA_TITLE, share_text);
            textIntent.putExtra(Intent.EXTRA_TEXT, url);
            textIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(textIntent, "分享"));
        }

    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static final String DEF_ROADS = "https://www.8090g.cn/jiexi/?url=";

    public static String getCurrentRoad(int positon) {
        String defRoad = "";
        String roads = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_ALL_ROADS, DEF_ROADS);
        if (roads == null || roads.trim().equals("") || roads.trim().equals("[]") || roads.equals(DEF_ROADS)) {
            defRoad = DEF_ROADS;
            return defRoad;
        }
        List<RoadBean> roadBeans = GsonUtil.toClass(roads, new TypeToken<List<RoadBean>>() {
        }.getType());
        if (roadBeans != null && roadBeans.size() > 0) {
            if (positon < roadBeans.size()) {
                defRoad = roadBeans.get(positon).getUrl();
                LogUtil.e("VideoPlay", "defRoad = " + defRoad);
            }
        } else {
            defRoad = DEF_ROADS;
        }

        return defRoad;
    }

    public static List<RoadBean> getAllRoads(int positon) {
        List<RoadBean> roadBeans = new ArrayList<>();
        String roads = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_ALL_ROADS, DEF_ROADS);
        if (roads == null || roads.trim().equals("") || roads.trim().equals("[]") || roads.equals(DEF_ROADS)) {
            return roadBeans;
        }
        roadBeans = GsonUtil.toClass(roads, new TypeToken<List<RoadBean>>() {
        }.getType());
        if (roadBeans == null) {
            roadBeans = new ArrayList<>();
        }
        if (positon >= 0) {
            if (roadBeans.size() > 0) {
                for (int i = 0; i < roadBeans.size(); i++) {
                    if (i == positon) {
                        roadBeans.get(i).setSelct(true);
                    } else {
                        roadBeans.get(i).setSelct(false);
                    }
                }
            }
        }
//        SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_ALL_ROADS, GsonUtil.toJson(roadBeans));
        return roadBeans;
    }

    public static int getPostion(RoadBean bean) {
        int position = 0;
        if (bean == null) {
            return position;
        }
        List<RoadBean> roadBeans = new ArrayList<>();
        String roads = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_ALL_ROADS, DEF_ROADS);
        if (roads == null || roads.trim().equals("") || roads.trim().equals("[]") || roads.equals(DEF_ROADS)) {
            return position;
        }
        roadBeans = GsonUtil.toClass(roads, new TypeToken<List<RoadBean>>() {
        }.getType());
        if (roadBeans == null) {
            roadBeans = new ArrayList<>();
        }
        for (int i = 0; i < roadBeans.size(); i++) {
            if (roadBeans.get(i).getId().trim().equals(bean.getId().trim())) {
                position = i;
            }
        }
        return position;
    }


    public static void openAdv(AdvBean advBean, Activity context) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).advClick(advBean);
        }
        String url = advBean.getTarget();
        if (advBean != null) {
            switch (advBean.getHandle()) {
                case 1: //外部跳转】
                    if (!url.contains("http")) {
                        url = "http://" + url;
                    }
                    Uri uri1 = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri1);
                    context.startActivity(intent);
                    break;
                case 2: //内部跳转
                    Intent intent1 = new Intent(context, BannerActivity.class);
                    intent1.putExtra(BannerActivity.M_URL, url);
                    context.startActivity(intent1);
                    break;
                case 3: //跳转到影视播放页
                    UserBean userBean1 = XGUtil.getMyUserInfo();
                    if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                        ToastUtil.showToastShort("请先绑定手机号");
                        Intent intent10 = new Intent(context, RegistActivity.class);
                        context.startActivityForResult(intent10, 1);
                    } else {

                        url = url.substring(2);
                        Intent intent3 = new Intent(context, VideoInfoActivity.class);
                        VideoPlayBean videoInfoBean = new VideoPlayBean();
                        videoInfoBean.setId(url);
                        intent3.putExtra(XGConstant.KEY_DATA, videoInfoBean);
                        context.startActivity(intent3);
                    }
                    break;
                case 4: //跳转到福利模块
//                    Intent intent4 = new Intent(context, ShareActivity.class);
////                    context.startActivity(intent4);
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).gotoFl();
                    }
                    break;
                case 5: //跳转到会员中心
                    Intent intent5 = new Intent(context, VipActivity.class);
                    context.startActivity(intent5);
                    break;
                case 6: //跳转代理专区申请
                    UserBean userBean2 = XGUtil.getMyUserInfo();
                    if (userBean2 == null || userBean2.getGroup_id().trim().equals("0")) { //游客
                        ToastUtil.showToastShort("请先绑定手机号");
                        Intent intent10 = new Intent(context, RegistActivity.class);
                        context.startActivityForResult(intent10, 1);
                    } else {
                        Intent intent6 = new Intent(context, AgentActivity.class);
                        context.startActivity(intent6);
                    }
                    break;
                case 7: //跳转分享页面
                    Intent intent7 = new Intent(context, ShareActivity.class);
                    context.startActivity(intent7);
                    break;
                case 8: //外链下载
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
                            .DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    String pre = "Android/data/awater/";

                    String downLoadPath = "";
                    if (context.getExternalCacheDir() != null) {
                        downLoadPath = context.getExternalCacheDir().getAbsolutePath() + File.separator + pre + advBean.getTitle() + ".apk";
                    } else {
                        downLoadPath = context.getCacheDir().getAbsolutePath() + File.separator + pre + advBean.getTitle() + ".apk";
                    }
                    LogUtil.e("downloadAPK", "downloadPath  = " + downLoadPath);

                    File file = new File(downLoadPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    request.setVisibleInDownloadsUi(true);
                    request.setDestinationUri(Uri.fromFile(file));
                    long reference = downloadManager.enqueue(request);
                    break;
            }
        }


    }

    /**
     * 将数据写入excel表格
     */
    public static void writeExcel(Context context, List<CreateCodeBean.UserCodeBean> datas) {
        if (getExternalStoragePath(context) == null) return;
        String excelFilePath = getExternalStoragePath(context) + "/mine.xls";

        File file = new File(excelFilePath);
        LogUtil.e("XGutil", "writeExcel path = " + excelFilePath);
//        if (checkFile(excelFilePath)) {
//            deleteByPath(excelFilePath);//如果文件存在则先删除原有的文件
//        }
//        File file = new File(getExternalStoragePath(context) + "/ExportExcel");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        createFile(file);
//        ExcelUtils.initExcel(excelFilePath, "中文版", colNames);//需要写入权限
//        ExcelUtils.writeObjListToExcel(datas, excelFilePath, context);
        ExcelUtils.writeToExcel(context, file, datas);
    }

    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
            } else {
                //创建目录之后再创建文件
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String createDir(String dirPath) {
        //因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 根据路径生成文件夹
     *
     * @param filePath
     */
    public static void makeDir(File filePath) {
        if (!filePath.getParentFile().exists()) {
            makeDir(filePath.getParentFile());
        }
        filePath.mkdir();
    }


    /**
     * 获取外部存储路径
     *
     * @return
     */
    public static String getExternalStoragePath(Context mContext) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        } else {
            Toast.makeText(mContext, "找不到外部存储路径，读写手机存储权限被禁止，请在权限管理中心手动打开权限", Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
