package com.water.melon.ui.me.agent.myuser;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AddVipBean;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.net.bean.TabBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class AgentUserPresent extends BasePresenterParent implements AgentUserContract.Present {
    public static final String TAG = "AgentUserPresent";

    private AgentUserContract.View mView;

    public AgentUserPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentUserContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getAgentUser(BaseRequest<AgentUserBean> baseRequest) {

        ApiImp.getInstance().getAgentUser(baseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.setAgentUser(null, true);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAgentUser.getResult() = " + data.getResult());
                AgentUserBean codeBean = null;
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    codeBean = (AgentUserBean) GsonUtil.toClass(result, AgentUserBean.class);
                }
                mView.setAgentUser(codeBean, false);
            }
        });

    }

    @Override
    public void addView(AddVipBean addVipBean) {
        ApiImp.getInstance().addVip(addVipBean, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "addVip.getResult() = " + data.getResult());
//                mView.addSucc();
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    if (addVipBean.getHandle().equals("before")) {
                        List<MyAgentBean.Vips> vips = GsonUtil.toClass(result, new TypeToken<List<MyAgentBean.Vips>>() {
                        }.getType());
                        mView.setVips(vips);
                    } else {
                        ToastUtil.showToastShort(data.getSuc());
                    }
                }

            }
        });

    }
}
