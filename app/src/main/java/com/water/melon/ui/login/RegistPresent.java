package com.water.melon.ui.login;

import com.google.gson.Gson;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.NetConstant;
import com.water.melon.net.bean.UserBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

public class RegistPresent extends BasePresenterParent implements RegistContract.Present {
    public static final String TAG = "RegistPresent";

    private RegistContract.View mView;

    public RegistPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (RegistContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void doRegist(String phone, String password) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().regist(phone, password, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.regsit(false);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "login.getResult() = " + data.getResult());
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_USER_INFO, data.getResult());
                UserBean userBean = (UserBean) GsonUtil.toClass(data.getResult(), UserBean.class);
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.XG_USER_ID, userBean.getUser_id());
                mView.regsit(true);

            }
        });

    }


    @Override
    public void start() {
        mView.initView();
    }
}
