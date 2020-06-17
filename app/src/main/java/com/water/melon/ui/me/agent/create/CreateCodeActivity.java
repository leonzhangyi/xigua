package com.water.melon.ui.me.agent.create;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.ui.me.agent.usercode.AgentCodeActivity;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class CreateCodeActivity extends BaseActivity implements AgentCodeContract.View {

    @BindView(R.id.layout_agent_create_code_code_rl)
    RelativeLayout layout_agent_create_code_code_rl;
    @BindView(R.id.layout_agent_create_code_code_tv)
    TextView layout_agent_create_code_code_tv;

    @BindView(R.id.create_code_count)
    EditText create_code_count;

    @BindView(R.id.agent_create_code_rec)
    RecyclerView recyclerView;

    private AgentCodePresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_agent_create_code;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new AgentCodePresent(this, this);
        present.start();
    }

    CreateCodeBean codeBean = new CreateCodeBean();

    @OnClick({R.id.layout_agent_create_code_code_rl, R.id.layout_agent_create_code_sub, R.id.toolbar_left_tv, R.id.toolbar_right_tv, R.id.agent_create_code_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_agent_create_code_code_rl:
                showSexChooseDialog();
                break;
            case R.id.layout_agent_create_code_sub:
                String count = create_code_count.getText().toString();
                if (count != null && !count.trim().equals("")) {
                    codeBean.setCount(count);
                    for (int i = 0; i < codeBeans.size(); i++) {
                        if (codeBeans.get(i).getTitle().equals(layout_agent_create_code_code_tv.getText().toString())) {
                            codeBean.setId(codeBeans.get(i).getId());
                            break;
                        }
                    }
                    present.createCode(codeBean);
                } else {
                    ToastUtil.showToastLong("请输入数量");
                }

                break;


            case R.id.toolbar_left_tv:
                this.finish();
                break;
            case R.id.toolbar_right_tv:
                redirectActivity(AgentCodeActivity.class);
                break;
            case R.id.agent_create_code_save:
                XGUtil.writeExcel(this, createCodeAdapter.getData());
                break;
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private List<CreateCodeBean.CodeBean> codeBeans;

    @Override
    public void setAgentInfo(List<CreateCodeBean.CodeBean> codeBean) {
        if (codeBean != null && codeBean.size() > 0) {
            codeBeans = codeBean;
            layout_agent_create_code_code_tv.setText(codeBean.get(0).getTitle());
            sexArry = new String[codeBean.size()];
            for (int i = 0; i < codeBean.size(); i++) {
                sexArry[i] = codeBean.get(i).getTitle();
            }
        }
    }

    @Override
    public void setUserCode(List<CreateCodeBean.UserCodeBean> codeBeans) {
        XGUtil.hideSoftKeyboard(this);
        ToastUtil.showToastLong("创建成功");
        createCodeAdapter.addData(codeBeans);
    }

    CreateCodeAdapter createCodeAdapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("生成激活码");
        setTitleNameColor(R.color.black);
        setToolBarRightView("使用记录", R.color.net_resource_item_tv);

        createCodeAdapter = new CreateCodeAdapter();
        createCodeAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(createCodeAdapter);

        LoadingUtil.init(this);
        present.getAgentInfo();
    }

    @Override
    public void setPresenter(AgentCodeContract.Present presenter) {
        present = (AgentCodePresent) presenter;
    }

    private String[] sexArry;

    private void showSexChooseDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                layout_agent_create_code_code_tv.setText(sexArry[which]);
//                changesex_textview.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder3.show();// 让弹出框显示
    }


//    private void hidSoft() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }

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
