package com.water.melon.ui.me.agent.applyhistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.ui.me.agent.setting.SettingAgentAdapter;
import com.water.melon.utils.LoadingUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class AgentApplyHistroyActivity extends BaseActivity implements AgentApplyHistoryContract.View {
    @BindView(R.id.agent_apply_recy)
    RecyclerView recyclerView;

    private AgentApplyHistoryPresent present;

    private AgentApplyHistoryAdapter adapter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_agent_apply_history;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new AgentApplyHistoryPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("代理申请记录");
        setTitleNameColor(R.color.black);

        View view = LayoutInflater.from(this).inflate(R.layout.netresource_fragment_empty, null);
        TextView no_data_tv = view.findViewById(R.id.no_data_tv);
        no_data_tv.setText("没有申请记录");

        adapter = new AgentApplyHistoryAdapter();
        adapter.setEmptyView(view);
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        LoadingUtil.init(this);

        present.getApplyList();
    }

    @OnClick({R.id.toolbar_left_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    @Override
    public void setPresenter(AgentApplyHistoryContract.Present presenter) {
        present = (AgentApplyHistoryPresent) presenter;
    }

    @Override
    public void setDate(List<AgentBean> vipBean) {
        if (vipBean != null) {
//            vipBean.clear();
            adapter.setNewData(vipBean);
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
