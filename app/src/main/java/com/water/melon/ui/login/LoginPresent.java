package com.water.melon.ui.login;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.UserBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

public class LoginPresent extends BasePresenterParent implements LoginContract.Present {
    public static final String TAG = "LoginPresent";

    private LoginContract.View mView;


    public LoginPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (LoginContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void login(String phone, String password) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().login(phone, password, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.login(false);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "login.getResult() = " + data.getResult());
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_USER_INFO, data.getResult());
                UserBean userBean = (UserBean) GsonUtil.toClass(data.getResult(), UserBean.class);
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.XG_USER_ID, userBean.getUser_id());
                mView.login(true);
            }
        });

    }

}
