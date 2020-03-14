package com.water.melon.ui.me.agent.myagent.setagent;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.ui.in.AgentUserItemClick;
import com.water.melon.utils.XGUtil;

import androidx.annotation.NonNull;

public class SetAgentAdapter extends BaseQuickAdapter<AgentUserBean.UserInfo, BaseViewHolder> {

    public SetAgentAdapter() {
        super(R.layout.layout_agent_set_agent, null);
    }

    private AgentUserItemClick itemClick;

    public void setItemClick(AgentUserItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentUserBean.UserInfo item) {
        TextView my_agent_name = helper.getView(R.id.my_agent_name);
        TextView my_agent_phone = helper.getView(R.id.my_agent_phone);
        TextView my_agent_time = helper.getView(R.id.my_agent_time);
        TextView my_agent_handle = helper.getView(R.id.my_agent_handle);


        my_agent_name.setText(item.getUsername());
        my_agent_phone.setText(item.getMobile());
        my_agent_time.setText(XGUtil.setVipDate(item.getGroup_id()));

        if (Integer.parseInt(item.getGroup_id().trim()) > 1) {
            my_agent_handle.setText("/");
            my_agent_handle.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
        }

        my_agent_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(item.getGroup_id().trim()) > 1) {

                } else {
                    itemClick.itemClick(item);
                }

            }
        });
    }
}
