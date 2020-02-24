package com.water.melon.ui.netresource;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.presenter.NetResouceItemPresenter;
import com.water.melon.presenter.contract.NetResouceItemContract;
import com.water.melon.ui.videoInfo.VideoInfoActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ui.recyler_view_item.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 网络资源下的各个分类模块
 */
public class NetResouceItemFragment extends BaseFragment implements NetResouceItemContract.View
        , ItemNetResouceItemAdapter.AdapterListen {
    @BindView(R.id.net_resource_item_rv)
    RecyclerView netResourceItemRv;
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;

    private NetResouceItemPresenter mPresenter;
    private ItemNetResouceItemAdapter netResouceItemAdapter;
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
            LogUtil.e("ssss", isFirstFragment + "====");
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

    @Override
    public void getListData(List<NetResoutVideoInfo> datas, boolean serviceError, boolean newData) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (null == netResouceItemAdapter) {
            netResouceItemAdapter = new ItemNetResouceItemAdapter(datas, this);
            netResouceItemAdapter.setPageSize(mPresenter.limit);
            if (serviceError) {
                netResouceItemAdapter.setEmptyClickListen(new BaseRVListAdapter.BaseRVListAdapterEmptyClickListen() {
                    @Override
                    public void emptyClickListen() {
                        showLoadingDialog(true);
                        mPresenter.getListData(request);
                    }
                });
                netResouceItemAdapter.setEmptyMsg("点击重新获取");
            }
            netResourceItemRv.setAdapter(netResouceItemAdapter);
            loadingLay.setVisibility(View.GONE);
        } else {
            if (newData) {
                netResouceItemAdapter.setDatas(datas);
            } else {
                netResouceItemAdapter.addDatas(datas);
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
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (netResouceItemAdapter != null && position == netResouceItemAdapter.getDatas().size()) {
                        return 2;
                    }
                    return 1;
                }
            });
            netResourceItemRv.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
            netResourceItemRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //加载更多
                    if ((recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                            >= recyclerView.computeVerticalScrollRange()) && !isRefreshing && netResouceItemAdapter != null
                            && !netResouceItemAdapter.isNoMoreData()) {
                        //滑动到底部
                        isRefreshing = true;
                        mPresenter.getListData(request);
                    }
                }
            });
            lazyLoad();
        }
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
        if (isVisible && null != mPresenter) {
            if (netResouceItemAdapter == null) {
                //获取数据
                mPresenter.getListData(request);
            }
        }
    }


    @Override
    public void videoItemClickListen(NetResoutVideoInfo item) {
        //列表单个电影点击
        Bundle bundle = new Bundle();
        SearchVideoInfoBean videoInfoBean = new SearchVideoInfoBean();
        videoInfoBean.setId(item.get_id());
        bundle.putSerializable(XGConstant.KEY_DATA, videoInfoBean);
        redirectActivity(VideoInfoActivity.class, bundle);
    }


}
