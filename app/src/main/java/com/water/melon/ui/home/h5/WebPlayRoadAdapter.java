package com.water.melon.ui.home.h5;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.net.bean.RoadBean;
import com.water.melon.ui.in.RoadsItemClick;

import androidx.annotation.NonNull;

public class WebPlayRoadAdapter extends BaseQuickAdapter<RoadBean, BaseViewHolder> {

    private RoadsItemClick onItemListener;

    public WebPlayRoadAdapter() {
        super(R.layout.layout_web_play_road_item, null);
    }

    public void setOnItemMusicListener(RoadsItemClick onItemMusicListener) {
        this.onItemListener = onItemMusicListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final RoadBean item) {
        RelativeLayout web_play_road_item_rl = helper.getView(R.id.web_play_road_item_rl);
        TextView web_play_road_item_tv = helper.getView(R.id.web_play_road_item_tv);
        if (item.isSelct()) {
            web_play_road_item_tv.setTextColor(MyApplication.getColorByResId(R.color.white));
            web_play_road_item_rl.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_web_play_item_sel));
        } else {
            web_play_road_item_tv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
            web_play_road_item_rl.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_web_play_item));
        }


        web_play_road_item_tv.setText(item.getName());

        web_play_road_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(item);
            }
        });
    }
}
