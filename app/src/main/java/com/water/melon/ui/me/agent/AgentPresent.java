package com.water.melon.ui.me.agent;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.ui.me.vip.VipBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

public class AgentPresent extends BasePresenterParent implements AgentContract.Present {
    public static final String TAG = "AgentPresent";
    private AgentContract.View mView;

    public AgentPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getAgentInfo() {
        ApiImp.getInstance().getAgentInfo(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAgentInfo.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    AgentBean vipBean = (AgentBean) GsonUtil.toClass(result, AgentBean.class);
                    mView.setAgentInfo(vipBean);
                }
            }
        });

    }

    @Override
    public void applyAgent(AgentBean agentBean) {
        BaseRequest request = new BaseRequest();
        request.setParameter(agentBean);

        ApiImp.getInstance().getApplyAgent(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getApplyAgent.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    VipBean vipBean = (VipBean) GsonUtil.toClass(result, VipBean.class);
//                    mView.setPayMethod(vipBean);
                }
            }
        });

    }
}
