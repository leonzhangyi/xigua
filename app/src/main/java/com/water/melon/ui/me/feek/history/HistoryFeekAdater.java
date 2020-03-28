package com.water.melon.ui.me.feek.history;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.FeedBean;
import com.water.melon.ui.in.FeekHistoryItemClick;
import com.water.melon.ui.in.VideoHistoryItemClick;

import androidx.annotation.NonNull;

public class HistoryFeekAdater extends BaseQuickAdapter<FeedBean.FeekItemBean, BaseViewHolder> {

    public HistoryFeekAdater() {
        super(R.layout.item_feek_history, null);
    }

    private FeekHistoryItemClick itemClick;

    public void setOnItemClick(FeekHistoryItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FeedBean.FeekItemBean item) {
        LinearLayout item_feek_history_ll = helper.getView(R.id.item_feek_history_ll);
        TextView item_feek_history_time = helper.getView(R.id.item_feek_history_time);

        TextView item_feek_history_content = helper.getView(R.id.item_feek_history_content);
        TextView item_feek_history_replay = helper.getView(R.id.item_feek_history_replay);

        item_feek_history_time.setText(item.getCreated_time().split(" ")[0]);
        item_feek_history_content.setText(item.getContent());
        String answer = item.getAnswer().trim();
        if (answer != null && !answer.equals("")) {
            item_feek_history_replay.setText("已回复");
        } else {
            item_feek_history_replay.setText("未回复");
        }


        item_feek_history_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                XGUtil.copyText(MyApplication.getContext(), item.getCode());
                itemClick.onItemClick(item);
            }
        });
    }
}
