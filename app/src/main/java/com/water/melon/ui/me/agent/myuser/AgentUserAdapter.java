package com.water.melon.ui.me.agent.myuser;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.ui.in.AgentUserItemClick;
import com.water.melon.utils.XGUtil;

import androidx.annotation.NonNull;

public class AgentUserAdapter extends BaseQuickAdapter<AgentUserBean.UserInfo, BaseViewHolder> {
    public AgentUserAdapter() {
        super(R.layout.layout_agent_user_item, null);
    }

    private AgentUserItemClick click;

    public void setOnItemClick(AgentUserItemClick click) {
        this.click = click;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AgentUserBean.UserInfo item) {
        TextView agent_user_name = helper.getView(R.id.agent_user_name);
        TextView agent_user_type = helper.getView(R.id.agent_user_type);
        TextView agent_user_start_time = helper.getView(R.id.agent_user_start_time);
        TextView agent_user_end_time = helper.getView(R.id.agent_user_end_time);
        TextView agent_user_handler = helper.getView(R.id.agent_user_handler);
        String name = item.getMobile();
        if (name == null || name.trim().equals("")) {
            name = item.getNickname();
        }

        agent_user_name.setText(name);
        agent_user_type.setText(item.getVip());
        agent_user_start_time.setText(item.getCreated_date().split(" ")[1]);
        agent_user_end_time.setText(item.getEnd_date().split(" ")[1]);


        agent_user_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemClick(item);
            }
        });
    }
}
