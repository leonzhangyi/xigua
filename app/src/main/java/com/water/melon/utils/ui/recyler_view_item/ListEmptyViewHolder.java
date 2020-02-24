package com.water.melon.utils.ui.recyler_view_item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.melon.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * 创建者： feifan.pi 在 2017/7/14.
 */

public class ListEmptyViewHolder extends RecyclerView.ViewHolder {
    private ImageView empty_img;
    private TextView empty_msg;

    public ListEmptyViewHolder(View itemView) {
        super(itemView);
        empty_img = (ImageView) itemView.findViewById(R.id.item_empty_img);
        empty_msg = (TextView) itemView.findViewById(R.id.item_empty_tv);
    }

    public void setEmpty_img(int emptyImgId) {
        this.empty_img.setImageResource(emptyImgId);
    }

    public void setEmpty_msg(String emptyMsg) {
        this.empty_msg.setText(emptyMsg);
    }

    public ImageView getEmpty_img() {
        return empty_img;
    }

    public TextView getEmpty_msg() {
        return empty_msg;
    }


}
