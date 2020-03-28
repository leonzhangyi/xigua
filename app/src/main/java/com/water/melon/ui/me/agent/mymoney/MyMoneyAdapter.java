package com.water.melon.ui.me.agent.mymoney;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.MyMoneyBean;
import com.water.melon.net.bean.TotalBean;
import com.water.melon.ui.in.AgentUserItemClick;

import androidx.annotation.NonNull;

public class MyMoneyAdapter extends BaseQuickAdapter<MyMoneyBean.UserMoney, BaseViewHolder> {
    public MyMoneyAdapter() {
        super(R.layout.layout_my_money_item, null);
    }

    private AgentUserItemClick click;

    public void setOnItemClick(AgentUserItemClick click) {
        this.click = click;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyMoneyBean.UserMoney item) {
        TextView my_money_item_name = helper.getView(R.id.my_money_item_name);
        TextView my_money_item_phone = helper.getView(R.id.my_money_item_phone);
        TextView my_money_item_data = helper.getView(R.id.my_money_item_data);
        TextView my_money_item_price = helper.getView(R.id.my_money_item_price);
        TextView my_money_item_money = helper.getView(R.id.my_money_item_money);


        my_money_item_name.setText(item.getUsername());
        my_money_item_phone.setText(item.getMobile());
        my_money_item_data.setText(item.getDate());
        my_money_item_price.setText(item.getMoney());
        my_money_item_money.setText(item.getProfit());


    }
}