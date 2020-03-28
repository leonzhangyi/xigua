package com.water.melon.ui.me.agent.mymoney;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.MyMoneyBean;
import com.water.melon.net.bean.TotalBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class MyMoneyPresent extends BasePresenterParent implements MyMoneyContract.Present {
    public static final String TAG = "MyMoneyPresent";
    private MyMoneyContract.View mView;

    public MyMoneyPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MyMoneyContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getMyMoney(BaseRequest<MyMoneyBean> baseRequest) {
        if (baseRequest.getParameter().getHandle().equals("after")) {
            mView.showLoadingDialog(true);
        }
        ApiImp.getInstance().getProfit(baseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.setUserDate(null, true);
                mView.showLoadingDialog(false);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getProfit.getResult() = " + data.getResult());
                List<TotalBean> codeBean = null;
                MyMoneyBean.MyUserMoney myUserMoney = null;
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    if (baseRequest.getParameter().getHandle().equals("before")) {
                        MyMoneyBean.BeforeBean beforeBean = (MyMoneyBean.BeforeBean) GsonUtil.toClass(result, MyMoneyBean.BeforeBean.class);
                        mView.setMoneyDate(beforeBean);
                    } else {
                        myUserMoney = (MyMoneyBean.MyUserMoney) GsonUtil.toClass(result, MyMoneyBean.MyUserMoney.class);

                    }
                }
                if (baseRequest.getParameter().getHandle().equals("after")) {
                    mView.setUserDate(myUserMoney, false);
                    mView.showLoadingDialog(false);
                }
            }
        });

    }
}
