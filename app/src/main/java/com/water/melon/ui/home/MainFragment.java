package com.water.melon.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.presenter.HomePresent;
import com.water.melon.presenter.contract.HomeContract;
import com.water.melon.ui.home.h5.AgentWebFragment;
import com.water.melon.ui.home.h5.H5VideoActivity;
import com.water.melon.ui.in.HomeAdapterItemClick;
import com.water.melon.ui.search.SearchActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.bannel.NetImageHolderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements OnItemClickListener, HomeContract.View {
    public static final String TAG = "MainFragment";
    @BindView(R.id.netConvenientBanner)
    ConvenientBanner netConvenientBanner;
    @BindView(R.id.fragment_gg_tv)
    TextView tvGg;
    @BindView(R.id.fragment_main_sf)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_main_rv)
    RecyclerView recyclerView;


    private HomePresent present;
    private MainHomeAdapter adapter;

    private List<String> netImages = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new HomePresent(this, this);
        present.start();

    }


    @Override
    public void initView() {
        tvGg.setSelected(true);
        adapter = new MainHomeAdapter();
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);  //禁止下拉刷新
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
//        recyclerView.addItemDecoration();
//        adapter.addHeaderView(header);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                carRefresh();
//            }
//        });
//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                carLoadMore();
//            }
//        });

        adapter.setOnItemMusicListener(new HomeAdapterItemClick() {
            @Override
            public void onItemClick(AdvBean position) {
                Bundle bundle = new Bundle();
                bundle.putString(AgentWebFragment.URL_KEY, position.getTarget());
                redirectActivity(H5VideoActivity.class, bundle);
//                redirectActivity(H5VideoActivity.class);
            }
        });


        recyclerView.setAdapter(adapter);

//        netRes();
        present.getHomeBean();
        present.getHomeAdv();
        present.getAppNotice();
    }

    @Override
    public void setPresenter(HomeContract.Present presenter) {
        this.present = (HomePresent) presenter;
    }


    @Override
    public void onItemClick(int position) {
        LogUtil.e(TAG, "bannel click");
    }


    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setHomeBean(List<AdvBean> advBeans) {
//        LogUtil.e(TAG, "homeBeans.size = " + homeBeans.size());
        adapter.setNewData(advBeans);
    }

    @Override
    public void setHomeAdv(List<AdvBean> advBeans) {
        if (advBeans != null && advBeans.size() > 0) {
            netImages.clear();
            for (int i = 0; i < advBeans.size(); i++) {
                netImages.add(advBeans.get(i).getUrl());
            }
            netConvenientBanner.setPages(new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new NetImageHolderView(itemView, context);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.bannel_item;
                }
            }, netImages).setPageIndicator(new int[]{R.mipmap.bannel_spoit, R.mipmap.bannel_spoit_sel})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                    //设置指示器是否可见
                    .setPointViewVisible(true)
                    //监听单击事件
                    .setOnItemClickListener(this)
                    .startTurning(2000);    //设置自动切换（同时设置了切换时间间隔）;
        }

    }

    @Override
    public void getAppNotice(List<AdvBean> advBeans) {
        if (advBeans != null && advBeans.size() > 0) {
            for (int i = 0; i < advBeans.size(); i++) {
                AdvBean bean = advBeans.get(i);
                if (bean.getType() == 1) { //走马灯
                    tvGg.setText(bean.getMessage());
                } else if (bean.getType() == 2) {//弹框


                }
            }
        }
    }


    @OnClick({R.id.fragment_main_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_main_search:
                redirectActivity(SearchActivity.class);
                break;
        }
    }
}
