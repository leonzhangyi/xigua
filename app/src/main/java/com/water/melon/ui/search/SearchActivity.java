package com.water.melon.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.presenter.SearchPresent;
import com.water.melon.presenter.contract.SearchContract;
import com.water.melon.ui.netresource.ItemNetResouceItemAdapter;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.ui.netresource.VideoPlayBean;
import com.water.melon.ui.videoInfo.VideoInfoActivity;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.ui.recyler_view_item.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchContract.View, ItemNetResouceItemAdapter.AdapterListen {

    @BindView(R.id.net_resource_item_rv)
    RecyclerView netResourceItemRv;
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;
    @BindView(R.id.search_edit_et)
    EditText search_edit_et;
    @BindView(R.id.activity_serch_no_video)
    RelativeLayout activity_serch_no_video;

    private SearchPresent present;
    private GridLayoutManager gridLayoutManager;
    private boolean isRefreshing = false;//是否在加载

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_search;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new SearchPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {
        loadingLay.setVisibility(View.GONE);
        activity_serch_no_video.setVisibility(View.GONE);
        if (null == gridLayoutManager) {
            gridLayoutManager = new GridLayoutManager(this, 3);
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
                        present.getListData(request);
                    }
                }
            });
        }
    }

    @Override
    public void setPresenter(SearchContract.Present presenter) {
        this.present = (SearchPresent) presenter;
    }


    private ItemNetResouceItemAdapter netResouceItemAdapter;
    private GetVideosRequest request;

    @Override
    public void getListData(List<NetResoutVideoInfo> datas, boolean serviceError, boolean newData) {
        loadingLay.setVisibility(View.GONE);
        netResourceItemRv.setVisibility(View.VISIBLE);
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (null == netResouceItemAdapter) {
            netResouceItemAdapter = new ItemNetResouceItemAdapter(datas, this);
            netResouceItemAdapter.setPageSize(present.limit);
            if (serviceError) {
                netResouceItemAdapter.setEmptyClickListen(new BaseRVListAdapter.BaseRVListAdapterEmptyClickListen() {
                    @Override
                    public void emptyClickListen() {
                        showLoadingDialog(true);
                        present.getListData(request);
                    }
                });
                netResouceItemAdapter.setEmptyMsg("点击重新获取");
            }
            netResourceItemRv.setAdapter(netResouceItemAdapter);
            loadingLay.setVisibility(View.GONE);
            if (datas == null || datas.size() == 0) {
                netResourceItemRv.setVisibility(View.GONE);
                activity_serch_no_video.setVisibility(View.VISIBLE);
            }
        } else {
            if (newData) {
                netResouceItemAdapter.setDatas(datas);
                if (datas == null || datas.size() == 0) {
                    netResourceItemRv.setVisibility(View.GONE);
                    activity_serch_no_video.setVisibility(View.VISIBLE);
                }
            } else {
                netResouceItemAdapter.addDatas(datas);
            }
        }
        isRefreshing = false;
    }

    @Override
    public void videoItemClickListen(NetResoutVideoInfo item) {
        //列表单个电影点击
        Bundle bundle = new Bundle();
        VideoPlayBean videoInfoBean = new VideoPlayBean();
        videoInfoBean.setId(item.get_id());
        bundle.putSerializable(XGConstant.KEY_DATA, videoInfoBean);
        redirectActivity(VideoInfoActivity.class, bundle);
    }

    @OnClick({R.id.main_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_vip:
                String text = search_edit_et.getText().toString();
                if (text != null && !text.trim().equals("")) {
                    hideInputKeyboard(search_edit_et);
                    loadingLay.setVisibility(View.VISIBLE);
                    activity_serch_no_video.setVisibility(View.GONE);

                    request = new GetVideosRequest();
                    request.setSearchWord(text);
                    present.setPage();
                    present.getListData(request);
                } else {
                    ToastUtil.showToastLong("请输入关键字");
                }
                break;
        }
    }


    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     */
    protected void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
