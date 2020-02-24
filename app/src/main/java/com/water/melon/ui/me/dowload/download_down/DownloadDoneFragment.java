package com.water.melon.ui.me.dowload.download_down;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.constant.XGConstant;
import com.water.melon.evenbus.EvenBusEven;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SpacesItemDecoration;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已完成下载界面
 */
public class DownloadDoneFragment extends BaseFragment {

    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;
    @BindView(R.id.fragment_download_done_lv)
    RecyclerView fragmentDownloadDoneLv;
    @BindView(R.id.edit_select_all_btn)
    Button editSelectAllBtn;
    @BindView(R.id.edit_delete_btn)
    Button editDeleteBtn;
    @BindView(R.id.edit_btn)
    Button editBtn;
    @BindView(R.id.download_edit_layout)
    LinearLayout downloadEditLayout;

    private List<LocalVideoInfo> mData;
    private DownloadDoneAdapter mAdapter;


    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        List<LocalVideoInfo> mDatas = XGUtil.loadCacheList();
        List<LocalVideoInfo> newList = new ArrayList<>();
        for (LocalVideoInfo item : mDatas) {
            try {
                MyApplication.getp2p().P2Pdoxadd(item.getUrl().getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!newList.contains(item)) {
                newList.add(item);
            }
        }
        if (newList.size() != mDatas.size()) {
            XGUtil.saveCacheList(newList);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download_done;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentDownloadDoneLv.setItemAnimator(null);
        fragmentDownloadDoneLv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        fragmentDownloadDoneLv.addItemDecoration(new SpacesItemDecoration(2));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (isVisible && null == mAdapter) {
            mData = XGUtil.loadCacheList();
            mAdapter = new DownloadDoneAdapter(mData);
            fragmentDownloadDoneLv.setAdapter(mAdapter);
            fragmentDownloadDoneLv.post(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter.getDatas().size() > 0 && null != downloadEditLayout) {
                        downloadEditLayout.setVisibility(View.VISIBLE);
                    }
                    loadingLay.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (null != mAdapter && mAdapter.isEditMoudle()) {
            editBtn.setText(MyApplication.getStringByResId(R.string.download_edit));
            mAdapter.setEditMoudle(false);
            mAdapter.setClickSelectAll(false);
            editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
            editSelectAllBtn.setVisibility(View.GONE);
            editDeleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听广播
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusEven.DownLoadDoneEven event) {
        LogUtil.e("下载完成界面", "已经完成的fragment收到广播了=============================");
        mData = XGUtil.loadCacheList();
        if (null != mAdapter) {
            //刷新数据
            mAdapter.setDatas(mData);
            if (null != downloadEditLayout) {
                if (mData.size() > 0 && downloadEditLayout.getVisibility() == View.GONE) {
                    downloadEditLayout.setVisibility(View.VISIBLE);
                } else if (mData.size() == 0 && downloadEditLayout.getVisibility() == View.VISIBLE) {
                    downloadEditLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @OnClick({R.id.edit_select_all_btn, R.id.edit_delete_btn, R.id.edit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_select_all_btn:
                //选中全部
                mAdapter.selectAllItem(!mAdapter.isClickSelectAll());
                if (mAdapter.isClickSelectAll()) {
                    editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all_cancle));
                } else {
                    editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
                }

                break;
            case R.id.edit_delete_btn:
                //删除选中的Item
                if (mAdapter.getCheckPosition().size() > 0) {
                    List<LocalVideoInfo> newData = new ArrayList<>();
                    int size = mAdapter.getDatas().size();
                    for (int i = 0; i < size; i++) {
                        if (!mAdapter.getCheckPosition().contains(i + "")) {
                            newData.add(mAdapter.getDatas().get(i));
                        } else {
                            //删除选中的本地下载文件
                            try {
                                MyApplication.getp2p().P2Pdoxdel(mAdapter.getDatas().get(i).getUrl().getBytes("GBK"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mAdapter.getCheckPosition().clear();
                    mAdapter.setDatas(newData);
                    //重新保存历史记录信息
                    XGUtil.saveCacheList(mAdapter.getDatas());
                    if (newData.size() == 0) {
                        downloadEditLayout.setVisibility(View.GONE);
                    }
                    mAdapter.setEditMoudle(false);
                    mAdapter.setClickSelectAll(false);
                    XGConstant.showSDSizeByUserClear = true;//通知更新手机内存大小
                } else {
                    ToastUtil.showToastShort(MyApplication.getStringByResId(R.string.download_edit_delete_empty));
                }
                break;
            case R.id.edit_btn:
                //开启编辑
                if (mAdapter != null) {
                    if (mAdapter.isEditMoudle()) {
                        editBtn.setText(MyApplication.getStringByResId(R.string.download_edit));
                        mAdapter.setEditMoudle(false);
                        mAdapter.setClickSelectAll(false);
                        editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
                        editSelectAllBtn.setVisibility(View.GONE);
                        editDeleteBtn.setVisibility(View.GONE);
                    } else {
                        editBtn.setText(MyApplication.getStringByResId(R.string.button_cancel));
                        mAdapter.setEditMoudle(true);
                        editSelectAllBtn.setVisibility(View.VISIBLE);
                        editDeleteBtn.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}
