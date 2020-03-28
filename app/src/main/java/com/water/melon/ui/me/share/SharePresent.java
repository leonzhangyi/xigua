package com.water.melon.ui.me.share;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.ShareBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

public class SharePresent extends BasePresenterParent implements ShareContract.Present {
    public static final String TAG = "AgentPresent";
    private ShareContract.View mView;

    public SharePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (ShareContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getShareDate() {
        ApiImp.getInstance().getShareDate(getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getShareDate.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.trim().equals("") && !result.trim().equals("[]")) {
                    ShareBean shareBean = (ShareBean) GsonUtil.toClass(result, ShareBean.class);
                    mView.setShareDate(shareBean);
                }
            }
        });

    }
}
