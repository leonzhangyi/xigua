package com.water.melon.ui.me.history;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunfusheng.GlideImageView;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequest;
import com.water.melon.R;
import com.water.melon.net.bean.VideoHistoryBean;
import com.water.melon.ui.in.VideoHistoryItemClick;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.glide.GlideHelper;

import androidx.annotation.NonNull;

public class VideoHistoryItemAdapter extends BaseQuickAdapter<VideoHistoryBean, BaseViewHolder> {

    public VideoHistoryItemAdapter() {
        super(R.layout.item_history_video, null);
    }

    private VideoHistoryItemClick itemClick;

    public void setOnItemClick(VideoHistoryItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoHistoryBean item) {
        LinearLayout item_history_layout = helper.getView(R.id.item_history_layout);

        GlideImageView item_history_image = helper.getView(R.id.item_history_image);

        TextView item_history_name = helper.getView(R.id.item_history_name);
        TextView item_history_play_position = helper.getView(R.id.item_history_play_position);
//        TextView create_code_item_copy = helper.getView(R.id.create_code_item_copy);

        item_history_name.setText(item.getTitle());
        item_history_play_position.setText("来源" + item.getProvide());
//        create_code_item_time.setText(item.getCreated_time().split(" ")[0].replaceAll("-", "/") + "-" + item.getEnd_time().split(" ")[0].replaceAll("-", "/"));

        String image = item.getUrl();

//        GlideRequest glideRequest = GlideApp.with(item_history_image.getContext())
//                .load(image);
//
//        if (TextUtils.isEmpty(image)) {
//            glideRequest.placeholder(R.mipmap.bg_local_video_default);
//        } else {
//            glideRequest.placeholder(R.mipmap.bg_video_plact_horizontal);
//        }
//        glideRequest.override(300, 200);
//        glideRequest.into(item_history_image);
        LogUtil.e("HistoryVIP","item.getUrl() = "+item.getUrl());
        GlideHelper.showRoundImageNoStroke(item_history_image, item.getPoster(), R.mipmap.home_def);

        item_history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                XGUtil.copyText(MyApplication.getContext(), item.getCode());
                itemClick.onItemClick(item);
            }
        });
    }
}