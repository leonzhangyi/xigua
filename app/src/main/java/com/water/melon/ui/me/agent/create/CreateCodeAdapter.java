package com.water.melon.ui.me.agent.create;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.utils.XGUtil;

import androidx.annotation.NonNull;

public class CreateCodeAdapter extends BaseQuickAdapter<CreateCodeBean.UserCodeBean, BaseViewHolder> {

    public CreateCodeAdapter() {
        super(R.layout.layout_create_code_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CreateCodeBean.UserCodeBean item) {
        TextView create_code_item_code = helper.getView(R.id.create_code_item_code);
        TextView create_code_item_type = helper.getView(R.id.create_code_item_type);
        TextView create_code_item_time = helper.getView(R.id.create_code_item_time);
        TextView create_code_item_copy = helper.getView(R.id.create_code_item_copy);

        create_code_item_code.setText(item.getCode());
        create_code_item_type.setText(item.getType());
        create_code_item_time.setText(item.getCreated_time().split(" ")[0].replaceAll("-", "/") + "-" + item.getEnd_time().split(" ")[0].replaceAll("-", "/"));
        create_code_item_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XGUtil.copyText(MyApplication.getContext(), item.getCode());
            }
        });
    }
}
