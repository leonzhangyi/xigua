package com.water.melon.ui.netresource;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.TabBean;
import com.water.melon.presenter.NetResourcePresent;
import com.water.melon.presenter.contract.NetResourceContract;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.bannel.NetImageHolderView;
import com.water.melon.views.tablayou.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class NetResouceFragment extends BaseFragment implements NetResourceContract.View, OnItemClickListener {
    public static final String TAG = "NetResouceFragment";
    @BindView(R.id.netConvenientBanner) //bannel
            ConvenientBanner netConvenientBanner;

    @BindView(R.id.net_resource_tabLayout)
    TabLayout netResourceTabLayout;

    @BindView(R.id.net_resource_viewPage)
    ViewPager netResourceViewPage;


    private List<String> netImages = new ArrayList<>();

    private NetResourcePresent present;


    public static String First_Big_Tab_Id;//方便本地记录第一个大类第一个标签数据

    @Override
    protected int getLayoutId() {
        return R.layout.netresource_fragment;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new NetResourcePresent(this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isVisible = true;
        present.start();
    }


    @Override
    protected void lazyLoad() {
//        if (isVisible) {
//            if (null != present) {
//                present.getBigTab();
//            }
//        }
    }

    @Override
    public void initView() {
        netRes();

        netResourceTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MyTabLayoutItem holder = new MyTabLayoutItem(tab.getCustomView());
                if (null == holder.mTabItem) {
                    return;
                }
                holder.mTabItem.setSelected(true);
                //设置选中后的字体大小
                holder.mTabItem.setTextSize(21);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //关联Viewpager
                netResourceViewPage.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MyTabLayoutItem holder = new MyTabLayoutItem(tab.getCustomView());
                holder.mTabItem.setSelected(false);
                //没被选中后的字体大小
                holder.mTabItem.setTextSize(16);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.black_80));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        present.getBigTab();
    }

    @Override
    public void setPresenter(NetResourceContract.Present presenter) {
        this.present = (NetResourcePresent) presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    /**
     * 广告栏播放网络图片资源
     */
    private void netRes() {
        loadNetTestDatas();
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

    /**
     * 加载网络图片资源
     */
    private void loadNetTestDatas() {
        netImages = Arrays.asList(MainFragment.imagesString);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void setBigTab(List<TabBean> data) {
        if (null != data && data.size() > 0) {
            RadioButton radioButton;
            for (TabBean datum : data) {
                //TODO 次数操作电影电视剧大分类
//                radioButton = (RadioButton) LayoutInflater.from(netResourceLeftTab.getContext()).inflate(R.layout.item_net_resouce_big_tab, netResourceLeftTab, false);
//                radioButton.setText("     " + datum.getName());
//                radioButton.setId(Integer.parseInt(datum.getId()));
//                radioButton.setTag(datum.getSub());
//                netResourceLeftTab.addView(radioButton);
//            }
//            First_Big_Tab_Id = data.get(0).getId();
//            netResourceLeftTab.getChildAt(0).performClick();
            }
            First_Big_Tab_Id = data.get(0).getId();
            List<TabBean.Sub> subs = data.get(0).getSub();
            setSmallTab(subs);
        } else {
            ToastUtil.showToastLong("获取失败,请稍后重试");
        }

    }

    public void setSmallTab(List<TabBean.Sub> data) {
        netResourceTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        NetResouceAdapter netResouceAdapter = new NetResouceAdapter(getChildFragmentManager(), data);
        netResourceViewPage.setAdapter(netResouceAdapter);
        netResourceViewPage.setOffscreenPageLimit(3);
        netResourceTabLayout.setupWithViewPager(netResourceViewPage);
        initTabLayout(netResouceAdapter, netResouceAdapter.getCount());
    }

    /**
     * 设置Tab Item的样式
     *
     * @param netResouceAdapter
     * @param size
     */
    private void initTabLayout(NetResouceAdapter netResouceAdapter, int size) {
        MyTabLayoutItem holder;
        for (int i = 0; i < size; i++) {
            //获取tab
            TabLayout.Tab tab = netResourceTabLayout.getTabAt(i);
            //给tab设置自定义布局
            if (null == tab) {
                continue;
            }
            tab.setCustomView(R.layout.item_tablayout);
            holder = new MyTabLayoutItem(tab.getCustomView());
            //填充数据
            holder.mTabItem.setText(netResouceAdapter.getPageTitle(i));
            //默认选择第一项
            if (i == 0) {
                holder.mTabItem.setSelected(true);
                holder.mTabItem.setTextSize(21);
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.mTabItem.setTextSize(16);
                //设置不为加粗
                holder.mTabItem.setTextColor(MyApplication.getColorByResId(R.color.black_80));
                holder.mTabItem.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }
        }
    }

    static class MyTabLayoutItem {
        //TabLayout的Item
        TextView mTabItem;

        MyTabLayoutItem(View tabView) {
            mTabItem = (TextView) tabView;
        }
    }
}
