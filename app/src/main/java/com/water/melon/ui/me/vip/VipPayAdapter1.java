package com.water.melon.ui.me.vip;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.ui.in.VipPayItemClick;

import androidx.annotation.NonNull;

public class VipPayAdapter1 extends BaseQuickAdapter<VipBean, BaseViewHolder> {
    private VipPayItemClick itemClick;

    public VipPayAdapter1() {
        super(R.layout.layout_vip_item1, null);
    }

    public void setItemClick(VipPayItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final VipBean item) {
        TextView name = helper.getView(R.id.layout_vip_item_nanme);
        TextView time = helper.getView(R.id.layout_vip_item_time);
        TextView price = helper.getView(R.id.layout_vip_item_price);
        RelativeLayout pay = helper.getView(R.id.layout_vip_item_pay);
        name.setText(item.getTitle());
        time.setText("有效期" + item.getExpiry() + "天");
        price.setText("¥" + item.getPrice());


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(item);
            }
        });

    }
}

