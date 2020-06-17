package com.water.melon.application;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.fm.openinstall.OpenInstall;
import com.tencent.bugly.crashreport.CrashReport;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.SystemInfo;
import com.water.melon.net.utils.MobileUtil;
import com.p2p.P2PClass;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.ScreenUtils;

import java.util.Vector;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

public class MyApplication extends Application {

    private static Context context;
    private static SystemInfo systemInfo;

    public static int downLoadingMax = 5;//TODO 同时下载最大数
    public static boolean isOpenCleanDia = false;//是否打开清理磁盘的对话框
    public static boolean isPlay = false;

    public static volatile P2PClass p;
    public static Vector<LocalVideoInfo> downTaskPositionList = new Vector<>();//后台下载任务的位置
    public static boolean isLoad = false;//是否刷新下载管理页面
    public static int tid = 0;//当前正在播放电影的tid
    public static boolean flag = false;//手机内存可用空间更新

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CrashReport.initCrashReport(getApplicationContext(), "8259b58347", false); //初始化bugly
        if (isMainProcess()) { //初始化 openinstall
            OpenInstall.init(getApplicationContext());
        }
        //获取手机屏幕大小
        XGConstant.Screen_Width = ScreenUtils.getScreenWidth(context);
        XGConstant.Screen_Height = ScreenUtils.getScreenHeight(context);
        XGConstant.Screen_Status_Height = ScreenUtils.getStatusHeight(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static SystemInfo getSystemInfo() {
        if (systemInfo == null) {
            systemInfo = MobileUtil.getSystemInfo(context);
        }
        return systemInfo;
    }

    public static Context getContext() {
        return context;
    }

    public static P2PClass getp2p() {
        if (p == null) {
            p = new P2PClass();
        }
        return p;
    }


    /**
     * 获得text文字
     *
     * @param strId
     * @return
     */
    public static String getStringByResId(int strId) {
        return MyApplication.getContext().getResources().getString(strId);
    }


    /**
     * 获得颜色值
     *
     * @param colorId
     * @return
     */
    public static int getColorByResId(int colorId) {
        return ContextCompat.getColor(MyApplication.getContext(), colorId);
    }

    public static Drawable getDrawableByResId(int drawableId) {
        return ContextCompat.getDrawable(MyApplication.getContext(), drawableId);
    }
    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        android.app.ActivityManager activityManager = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }
}
