package com.water.melon.utils.update;

import android.text.TextUtils;


import com.water.melon.constant.XGConstant;
import com.water.melon.utils.SharedPreferencesUtil;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.base.CheckCallback;
import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;
import java.util.Calendar;

public class CheckAppVersionUtil {
    public interface CheckAppVersionListen {
        void onCheckStart();

        void hasUpdate(boolean has, String msg);
    }

    /**
     * 检查版本更新
     *
     * @param mustShowDialog 强制弹出更新弹窗
     */
    public static void checkApp(boolean mustShowDialog) {
        checkApp(mustShowDialog, null);
    }

    /**
     * 检查版本更新
     *
     * @param mustShowDialog 强制弹出更新弹窗
     */
    public static void checkApp(boolean mustShowDialog, CheckAppVersionListen versionListen) {
        //今天是否提醒
        String todayRemidJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_CHECK_APP_VERSION, "");
        boolean isFoces = SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KEY_CHECK_APP_VERSION_FORCS, false);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;   //月份从0开始
        int day = c.get(Calendar.DAY_OF_MONTH);
        String today = "" + year + month + day;
        if (TextUtils.isEmpty(todayRemidJson) || !today.equals(todayRemidJson) || mustShowDialog || isFoces) {
            //一天提醒一次
            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_CHECK_APP_VERSION, today);
            UpdateBuilder.create().setUpdateStrategy(new AllDialogShowStrategy(true)).setCheckCallback(new CheckCallback() {
                @Override
                public void onCheckStart() {
                    if (null != versionListen) {
                        versionListen.onCheckStart();
                    }
                }

                public void hasUpdate(Update update) {
//                    LogUtil.e("aaaa", "有新的版本");
                    XGConstant.HasNewVersion = true;
                    //记录是否有强制
                    SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KEY_CHECK_APP_VERSION_FORCS, update.isForced());
                    if (null != versionListen) {
                        versionListen.hasUpdate(true, "");
                    }
                }

                @Override
                public void noUpdate() {
                    XGConstant.HasNewVersion = false;
                    if (null != versionListen) {
                        versionListen.hasUpdate(false, "已经是最新版本");
                    }
                }

                @Override
                public void onCheckError(Throwable t) {
                    if (null != versionListen) {
                        versionListen.hasUpdate(false, t.getMessage());
                    }
                }

                @Override
                public void onUserCancel() {
                    if (null != versionListen) {
                        versionListen.hasUpdate(false, "");
                    }
                }

                @Override
                public void onCheckIgnore(Update update) {
                    if (null != versionListen) {
                        versionListen.hasUpdate(false, "");
                    }
                }
            }).setDownloadCallback(new DownloadCallback() {
                @Override
                public void onDownloadStart() {
                }

                @Override
                public void onDownloadComplete(File file) {
//                    LogUtil.e("aaaa", file.getAbsolutePath());
                }

                @Override
                public void onDownloadProgress(long current, long total) {
//                    LogUtil.e("aaaa", current + "==speed==" + total);
                }

                @Override
                public void onDownloadError(Throwable t) {
//                    LogUtil.e("aaaa", t.getMessage());
                }
            }).check();// 启动更新任务.setCheckCallback(new CheckCallback() {
        }
    }
}
