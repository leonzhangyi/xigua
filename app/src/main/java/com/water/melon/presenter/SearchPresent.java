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
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.presenter.contract.SearchContract;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchPresent extends BasePresenterParent implements SearchContract.Present {
    public static final String TAG = "SearchPresent";
    private SearchContract.View mView;
    private int page = 1;
    public int limit = 15;
    private boolean isFirstData = true;

    public SearchPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (SearchContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getListData(GetVideosRequest request) {
        request.setPage(page);
        request.setLimit(limit);
        ApiImp.getInstance().getNetVideoList(request, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastLong(error.getErr());
                mView.getListData(new ArrayList<>(), true, false);
            }

            @Override
            public void onNext(BaseApiResultData result) {
                LogUtil.e(TAG, "getListData.getResult() = " + result.getResult());
                String data = result.getResult();
                if (data != null && !data.equals("") && !data.equals("[]")) {
                    List<NetResoutVideoInfo> tabBeans = GsonUtil.toClass(data, new TypeToken<List<NetResoutVideoInfo>>() {
                    }.getType());
                    if (isFirstData && null != tabBeans && tabBeans.size() > 0) {
                        isFirstData = false;
                        mView.getListData(tabBeans, false, true);
                    } else {
                        mView.getListData(tabBeans, false, false);
                        LogUtil.e("ssss", "setVlue====");
                    }
                    page++;
                } else {
                    if (isFirstData) {
                        mView.getListData(new ArrayList<>(), false, true);
                    }else{
                        mView.getListData(new ArrayList<>(), true, false);
                    }

                }
            }
        });


    }

    @Override
    public void setPage() {
        page = 1;
        isFirstData = true;
    }
}
