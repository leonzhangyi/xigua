package com.water.melon.ui.me.agent.applyhistory;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.AgentBean;

import androidx.annotation.NonNull;

public class AgentApplyHistoryAdapter extends BaseQuickAdapter<AgentBean, BaseViewHolder> {

    public AgentApplyHistoryAdapter() {
        super(R.layout.layout_agent_apply_history_adapter, null);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentBean item) {
        TextView agent_apply_time = helper.getView(R.id.agent_apply_time);
        TextView agent_apply_price = helper.getView(R.id.agent_apply_price);
        TextView agent_apply_state = helper.getView(R.id.agent_apply_state);

        agent_apply_time.setText(item.getCreated_time().split(" ")[0]);
        agent_apply_price.setText(item.getMoney());
        int status = Integer.parseInt(item.getStatus().trim());
        String state = "";
        if (status == 0) {
            state = "待审核";
        } else if (status == 1) {
            state = "已通过";
        } else if (status == 2) {
            state = "已拒绝";
        }
        agent_apply_state.setText(state);  //0待审核,1已审核,2已拒绝
    }


}
