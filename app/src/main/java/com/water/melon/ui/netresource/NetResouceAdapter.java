package com.water.melon.ui.netresource;

import android.annotation.SuppressLint;
import android.os.Bundle;


import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.net.bean.TabBean;
import com.water.melon.ui.netresource.NetResouceFragment;
import com.water.melon.ui.netresource.NetResouceItemFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class NetResouceAdapter extends FragmentStatePagerAdapter {

    private List<NetResouceItemFragment> fragments;
    private List<TabBean.Sub> smallTabId;

    @SuppressLint("WrongConstant")
    public NetResouceAdapter(@NonNull FragmentManager fm, List<TabBean.Sub> smallTabId) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.smallTabId = smallTabId;
        int size = smallTabId.size();
        fragments = new ArrayList<>(size);
        NetResouceItemFragment netResouceItemFragment;
        for (int i = 0; i < size; i++) {
            netResouceItemFragment = new NetResouceItemFragment();
            Bundle bundle = new Bundle();
            GetVideosRequest request = new GetVideosRequest();
            request.setBigTabId(smallTabId.get(i).getPid());
            request.setSmallTabId(smallTabId.get(i).getId());
            request.setSmallName(smallTabId.get(i).getName());
            bundle.putSerializable(XGConstant.KEY_DATA, request);
            bundle.putBoolean(XGConstant.KEY_DATA_2, i == 0 && smallTabId.get(i).getPid().equals(NetResouceFragment.First_Big_Tab_Id));
            netResouceItemFragment.setArguments(bundle);
            fragments.add(netResouceItemFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return smallTabId.get(position).getName();
    }
}
