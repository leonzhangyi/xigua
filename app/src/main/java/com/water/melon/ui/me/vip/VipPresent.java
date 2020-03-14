package com.water.melon.ui.me.vip;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.net.bean.TabBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import java.util.List;

public class VipPresent extends BasePresenterParent implements VipContract.Present {
    public static final String TAG = "VipPresent";
    private VipContract.View mView;

    public VipPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (VipContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getVipDate() {
        ApiImp.getInstance().getVIP(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getVIP.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<VipBean> tabBeans = GsonUtil.toClass(result, new TypeToken<List<VipBean>>() {
                    }.getType());

                    mView.setVipDate(tabBeans);

                }
            }
        });


    }

    @Override
    public void doPay(VipBean item) {
        BaseRequest<VipBean> vipBeanBaseRequest = new BaseRequest<>();
        vipBeanBaseRequest.setParameter(item);
        ApiImp.getInstance().getDoPay(vipBeanBaseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "doPay.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    VipBean vipBean = (VipBean) GsonUtil.toClass(result, VipBean.class);
                    mView.setPayMethod(vipBean);
                }
            }
        });

    }

    @Override
    public void doBdVip(CreateCodeBean.UserCodeBean userCodeBean) {
        BaseRequest<CreateCodeBean.UserCodeBean> vipBeanBaseRequest = new BaseRequest<>();
        vipBeanBaseRequest.setParameter(userCodeBean);

        ApiImp.getInstance().getBdCode(vipBeanBaseRequest, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "doPay.getResult() = " + data.getResult());
                if (data.getErr() == null || data.getErr().trim().equals("")) {
                    ToastUtil.showToastLong(data.getSuc());

                    updataUserInfo();
                }
            }
        });

    }

    public void updataUserInfo() {
        ApiImp.getInstance().getUserNo(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                    mView.updata();
                }

            }
        });
    }
}
