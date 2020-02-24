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
import com.water.melon.presenter.contract.NetResouceItemContract;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class NetResouceItemPresenter extends BasePresenterParent implements NetResouceItemContract.Presenter {
    private NetResouceItemContract.View mView;
    private boolean isFirstData;
    private int page = 1;
    public int limit = 20;

    public NetResouceItemPresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider, boolean isFirstData) {
        super(mBaseView, lifecycleProvider);
        this.mView = (NetResouceItemContract.View) mBaseView;
        this.isFirstData = isFirstData;
        mView.setPresenter(this);
    }

    @Override
    public void getListData(GetVideosRequest request) {
        if (isFirstData) {
            String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
            if (!TextUtils.isEmpty(localData)) {
                List<NetResoutVideoInfo> items = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
                }.getType());
                mView.getListData(items, false, false);
            }
        }
        request.setPage(page);
        request.setLimit(limit);
        ApiImp.getInstance().getNetResourceList(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData<List<NetResoutVideoInfo>>>() {
            @Override
            public void onCompleted() {
                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastLong(error.getMessage());
                mView.getListData(new ArrayList<>(), true, false);
            }

            @Override
            public void onNext(BaseApiResultData<List<NetResoutVideoInfo>> data) {
                if (isFirstData && null != data.getData() && data.getData().size() > 0) {
                    isFirstData = false;
                    String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
                    if (TextUtils.isEmpty(localData)) {
                        mView.getListData(data.getData(), false, false);
                    } else {
                        List<NetResoutVideoInfo> oldData = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
                        }.getType());
                        if (null != oldData && null != oldData.get(0) && !oldData.get(0).get_id().equals(data.getData().get(0).get_id())) {
                            mView.getListData(data.getData(), false, true);
                        }
                    }
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Small_Tab_List, data.getData().toString());
                } else {
                    mView.getListData(data.getData(), false, false);
                    LogUtil.e("ssss", "setVlue====");
                }
                page++;
            }
        });

    }

    @Override
    public void start() {
        mView.initView();
    }
}
