package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.ui.in.AdapterItemClick;
import com.water.melon.ui.me.agent.myuser.AgentUserActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddVipDialog extends Dialog {
    private Context mContext;

    public AddVipDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public AddVipDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AddVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private RelativeLayout layout_agent_create_code_code_rl;
    public TextView layout_agent_create_code_code_tv;
    public EditText layout_add_vip_et;

    private AdapterItemClick adapterItemClick1;

    public void setOnChooesClick(AdapterItemClick adapterItemClick1) {
        this.adapterItemClick1 = adapterItemClick1;
    }

    private AdapterItemClick adapterItemClick2;

    public void setOnChooesClick2(AdapterItemClick adapterItemClick2) {
        this.adapterItemClick2 = adapterItemClick2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setContentView(R.layout.layout_view_add_vip);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        layout_agent_create_code_code_rl = findViewById(R.id.layout_agent_create_code_code_rl);
        layout_agent_create_code_code_tv = findViewById(R.id.layout_agent_create_code_code_tv);
        layout_add_vip_et = findViewById(R.id.layout_add_vip_et);

        layout_agent_create_code_code_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClick1.onItemClick(0);
            }
        });

        findViewById(R.id.layout_agent_create_code_sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                adapterItemClick2.onItemClick(0);
            }
        });
    }

}
