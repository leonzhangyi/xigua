package com.water.melon.utils.ui.recyler_view_item;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.application.MyApplication;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 列表加载更多布局
 * 创建者： feifan.pi 在 2017/7/14.
 */

public class ListBottomViewHolder extends RecyclerView.ViewHolder {
    private TextView msgTv;
    private ImageView loadingPB;
    private ProgressBar item_list_no_more_progress_bar_1;

    public ListBottomViewHolder(View itemView) {
        super(itemView);
        msgTv = (TextView) itemView.findViewById(R.id.item_list_no_more_tv);
        loadingPB = (ImageView) itemView.findViewById(R.id.item_list_no_more_progress_bar);
        item_list_no_more_progress_bar_1 = itemView.findViewById(R.id.item_list_no_more_progress_bar_1);
    }

    public void setMsg(String msg) {
        msgTv.setText(msg);
    }


    public void setNoMoreData() {
        item_list_no_more_progress_bar_1.setVisibility(View.GONE);
    }

    public void setMsgBg(int resId) {
        msgTv.setBackgroundResource(resId);
        msgTv.setPadding(35, 10, 35, 10);
    }

    public void setMsgColor(int colorId) {
        msgTv.setTextColor(MyApplication.getColorByResId(colorId));
    }

    public void setloadingPBVisible(boolean visible) {
        if (visible) {
            loadingPB.setVisibility(View.VISIBLE);
        } else {
            loadingPB.setVisibility(View.GONE);
        }
    }

    public int getLoadingPBVisible() {
        return loadingPB.getVisibility();
    }
}
