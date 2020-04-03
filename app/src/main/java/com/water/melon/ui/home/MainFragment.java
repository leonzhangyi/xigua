package com.water.melon.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.presenter.HomePresent;
import com.water.melon.presenter.contract.HomeContract;
import com.water.melon.ui.home.h5.AgentWebFragment;
import com.water.melon.ui.home.h5.H5VideoActivity;
import com.water.melon.ui.home.h5.WebPlayActivity;
import com.water.melon.ui.in.HomeAdapterItemClick;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.history.VideoHistroryActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.ui.search.SearchActivity;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.bannel.NetImageHolderView;
import com.water.melon.views.HomeDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements OnItemClickListener, HomeContract.View {
    public static final String TAG = "MainFragment";
    //    @BindView(R.id.netConvenientBanner)
//    ConvenientBanner netConvenientBanner;
//    @BindView(R.id.fragment_gg_tv)
//    TextView tvGg;
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


    TextView tvGg;
    ConvenientBanner netConvenientBanner;

    @Override
    public void initView() {


        View header = LayoutInflater.from(context).inflate(R.layout.fragment_main_header, null);
        netConvenientBanner = header.findViewById(R.id.netConvenientBanner);
        tvGg = header.findViewById(R.id.fragment_gg_tv);
        tvGg.setSelected(true);

        adapter = new MainHomeAdapter();
        adapter.addHeaderView(header);
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
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    if (userBean1 == null || userBean1.getVip().trim().equals("0")) {//非会员
                        redirectActivity(VipActivity.class);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(AgentWebFragment.URL_KEY, position.getTarget());
//                bundle.putString(AgentWebFragment.URL_KEY, "https://jiexxx.joja.top/juying/wabi/?url=https://m.iqiyi.com/v_19rvovox5w.html");
                        bundle.putString(AgentWebFragment.URL_POSTER_NAME, position.getTitle());
                        redirectActivity(H5VideoActivity.class, bundle);


//                Intent intent = new Intent(getContext(), WebPlayActivity.class);
//                intent.putExtra(AgentWebFragment.URL_KEY, "https://jiexxx.joja.top/juying/wabi/?url=https://m.iqiyi.com/v_19rvovox5w.html");
//                startActivity(intent);
                    }
                }

                present.doAdvClick(position);
            }
        });


        recyclerView.setAdapter(adapter);


        isVisible = true;
        LoadingUtil.init(context);
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
        if (advBeans != null && advBeans.size() > position) {
            XGUtil.openAdv(advBeans.get(position), MainActivity.mainActivity);
        }

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

    private List<AdvBean> advBeans;

    @Override
    public void setHomeAdv(List<AdvBean> advBeans) {
        this.advBeans = advBeans;
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
                    setHomeDialog(bean);

                }
            }
        }
    }


    @OnClick({R.id.fragment_main_search, R.id.mian_history, R.id.main_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_main_search:
                redirectActivity(SearchActivity.class);
                break;
            case R.id.mian_history:
//                redirectActivity(SearchActivity.class);
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    redirectActivity(VideoHistroryActivity.class);
                }
                break;
            case R.id.main_vip:
                redirectActivity(VipActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.e("MeFragment", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1001) {
            VipActivity.isRefresh = true;
        }
    }

    private HomeDialog homeDialog;

    public void setHomeDialog(AdvBean bean) {
        if (homeDialog == null) {
            homeDialog = new HomeDialog(context, R.style.dialog);
        }
        homeDialog.show();
        homeDialog.setDate(bean);
    }
}
