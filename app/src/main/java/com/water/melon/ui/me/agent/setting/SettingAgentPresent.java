package com.water.melon.ui.me.agent.setting;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.ui.me.agent.AgentContract;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

public class SettingAgentPresent extends BasePresenterParent implements SettingAgentContract.Present {
    public static final String TAG = "SettingAgentPresent";
    private SettingAgentContract.View mView;

    public SettingAgentPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (SettingAgentContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void setDate(MyAgentBean request) {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().doAgentSet(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "addAgent.getResult() = " + data.getResult());
//                mView.addSucc();
                String result = data.getResult();

                if (request.getHandle().equals("afeter")) {
                    ToastUtil.showToastShort("操作成功");
                    mView.setSucDate(request.isDef());
                } else {
                    if (result != null && !result.equals("[]")) {
                        MyAgentBean myAgentBean = (MyAgentBean) GsonUtil.toClass(result, MyAgentBean.class);
                        mView.setDate(myAgentBean);

                    }
                }
            }
        });
    }

}
