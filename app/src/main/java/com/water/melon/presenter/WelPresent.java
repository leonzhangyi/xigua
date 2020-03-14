package com.water.melon.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

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
import com.water.melon.presenter.contract.WelfContract;
import com.water.melon.ui.home.HomeBean;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WelPresent extends BasePresenterParent implements WelfContract.present {
    public static final String TAG = "WelPresent";

    private WelfContract.View mView;

    public WelPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (WelfContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
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
    public void getWelfBeans() {
//        setHomeBeans();
        BaseRequest request = new BaseRequest(1, 20);
        ApiImp.getInstance().getWelPre(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "getWelfBeans.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
//                    mView.setWelTopAdv(advBeans);
                    mView.getwelfBeans(advBeans);
                }
            }
        });


    }

    @Override
    public void getWelTopAdv() {
        BaseRequest request = new BaseRequest(1, 10);
        ApiImp.getInstance().getWelAdv(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "getWelfareAdv.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setWelTopAdv(advBeans);
                }
            }
        });
    }

    @Override
    public void getMidAdv() {
        BaseRequest request = new BaseRequest(1, 10);
        ApiImp.getInstance().getWelMidAdv(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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
                LogUtil.e(TAG, "getMidAdv.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setMidAdv(advBeans);
                }
            }
        });
    }

    @Override
    public void getBtnBeans(int page) {
        BaseRequest request = new BaseRequest(page, 20);
        ApiImp.getInstance().getWelBtnBean(request, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
//                mView.showLoadingDialog(false);
            }

            @Override
            public void onError(ErrorResponse error) {
//                if (error.getCode() != 2) {
//                    ToastUtil.showToastLong(error.getErr());
//                }
                if (page > 1) {
                    mView.setBtnBeans(null, 1);
                }
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getBtnBeans.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    List<AdvBean> advBeans = GsonUtil.toClass(result, new TypeToken<List<AdvBean>>() {
                    }.getType());
                    mView.setBtnBeans(advBeans, 0);
                } else {
                    mView.setBtnBeans(null, 2);
                }
            }
        });
    }

    @Override
    public void downloadAPK(String downLoadUrl, Context context, String name) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
                .DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downLoadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        String pre = "Android/data/awater/";

        String downLoadPath = "";
        if (context.getExternalCacheDir() != null) {
            downLoadPath = context.getExternalCacheDir().getAbsolutePath() + File.separator + pre + name + ".apk";
        } else {
            downLoadPath = context.getCacheDir().getAbsolutePath() + File.separator + pre + name + ".apk";
        }
        LogUtil.e("downloadAPK","downloadPath  = "+downLoadPath);

        File file = new File(downLoadPath);
        if (file.exists()) {
            file.delete();
        }
        request.setVisibleInDownloadsUi(true);
        request.setDestinationUri(Uri.fromFile(file));
        long reference = downloadManager.enqueue(request);

//        Logger.t("update").e("下載完成");

//        Intent i = new Intent();
//        i.setAction(Intent.ACTION_VIEW);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        context.startActivity(i);

    }
}
