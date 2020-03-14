package com.water.melon.ui.me.agent.myagent.setagent;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AddAgentPresent extends BasePresenterParent implements AddAgentContract.Present {
    public static final String TAG = "AddAgentPresent";
    private AddAgentContract.View mView;

    public AddAgentPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AddAgentContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getAgentInfo(String id) {
        ApiImp.getInstance().getMYAgentInfo(id, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAgentInfo.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    MyAgentBean agentBean = (MyAgentBean) GsonUtil.toClass(result, MyAgentBean.class);
                    mView.setAgentDate(agentBean);
                }

            }
        });

    }

    @Override
    public void addAgent(MyAgentBean request) {
        ApiImp.getInstance().addAgent(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "addAgent.getResult() = " + data.getResult());
                mView.addSucc();

            }
        });

    }
}
