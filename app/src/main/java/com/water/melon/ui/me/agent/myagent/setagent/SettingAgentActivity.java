package com.water.melon.ui.me.agent.myagent.setagent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.ui.in.AgentUserItemClick;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SettingAgentActivity extends BaseActivity implements SetAgentContract.View {

    @BindView(R.id.my_agent_rl)
    RecyclerView recyclerView;

    @BindView(R.id.set_agent_et)
    EditText set_agent_et;

    private SetAgentPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_set_agent;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new SetAgentPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @OnClick({R.id.layout_set_agent_search, R.id.toolbar_left_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_set_agent_search:
                String text = set_agent_et.getText().toString();
                if (text != null && !text.trim().equals("")) {
                    baseRequest.setPage(page);
                    baseRequest.setRows(20);
                    AgentUserBean bean = new AgentUserBean();
                    bean.setKeyword(text);
                    baseRequest.setParameter(bean);
                    present.getAgentData(baseRequest);
                } else {
                    ToastUtil.showToastLong("请输入用户手机号码");
                }
                break;
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    private int page = 1;
    private SetAgentAdapter setAgentAdapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("添加代理");
        setTitleNameColor(R.color.black);


        setAgentAdapter = new SetAgentAdapter();
        setAgentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });


        setAgentAdapter.setItemClick(new AgentUserItemClick() {
            @Override
            public void itemClick(AgentUserBean.UserInfo userInfo) {
                //设置为代理
//                present.setAgent(userInfo);
                Bundle bundle = new Bundle();
                bundle.putString(XGUtil.AGENT_STATE,AddAgentActivity.ADD_AGENT);
                bundle.putString(XGUtil.AGENT_PHONE,userInfo.getMobile());
                bundle.putString(XGUtil.AGENT_ID,userInfo.getId());
                redirectActivity(AddAgentActivity.class,bundle);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(setAgentAdapter);


    }

    BaseRequest<AgentUserBean> baseRequest = new BaseRequest<>();

    private void loadMore() {
        page++;
        baseRequest.setPage(page);

        present.getAgentData(baseRequest);
    }

    @Override
    public void setPresenter(SetAgentContract.Present presenter) {
        present = (SetAgentPresent) presenter;
    }

    @Override
    public void setAgentDate(List<AgentUserBean.UserInfo> myAgentBeans, boolean err) {
        if (err) {
            setAgentAdapter.loadMoreFail();
        } else {
            if (myAgentBeans == null) {
                myAgentBeans = new ArrayList<>();
            }
            if (page == 1) {
                setAgentAdapter.setNewData(myAgentBeans);

            } else {
                if (myAgentBeans == null || myAgentBeans.size() == 0) {
                    setAgentAdapter.loadMoreEnd();
                } else {
                    setAgentAdapter.addData(myAgentBeans);
                    setAgentAdapter.loadMoreComplete();
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
