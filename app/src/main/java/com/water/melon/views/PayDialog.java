package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.water.melon.R;
import com.water.melon.ui.in.PayDialogClick;
import com.water.melon.ui.in.VipPayItemClick;
import com.water.melon.ui.in.VipPayItemClick2;
import com.water.melon.ui.me.vip.PayDialogAdapter;
import com.water.melon.ui.me.vip.VipBean;
import com.water.melon.ui.me.vip.VipPayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PayDialog extends Dialog {
    private Context mContext;

    private String orderid;
    private String method;

    public PayDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private PayDialogClick click;

    public void setBuyClick(PayDialogClick click) {
        this.click = click;
    }

    List<VipBean.PayMethod> methods;

    public void setData(VipBean vipBean) {
        if (vipBean != null) {
            orderid = vipBean.getOrder_id();
            if (vipBean.getMethod() != null && vipBean.getMethod().size() > 0) {
                methods = vipBean.getMethod();
                payDialogAdapter.setNewData(methods);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(R.layout.layout_pay_dialog);
        initView();

        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

//        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metrics);
//        int dpHig = DensityUtil.dip2px(mContext, 379);
//        if (metrics.heightPixels < dpHig) {
//            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        } else {
//            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, dpHig);
//        }
    }

    private RecyclerView recyclerView;
    private RelativeLayout layout_pay_rl;
    private PayDialogAdapter payDialogAdapter;

    private VipBean.PayMethod selectMethod;

    private void initView() {
        recyclerView = findViewById(R.id.layout_pay_recy);
        layout_pay_rl = findViewById(R.id.layout_pay_rl);
        payDialogAdapter = new PayDialogAdapter();
        payDialogAdapter.setOnItemClick(new VipPayItemClick2() {
            @Override
            public void onItemClick(VipBean.PayMethod item) {
                if (methods != null) {
                    for (int i = 0; i < methods.size(); i++) {
                        if (methods.get(i).getId().trim().equals(item.getId().trim())) {
                            methods.get(i).setSelct(true);
                            selectMethod = methods.get(i);
                        } else {
                            methods.get(i).setSelct(false);
                        }
                    }
                    payDialogAdapter.setNewData(methods);
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(payDialogAdapter);

        layout_pay_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMydismis();
                method = selectMethod.getMethod();
                click.onItemClick(orderid, method);
            }
        });
    }


    private void doMydismis() {
        this.dismiss();
    }
}
