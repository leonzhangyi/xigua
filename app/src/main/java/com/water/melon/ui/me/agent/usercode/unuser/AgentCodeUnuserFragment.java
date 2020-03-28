package com.water.melon.ui.me.agent.usercode.unuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class AgentCodeUnuserFragment extends BaseFragment implements AgentCodeUnuserContract.View {
    @BindView(R.id.agent_code_unuser_rev)
    RecyclerView recyclerView;

    @BindView(R.id.layout_agent_create_code_code_tv)
    TextView layout_agent_create_code_code_tv;

    @BindView(R.id.layout_agent_create_code_xiao_tv)
    TextView layout_agent_create_code_xiao_tv;


    @BindView(R.id.agent_use_et)
    EditText agent_use_et;


    private AgentCodeUnuserPresent present;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new AgentCodeUnuserPresent(this, this);
        present.start();
    }

    private AgentCodeUnuserAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_agent_code_unuser;
    }

    AgentCodeHisBean agentCodeHisBean;
    String type = "";

    @Override
    public void initView() {
        adapter = new AgentCodeUnuserAdapter();
//        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });


        setXiaoData();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        AgentCodeHisBean agentCodeHisBean1 = new AgentCodeHisBean();
        //拉取卡信息
        agentCodeHisBean1.setHandle("before");
        agentCodeHisBean1.setStatus("-1");
        agentCodeHisBean1.setPage("1");
        agentCodeHisBean1.setRows("20");
        agentCodeHisBean1.setType("");
        present.getCodeList(agentCodeHisBean1);


        if (agentCodeHisBean == null) {
            agentCodeHisBean = new AgentCodeHisBean();
        }
        agentCodeHisBean.setHandle("after");
        agentCodeHisBean.setStatus(status);
        agentCodeHisBean.setPage(page + "");
        agentCodeHisBean.setRows("20");
        agentCodeHisBean.setType(type);
        agentCodeHisBean.setKeyword("");
        present.getDate(agentCodeHisBean);
    }

    private int page = 1;
    private String status = "-1"; //1已使用/-1未使用(全部)/2失效/0正常

    private void loadMore() {
        page++;
        agentCodeHisBean.setPage(page + "");
        present.getDate(agentCodeHisBean);
    }

    @Override
    public void setPresenter(AgentCodeUnuserContract.Present presenter) {
        present = (AgentCodeUnuserPresent) presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setDate(AgentCodeHisBean Types, boolean err) {

        if (err) {
            adapter.loadMoreFail();
        } else {
            if (Types == null) {
                Types = new AgentCodeHisBean();
            }
            List<AgentCodeHisBean.CodeBean> userInfos = Types.getList();
            if (page == 1) {
                if (userInfos == null) {
                    userInfos = new ArrayList<>();
                }
                adapter.setNewData(userInfos);

            } else {
                if (userInfos == null || userInfos.size() == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(userInfos);
                    adapter.loadMoreComplete();
                }

            }

        }


    }

    @OnClick({R.id.layout_agent_create_code_code_rl, R.id.layout_agent_create_code_xiao_rl,R.id.agent_user_one_search_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_agent_create_code_code_rl:
                showSexChooseDialog();
                break;
            case R.id.layout_agent_create_code_xiao_rl:
                showSexChooseDialog1();
                break;

            case R.id.agent_user_one_search_rl:
                String keyword = agent_use_et.getText().toString().trim();
                if (keyword != null && !keyword.equals("")) {
                    page = 1;
                    agentCodeHisBean.setHandle("after");
                    agentCodeHisBean.setStatus(status);
                    agentCodeHisBean.setPage(page + "");
                    agentCodeHisBean.setRows("20");
                    agentCodeHisBean.setType(type);
                    agentCodeHisBean.setKeyword(keyword);
                    present.getDate(agentCodeHisBean);
                } else {
                    ToastUtil.showToastShort("请输入激活码");
                }
                break;
        }
    }

    private String[] sexArry;

    private void showSexChooseDialog() {
        if (sexArry == null || sexArry.length == 0) {
            return;
        }
        AlertDialog.Builder builder3 = new AlertDialog.Builder(context);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                layout_agent_create_code_code_tv.setText(sexArry[which]);
                for (int i = 0; i < mTypes.size(); i++) {
                    if (mTypes.get(i).getTitle().equals(sexArry[which])) {
                        type = mTypes.get(i).getType();
                        return;
                    }
                }

            }
        });
        builder3.show();// 让弹出框显示
    }

    private void setXiaoData() {
        sexArry1 = new String[3];
        sexArry1[0] = "全部";
        sexArry1[1] = "有效";
        sexArry1[2] = "失效";


        layout_agent_create_code_xiao_tv.setText(sexArry1[0]);

    }


    private String[] sexArry1;

    private void showSexChooseDialog1() {
        if (sexArry1 == null || sexArry1.length == 0) {
            return;
        }
        AlertDialog.Builder builder3 = new AlertDialog.Builder(context);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry1, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
                layout_agent_create_code_xiao_tv.setText(sexArry1[which]);

                switch (which) {
                    case 0:
                        status = "-1";
                        break;
                    case 1:
                        status = "0";
                        break;

                    case 2:
                        status = "2";
                        break;
                }

            }
        });
        builder3.show();// 让弹出框显示
    }

    private List<AgentCodeHisBean.Types> mTypes;

    @Override
    public void setType(List<AgentCodeHisBean.Types> type) {
        AgentCodeHisBean.Types mtype = new AgentCodeHisBean.Types();
        mtype.setType("");
        mtype.setTitle("全部激活码");
        type.add(0, mtype);
        this.mTypes = type;
        if (type != null && type.size() > 0) {
            layout_agent_create_code_code_tv.setText(type.get(0).getTitle());
            sexArry = new String[type.size()];
            for (int i = 0; i < type.size(); i++) {
                sexArry[i] = type.get(i).getTitle();
            }
        }
    }
}

