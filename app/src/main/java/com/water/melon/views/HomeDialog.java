package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunfusheng.util.Utils;
import com.water.melon.R;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.ui.me.share.ShareActivity;
import com.water.melon.utils.ZXingUtils;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.TaskExecutor;

public class HomeDialog extends Dialog {

    private Context mContext;

    public HomeDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public HomeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    protected HomeDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private ImageView layout_home_image;
    private TextView layout_home_tv1;

    public void setDate(AdvBean bean) {
        layout_home_tv1.setText(bean.getMessage());
        Bitmap bitmap = ZXingUtils.createQRCodeWithLogo(bean.getTarget(), Utils.dp2px(mContext, 150),
                BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.def_icon));
        layout_home_image.setImageBitmap(bitmap);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.layout_home_dialog);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        layout_home_tv1 = findViewById(R.id.layout_home_tv1);
        layout_home_image = findViewById(R.id.layout_home_image);


        findViewById(R.id.me_about_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.layout_home_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ShareActivity.class));
            }
        });
    }
}
