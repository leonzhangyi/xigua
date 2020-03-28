package com.water.melon.ui.me.agent.mymoney.history;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.MyMoneyBean;

import androidx.annotation.NonNull;

public class MyMoneyHistoryAdapter extends BaseQuickAdapter<MyMoneyBean.MyMoneyHistory, BaseViewHolder> {
    public MyMoneyHistoryAdapter() {
        super(R.layout.layout_my_money_history_item, null);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyMoneyBean.MyMoneyHistory item) {
        TextView layout_my_money_history_item_data = helper.getView(R.id.layout_my_money_history_item_data);
        TextView layout_my_money_history_item_money = helper.getView(R.id.layout_my_money_history_item_money);
        TextView layout_my_money_history_item_status = helper.getView(R.id.layout_my_money_history_item_status);


        layout_my_money_history_item_data.setText(item.getData());
        layout_my_money_history_item_money.setText(item.getMoney());
        layout_my_money_history_item_status.setText(item.getMsg());


    }
}