package com.water.melon.ui.me.agent.mymoney.history;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.MyMoneyBean;
import com.water.melon.net.bean.TotalBean;
import com.water.melon.net.bean.VideoHistoryBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class MyMoneyHistoryPresent extends BasePresenterParent implements MyMoneyHistoryContract.Present {
    public static final String TAG = "MyMoneyHistoryPresent";
    private MyMoneyHistoryContract.View mView;

    public MyMoneyHistoryPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MyMoneyHistoryContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getHistoryData(MyMoneyBean request) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().getMoneyHistory(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                List<MyMoneyBean.MyMoneyHistory> codeBean = null;
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    codeBean = GsonUtil.toClass(result, new TypeToken<List<MyMoneyBean.MyMoneyHistory>>() {
                    }.getType());
                }
                mView.setUserDate(codeBean, false);
                mView.showLoadingDialog(false);

            }
        });

    }
}
