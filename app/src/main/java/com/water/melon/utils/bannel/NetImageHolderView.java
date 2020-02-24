package com.water.melon.utils.bannel;

import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.sunfusheng.GlideImageView;
import com.water.melon.R;
import com.water.melon.utils.glide.GlideHelper;

public class NetImageHolderView extends Holder<String> {
    private GlideImageView mImageView;

    private Context mContext;

    public NetImageHolderView(View itemView, Context context) {
        super(itemView);
        mContext = context;
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.item_image);
    }

    @Override
    public void updateUI(String data) {
        GlideHelper.showImage(mImageView,data,R.mipmap.bannel_def);
    }
}
