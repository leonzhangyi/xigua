package com.water.melon.ui.me.agent.mymoney.submit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.me.agent.mymoney.history.MyMoneyHistory;
import com.water.melon.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MyMoneySubmitAcivity extends BaseActivity implements MyMoneySubmitContract.View {

    private MyMoneySubmitPresent present;

    @BindView(R.id.money_sub_number)
    TextView money_sub_number;
    @BindView(R.id.money_sub_name)
    TextView money_sub_name;
    @BindView(R.id.money_sub_mobile)
    TextView money_sub_mobile;
    @BindView(R.id.money_sub_code)
    TextView money_sub_code;

    @BindView(R.id.sub_money)
    TextView sub_money;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_my_money_sbumit;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new MyMoneySubmitPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    String price = "0.00";

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("申请提现");
        setTitleNameColor(R.color.black);
        setToolBarRightView("提现记录", R.color.net_resource_item_tv);

        price = getIntent().getStringExtra("myCurrentMoeny");
        sub_money.setText("¥" + price);
    }

    @OnClick({R.id.toolbar_left_tv, R.id.my_money_sub, R.id.toolbar_right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                finish();
                break;

            case R.id.my_money_sub:
                if (price.trim().equals("0.00")) {
                    ToastUtil.showToastShort("无可提现金额");
                } else {
                    String number = money_sub_number.getText().toString();
                    String name = money_sub_name.getText().toString();
                    String mobile = money_sub_mobile.getText().toString();
                    String code = money_sub_code.getText().toString();
                    if (number != null && !number.trim().equals("")) {
                        if (name != null && !name.trim().equals("")) {
                            if (mobile != null && !mobile.trim().equals("")) {
                                if (code != null && !code.trim().equals("")) {
                                    present.submit(number, price, code);
                                } else {
                                    ToastUtil.showToastShort("请输入验证码");
                                }
                            } else {
                                ToastUtil.showToastShort("请输入手机号");
                            }
                        } else {
                            ToastUtil.showToastShort("请输入姓名");
                        }
                    } else {
                        ToastUtil.showToastShort("请输入提现账号");
                    }

                }
                break;

            case R.id.toolbar_right_tv:
                redirectActivity(MyMoneyHistory.class);
                break;
        }
    }

    @Override
    public void setPresenter(MyMoneySubmitContract.Present presenter) {
        present = (MyMoneySubmitPresent) presenter;
    }

    @Override
    public void subSuc() {
        sub_money.setText("￥0.00");
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
