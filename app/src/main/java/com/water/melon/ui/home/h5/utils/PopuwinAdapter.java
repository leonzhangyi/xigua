package com.water.melon.ui.home.h5.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.net.bean.RoadBean;
import com.water.melon.ui.in.RoadsItemClick;

import androidx.annotation.NonNull;

public class PopuwinAdapter extends BaseQuickAdapter<RoadBean, BaseViewHolder> {

    private RoadsItemClick onItemListener;

    public PopuwinAdapter() {
        super(R.layout.layout_popuwin_item, null);
    }

    public void setOnItemMusicListener(RoadsItemClick onItemMusicListener) {
        this.onItemListener = onItemMusicListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final RoadBean item) {
        ImageView popuwin_item_iv = helper.getView(R.id.popuwin_item_iv);
        TextView popuwin_item_tv = helper.getView(R.id.popuwin_item_tv);
        if (item.isSelct()) {
            popuwin_item_iv.setVisibility(View.VISIBLE);
            popuwin_item_tv.setTextColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
        } else {
            popuwin_item_iv.setVisibility(View.INVISIBLE);
            popuwin_item_tv.setTextColor(MyApplication.getColorByResId(R.color.white));
        }


        popuwin_item_tv.setText(item.getName());

        helper.getView(R.id.popuwin_item_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(item);
            }
        });
    }
}
