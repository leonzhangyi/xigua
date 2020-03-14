package com.water.melon.ui.me.agent.myagent;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.ui.in.AgentItemClick;

import androidx.annotation.NonNull;

public class MyAgentDialog extends BaseQuickAdapter<MyAgentBean, BaseViewHolder> {

    public MyAgentDialog() {
        super(R.layout.layout_agent_my_agent_item, null);
    }

    private AgentItemClick agentItemClick;

    public void setAgentItemClick(AgentItemClick agentItemClick) {
        this.agentItemClick = agentItemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyAgentBean item) {
        TextView my_agent_name = helper.getView(R.id.my_agent_name);
        TextView my_agent_phone = helper.getView(R.id.my_agent_phone);
        TextView my_agent_time = helper.getView(R.id.my_agent_time);
        TextView my_agent_num = helper.getView(R.id.my_agent_num);
        TextView my_agent_handle = helper.getView(R.id.my_agent_handle);


        my_agent_name.setText(item.getProxy_name());
        my_agent_phone.setText(item.getTel());
        my_agent_time.setText(item.getCreated_time().split(" ")[1]);
        my_agent_num.setText(item.getCount());


        my_agent_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agentItemClick.itemClick(item);
            }
        });
    }
}
