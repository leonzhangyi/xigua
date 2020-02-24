package com.water.melon.ui.welfare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.presenter.WelPresent;
import com.water.melon.presenter.contract.WelfContract;
import com.water.melon.ui.home.MainHomeAdapter;
import com.water.melon.ui.home.HomeBean;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.ui.in.HomeAdapterItemClick;
import com.water.melon.utils.bannel.NetImageHolderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class WelfareFragment extends BaseFragment implements WelfContract.View , OnItemClickListener {

    @BindView(R.id.welfare_banner)
    ConvenientBanner welfBanner;
    @BindView(R.id.fragment_wef_sf)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_wef_rv)
    RecyclerView recyclerView;

    private WelPresent present;
    private MainHomeAdapter adapter;

    private List<String> netImages = new ArrayList<>();

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new WelPresent(this,this);
        present.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.welfare_fragment;
    }

    @Override
    public void initView() {
        adapter = new MainHomeAdapter();
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);  //禁止下拉刷新
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));


        adapter.setOnItemMusicListener(new HomeAdapterItemClick() {
            @Override
            public void onItemClick(HomeBean position) {

            }
        });


        recyclerView.setAdapter(adapter);

        netRes();
        present.setWelfBeans();
    }

    @Override
    public void setPresenter(WelfContract.present presenter) {
        this.present = (WelPresent) presenter;
    }


    @Override
    public boolean isActive() {
        return false;
    }

    /**
     * 广告栏播放网络图片资源
     */
    private void netRes() {
        loadNetTestDatas();
        welfBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new NetImageHolderView(itemView, context);
            }

            @Override
            public int getLayoutId() {
                return R.layout.bannel_item;
            }
        }, netImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器，不需要圆点指示器可以不设
                .setPageIndicator(new int[]{R.mipmap.bannel_spoit, R.mipmap.bannel_spoit_sel})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                //设置指示器是否可见
                .setPointViewVisible(true)
                //监听单击事件
                .setOnItemClickListener(this)
                .startTurning(2000)     //设置自动切换（同时设置了切换时间间隔）
                //监听翻页事件
//                .setOnPageChangeListener(this)
        ;
    }

    /**
     * 加载网络图片资源
     */
    private void loadNetTestDatas() {
        netImages = Arrays.asList(MainFragment.imagesString);
    }

    @Override
    public void getwelfBeans(ArrayList<HomeBean> beans) {
        adapter.setNewData(beans);
    }

    @Override
    public void onItemClick(int position) {

    }
}
