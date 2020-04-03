package com.water.melon.ui.me.agent.myuser.total;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.TotalBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class MyUserTotalPresent extends BasePresenterParent implements MyUserTotalContract.Present {
    public static final String TAG = "MyUserTotalPresent";
    private MyUserTotalContract.View mView;

    public MyUserTotalPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MyUserTotalContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getTotalUser(BaseRequest<AgentUserBean> baseRequest) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().getTotalUser(baseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.setAgentUser(null, true);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getTotalUser.getResult() = " + data.getResult());
                List<TotalBean> codeBean = null;
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    codeBean = GsonUtil.toClass(result, new TypeToken<List<TotalBean>>() {
                    }.getType());
                }
                mView.setAgentUser(codeBean, false);
            }
        });

    }
}
