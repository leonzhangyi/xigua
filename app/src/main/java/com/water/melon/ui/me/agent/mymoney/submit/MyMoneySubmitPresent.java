package com.water.melon.ui.me.agent.mymoney.submit;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

public class MyMoneySubmitPresent extends BasePresenterParent implements MyMoneySubmitContract.Present {
    public static final String TAG = "MyMoneySubmitPresent";
    private MyMoneySubmitContract.View mView;

    public MyMoneySubmitPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MyMoneySubmitContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void submit(String mobile, String money, String code) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().getSubMoney(mobile, money, code, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getSubMoney.getResult() = " + data.getResult());
                ToastUtil.showToastShort(data.getSuc());
                mView.subSuc();
            }
        });
    }
}
