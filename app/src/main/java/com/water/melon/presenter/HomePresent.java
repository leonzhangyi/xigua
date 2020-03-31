package com.water.melon.presenter;

import android.text.TextUtils;

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
import com.water.melon.presenter.contract.HomeContract;
import com.water.melon.ui.home.HomeBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HomePresent extends BasePresenterParent implements HomeContract.Present {
    public static final String TAG = "HomePresent";
    private HomeContract.View mView;

    public HomePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (HomeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getHomeBean() {
//        setHomeBeans();
//        mView.setHomeBean(homeBeans);
        mView.showLoadingDialog(true);
        BaseRequest request = new BaseRequest(1, 30);
        ApiImp.getInstance().getHomeVIP(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
                ToastUtil.showToastLong(error.getErr());
//                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getHomeVIP.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setHomeBean(advBeans);
                }
            }
        });
    }

    @Override
    public void getHomeAdv() {
        BaseRequest request = new BaseRequest(1, 10);
        ApiImp.getInstance().getHomeAdv(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
//                    ToastUtil.showToastLong(error.getErr());
//                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getHomeAdv.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setHomeAdv(advBeans);
                }
            }
        });

    }

    @Override
    public void getAppNotice() {
        BaseRequest request = new BaseRequest(1, 10);
        ApiImp.getInstance().getAppNotice(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
//                    ToastUtil.showToastLong(error.getErr());
//                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getAppNotice.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.getAppNotice(advBeans);
                }
            }
        });

    }


    private ArrayList<HomeBean> homeBeans = new ArrayList<>();

    private void setHomeBeans() {
        for (int i = 0; i < 12; i++) {
            HomeBean homeBean = new HomeBean();
            homeBean.setName("测试" + i);
            homeBean.setIcon("");
            homeBeans.add(homeBean);
        }
    }


    @Override
    public void doAdvClick(AdvBean advBean) {
        ApiImp.getInstance().doClickAdv(advBean, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
//                    ToastUtil.showToastLong(error.getErr());
//                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "doClickAdv.getResult() = " + data.getResult());
//                String result = data.getResult();
//                if (result != null && !result.equals("") && !result.equals("[]")) {
//                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
//                    }.getType());
//                    mView.setHomeAdv(advBeans);
//                }
            }
        });

    }

}
