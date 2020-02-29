package com.water.melon.presenter;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.NetResourceRequest;
import com.water.melon.net.bean.TabBean;
import com.water.melon.presenter.contract.NetResourceContract;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class NetResourcePresent extends BasePresenterParent implements NetResourceContract.Present {
    private NetResourceContract.View mView;

    public NetResourcePresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (NetResourceContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }


    @Override
    public void getBigTab() {
        String bigTabLoca = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Big_Tab, "");
        if (!TextUtils.isEmpty(bigTabLoca)) {
            List<TabBean> tabBeans = GsonUtil.toClass(bigTabLoca, new TypeToken<List<TabBean>>() {
            }.getType());
            mView.setBigTab(tabBeans);
        } else {
            mView.showLoadingDialog(true);
        }

        NetResourceRequest request = new NetResourceRequest();
        request.setPage(1);
        request.setLimit(10);
        ApiImp.getInstance().getNetResourceBigTab(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData<List<TabBean>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getMessage());
            }

            @Override
            public void onNext(BaseApiResultData<List<TabBean>> data) {
                String bigTag = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Big_Tab, "");
                if (TextUtils.isEmpty(bigTag)) {
                    mView.setBigTab(data.getData());
                }
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Big_Tab, data.getData().toString());
            }
        });

    }

    @Override
    public void getSmallTab(List<TabBean.Sub> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mView.setSmallTab(data);
    }
}
