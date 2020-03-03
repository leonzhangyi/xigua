package com.water.melon.utils.update;

import android.app.Activity;


import com.water.melon.views.MessageButtonDialog;

import org.lzh.framework.updatepluginlib.base.DownloadCallback;
import org.lzh.framework.updatepluginlib.base.DownloadNotifier;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.util.ActivityManager;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

import java.io.File;

public class MyDownloadNotifier extends DownloadNotifier {
    @Override
    public DownloadCallback create(Update update, Activity activity) {
        final UpProgressDialog dialog = new UpProgressDialog(activity);
        if (update.isForced()) {
            //强制更新
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
        SafeDialogHandle.safeShowDialog(dialog);
        return new DownloadCallback() {
            @Override
            public void onDownloadStart() {
            }

            @Override
            public void onDownloadComplete(File file) {
                SafeDialogHandle.safeDismissDialog(dialog);
            }

            @Override
            public void onDownloadProgress(long current, long total) {
                int percent = (int) (current * 1.0f / total * 100);
                dialog.setProgress(percent);
            }

            @Override
            public void onDownloadError(Throwable t) {
                SafeDialogHandle.safeDismissDialog(dialog);
                createRestartDialog();
            }
        };
    }

    private void createRestartDialog() {
        new MessageButtonDialog(ActivityManager.get().topActivity(), "下载apk失败"
                , "是否重新下载？", false, new MessageButtonDialog.MyDialogOnClick() {
            @Override
            public void btnOk(MessageButtonDialog dialog) {
                restartDownload();
            }

            @Override
            public void btnNo(MessageButtonDialog dialog) {
                if (update.isForced()) {
                    ActivityManager.get().exit();
                } else {
                    dialog.dismiss();
                }
            }
        }).show();
    }
}
