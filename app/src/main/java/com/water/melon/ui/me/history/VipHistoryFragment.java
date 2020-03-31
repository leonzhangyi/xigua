package com.water.melon.ui.me.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.VideoHistoryBean;
import com.water.melon.ui.home.h5.AgentWebFragment;
import com.water.melon.ui.home.h5.WebPlayActivity;
import com.water.melon.ui.in.VideoHistoryItemClick;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class VipHistoryFragment extends BaseFragment implements VIpHistoryContract.View {
    @BindView(R.id.history_recyclerview)
    RecyclerView recyclerView;


    private VIpHistoryPresent present;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new VIpHistoryPresent(this, this);
        present.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_vip_history;
    }

    private int page = 1;
    private VideoHistoryItemAdapter adapter;

    @Override
    public void initView() {
        adapter = new VideoHistoryItemAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        View emptyView = LayoutInflater.from(context).inflate(R.layout.netresource_fragment_empty, null);
        TextView no_data_tv = emptyView.findViewById(R.id.no_data_tv);
        no_data_tv.setText("没有播放记录");

        adapter.setEmptyView(emptyView);

        adapter.setOnItemClick(new VideoHistoryItemClick() {
            @Override
            public void onItemClick(VideoHistoryBean item) {
                Intent intent = new Intent(getContext(), WebPlayActivity.class);
                intent.putExtra(AgentWebFragment.URL_KEY, item.getUrl());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        present.getDate(page);
    }

    private void loadMore() {
        page++;
        present.getDate(page);
    }

    @Override
    public void setPresenter(VIpHistoryContract.Present presenter) {
        present = (VIpHistoryPresent) presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setDate(List<VideoHistoryBean> videoHistoryBeans, boolean err) {
        showLoadingDialog(false);
        if (err) {
            adapter.loadMoreFail();
        } else {
            if (videoHistoryBeans == null) {
                videoHistoryBeans = new ArrayList<>();
            }
            if (page == 1) {
                adapter.setNewData(videoHistoryBeans);

            } else {
                if (videoHistoryBeans == null || videoHistoryBeans.size() == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(videoHistoryBeans);
                    adapter.loadMoreComplete();
                }

            }

        }


    }
}
