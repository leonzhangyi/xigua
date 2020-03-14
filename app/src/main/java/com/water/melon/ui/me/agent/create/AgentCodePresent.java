package com.water.melon.ui.me.agent.create;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.ui.me.vip.VipBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class AgentCodePresent extends BasePresenterParent implements AgentCodeContract.Present {
    public static final String TAG = "AgentCodePresent";

    private AgentCodeContract.View mView;

    public AgentCodePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentCodeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void getAgentInfo() {
        ApiImp.getInstance().getCreateCode(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                    List<CreateCodeBean.CodeBean> codeBean = GsonUtil.toClass(result, new TypeToken<List<CreateCodeBean.CodeBean>>() {
                    }.getType());
                    mView.setAgentInfo(codeBean);
                }
            }
        });

    }

    @Override
    public void createCode(CreateCodeBean codeBean) {
        BaseRequest request = new BaseRequest();
        request.setParameter(codeBean);
        ApiImp.getInstance().getCreateCode(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                    List<CreateCodeBean.UserCodeBean> codeBean = GsonUtil.toClass(result, new TypeToken<List<CreateCodeBean.UserCodeBean>>() {
                    }.getType());
                    mView.setUserCode(codeBean);
                }
            }
        });

    }

    @Override
    public void start() {
        mView.initView();
    }
}
