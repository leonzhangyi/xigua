package com.water.melon.ui.me.agent.usercode.use;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class AgentCodeUserFragment extends BaseFragment implements AgentCodeUserContract.View {
    @BindView(R.id.agent_code_user_rev)
    RecyclerView recyclerView;

    @BindView(R.id.agent_use_et)
    EditText agent_use_et;

    @BindView(R.id.layout_agent_create_code_code_tv)
    TextView layout_agent_create_code_code_tv;


    private AgentCodeUserPresent present;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new AgentCodeUserPresent(this, this);
        present.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_agent_code_user;
    }

    @OnClick({R.id.layout_agent_create_code_code_rl, R.id.agent_user_one_search_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_agent_create_code_code_rl:
                showSexChooseDialog();
                break;
            case R.id.agent_user_one_search_rl:
                String keyword = agent_use_et.getText().toString().trim();
                if (keyword != null && !keyword.equals("")) {
                    page = 1;
                    agentCodeHisBean.setHandle("after");
                    agentCodeHisBean.setStatus("1");
                    agentCodeHisBean.setPage(page + "");
                    agentCodeHisBean.setRows("20");
                    agentCodeHisBean.setType(type);
                    agentCodeHisBean.setKeyword(keyword);
                    present.getCodeList1(agentCodeHisBean);
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

    private CodeUserAdapter adapter;


    AgentCodeHisBean agentCodeHisBean;

    private int page = 1;
    private String type = "";

    @Override
    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.netresource_fragment_empty, null);
        TextView no_data_tv = view.findViewById(R.id.no_data_tv);
        no_data_tv.setText("没有使用记录");
        adapter = new CodeUserAdapter();
        adapter.setEmptyView(view);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        AgentCodeHisBean agentCodeHisBean1 = new AgentCodeHisBean();
        //拉取卡信息
        agentCodeHisBean1.setHandle("before");
        agentCodeHisBean1.setStatus("1");
        agentCodeHisBean1.setPage("1");
        agentCodeHisBean1.setRows("20");
        agentCodeHisBean1.setType("");
        present.getCodeList(agentCodeHisBean1);

        LoadingUtil.init(context);

        if (agentCodeHisBean == null) {
            agentCodeHisBean = new AgentCodeHisBean();
        }
        page = 1;
        agentCodeHisBean.setHandle("after");
        agentCodeHisBean.setStatus("1");
        agentCodeHisBean.setPage(page + "");
        agentCodeHisBean.setRows("20");
        agentCodeHisBean.setType(type);
        agentCodeHisBean.setKeyword("");
        present.getCodeList1(agentCodeHisBean);

    }

    private void loadMore() {
        page++;
        agentCodeHisBean.setPage(page + "");
        present.getCodeList1(agentCodeHisBean);
    }

    @Override
    public void setPresenter(AgentCodeUserContract.Present presenter) {
        present = (AgentCodeUserPresent) presenter;

    }

    @Override
    public boolean isActive() {
        return false;
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

    @Override
    public void setDate(List<AgentCodeHisBean.CodeBean> date, boolean err) {
//        showLoadingDialog(false);
        if (err) {
            adapter.loadMoreFail();
        } else {
            if (date == null) {
                date = new ArrayList<>();
            }
            if (page == 1) {
                adapter.setNewData(date);
            } else {
                if (date == null || date.size() == 0) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.addData(date);
                    adapter.loadMoreComplete();
                }

            }

        }
    }
}
