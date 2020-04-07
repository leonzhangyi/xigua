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
import com.water.melon.net.bean.GetVideosRequest;
import com.water.melon.net.bean.TabBean;
import com.water.melon.presenter.contract.NetResouceItemContract;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class NetResouceItemPresenter extends BasePresenterParent implements NetResouceItemContract.Presenter {
    public static final String TAG = "NetResouceItemPresenter";
    private NetResouceItemContract.View mView;
    private boolean isFirstData;
//    private int page = 1;
    public int limit = 12;

    public NetResouceItemPresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider, boolean isFirstData) {
        super(mBaseView, lifecycleProvider);
        this.mView = (NetResouceItemContract.View) mBaseView;
        this.isFirstData = isFirstData;
        mView.setPresenter(this);
    }

//    @Override
//    public void getListData(GetVideosRequest request) {
//        if (isFirstData) {
//            String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
//            if (!TextUtils.isEmpty(localData)) {
//                List<NetResoutVideoInfo> items = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
//                }.getType());
//                mView.getListData(items, false, false);
//            }
//        }
//        request.setPage(page);
//        request.setLimit(limit);
//
//        ApiImp.getInstance().getNetResourceList(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData<List<NetResoutVideoInfo>>>() {
//            @Override
//            public void onCompleted() {
//                mView.showLoadingDialog(false);
//            }
//
//            @Override
//            public void onError(ErrorResponse error) {
//                ToastUtil.showToastLong(error.getErr());
//                mView.getListData(new ArrayList<>(), true, false);
//            }
//
//            @Override
//            public void onNext(BaseApiResultData<List<NetResoutVideoInfo>> data) {
//                if (isFirstData && null != data.getData() && data.getData().size() > 0) {
//                    isFirstData = false;
//                    String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
//                    if (TextUtils.isEmpty(localData)) {
//                        mView.getListData(data.getData(), false, false);
//                    } else {
//                        List<NetResoutVideoInfo> oldData = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
//                        }.getType());
//                        if (null != oldData && null != oldData.get(0) && !oldData.get(0).get_id().equals(data.getData().get(0).get_id())) {
//                            mView.getListData(data.getData(), false, true);
//                        }
//                    }
//                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Small_Tab_List, data.getData().toString());
//                } else {
//                    mView.getListData(data.getData(), false, false);
//                    LogUtil.e("ssss", "setVlue====");
//                }
//                page++;
//            }
//        });
//
//    }

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
//        request.setPage(page);
        request.setLimit(limit);
//        request.setSearchWord("半个喜剧");
//        request.setBigTabId("");
//        request.setSmallName("");
        ApiImp.getInstance().getNetVideoList(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                        LogUtil.e("ssss", "isFirstData====");
                        isFirstData = false;
                        String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
                        if (TextUtils.isEmpty(localData)) {
                            mView.getListData(tabBeans, false, false);
                        } else {
                            List<NetResoutVideoInfo> oldData = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
                            }.getType());
                            if (null != oldData && null != oldData.get(0) && !oldData.get(0).get_id().equals(tabBeans.get(0).get_id())) {
                                mView.getListData(tabBeans, false, true);
                            }
                        }
                        SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Small_Tab_List, tabBeans.toString());
                    } else {
                        mView.getListData(tabBeans, false, false);
                        LogUtil.e("ssss", "setVlue====");
                    }
//                    page++;
                }else {
                    if (isFirstData) {
                        mView.getListData(new ArrayList<>(), false, true);
                    }else{
                        mView.getListData(new ArrayList<>(), true, false);
                    }

                }
            }
        });


//        ApiImp.getInstance().getNetResourceList(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData<List<NetResoutVideoInfo>>>() {
//            @Override
//            public void onCompleted() {
//                mView.showLoadingDialog(false);
//            }
//
//            @Override
//            public void onError(ErrorResponse error) {
//                ToastUtil.showToastLong(error.getErr());
//                mView.getListData(new ArrayList<>(), true, false);
//            }
//
//            @Override
//            public void onNext(BaseApiResultData<List<NetResoutVideoInfo>> data) {
//                if (isFirstData && null != data.getData() && data.getData().size() > 0) {
//                    isFirstData = false;
//                    String localData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_Small_Tab_List, "");
//                    if (TextUtils.isEmpty(localData)) {
//                        mView.getListData(data.getData(), false, false);
//                    } else {
//                        List<NetResoutVideoInfo> oldData = GsonUtil.toClass(localData, new TypeToken<List<NetResoutVideoInfo>>() {
//                        }.getType());
//                        if (null != oldData && null != oldData.get(0) && !oldData.get(0).get_id().equals(data.getData().get(0).get_id())) {
//                            mView.getListData(data.getData(), false, true);
//                        }
//                    }
//                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_Small_Tab_List, data.getData().toString());
//                } else {
//                    mView.getListData(data.getData(), false, false);
//                    LogUtil.e("ssss", "setVlue====");
//                }
//                page++;
//            }
//        });

    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getAdv() {
        BaseRequest request = new BaseRequest(1, 10);
        ApiImp.getInstance().getNetAdv(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
//                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
//                    ToastUtil.showToastLong(error.getErr());
//                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getNetAdv.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setAdv(advBeans);
                }
            }
        });

    }
}
