package com.water.melon.utils.update;

import android.app.Activity;
import android.app.Dialog;

import org.lzh.framework.updatepluginlib.base.CheckNotifier;
import org.lzh.framework.updatepluginlib.util.SafeDialogHandle;

public class MyUpdateNotifier extends CheckNotifier {
    @Override
    public Dialog create(Activity activity) {
        UpDialog upDialog = new UpDialog(activity);
        upDialog.setdata(update);
        upDialog.setDialogListen(new UpDialog.DialogListen() {
            @Override
            public void btn_cancle(Dialog dialog) {
                //取消
                sendUserCancel();
                SafeDialogHandle.safeDismissDialog((dialog));
            }

            @Override
            public void btn_igone(Dialog dialog) {
                //忽略此版本
                sendUserIgnore();
                SafeDialogHandle.safeDismissDialog(dialog);
            }

            @Override
            public void btn_ok(Dialog dialog) {
                //立即更新
                sendDownloadRequest();
                SafeDialogHandle.safeDismissDialog(dialog);
            }
        });
        return upDialog;
    }
}
