package com.water.melon.presenter;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.UserBean;
import com.water.melon.presenter.contract.MeContract;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.update.CheckAppVersionUtil;

public class MePresenter extends BasePresenterParent implements MeContract.Presenter {
    public static final String TAG = "MePresenter";

    private MeContract.View mView;

    public MePresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (MeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void checkAppVersion() {
        CheckAppVersionUtil.checkApp(true, new CheckAppVersionUtil.CheckAppVersionListen() {
            @Override
            public void onCheckStart() {
            }

            @Override
            public void hasUpdate(boolean has, String msg) {
                mView.checkAppVersion(has, msg);
            }
        });
    }

    @Override
    public void getUserInfo() {
        ApiImp.getInstance().getUserNo(null, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {
                LogUtil.e(TAG, "getUserInfo er:" + error.toString());
            }

            @Override
            public void onNext(BaseApiResultData listBaseApiResultData) {
                String result = listBaseApiResultData.getResult();
                LogUtil.e(TAG, "getUserInfo :" + result);
                if (result != null && !result.trim().equals("") && !result.trim().equals("[]")) {
//                    InitResultBean bean = (InitResultBean) GsonUtil.toClass(result, InitResultBean.class);
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_USER_INFO, result);

                    UserBean userBean = (UserBean) GsonUtil.toClass(result, UserBean.class);
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_WEIXIN, userBean.getWx());
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_QQ, userBean.getQq());
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_WATER_HELP, userBean.getHelp());

                    mView.setBaseDate();

                }

            }
        });

    }

}
