package com.water.melon.ui.me.agent.myagent;

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
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MyAgentPresent extends BasePresenterParent implements MyAgentContract.Present {
    public static final String TAG = "MyAgentPresent";
    private MyAgentContract.View mView;

    public MyAgentPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MyAgentContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    BaseRequest request = null;

    @Override
    public void getAgentData(int page) {
        if (request == null) {
            request = new BaseRequest();
            request.setRows(20);
        }
        request.setPage(page);


        ApiImp.getInstance().getMygent(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.setAgentDate(null, true);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAgentData.getResult() = " + data.getResult());
                String result = data.getResult();
                List<MyAgentBean> advBeans = new ArrayList<>();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    MyAgentBean.MyAgent myAgent = (MyAgentBean.MyAgent) GsonUtil.toClass(result, MyAgentBean.MyAgent.class);
                    if (myAgent != null && myAgent.getList() != null) {
                        advBeans = myAgent.getList();
                    }

                }
                mView.setAgentDate(advBeans, false);
            }
        });

    }
}
