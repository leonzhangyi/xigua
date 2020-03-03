package com.water.melon.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.constant.XGConstant;
import com.water.melon.utils.ClickTooQucik;

import org.lzh.framework.updatepluginlib.model.Update;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UpDialog extends Dialog implements View.OnClickListener {
    TextView upAppContent;
    TextView upAppIgone;
    Button upAppCancle;
    Button upAppOk;


    private String content;
    private DialogListen dialogListen;
    private boolean isForcs;//是否强制升级

    public UpDialog(@NonNull Context context) {
        this(context, 0);
    }

    public UpDialog(@NonNull Context context, int themeResId) {
        this(context, false, null);
    }

    protected UpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setDialogListen(DialogListen dialogListen) {
        this.dialogListen = dialogListen;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    public interface DialogListen {
        void btn_cancle(Dialog dialog);

        void btn_igone(Dialog dialog);

        void btn_ok(Dialog dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_up_app);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setWindowAnimations(R.style.DialogBottomAnimation);
        dialogWindow.setGravity(Gravity.CENTER);
//        ViewGroup.LayoutParams params = findViewById(R.id.dialog_up_img_lay).getLayoutParams();
        lp.width = (int) (XGConstant.Screen_Width * 0.85); // 宽度
//        lp.height = params.height; // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);
        initView();
    }

    private void initView() {
        upAppContent = findViewById(R.id.up_app_content);
        upAppIgone = findViewById(R.id.up_app_igone);
        upAppCancle = findViewById(R.id.up_app_cancle);
        upAppOk = findViewById(R.id.up_app_ok);
        upAppIgone.setOnClickListener(this);
        upAppCancle.setOnClickListener(this);
        upAppOk.setOnClickListener(this);

        upAppContent.setText(content);

        if (isForcs) {
            //强制更新
            upAppCancle.setVisibility(View.GONE);
            setCanceledOnTouchOutside(false);
        }
    }


    public void setdata(Update update) {
        StringBuilder sbContent = new StringBuilder()
                .append(MyApplication.getStringByResId(R.string.up_app_version))
                .append(update.getVersionName())
                .append("\n")
//                .append(MyApplication.getStringByResId(R.string.up_app_size))
//                .append(update.getAppSize())
                .append("\n\n")
                .append(MyApplication.getStringByResId(R.string.up_app_content))
                .append("\n")
                .append(update.getUpdateContent());
        content = sbContent.toString();
        isForcs=update.isForced();
    }

    @Override
    public void onClick(View v) {
        if (ClickTooQucik.isFastClick() || null == dialogListen) {
            return;
        }
        switch (v.getId()) {
            case R.id.up_app_cancle:
                dialogListen.btn_cancle(this);
                break;
            case R.id.up_app_ok:
                dialogListen.btn_ok(this);
                break;
            case R.id.up_app_igone:
                dialogListen.btn_igone(this);
                break;
        }
    }

}
