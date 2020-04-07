package com.water.melon.ui.netresource;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.net.bean.UserBean;
import com.water.melon.presenter.NetResouceItemPresenter;
import com.water.melon.presenter.contract.NetResouceItemContract;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.ui.videoInfo.VideoInfoActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.bannel.NetImageHolderView;
import com.water.melon.utils.ui.recyler_view_item.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网络资源下的各个分类模块
 */
public class NetResouceItemFragment extends BaseFragment implements NetResouceItemContract.View
        , ItemNetResouceItemAdapter.AdapterListen, OnItemClickListener {
    //    @BindView(R.id.netConvenientBanner)
    ConvenientBanner netConvenientBanner;
    @BindView(R.id.net_resource_item_rv)
    RecyclerView netResourceItemRv;
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;

    private NetResouceItemPresenter mPresenter;
    private ItemNetResouceItemAdapter1 netResouceItemAdapter;
    private GridLayoutManager gridLayoutManager;
    private GetVideosRequest request;
    private boolean isRefreshing = false;//是否在加载
    private boolean isFirstFragment;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (null != args) {
            request = (GetVideosRequest) args.getSerializable(XGConstant.KEY_DATA);
            isFirstFragment = args.getBoolean(XGConstant.KEY_DATA_2);
            LogUtil.e("ssss", "isFirstFragment ====" + isFirstFragment);
        }
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new NetResouceItemPresenter(this, this, isFirstFragment);
        mPresenter.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_net_resource_item;
    }

    Handler handler = new Handler();


    @Override
    public void getListData(List<NetResoutVideoInfo> datas, boolean serviceError, boolean newData) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (serviceError) {
            netResouceItemAdapter.loadMoreFail();
        }
        if (newData) {
            loadingLay.setVisibility(View.GONE);
            showLoadingDialog(false);
//            List<NetResoutVideoInfo> mdatas = new ArrayList<NetResoutVideoInfo>();
//            mdatas.add(datas.get(0));
            LogUtil.e("首推", "");
            List<NetResoutVideoInfo> finalDatas = datas;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    netResouceItemAdapter.setNewData(finalDatas);
                }
            });

        } else {
            if (datas.size() > 0) {
                List<NetResoutVideoInfo> finalDatas1 = datas;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netResouceItemAdapter.addData(finalDatas1);
                        netResouceItemAdapter.loadMoreComplete();
                    }
                });

            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netResouceItemAdapter.loadMoreEnd();
                    }
                });

            }

        }
        isRefreshing = false;


    }

    @Override
    public void initView() {
        if (null == request) {
            return;
        }
        if (null == gridLayoutManager) {
            gridLayoutManager = new GridLayoutManager(context, 3);
            netResourceItemRv.setLayoutManager(gridLayoutManager);
//            netResourceItemRv.setHasFixedSize(true);
//            netResourceItemRv.setNestedScrollingEnabled(false);
//            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (netResouceItemAdapter != null && position == netResouceItemAdapter.getDatas().size()) {
//                        return 2;
//                    }
//                    return 1;
//                }
//            });
//            netResourceItemRv.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
//            netResourceItemRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    //加载更多
//                    if ((recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
//                            >= recyclerView.computeVerticalScrollRange()) && !isRefreshing && netResouceItemAdapter != null
//                            && !netResouceItemAdapter.isNoMoreData()) {
//                        //滑动到底部
//                        isRefreshing = true;
//                        mPresenter.getListData(request);
//                    }
//                }
//            });
            lazyLoad();
        }

        View header = LayoutInflater.from(context).inflate(R.layout.netresource_fragment_header, null);
        netConvenientBanner = header.findViewById(R.id.netConvenientBanner);

        View emptView = LayoutInflater.from(context).inflate(R.layout.netresource_fragment_empty, null);
        TextView no_data_tv = emptView.findViewById(R.id.no_data_tv);
        no_data_tv.setText("没有该类视频，看看其他分类吧！");
        netResouceItemAdapter = new ItemNetResouceItemAdapter1();
        netResouceItemAdapter.addHeaderView(header);
        netResouceItemAdapter.setEmptyView(emptView);
        netResouceItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        netResouceItemAdapter.setAdapterListen(this);
        netResourceItemRv.setAdapter(netResouceItemAdapter);


        mPresenter.getAdv();

        request.setPage(page);
        mPresenter.getListData(request);

    }

    private int page = 1;

    private void loadMore() {
        page++;
        request.setPage(page);
        mPresenter.getListData(request);
    }

    @Override
    public void setPresenter(NetResouceItemContract.Presenter presenter) {
        mPresenter = (NetResouceItemPresenter) presenter;
    }

    @Override
    public boolean isActive() {
        return isVisible;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }


    @Override
    public void videoItemClickListen(NetResoutVideoInfo item) {
        UserBean userBean1 = XGUtil.getMyUserInfo();
        if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
            ToastUtil.showToastShort("请先绑定手机号");
            Intent intent = new Intent(getContext(), RegistActivity.class);
            redirectActivityForResult(intent, 1);
        } else {
            //列表单个电影点击
            Bundle bundle = new Bundle();
            VideoPlayBean videoInfoBean = new VideoPlayBean();
            videoInfoBean.setId(item.get_id());
            bundle.putSerializable(XGConstant.KEY_DATA, videoInfoBean);
            redirectActivity(VideoInfoActivity.class, bundle);
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

    private List<AdvBean> advBeans;

    @Override
    public void onItemClick(int position) {
        if (advBeans != null && advBeans.size() > position) {
            XGUtil.openAdv(advBeans.get(position), MainActivity.mainActivity);
        }
    }

    private List<String> netImages = new ArrayList<>();

    @Override
    public void setAdv(List<AdvBean> advBeans) {
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

}
