package com.water.melon.ui.welfare;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunfusheng.GlideImageView;
import com.water.melon.R;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.ui.in.HomeAdapterItemClick;
import com.water.melon.utils.glide.GlideHelper;

import androidx.annotation.NonNull;

public class WelfBtnAdapter  extends BaseQuickAdapter<AdvBean, BaseViewHolder> {
    private HomeAdapterItemClick onItemListener;

    public WelfBtnAdapter() {
        super(R.layout.layout_welf_botton_item, null);
    }

    public void setOnItemMusicListener(HomeAdapterItemClick onItemMusicListener) {
        this.onItemListener = onItemMusicListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final AdvBean item) {
        GlideImageView home_fragment_item_iv = helper.getView(R.id.welf_btn_item_iv);
        RelativeLayout welf_btn_item_btn = helper.getView(R.id.welf_btn_item_btn);
        TextView welf_btn_item_btn_name = helper.getView(R.id.welf_btn_item_btn_name);
        TextView welf_btn_item_btn_dec = helper.getView(R.id.welf_btn_item_btn_dec);

        welf_btn_item_btn_name.setText(item.getTitle());
        welf_btn_item_btn_dec.setText(item.getDesc());
        GlideHelper.showRoundImageNoStroke(home_fragment_item_iv, item.getUrl(), R.mipmap.ic_launcher_round);


        welf_btn_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(item);
            }
        });

    }
}
