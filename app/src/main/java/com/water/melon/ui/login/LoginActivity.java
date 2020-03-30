package com.water.melon.ui.login;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.layout_login_et)
    EditText layout_login_et;

    @BindView(R.id.layout_password_et)
    EditText layout_password_et;


    private LoginPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstan) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.layout_login;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new LoginPresent(this, this);
        present.start();
    }

    @OnClick({R.id.login_back, R.id.login_submit, R.id.layout_login_regist, R.id.layout_login_find})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                this.finish();
                break;

            case R.id.login_submit:
                String phone = layout_login_et.getText().toString();
                String password = layout_password_et.getText().toString();
                if (phone != null && XGUtil.isMobileNO(phone)) {
                    if (password != null && !password.trim().equals("")) {
                        present.login(phone, password);
                    } else {
                        ToastUtil.showToastShort("请输入密码");
                    }
                } else {
                    ToastUtil.showToastShort("请输入正确的手机号");
                }

                break;

            case R.id.layout_login_regist:
                Intent intent = new Intent(this, RegistActivity.class);
                redirectActivityForResult(intent, 2);
                break;

            case R.id.layout_login_find:
                ToastUtil.showToastLong("找回密码请联系上级处理");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("LoginActivity", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 1002) {
            setResult(1001);
            this.finish();
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setPresenter(LoginContract.Present presenter) {
        present = (LoginPresent) presenter;
    }

    @Override
    public void login(boolean isSuc) {
        showLoadingDialog(false);
        if (isSuc) {
            // TODO 更新个人资料,跳转到个人中心页面
            ToastUtil.showToastShort("登录成功");
//            setResult(1001);
            setResult(1002);
            this.finish();
        }
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .navigationBarColor(R.color.main_botton_bac)
//                .statusBarDarkFont(true)
//                .statusBarColor(R.color.main_botton_bac)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }
}
