package com.water.melon.ui.me.feek.history;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.FeedBean;
import com.water.melon.ui.in.FeekHistoryItemClick;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class FeekHistoryActivity extends BaseActivity implements FeekHistoryContract.View {
    @BindView(R.id.history_feek_recyclerview)
    RecyclerView recyclerView;

    private FeekHistoryPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_history_feek;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new FeekHistoryPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private int page = 1;

    private HistoryFeekAdater adapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("反馈记录");
        setTitleNameColor(R.color.black);

        adapter = new HistoryFeekAdater();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

        adapter.setOnItemClick(new FeekHistoryItemClick() {
            @Override
            public void onItemClick(FeedBean.FeekItemBean item) {
                if (item != null && item.getCreated_time() != null && !item.getAnswer().trim().equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FeekItemActivity.FEEK_BEAN, item);
                    redirectActivity(FeekItemActivity.class, bundle);
                } else {
                    ToastUtil.showToastShort("请耐心等待工作人员的回复");
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        present.getDate(page);

    }

    private void loadMore() {
        page++;
        present.getDate(page);
    }

    @OnClick(R.id.toolbar_left_tv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    @Override
    public void setPresenter(FeekHistoryContract.Present presenter) {
        present = (FeekHistoryPresent) presenter;
    }

    @Override
    public void setDate(FeedBean feedBean, boolean isSuc) {
        showLoadingDialog(false);
        if (isSuc) {
            adapter.loadMoreFail();
        } else {
            if (feedBean == null) {
                feedBean = new FeedBean();
            }
            List<FeedBean.FeekItemBean> userInfos = feedBean.getList();
            if (page == 1) {
//                String total = agentUserBean.getCount();
                if (userInfos == null) {
                    userInfos = new ArrayList<>();
                }
                adapter.setNewData(userInfos);
            } else {
                if (userInfos == null || userInfos.size() == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(userInfos);
                    adapter.loadMoreComplete();
                }

            }

        }


    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.main_botton_bac)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.main_botton_bac)
                .init();
    }
}
