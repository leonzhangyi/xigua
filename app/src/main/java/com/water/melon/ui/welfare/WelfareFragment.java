package com.water.melon.ui.welfare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sunfusheng.GlideImageView;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.presenter.WelPresent;
import com.water.melon.presenter.contract.WelfContract;
import com.water.melon.ui.home.MainHomeAdapter;
import com.water.melon.ui.home.HomeBean;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.ui.home.h5.AgentWebFragment;
import com.water.melon.ui.home.h5.H5VideoActivity;
import com.water.melon.ui.in.HomeAdapterItemClick;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.ui.search.SearchActivity;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.bannel.NetImageHolderView;
import com.water.melon.utils.glide.GlideHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class WelfareFragment extends BaseFragment implements WelfContract.View, OnItemClickListener {

    @BindView(R.id.fragment_wef_botton_rv)
    RecyclerView btnRv;
    @BindView(R.id.main_vip)
    ImageView main_vip;


    private WelPresent present;
    private MainHomeAdapter adapter;

    private List<String> netImages = new ArrayList<>();

    private WelfBtnAdapter btnAdapter;
    private int btnPage = 1;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new WelPresent(this, this);
        present.start();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.welf_fragment_main;
    }

    ConvenientBanner welfBanner;
    RecyclerView recyclerView;
    GlideImageView fragment_wef_mid_adv;


    @Override
    public void initView() {
        View header = LayoutInflater.from(context).inflate(R.layout.welfare_fragment, null);


        welfBanner = header.findViewById(R.id.welfare_banner);
        recyclerView = header.findViewById(R.id.fragment_wef_rv);
        fragment_wef_mid_adv = header.findViewById(R.id.fragment_wef_mid_adv);
        fragment_wef_mid_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advs != null && advs.size() > 0) {
                    XGUtil.openAdv(advs.get(0), MainActivity.mainActivity);
                }

            }
        });


        adapter = new MainHomeAdapter();
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));


        adapter.setOnItemMusicListener(new HomeAdapterItemClick() {
            @Override
            public void onItemClick(AdvBean position) {
                Bundle bundle = new Bundle();
                bundle.putString(AgentWebFragment.URL_KEY, position.getTarget());
                redirectActivity(WelfH5Activity.class, bundle);
            }
        });


        recyclerView.setAdapter(adapter);


        btnRv.setLayoutManager(new LinearLayoutManager(context));
        btnAdapter = new WelfBtnAdapter();
//        btnAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        btnAdapter.setOnItemMusicListener(new HomeAdapterItemClick() {
            @Override
            public void onItemClick(AdvBean position) {
//                redirectActivity(H5VideoActivity.class);
                String name = position.getTitle();
//                ToastUtil.showToastShort("正在下载" + name, Gravity.BOTTOM);

                Toast.makeText(MyApplication.getContext(), "正在下载" + name, Toast.LENGTH_SHORT).show();
//                present.downloadAPK("http://gdown.baidu.com/data/wisegame/cfdb6ba461b2c8ad/baidu_97519360.apk", context, name);
                present.downloadAPK(position.getTarget(), context, name);
            }
        });
        btnAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                btnPage++;
                present.getBtnBeans(btnPage);
            }
        });

        btnAdapter.addHeaderView(header);
        btnRv.setAdapter(btnAdapter);

        isVisible = true;
        LoadingUtil.init(context);
//        netRes();
        present.getWelfBeans();
        present.getWelTopAdv();
        present.getMidAdv();
        present.getBtnBeans(btnPage);
    }

    @OnClick({R.id.welf_search, R.id.main_vip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_vip:
                redirectActivity(VipActivity.class);
                break;
            case R.id.welf_search:
                redirectActivity(SearchActivity.class);
                break;
        }

    }

    @Override
    public void setPresenter(WelfContract.present presenter) {
        this.present = (WelPresent) presenter;
    }


    @Override
    public boolean isActive() {
        return false;
    }


    @Override
    public void getwelfBeans(List<AdvBean> advs) {
        adapter.setNewData(advs);

    }

    @Override
    public void onItemClick(int position) {
        if (advBeans != null && advBeans.size() > position) {
            XGUtil.openAdv(advBeans.get(position), MainActivity.mainActivity);
        }
    }

    @Override
    public void setWelTopAdv(List<AdvBean> advBeans) {
//        loadNetTestDatas();
        if (advBeans != null && advBeans.size() > 0) {
            netImages.clear();
            this.advBeans = advBeans;
            for (int i = 0; i < advBeans.size(); i++) {
                netImages.add(advBeans.get(i).getUrl());
            }
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
    }

    private List<AdvBean> advs;

    @Override
    public void setMidAdv(List<AdvBean> advs) {
        this.advs = advs;
        if (advs != null && advs.size() > 0) {
            GlideHelper.showImage(fragment_wef_mid_adv, advs.get(0).getUrl(), R.mipmap.fuli_adv1_def);
        }
    }

    private List<AdvBean> advBeans;

    @Override
    public void setBtnBeans(List<AdvBean> advs, int type) {
        LogUtil.e("setBtnBeans", "TYPE = " + type);
        switch (type) {
            case 0:
                if (btnPage == 1) {
                    btnAdapter.setNewData(advs);
                } else {
                    btnAdapter.addData(advs);
                    btnAdapter.loadMoreComplete();
                }
                break;
            case 1:
                if (btnPage > 0) {
                    btnAdapter.loadMoreFail();
                }
                break;
            case 2:
                if (btnPage > 0) {
                    btnAdapter.loadMoreEnd();
                }
                break;
        }


    }
}
