package com.water.melon.ui.me.agent.usercode.use;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.utils.XGUtil;

import androidx.annotation.NonNull;

public class CodeUserAdapter extends BaseQuickAdapter<AgentCodeHisBean.CodeBean, BaseViewHolder> {

    public CodeUserAdapter() {
        super(R.layout.layout_code_user_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentCodeHisBean.CodeBean item) {
        TextView layout_code_user_code = helper.getView(R.id.layout_code_user_code);
        TextView layout_code_user_type = helper.getView(R.id.layout_code_user_type);
        TextView layout_code_user_time = helper.getView(R.id.layout_code_user_time);
        TextView layout_code_user_user = helper.getView(R.id.layout_code_user_user);
        TextView layout_code_user_handler = helper.getView(R.id.layout_code_user_handler);

        layout_code_user_code.setText(item.getCode());
        layout_code_user_type.setText(item.getType());
        layout_code_user_time.setText(item.getDate());
        layout_code_user_user.setText(item.getMobile());
//        layout_code_user_handler.setText(item.getType());
        layout_code_user_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XGUtil.copyText(mContext,item.getCode());
            }
        });


//        create_code_item_time.setText(item.getCreated_time().split(" ")[0].replaceAll("-", "/") + "-" + item.getEnd_time().split(" ")[0].replaceAll("-", "/"));
//        create_code_item_copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                XGUtil.copyText(MyApplication.getContext(), item.getCode());
//            }
//        });
    }
}
