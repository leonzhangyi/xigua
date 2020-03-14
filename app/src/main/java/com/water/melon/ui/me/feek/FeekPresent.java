package com.water.melon.ui.me.feek;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.FeedBean;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

public class FeekPresent extends BasePresenterParent implements FeekContract.Present {
    public static final String TAG = "FeekPresent";
    private FeekContract.View mView;

    public FeekPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (FeekContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void subMessage(String msg) {
        FeedBean request = new FeedBean();
        request.setContent(msg);
        ApiImp.getInstance().addFeedback(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
                mView.subSucc(false);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "subMessage.getResult() = " + data.getResult());
                mView.subSucc(true);

            }
        });
    }
}
