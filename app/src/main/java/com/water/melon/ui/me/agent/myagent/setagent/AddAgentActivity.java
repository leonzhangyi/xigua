package com.water.melon.ui.me.agent.myagent.setagent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AddAgentActivity extends BaseActivity implements AddAgentContract.View {
    @BindView(R.id.add_agent_name_et)
    EditText add_agent_name_et;
    @BindView(R.id.add_agent_phone_et)
    EditText add_agent_phone_et;
    @BindView(R.id.add_agent_price_et)
    EditText add_agent_price_et;

    @BindView(R.id.add_agent_sure_tv)
    TextView add_agent_sure_tv;


    private AddAgentPresent present;

    public static final String LOOK_AGENT = "1";
    public static final String ADD_AGENT = "2";

    private String state = "1";
    private String phone = "";
    private String agentId = "";

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_add_agent;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        state = getIntent().getStringExtra(XGUtil.AGENT_STATE);
        phone = getIntent().getStringExtra(XGUtil.AGENT_PHONE);
        agentId = getIntent().getStringExtra(XGUtil.AGENT_ID);


        new AddAgentPresent(this, this);
        present.start();


    }

    @Override
    protected void onClickTitleBack() {

    }

    @OnClick({R.id.toolbar_left_tv, R.id.add_agent_sure_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;

            case R.id.add_agent_sure_tv:
                String name = add_agent_name_et.getText().toString();
                String phone = add_agent_phone_et.getText().toString();
                String price = add_agent_price_et.getText().toString();
                if (name == null || name.trim().equals("")) {
                    ToastUtil.showToastLong("请输入代理人");
                    break;
                }
                if (phone == null || !XGUtil.isMobileNO(phone.trim())) {
                    ToastUtil.showToastLong("请输入正确的手机号");
                    break;
                }
                if (price == null || price.trim().equals("")) {
                    ToastUtil.showToastLong("请设置代理分成比例");
                    break;
                }
                MyAgentBean request = new MyAgentBean();
                request.setId(agentId);
                request.setBuckle_u(price);
                request.setContacts(name);
                request.setTel(phone);
                present.addAgent(request);
                break;
        }
    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("设置代理");
        setTitleNameColor(R.color.black);
        if (state == LOOK_AGENT) {
            //TODO拉取用户信息
            add_agent_sure_tv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
            add_agent_sure_tv.setBackgroundColor(R.drawable.layout_agent_pay_back_1);
            add_agent_sure_tv.setClickable(false);

            add_agent_name_et.setEnabled(false);
            add_agent_phone_et.setEnabled(false);
            add_agent_price_et.setEnabled(false);
            present.getAgentInfo(agentId);
        } else {
            add_agent_phone_et.setText(phone);

            add_agent_sure_tv.setTextColor(MyApplication.getColorByResId(R.color.white));
            add_agent_sure_tv.setBackgroundColor(R.drawable.layout_agent_pay_back);
            add_agent_name_et.setEnabled(true);
            add_agent_phone_et.setEnabled(true);
            add_agent_price_et.setEnabled(true);
        }


    }

    @Override
    public void setPresenter(AddAgentContract.Present presenter) {
        present = (AddAgentPresent) presenter;
    }

    @Override
    public void setAgentDate(MyAgentBean agentBean) {
        if (agentBean != null) {
            add_agent_name_et.setText(agentBean.getContacts());
            add_agent_phone_et.setText(agentBean.getTel());
            add_agent_price_et.setText(agentBean.getBuckle_u());
        }
    }

    @Override
    public void addSucc() {
        ToastUtil.showToastLong("恭喜您操作成功！");
        add_agent_sure_tv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
        add_agent_sure_tv.setBackgroundColor(R.drawable.layout_agent_pay_back_1);
        add_agent_sure_tv.setClickable(false);

        add_agent_name_et.setEnabled(false);
        add_agent_phone_et.setEnabled(false);
        add_agent_price_et.setEnabled(false);

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
