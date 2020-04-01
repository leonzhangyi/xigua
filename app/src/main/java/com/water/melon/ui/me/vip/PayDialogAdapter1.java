package com.water.melon.ui.me.vip;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunfusheng.GlideImageView;
import com.water.melon.R;
import com.water.melon.ui.in.VipPayItemClick2;
import com.water.melon.utils.glide.GlideHelper;

import androidx.annotation.NonNull;

public class PayDialogAdapter1 extends BaseQuickAdapter<VipBean.PayMethod, BaseViewHolder> {
    public PayDialogAdapter1() {
        super(R.layout.layout_pay_dilaog_item1, null);
    }

    private VipPayItemClick2 itemClick;

    public void setOnItemClick(VipPayItemClick2 itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VipBean.PayMethod item) {
//        ImageView pay_dialog_check = helper.getView(R.id.pay_dialog_check);
        GlideImageView pay_dialog_icon = helper.getView(R.id.pay_dialog_icon);
        TextView pay_dialog_tv = helper.getView(R.id.pay_dialog_tv);

//        pay_dialog_check.setSelected(item.isSelct());

        pay_dialog_tv.setText(item.getTitle());
        GlideHelper.showRoundImageNoStroke(pay_dialog_icon, item.getIcon(), R.mipmap.weixin);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pay_dialog_check.setSelected(!pay_dialog_check.isSelected());
                itemClick.onItemClick(item);
            }

        });

    }
}
