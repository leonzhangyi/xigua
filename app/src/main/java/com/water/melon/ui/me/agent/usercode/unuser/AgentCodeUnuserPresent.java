package com.water.melon.ui.me.agent.usercode.unuser;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class AgentCodeUnuserPresent extends BasePresenterParent implements AgentCodeUnuserContract.Present {
    public static final String TAG = "AgentCodeUnuserPresent";
    private AgentCodeUnuserContract.View mView;

    public AgentCodeUnuserPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentCodeUnuserContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getDate(AgentCodeHisBean agentCodeHisBean) {
        ApiImp.getInstance().getCodeList(agentCodeHisBean, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
                mView.setDate(null, true);

            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, agentCodeHisBean.getHandle());
                LogUtil.e(TAG, "getCodeList.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("")) {
                    AgentCodeHisBean Types = (AgentCodeHisBean) GsonUtil.toClass(result, AgentCodeHisBean.class);
                    mView.setDate(Types, false);
                } else {
                    mView.setDate(null, false);
                }

            }
        });

    }

    @Override
    public void getCodeList(AgentCodeHisBean agentCodeHisBean1) {
        ApiImp.getInstance().getCodeList(agentCodeHisBean1, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
//                if (!agentCodeHisBean.getHandle().equals("before")) {
//                    mView.setDate(null, true);
//                }

            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getCodeList1.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("")) {
                        List<AgentCodeHisBean.Types> Types = GsonUtil.toClass(result, new TypeToken<List<AgentCodeHisBean.Types>>() {
                        }.getType());
                        mView.setType(Types);
                }

            }
        });

    }
}