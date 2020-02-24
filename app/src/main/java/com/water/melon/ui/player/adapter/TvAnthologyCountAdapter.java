package com.water.melon.ui.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.utils.ClickTooQucik;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TvAnthologyCountAdapter extends BaseRVListAdapter<SearchVideoInfoBean.Torrents> implements View.OnClickListener {
    private AdapterLsiten adapterLsiten;
    private int module = 1;//1==详情横向滑动的布局，2=弹窗九宫格布局
    private int checkPositon = -1, oldCheckPosition = -1;//当前选中的Item

    public TvAnthologyCountAdapter(List<SearchVideoInfoBean.Torrents> datas, AdapterLsiten adapterLsiten) {
        super(datas);
        setNoBottomView(true);
        this.adapterLsiten = adapterLsiten;
    }

    public TvAnthologyCountAdapter(List<SearchVideoInfoBean.Torrents> datas, AdapterLsiten adapterLsiten, int module) {
        super(datas);
        setNoBottomView(true);
        this.adapterLsiten = adapterLsiten;
        this.module = module;
    }

    public void initCheckPositon(int checkPositon) {
        this.checkPositon = checkPositon;
        oldCheckPosition = checkPositon;
    }

    public void setCheckPositon(int checkPositon) {
        this.checkPositon = checkPositon;
        if (oldCheckPosition != checkPositon) {
            notifyDataSetChanged();
            oldCheckPosition = checkPositon;
        }
    }

    public int getCheckPositon() {
        return checkPositon;
    }

    public interface AdapterLsiten {
        void onTvAnthologyItemClick(SearchVideoInfoBean.Torrents tv, int position);
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (module == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_anthology, parent, false);
        } else {
            //九宫格
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_anthology_vertical, parent, false);
        }
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.setData(getDatas().get(position).getTitle());
        if (checkPositon == position) {
            myHolder.itemTvAnthologyTx.setBackgroundResource(R.drawable.bg_main_light_radius_5);
            myHolder.itemTvAnthologyTx.setTextColor(MyApplication.getColorByResId(R.color.colorPrimary));
        } else {
            myHolder.itemTvAnthologyTx.setBackgroundResource(R.drawable.bg_grayed_radius_5);
            myHolder.itemTvAnthologyTx.setTextColor(MyApplication.getColorByResId(R.color.main_black));
        }

        myHolder.itemTvAnthologyTx.setTag(R.id.tag_id1, getDatas().get(position));
        myHolder.itemTvAnthologyTx.setTag(R.id.tag_id2, position);
        myHolder.itemTvAnthologyTx.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ClickTooQucik.isFastClick() || null == adapterLsiten) {
            return;
        }
        checkPositon = (int) view.getTag(R.id.tag_id2);
        if (oldCheckPosition != checkPositon) {
            adapterLsiten.onTvAnthologyItemClick((SearchVideoInfoBean.Torrents) view.getTag(R.id.tag_id1), checkPositon);
//            //刷新Item样式
//            if (oldCheckPosition >= 0) {
//                notifyItemChanged(oldCheckPosition);
//            }
//            oldCheckPosition = checkPositon;
//            notifyItemChanged(checkPositon);
        }
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_tv_anthology_tx)
        TextView itemTvAnthologyTx;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(String txt) {
            itemTvAnthologyTx.setText(txt);
        }
    }
}
