package com.water.melon.ui.me.dowload;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.constant.XGConstant;
import com.water.melon.evenbus.EvenBusEven;
import com.water.melon.ui.me.dowload.download_down.DownloadDoneFragment;
import com.water.melon.ui.me.dowload.offline_download.OffLineDownFragment;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SDUtils;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.RemindDialog;
import com.water.melon.views.tablayou.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class DownLoadActivity extends BaseActivity {
    public static final String TAG = "DownLoadActivity";

    @BindView(R.id.fragment_download_manage_tabLayout)
    TabLayout fragmentDownloadManageTabLayout;
    @BindView(R.id.fragment_download_manage_viewpager)
    ViewPager fragmentDownloadManageViewpager;
    @BindView(R.id.fragment_download_manage_progress_free)
    ProgressBar fragmentDownloadManageProgressFree;
    @BindView(R.id.fragment_download_manage_space_available_text)
    TextView fragmentDownloadManageSpaceAvailableText;
    @BindView(R.id.fragment_download_manage_space_available)
    TextView fragmentDownloadManageSpaceAvailable;
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;

    @BindView(R.id.video_download_page_downloadend)
    RelativeLayout dwonloadEnd;
    @BindView(R.id.video_download_page_downloading)
    RelativeLayout dwonloading;
    @BindView(R.id.video_download_page_downloadend_tv)
    TextView dwonloadEndTv;
    @BindView(R.id.video_download_page_downloading_tv)
    TextView dwonloadingTv;


    private long totalSize = 0;
    private long oldFree = 0;
    private boolean isloop = true;
    private MyHandler mHandler;
    private DownLoadManagerAdapter downLoadManagerAdapter;

    private int currentPage = 0;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.video_download_view;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (null == downLoadManagerAdapter) {
            fragmentDownloadManageViewpager.post(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            });
        }
    }

    private void initView() {
        initToolBar();
        mHandler = new MyHandler();
        downLoadManagerAdapter = new DownLoadManagerAdapter(getSupportFragmentManager());
        fragmentDownloadManageViewpager.setAdapter(downLoadManagerAdapter);
        fragmentDownloadManageViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        fragmentDownloadManageViewpager.setOffscreenPageLimit(2);
        fragmentDownloadManageTabLayout.setupWithViewPager(fragmentDownloadManageViewpager);

        initTabLayout(downLoadManagerAdapter, downLoadManagerAdapter.getTitles().length);

        totalSize = SDUtils.getSDTotalSize(this);
        long free = MyApplication.getp2p().P2PGetFree();
        setAvail(totalSize, free);
        showSDSize();
    }

    private void initToolBar() {
        setToolBarLeftView(R.drawable.back_left);
        setToolLine(true);
        setTitleName(MyApplication.getStringByResId(R.string.video_download));
        setTitleNameColor(R.color.black_ff);

        setToolBarRightView(R.drawable.layout_video_downlaod_del);
        setToolBarRightView(MyApplication.getStringByResId(R.string.video_clear_all), R.color.net_resource_item_tv);
    }

    /**
     * 显示内存大小
     */
    private void showSDSize() {
        isloop = true;
        new Thread() {
            public void run() {
                while (isloop) {
                    try {
                        sleep(5000);
                        if (MyApplication.downTaskPositionList.size() <= 0 && !XGConstant.showSDSizeByUserClear) {
                            //没有下载，也没有清除缓存
                            continue;
                        }
                        XGConstant.showSDSizeByUserClear = false;
                        Message msg = Message.obtain();
                        long free = MyApplication.getp2p().P2PGetFree();

                        if (oldFree != free) {
                            msg.obj = free;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                            oldFree = free;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @OnClick({R.id.toolbar_left_tv, R.id.video_download_page_downloadend, R.id.video_download_page_downloading,R.id.toolbar_right_tv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                onClickTitleBack();
                break;
            case R.id.video_download_page_downloadend:
                currentPage = 0;
                fragmentDownloadManageViewpager.setCurrentItem(currentPage);
                doPageSelect(currentPage);
                break;
            case R.id.video_download_page_downloading:
                currentPage = 1;
                fragmentDownloadManageViewpager.setCurrentItem(currentPage);
                doPageSelect(currentPage);
                break;
            case R.id.toolbar_right_tv:
                if (null != downLoadManagerAdapter ) {
                    if (currentPage == 0) {
                        ((OffLineDownFragment) downLoadManagerAdapter.getItem(0)).delteAllDialog();
                    } else if (currentPage == 1) {
                        ((DownloadDoneFragment) downLoadManagerAdapter.getItem(1)).delteAllDialog();
                    }
                   }
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
    protected void onClickTitleBack() {
        LogUtil.e(TAG, "onClickTitleBack click");
        this.finish();
    }


    @Override
    protected void onClickTitleRight() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != downLoadManagerAdapter) {
            ((OffLineDownFragment) downLoadManagerAdapter.getItem(0)).starOrStopNotifinDownLoadSpeed(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != downLoadManagerAdapter ) {
            ((OffLineDownFragment) downLoadManagerAdapter.getItem(0)).starOrStopNotifinDownLoadSpeed(false);
        }
        MyApplication.flag = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isloop = false;
    }

    /**
     * 设置Tab Item的样式
     *
     * @param downLoadManagerAdapter
     * @param size
     */
    private void initTabLayout(DownLoadManagerAdapter downLoadManagerAdapter, int size) {
        MyTabLayoutItem holder;
        for (int i = 0; i < size; i++) {
            //获取tab
            TabLayout.Tab tab = fragmentDownloadManageTabLayout.getTabAt(i);
            //给tab设置自定义布局
            if (null == tab) {
                continue;
            }
            tab.setCustomView(R.layout.item_tablayout);
            holder = new MyTabLayoutItem(tab.getCustomView());
            //填充数据
            holder.mTabItem.setText(downLoadManagerAdapter.getTitles()[i]);
//            //默认选择第一项
            if (i == 0) {
                holder.mTabItem.setSelected(true);
                holder.mTabItem.setTextSize(18);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.mTabItem.setTextSize(16);
                //设置不为加粗
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.gray_EE));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }
        }

        fragmentDownloadManageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MyTabLayoutItem holder = new MyTabLayoutItem(tab.getCustomView());
                if (null == holder.mTabItem) {
                    return;
                }
                holder.mTabItem.setSelected(true);
                //设置选中后的字体大小
                holder.mTabItem.setTextSize(18);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //关联Viewpager
                fragmentDownloadManageViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MyTabLayoutItem holder = new MyTabLayoutItem(tab.getCustomView());
                holder.mTabItem.setSelected(false);
                //没被选中后的字体大小
                holder.mTabItem.setTextSize(16);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.gray_EE));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (loadingLay.getVisibility() == View.VISIBLE) {
                    loadingLay.setVisibility(View.GONE);
                }
            }
        });
        Objects.requireNonNull(fragmentDownloadManageTabLayout.getTabAt(0)).select();
    }

    public void setAvail(long all, long free) {
        if (null != fragmentDownloadManageSpaceAvailable) {
            String availstr = FileUtil.getSize(free);
            fragmentDownloadManageSpaceAvailable.setText(availstr);
            fragmentDownloadManageProgressFree.setProgress(Math.round((all - free) * 1000 / all));
        }
    }


    class MyHandler extends Handler {

        public MyHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long free = (Long) msg.obj;
            long limitFree = (long) (200 * 1024);//LS:这个是在原始的数据基础上修改的，测试用的是200 兆的判断；
            if (free / 1024 < limitFree) {
                if (MyApplication.isOpenCleanDia || MyApplication.isPlay) {//LS:判断是否打开清理磁盘，或者是否在播放视频；
                    return;
                }

                MyApplication.isOpenCleanDia = true;
                if (null != OffLineDownFragment.threadRun) {
                    OffLineDownFragment.threadRun.dispose();
                }
                XGUtil.stopAll("", true);
                EventBus.getDefault().post(new EvenBusEven.OffLineDownEven(3));

                //flag = false;
                new RemindDialog.Builder(DownLoadActivity.this)
                        .setTitle(MyApplication.getStringByResId(R.string.waring))
                        .setMessage(MyApplication.getStringByResId(R.string.sd_no_more))
                        .setPositive(MyApplication.getStringByResId(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        }).createDialog().show();

            } else {
                MyApplication.isOpenCleanDia = false;
            }
            setAvail(totalSize, free);//LS:第一个参数数 手机总的存储大小，第二个参数是：手机剩余空间的大小；
        }
    }

    static class MyTabLayoutItem {
        //TabLayout的Item
        TextView mTabItem;

        MyTabLayoutItem(View tabView) {
            mTabItem = (TextView) tabView;
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
