package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.water.melon.R;
import com.water.melon.ui.in.AdapterItemClick;
import com.water.melon.ui.me.vip.PayDialogAdapter;
import com.water.melon.ui.me.vip.VipBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SaveAgentDialog extends Dialog {
    private Context mContext;

    public SaveAgentDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public SaveAgentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SaveAgentDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.layout_save_agent_dialog);
        initView();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private AdapterItemClick itemClick;
    public void setOnClickListener(AdapterItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private RelativeLayout layout_save_sure;
    private RelativeLayout layout_save_cancel;

    private void initView() {
        layout_save_sure = findViewById(R.id.layout_save_sure);
        layout_save_cancel = findViewById(R.id.layout_save_cancel);

        layout_save_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMydismis();
                itemClick.onItemClick(0);

            }
        });

        layout_save_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMydismis();
            }
        });
    }

    private void doMydismis() {
        this.dismiss();
    }
}
