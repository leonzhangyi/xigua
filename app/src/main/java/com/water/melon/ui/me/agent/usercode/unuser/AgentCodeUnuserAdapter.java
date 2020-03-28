package com.water.melon.ui.me.agent.usercode.unuser;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.utils.XGUtil;

import androidx.annotation.NonNull;

public class AgentCodeUnuserAdapter extends BaseQuickAdapter<AgentCodeHisBean.CodeBean, BaseViewHolder> {
    public AgentCodeUnuserAdapter() {
        super(R.layout.layout_agent_code_unuser_item, null);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentCodeHisBean.CodeBean item) {
        TextView agent_code_unuser_code = helper.getView(R.id.agent_code_unuser_code);
        TextView agent_code_unuser_type = helper.getView(R.id.agent_code_unuser_type);
        TextView agent_code_unuser_xiao = helper.getView(R.id.agent_code_unuser_xiao);
        TextView agent_code_unuser_handler = helper.getView(R.id.agent_code_unuser_handler);

        agent_code_unuser_code.setText(item.getCode());
        agent_code_unuser_type.setText(item.getType());
        agent_code_unuser_xiao.setText(item.getEnd_time().split(" ")[0]);

        agent_code_unuser_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XGUtil.copyText(mContext, item.getCode());
            }
        });

    }
}
