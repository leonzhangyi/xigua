package com.water.melon.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements RegistContract.View {

    @BindView(R.id.layout_login_et)
    EditText layout_login_et;

    @BindView(R.id.layout_password_et)
    EditText layout_password_et;

    @BindView(R.id.layout_password_sure_et)
    EditText layout_password_sure_et;


    private RegistPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.layout_regist;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new RegistPresent(this, this);
        present.start();
    }

    String phone;
    String password;

    @OnClick({R.id.login_back, R.id.login_submit, R.id.layout_login_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                this.finish();
                break;

            case R.id.login_submit:
                phone = layout_login_et.getText().toString();
                password = layout_password_et.getText().toString();
                String rePassword = layout_password_sure_et.getText().toString();

                if (phone != null && XGUtil.isMobileNO(phone)) {
                    if (password != null && !password.trim().equals("")) {
                        if (rePassword != null && !rePassword.trim().equals("")) {
                            if (rePassword.equals(password)) {
                                present.doRegist(phone, password);
                            } else {
                                ToastUtil.showToastShort("两次密码不一致");
                            }

                        } else {
                            ToastUtil.showToastShort("请再次输入密码");
                        }
                    } else {
                        ToastUtil.showToastShort("请输入密码");
                    }
                } else {
                    ToastUtil.showToastShort("请输入正确的手机号");
                }

                break;
            case R.id.layout_login_regist:
                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
                redirectActivityForResult(intent, 2);
                break;
        }

    }


    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void regsit(boolean isSuc) {
        showLoadingDialog(false);
        if (isSuc) {
            // TODO 更新个人资料,跳转到个人中心页面
            ToastUtil.showToastShort("绑定成功");
            setResult(1001);
            this.finish();
        }
    }


    @Override
    public void initView() {

    }

    @Override
    public void setPresenter(RegistContract.Present presenter) {
        present = (RegistPresent) presenter;
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
}
