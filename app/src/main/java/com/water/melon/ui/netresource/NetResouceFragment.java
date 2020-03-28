package com.water.melon.ui.netresource;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.TabBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.presenter.NetResourcePresent;
import com.water.melon.presenter.contract.NetResourceContract;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.dowload.DownLoadActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.ui.search.SearchActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.bannel.NetImageHolderView;
import com.water.melon.views.tablayou.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class NetResouceFragment extends BaseFragment implements NetResourceContract.View, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "NetResouceFragment";
//    @BindView(R.id.netConvenientBanner) //bannel
//            ConvenientBanner netConvenientBanner;

    @BindView(R.id.net_resource_tabLayout)
    TabLayout netResourceTabLayout;

    @BindView(R.id.net_resource_viewPage)
    ViewPager netResourceViewPage;

    @BindView(R.id.net_resource_left_tab)
    RadioGroup netResourceLeftTab;


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
//        netRes();

        netResourceLeftTab.setOnCheckedChangeListener(this);

        netResourceTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MyTabLayoutItem holder = new MyTabLayoutItem(tab.getCustomView());
                if (null == holder.mTabItem) {
                    return;
                }
                holder.mTabItem.setSelected(true);
                //设置选中后的字体大小
                holder.mTabItem.setTextSize(16);
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
//        present.getAdv();
    }

    @Override
    public void setPresenter(NetResourceContract.Present presenter) {
        this.present = (NetResourcePresent) presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }


    @Override
    public void setBigTab(List<TabBean> data) {
        if (null != data && data.size() > 0) {
            if (netResourceLeftTab.getChildCount() > 0) {
                netResourceLeftTab.removeAllViews();
            }
            RadioButton radioButton;
            for (TabBean datum : data) {
                radioButton = (RadioButton) LayoutInflater.from(netResourceLeftTab.getContext()).inflate(R.layout.item_net_resouce_big_tab, netResourceLeftTab, false);
                radioButton.setText("     " + datum.getName());
                radioButton.setId(Integer.parseInt(datum.getId()));
                radioButton.setTag(datum.getSub());
                netResourceLeftTab.addView(radioButton);
            }
            First_Big_Tab_Id = data.get(0).getId();
            netResourceLeftTab.getChildAt(0).performClick();
        } else {
            ToastUtil.showToastLong("获取失败,请稍后重试");
        }
    }

    @Override
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
                holder.mTabItem.setTextSize(16);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton item = group.findViewById(checkedId);
        if (item != null) {
            present.getSmallTab((List<TabBean.Sub>) item.getTag());
        }

    }


    static class MyTabLayoutItem {
        //TabLayout的Item
        TextView mTabItem;

        MyTabLayoutItem(View tabView) {
            mTabItem = (TextView) tabView;
        }
    }


    @OnClick({R.id.netresource_search, R.id.netresource_goto_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.netresource_search:
                redirectActivity(SearchActivity.class);
                break;
            case R.id.netresource_goto_download:
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    redirectActivity(DownLoadActivity.class);
                }
                break;
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

}
