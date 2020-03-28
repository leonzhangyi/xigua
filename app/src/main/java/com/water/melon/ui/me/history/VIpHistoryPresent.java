package com.water.melon.ui.me.history;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.VideoHistoryBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;

import java.util.List;

public class VIpHistoryPresent extends BasePresenterParent implements VIpHistoryContract.Present {
    public static final String TAG = "VIpHistoryPresent";
    private VIpHistoryContract.View mView;

    public VIpHistoryPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (VIpHistoryContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    BaseRequest request;

    @Override
    public void getDate(int page) {
        if(page == 1) {
            mView.showLoadingDialog(true);
        }
        if (request == null) {
            request = new BaseRequest();
            request.setRows(20);
        }
        request.setPage(page);
        ApiImp.getInstance().getHistoryDate(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "getHistoryDate.getResult() = " + data.getResult());
                String result = data.getResult();
                List<VideoHistoryBean> videoHistoryBeans = null;
                if (result != null && !result.trim().equals("") && !result.trim().equals("[]")) {
                    videoHistoryBeans = GsonUtil.toClass(result, new TypeToken<List<VideoHistoryBean>>() {
                    }.getType());
                }
                mView.setDate(videoHistoryBeans, false);
            }
        });

    }
}
