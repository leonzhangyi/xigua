package com.water.melon.ui.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.utils.ClickTooQucik;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoInfoAdapter extends BaseRVListAdapter<VideoPlayBean.Zh> implements View.OnClickListener {
    private AdapterListen mAdapterListen;
    private int oldCheckPosition = -1;

    public int getOldCheckPosition() {
        return oldCheckPosition;
    }

    public interface AdapterListen {
        void itemClick(VideoPlayBean.Zh item, int position);
    }

    public VideoInfoAdapter(List<VideoPlayBean.Zh> datas, AdapterListen mAdapterListen) {
        super(datas);
        setNoBottomView(true);
        this.mAdapterListen = mAdapterListen;
    }

    /**
     * 变更选中颜色
     *
     * @param checkPosition 变色下标
     */
    public void changeItemColor(int checkPosition) {
        if (oldCheckPosition > -1) {
            getDatas().get(oldCheckPosition).setCheck(false);
            notifyItemChanged(oldCheckPosition);
        }
        getDatas().get(checkPosition).setCheck(true);
        notifyItemChanged(checkPosition);
        oldCheckPosition = checkPosition;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_info, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.setData(getDatas().get(position));
        myHolder.itemView.setTag(R.id.tag_id1, getDatas().get(position));
        myHolder.itemView.setTag(R.id.tag_id2, position);
        myHolder.itemView.setTag(getDatas().get(position));
        myHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int posiTion = (int) view.getTag(R.id.tag_id2);
        if (ClickTooQucik.isFastClick() || mAdapterListen == null || posiTion == oldCheckPosition) {
            return;
        }

        mAdapterListen.itemClick((VideoPlayBean.Zh) view.getTag(R.id.tag_id1), posiTion);
        if (oldCheckPosition > -1) {
            getDatas().get(oldCheckPosition).setCheck(false);
            notifyItemChanged(oldCheckPosition);
        }
        getDatas().get(posiTion).setCheck(true);
        notifyItemChanged(posiTion);
        oldCheckPosition = posiTion;
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_video_info)
        TextView itemVideoInfo;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(VideoPlayBean.Zh data) {
            if (data.isCheck()) {
                itemVideoInfo.setTextColor(MyApplication.getColorByResId(R.color.colorPrimary));
            } else {
                itemVideoInfo.setTextColor(MyApplication.getColorByResId(R.color.gray_F0));
            }
            itemVideoInfo.setText(data.getTitle());
        }
    }
}
