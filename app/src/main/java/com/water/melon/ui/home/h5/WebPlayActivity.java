package com.water.melon.ui.home.h5;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.RoadBean;
import com.water.melon.ui.home.h5.utils.FragmentKeyDown;
import com.water.melon.ui.home.h5.utils.PopuwinAdapter;
import com.water.melon.ui.home.h5.utils.VideoPlayWebFragment;
import com.water.melon.ui.in.RoadsItemClick;
import com.water.melon.utils.XGUtil;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class WebPlayActivity extends BaseActivity {
    @BindView(R.id.h5_play_rec)
    RecyclerView recyclerView;

    @Override
    public int getContentViewByBase(Bundle savedInstan) {
        return R.layout.layout_h5_play_activity;
    }


    private FragmentManager mFragmentManager;
    public WebPlayRoadAdapter adapter;

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
//        setStausColor(R.color.black); //设置导航栏颜色值
        mFragmentManager = this.getSupportFragmentManager();
        openFragment();

        adapter = new WebPlayRoadAdapter();
        adapter.setOnItemMusicListener(new RoadsItemClick() {
            @Override
            public void onItemClick(RoadBean item) {
                mVideoPlayWebFragment.position = XGUtil.getPostion(item);
                mVideoPlayWebFragment.gotoUrl(item.getUrl());
                adapter.setNewData(XGUtil.getAllRoads(mVideoPlayWebFragment.position));
//                adapter.addData(XGUtil.getAllRoads(mVideoPlayWebFragment.position));
            }
        });
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        adapter.setNewData(XGUtil.getAllRoads(mVideoPlayWebFragment.position));
//        adapter.addData(XGUtil.getAllRoads(mVideoPlayWebFragment.position));
    }

    private VideoPlayWebFragment mVideoPlayWebFragment;

    private void openFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.container_framelayout, mVideoPlayWebFragment = VideoPlayWebFragment.getInstance(mBundle = new Bundle()), VideoPlayWebFragment.class.getName());
//        mBundle.putString(VideoPlayWebFragment.URL_KEY, "https://www.iqiyi.com");
        mBundle.putString(VideoPlayWebFragment.URL_KEY, getIntent().getStringExtra(VideoPlayWebFragment.URL_KEY));
        ft.commit();
        mVideoPlayWebFragment.setWebPlayActivity(this);
    }


    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        VideoPlayWebFragment mVideoPlayWebFragment = this.mVideoPlayWebFragment;
        if (mVideoPlayWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mVideoPlayWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }
}
