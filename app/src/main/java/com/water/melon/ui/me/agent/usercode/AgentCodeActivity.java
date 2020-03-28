package com.water.melon.ui.me.agent.usercode;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.me.history.VideoHistoryManagerAdapter;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class AgentCodeActivity extends BaseActivity implements AgentCodeContract.View {
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

    private AgentCodePresent present;
    private int currentPage = 0;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.agent_user_code;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new AgentCodePresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private AgentCodeHistoryManagerAdapter downLoadManagerAdapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("激活码使用记录");
        setTitleNameColor(R.color.black);
//        setToolBarRightView("代理设置", R.color.agent_setting_text);

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
        downLoadManagerAdapter = new AgentCodeHistoryManagerAdapter(getSupportFragmentManager());
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
    public void setPresenter(AgentCodeContract.Present presenter) {
        present = (AgentCodePresent) presenter;
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
}
