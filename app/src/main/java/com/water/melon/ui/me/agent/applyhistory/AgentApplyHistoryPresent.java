package com.water.melon.ui.me.agent.applyhistory;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.ui.me.vip.VipBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class AgentApplyHistoryPresent extends BasePresenterParent implements AgentApplyHistoryContract.Present {
    public static final String TAG = "AgentApplyHistoryPresent";
    private AgentApplyHistoryContract.View mView;

    public AgentApplyHistoryPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentApplyHistoryContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getApplyList() {
        mView.showLoadingDialog(true);
        ApiImp.getInstance().getApplyHistory(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "getApplyHistory.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AgentBean> vipBean = GsonUtil.toClass(result, new TypeToken<List<AgentBean>>() {
                    }.getType());
                    mView.setDate(vipBean);
                }
            }
        });

    }
}
