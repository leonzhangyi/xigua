package com.water.melon.ui.me.agent.usercode;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.ui.me.agent.usercode.unuser.AgentCodeUnuserFragment;
import com.water.melon.ui.me.agent.usercode.use.AgentCodeUserFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AgentCodeHistoryManagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;
    private String[] titles = {
            MyApplication.getStringByResId(R.string.agent_code_user),
            MyApplication.getStringByResId(R.string.agent_code_unuser)};

    public AgentCodeHistoryManagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new Fragment[2];
        fragments[0] = new AgentCodeUserFragment();//L_S:"资源管理" --> "已完成"
        fragments[1] = new AgentCodeUnuserFragment();//L_S:"资源管理" --> "播放记录"
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
