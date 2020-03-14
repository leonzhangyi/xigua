package com.water.melon.ui.me.agent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.ui.me.agent.create.CreateCodeActivity;
import com.water.melon.ui.me.agent.myagent.MyAgentActivity;
import com.water.melon.ui.me.agent.myuser.AgentUserActivity;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AgentActivity extends BaseActivity implements AgentContract.View {
    private AgentPresent mPresent;

    @BindView(R.id.agent_people)
    TextView agent_people;

    @BindView(R.id.agent_user_people_tv)
    TextView agent_user_people_tv;

    @BindView(R.id.agent_more_money_tv)
    TextView agent_more_money_tv;

    @BindView(R.id.agent_payment)
    TextView agent_payment;
    @BindView(R.id.agent_price)
    TextView agent_price;

    @BindView(R.id.layout_agent_phone_et)
    EditText layout_agent_phone_et;

    @BindView(R.id.layout_agent_name_et)
    EditText layout_agent_name_et;


    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_agent;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new AgentPresent(this, this);
        mPresent.start();
    }

    private AgentBean agentBean;

    @OnClick({R.id.toolbar_left_tv, R.id.agent_payment, R.id.layout_me_page_rl, R.id.layout_me_agent_rl, R.id.layout_me_user_rl, R.id.layout_me_price_rl})
    public void onClick(View view) {
        UserBean userBean = XGUtil.getMyUserInfo();
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                onClickTitleBack();
                break;
            case R.id.agent_payment:
                String name = layout_agent_name_et.getText().toString();
                String phone = layout_agent_phone_et.getText().toString();
                if (name != null && !name.trim().equals("") && phone != null && !phone.trim().equals("")) {
                    if (phone != null && XGUtil.isMobileNO(phone)) {
                        if (agentBean == null) {
                            agentBean = new AgentBean();
                        }
                        agentBean.setContacts(name);
                        agentBean.setTel(phone);
                        mPresent.applyAgent(agentBean);
                    } else {
                        ToastUtil.showToastLong("请输入正确的手机号码");
                    }
                } else {
                    ToastUtil.showToastLong("请输入姓名和联系方式");
                }

                break;

            case R.id.layout_me_page_rl://生成激活码
                if(userBean.getGroup_id().trim().equals("1")) {
                    ToastUtil.showToastShort("成为代理，方可使用此功能");
                }else{
                    redirectActivity(CreateCodeActivity.class);
                }

                break;

            case R.id.layout_me_agent_rl://我的代理
                if(userBean.getGroup_id().trim().equals("3")) {
                    redirectActivity(MyAgentActivity.class);
                }else{
                    ToastUtil.showToastShort("成为总代理，方可使用此功能");
                }
                break;
            case R.id.layout_me_user_rl://我的用户
                if(userBean.getGroup_id().trim().equals("1")) {
                    ToastUtil.showToastShort("成为代理，方可使用此功能");
                }else{
                    redirectActivity(AgentUserActivity.class);
                }

                break;
            case R.id.layout_me_price_rl://我的收益
                if(userBean.getGroup_id().trim().equals("1")) {
                    ToastUtil.showToastShort("成为代理，方可使用此功能");
                }else{
//                    redirectActivity(AgentUserActivity.class);
                }
                break;
        }
    }

    @Override
    protected void onClickTitleBack() {
        this.finish();
    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("代理专区");
        setTitleNameColor(R.color.black);

        mPresent.getAgentInfo();
    }

    @Override
    public void setPresenter(AgentContract.Present presenter) {
        this.mPresent = (AgentPresent) presenter;
    }

    @Override
    public void setAgentInfo(AgentBean agentInfo) {
        if (agentInfo != null) {
            agentBean = agentInfo;
            agent_people.setText("代理" + agentInfo.getProxy_nums() + "人");
            agent_user_people_tv.setText("累计用户" + agentInfo.getUser_nums() + "人");
            agent_more_money_tv.setText("当前收益" + agentInfo.getPrice() + "元");
        }


        String baseInfo = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        if (baseInfo != null && !baseInfo.trim().equals("") && !baseInfo.trim().equals("[]")) {
            UserBean userBean = (UserBean) GsonUtil.toClass(baseInfo, UserBean.class);
            if (userBean != null) {
                int groupID = Integer.parseInt(userBean.getGroup_id().trim());
                setVipDate(groupID, agentInfo);
            }
        }
    }


    private void setVipDate(int groupID, AgentBean agentInfo) {
        switch (groupID) {
            case 0: //TODO 0的时候 不让进来
                if (agentInfo.getStatus().trim().equals("0")) {
                    agent_payment.setText("支付¥" + agentInfo.getPrice() + "并申请");
                    agent_price.setText("代理费用" + agentInfo.getPrice());
                    agent_payment.setClickable(true);
                    layout_agent_phone_et.setText(agentInfo.getTel());
                } else if (agentInfo.getStatus().trim().equals("2")) {
                    agent_payment.setText("申请审核中");
                    agent_payment.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_agent_pay_back_1));
                    agent_payment.setClickable(false);
                    agent_price.setText("代理费用" + agentInfo.getPrice());
                    layout_agent_phone_et.setText(agentInfo.getTel());
                    layout_agent_name_et.setText(agentInfo.getContacts());
                }
                break;
            case 1:
                if (agentInfo.getStatus().trim().equals("0")) {
                    agent_payment.setText("支付¥" + agentInfo.getPrice() + "并申请");
                    agent_price.setText("代理费用" + agentInfo.getPrice());
                    agent_payment.setClickable(true);
                    layout_agent_phone_et.setText(agentInfo.getTel());
                } else if (agentInfo.getStatus().trim().equals("2")) {
                    agent_payment.setText("申请审核中");
                    agent_payment.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_agent_pay_back_1));
                    agent_payment.setClickable(false);
                    agent_price.setText("代理费用" + agentInfo.getPrice());
                    layout_agent_phone_et.setText(agentInfo.getTel());
                    layout_agent_name_et.setText(agentInfo.getContacts());
                }
                break;
            case 2:
                agent_payment.setText("已成为代理");
                agent_payment.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_agent_pay_back_1));
                agent_payment.setClickable(false);
                agent_price.setText("代理费用" + agentInfo.getPrice());
                layout_agent_phone_et.setText(agentInfo.getTel());
                layout_agent_name_et.setText(agentInfo.getContacts());

                break;
            case 3:
                agent_payment.setText("已成为代理");
                agent_payment.setBackground(MyApplication.getDrawableByResId(R.drawable.layout_agent_pay_back_1));
                agent_payment.setClickable(false);
                agent_price.setText("代理费用" + agentInfo.getPrice());
                layout_agent_phone_et.setText(agentInfo.getTel());
                layout_agent_name_et.setText(agentInfo.getContacts());
                break;
        }
    }
}