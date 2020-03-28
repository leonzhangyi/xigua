package com.water.melon.ui.me.feek.history;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.FeedBean;
import com.water.melon.net.bean.VideoHistoryBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;

import java.util.List;

public class FeekHistoryPresent extends BasePresenterParent implements FeekHistoryContract.Present {
    public static final String TAG = "FeekHistoryPresent";
    private FeekHistoryContract.View mView;

    public FeekHistoryPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (FeekHistoryContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    BaseRequest request;

    @Override
    public void getDate(int page) {
        mView.showLoadingDialog(true);
        if (page == 1) {
            mView.showLoadingDialog(true);
        }
        if (request == null) {
            request = new BaseRequest();
            request.setRows(20);
        }
        request.setPage(page);
        ApiImp.getInstance().getFeekLsit(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                ToastUtil.showToastShort(error.getErr());
//                mView.subSucc(false);
                mView.setDate(null, true);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getFeekLsit.getResult() = " + data.getResult());
                String result = data.getResult();
                FeedBean feedBean = null;
                if (result != null && !result.trim().equals("") && !result.trim().equals("[]")) {
                    feedBean = (FeedBean) GsonUtil.toClass(result, FeedBean.class);
                }
                mView.setDate(feedBean, false);
            }
        });
    }
}
