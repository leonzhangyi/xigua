package com.water.melon.ui.home.h5;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

public class H5VideoPresent extends BasePresenterParent implements H5VideoConstract.Present {
    public static final String TAG = "H5VideoPresent";
    private H5VideoConstract.View mView;

    public H5VideoPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (H5VideoConstract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void getRoads() {
        ApiImp.getInstance().getRows(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                ToastUtil.showToastShort(error.getErr());
//                mView.subSucc(false);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getRoads.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.trim().equals("") && !result.trim().equals("[]")) {
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_ALL_ROADS, result);
                }

            }
        });

    }

    @Override
    public void start() {
        mView.initView();
    }
}
