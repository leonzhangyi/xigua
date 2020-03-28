package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.water.melon.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AboutDialog extends Dialog {

    private Context mContext;

    public AboutDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public AboutDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AboutDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setContentView(R.layout.layout_view_about);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        findViewById(R.id.me_about_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
