package com.water.melon.ui.home;

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

public class MainHomeAdapter extends BaseQuickAdapter<AdvBean, BaseViewHolder> {
    private HomeAdapterItemClick onItemListener;

    public MainHomeAdapter() {
        super(R.layout.layout_home_fragment_item, null);
    }

    public void setOnItemMusicListener(HomeAdapterItemClick onItemMusicListener) {
        this.onItemListener = onItemMusicListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final AdvBean item) {
        GlideImageView home_fragment_item_iv = helper.getView(R.id.home_fragment_item_iv);
        TextView home_fragment_item_tv = helper.getView(R.id.home_fragment_item_tv);
        RelativeLayout layout_home_item_line = helper.getView(R.id.layout_home_item_line);
        home_fragment_item_tv.setText(item.getTitle());
        GlideHelper.showRoundImageNoStroke(home_fragment_item_iv, item.getUrl(), R.mipmap.home_def);

        if (getData().indexOf(item) % 4 == 3) {
            layout_home_item_line.setVisibility(View.GONE);
        } else {
            layout_home_item_line.setVisibility(View.VISIBLE);
        }

        home_fragment_item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(item);
            }
        });

    }
}
