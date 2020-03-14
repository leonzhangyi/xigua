package com.water.melon.ui.me.history;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.ui.me.dowload.download_down.DownloadDoneFragment;
import com.water.melon.ui.me.dowload.offline_download.OffLineDownFragment;
import com.water.melon.ui.me.dowload.play_record.PlayingRecordFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VideoHistoryManagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;
    private String[] titles = {
            MyApplication.getStringByResId(R.string.download_manage_tab_2),
            MyApplication.getStringByResId(R.string.download_manage_tab_3)};

    public VideoHistoryManagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new Fragment[2];
//        fragments[0] = new OffLineDownFragment();//L_S:"资源管理" --> "未完成"
        fragments[0] = new VipHistoryFragment();//L_S:"资源管理" --> "已完成"
        fragments[1] = new PlayingRecordFragment();//L_S:"资源管理" --> "播放记录"
    }

    public String[] getTitles() {
        return titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
