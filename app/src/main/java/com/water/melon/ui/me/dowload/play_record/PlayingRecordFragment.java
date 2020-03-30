package com.water.melon.ui.me.dowload.play_record;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.evenbus.EvenBusEven;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SpacesItemDecoration;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 播放记录
 */
public class PlayingRecordFragment extends BaseFragment {
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;
    @BindView(R.id.fragment_play_record_lv)
    RecyclerView fragmentPlayRecordLv;
    @BindView(R.id.edit_select_all_btn)
    Button editSelectAllBtn;
    @BindView(R.id.edit_delete_btn)
    Button editDeleteBtn;
    @BindView(R.id.edit_btn)
    Button editBtn;
    @BindView(R.id.download_edit_layout)
    LinearLayout downloadEditLayout;

    private List<LocalVideoInfo> mData;
    private PlayRecordAdapter mAdapter;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        //生成广播处理
        EventBus.getDefault().register(this);

        fragmentPlayRecordLv.setLayoutManager(new LinearLayoutManager(context));
        fragmentPlayRecordLv.addItemDecoration(new SpacesItemDecoration(2));
        fragmentPlayRecordLv.setItemAnimator(null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (isVisible && null == mAdapter) {
            mData = XGUtil.loadHistoryList();
            mAdapter = new PlayRecordAdapter(mData);
            fragmentPlayRecordLv.setAdapter(mAdapter);
            fragmentPlayRecordLv.post(new Runnable() {
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
    protected int getLayoutId() {
        return R.layout.fragment_play_record;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//注销广播
    }

    /**
     * 监听广播
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusEven.PlayRecordEven event) {
        LogUtil.e("播放历史界面", "播放历史界的fragment收到广播了=============================");
        mData = XGUtil.loadHistoryList();
        if (mAdapter != null) {
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
                        }
                    }
                    mAdapter.getCheckPosition().clear();
                    mAdapter.setDatas(newData);
                    //重新保存历史记录信息
                    XGUtil.saveHistoryList(mAdapter.getDatas());
                    if (newData.size() == 0) {
                        downloadEditLayout.setVisibility(View.GONE);
                    }
                    mAdapter.setEditMoudle(false);
                    mAdapter.setClickSelectAll(false);
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

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.main_botton_bac)
                .statusBarDarkFont(true)
                .init();
    }
}
