package com.water.melon.ui.me.history;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class VideoHistroryActivity extends BaseActivity {

    @BindView(R.id.video_history_vp)
    ViewPager video_history_vp;
    @BindView(R.id.video_download_page_downloadend)
    RelativeLayout dwonloadEnd;
    @BindView(R.id.video_download_page_downloading)
    RelativeLayout dwonloading;
    @BindView(R.id.video_download_page_downloadend_tv)
    TextView dwonloadEndTv;
    @BindView(R.id.video_download_page_downloading_tv)
    TextView dwonloadingTv;


    private int currentPage = 0;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_video_history;
    }


    private VideoHistoryManagerAdapter downLoadManagerAdapter;

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("观看历史");
        setTitleNameColor(R.color.black);
//        setToolBarRightView("反馈记录", R.color.net_resource_item_tv);

        if (null == downLoadManagerAdapter) {
            video_history_vp.post(new Runnable() {
                @Override
                public void run() {
                    startInit();
                }
            });
        }
    }


    private void startInit() {
        downLoadManagerAdapter = new VideoHistoryManagerAdapter(getSupportFragmentManager());
        video_history_vp.setAdapter(downLoadManagerAdapter);
        video_history_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                doPageSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设置viewpager保留界面不重新加载的页面数量
        video_history_vp.setOffscreenPageLimit(2);
//        fragmentDownloadManageViewpager.setCurrentItem(currentPage);

    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @OnClick({R.id.toolbar_left_tv, R.id.video_download_page_downloadend, R.id.video_download_page_downloading})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_download_page_downloadend:
                currentPage = 0;
                video_history_vp.setCurrentItem(currentPage);
                doPageSelect(currentPage);
                break;
            case R.id.video_download_page_downloading:
                currentPage = 1;
                video_history_vp.setCurrentItem(currentPage);
                doPageSelect(currentPage);
                break;
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    private void doPageSelect(int position) {
        switch (position) {
            case 0:
                dwonloadEnd.setBackground(MyApplication.getDrawableByResId(R.drawable.video_download_main_page_back_press));
                dwonloading.setBackground(MyApplication.getDrawableByResId(R.drawable.video_download_main_page_back));
                dwonloadEndTv.setTextColor(MyApplication.getColorByResId(R.color.white));
                dwonloadingTv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
                break;
            case 1:
                dwonloadEnd.setBackground(MyApplication.getDrawableByResId(R.drawable.video_download_main_page_back));
                dwonloading.setBackground(MyApplication.getDrawableByResId(R.drawable.video_download_main_page_back_press));
                dwonloadEndTv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
                dwonloadingTv.setTextColor(MyApplication.getColorByResId(R.color.white));
                break;
        }
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.main_botton_bac)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.main_botton_bac)
                .init();
    }
}
