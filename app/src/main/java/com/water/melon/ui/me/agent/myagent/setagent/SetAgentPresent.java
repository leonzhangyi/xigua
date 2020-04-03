package com.water.melon.ui.me.agent.myagent.setagent;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

public class SetAgentPresent extends BasePresenterParent implements SetAgentContract.Present {
    public static final String TAG = "SetAgentPresent";
    private SetAgentContract.View mView;

    public SetAgentPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (SetAgentContract.View) mBaseView;
        mView.setPresenter(this);
    }


    @Override
    public void getAgentData(BaseRequest<AgentUserBean> baseRequest) {
        mView.showLoadingDialog(true);

        ApiImp.getInstance().getAgentUser(baseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.setAgentDate(null, true);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAgentUser.getResult() = " + data.getResult());
                AgentUserBean codeBean = null;
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    codeBean = (AgentUserBean) GsonUtil.toClass(result, AgentUserBean.class);
                }
                if (codeBean == null) {
                    codeBean = new AgentUserBean();
                }
                mView.setAgentDate(codeBean.getList(), false);
            }
        });

    }

    @Override
    public void setAgent(AgentUserBean.UserInfo userInfo) {

    }

    @Override
    public void start() {
        mView.initView();
    }
}
