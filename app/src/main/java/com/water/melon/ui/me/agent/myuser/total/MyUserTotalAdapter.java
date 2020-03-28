package com.water.melon.ui.me.agent.myuser.total;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.TotalBean;
import com.water.melon.ui.in.AgentUserItemClick;

import androidx.annotation.NonNull;

public class MyUserTotalAdapter extends BaseQuickAdapter<TotalBean, BaseViewHolder> {
    public MyUserTotalAdapter() {
        super(R.layout.layout_my_user_total_item, null);
    }

    private AgentUserItemClick click;

    public void setOnItemClick(AgentUserItemClick click) {
        this.click = click;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TotalBean item) {
        TextView user_total_date = helper.getView(R.id.user_total_date);
        TextView user_total_user = helper.getView(R.id.user_total_user);
        TextView user_total_new_user = helper.getView(R.id.user_total_new_user);
        TextView user_total_new_alive = helper.getView(R.id.user_total_new_alive);


        user_total_date.setText(item.getDate());
        user_total_user.setText(item.getTotal());
        user_total_new_user.setText(item.getAdd());
        user_total_new_alive.setText(item.getLive());


    }
}