package com.water.melon.ui.me.agent.myagent;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.ui.in.AgentItemClick;
import com.water.melon.ui.me.agent.myagent.setagent.AddAgentActivity;
import com.water.melon.ui.me.agent.myagent.setagent.SettingAgentActivity;
import com.water.melon.utils.XGUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MyAgentActivity extends BaseActivity implements MyAgentContract.View {

    @BindView(R.id.my_agent_rl)
    RecyclerView recyclerView;


    private MyAgentPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.me_my_agent;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new MyAgentPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @OnClick({R.id.toolbar_right_tv, R.id.toolbar_left_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_right_tv:
                redirectActivity(SettingAgentActivity.class);
                break;
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    private int page = 1;
    private MyAgentDialog myAgentDialog;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("我的代理");
        setTitleNameColor(R.color.black);
        setToolBarRightView("设置代理", R.color.net_resource_item_tv);


        myAgentDialog = new MyAgentDialog();
        myAgentDialog.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        myAgentDialog.setAgentItemClick(new AgentItemClick() {
            @Override
            public void itemClick(MyAgentBean userInfo) {
                Bundle bundle = new Bundle();
                bundle.putString(XGUtil.AGENT_STATE, AddAgentActivity.LOOK_AGENT);
                bundle.putString(XGUtil.AGENT_PHONE, userInfo.getTel());
                bundle.putString(XGUtil.AGENT_ID, userInfo.getId());
                redirectActivity(AddAgentActivity.class, bundle);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAgentDialog);


        present.getAgentData(page);
    }

    private void loadMore() {
        page++;
        present.getAgentData(page);
    }

    @Override
    public void setPresenter(MyAgentContract.Present presenter) {
        this.present = (MyAgentPresent) presenter;
    }

    @Override
    public void setAgentDate(List<MyAgentBean> myAgentBeans, boolean err) {
        if (err) {
            myAgentDialog.loadMoreFail();
        } else {
            if (myAgentBeans == null) {
                myAgentBeans = new ArrayList<>();
            }
            if (page == 1) {
                myAgentDialog.setNewData(myAgentBeans);

            } else {
                if (myAgentBeans == null || myAgentBeans.size() == 0) {
                    myAgentDialog.loadMoreEnd();
                } else {
                    myAgentDialog.addData(myAgentBeans);
                    myAgentDialog.loadMoreComplete();
                }

            }

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
