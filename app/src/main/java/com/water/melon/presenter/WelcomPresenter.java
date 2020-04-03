package com.water.melon.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.NetConstant;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.net.bean.InitResultBean;
import com.water.melon.net.bean.TabBean;
import com.water.melon.net.utils.AESCipherforJiaMi;
import com.water.melon.presenter.contract.WelcomeContract;
import com.water.melon.ui.sqlites.SqDao;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;

import java.util.List;

public class WelcomPresenter extends BasePresenterParent implements WelcomeContract.Presenter {
    public static final String TAG = "WelcomPresenter";
    private WelcomeContract.View mView;
    private SqDao sqDao;

    public WelcomPresenter(BaseView mBaseView, LifecycleProvider lifecycleProvider, Context context) {
        super(mBaseView, lifecycleProvider);
        sqDao = new SqDao(context);
        this.mView = (WelcomeContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    public static int page = 0;//记录拉取第几个域名

    @Override
    public void getMyYm() {
//        mView.showLoadingDialog(true);
        String url = sqDao.getDomain();
        LogUtil.e(TAG, "数据库中的url = " + url);
        String ym = getTextYm(page, url);
        if (ym.equals(NetConstant.XG_HTTPS + "nodomain" + NetConstant.XG_VERSION_V)) { //沒有可用的域名
            LogUtil.e(TAG, "AdvActivity.failDoMain = true");
//            AdvActivity.failDoMain = true;
            mView.doErrCode(1);
            return;
        }

        ApiImp.getInstance().getDoMain(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {
                page++;
                getMyYm();
            }

            @Override
            public void onNext(BaseApiResultData listBaseApiResultData) {
                LogUtil.e(TAG, "getDoMain.getResult() = " + listBaseApiResultData.getResult());
                String fileResult = listBaseApiResultData.getResult();
                if (fileResult != null && !fileResult.trim().equals("")) {
                    fileResult = fileResult.substring(1, fileResult.length() - 1);
                    checkDoMain(fileResult);
                } else {
                    page++;
                    getMyYm();
                }

            }
        }, ym);
    }

    @Override
    public void getOpenAdv() {
        ApiImp.getInstance().getOpenAdv(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
                mView.init1(false, null);
            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, "getOpenAdv.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("") && !result.equals("[]")) {
                    AdvBean advBeans = (AdvBean) GsonUtil.toClass(result, AdvBean.class);
                    if (advBeans == null) {
                        mView.init1(false, null);
                    } else {
                        mView.init1(true, advBeans);
                    }

                } else {
                    mView.init1(false, null);
                }
            }
        });


    }

    String[] allUrl = null;
    static int judgePage = 0;
    String mYm;

    public void checkDoMain(String result) {
        if (result.contains(",")) {
            allUrl = result.split(",");
        } else {
            allUrl = new String[]{result};
        }

        if (allUrl != null) {
            if (judgePage < allUrl.length) {
                mYm = allUrl[judgePage];//
            } else {
//                AdvActivity.failDoMain = true;
                mView.doErrCode(1);
                return;
            }
        } else {
            mYm = result;
        }

        mYm = NetConstant.XG_HTTPS + mYm + NetConstant.XG_VERSION_V;

        ApiImp.getInstance().doMainJudge(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {
                LogUtil.e(TAG, "checkDoMain.error() = " + error.toString());
                judgePage++;
                checkDoMain(result);
            }

            @Override
            public void onNext(BaseApiResultData listBaseApiResultData) {
                LogUtil.e(TAG, "checkDoMain.getResult() = " + listBaseApiResultData.toString());
                if (listBaseApiResultData.getCode() == 0) {
                    String url = sqDao.getDomain();
                    if (url == null || url.trim().equals("")) {
                        sqDao.add(result);
                    } else {
                        sqDao.update(result);

                    }
                    SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.XG_DOMAIN, mYm);
                    initPhone();
                } else {
                    judgePage++;
                    checkDoMain(result);
                }
            }
        }, mYm);
    }

    public void initPhone() {
        ApiImp.getInstance().getInit(null, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ErrorResponse error) {
                LogUtil.e(TAG, "initPhone er:" + error.toString());
//                initPhone();
                mView.doErrCode(2);
            }

            @Override
            public void onNext(BaseApiResultData listBaseApiResultData) {
                String result = listBaseApiResultData.getResult();
                LogUtil.e(TAG, "initPhone onNext:" + result);
                InitResultBean bean = (InitResultBean) GsonUtil.toClass(result, InitResultBean.class);
                if (bean != null) {
                    if (bean.getMobile() != null) {
                        SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.XG_PHONE, bean.getMobile());
                    }
                    if (bean.getUser_id() != null) {
                        SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.XG_USER_ID, bean.getUser_id());
                        mView.doFinishInit();
                    } else {
                        mView.doErrCode(2);
                    }

                } else {
                    mView.doErrCode(2);
                }
            }
        });
    }

    private String getTextYm(int page, String url) {
        String ym = "";
        if (url != null && !url.trim().equals("")) {
//            try {
//                url = AESCipherforJiaMi.desEncrypt(NetConstant.MY_YM_URL);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            String[] yms = url.split(",");// 讀取上次域名數據
            if (yms.length > page) {
                ym = yms[page];
                ym = NetConstant.XG_HTTPS + ym + NetConstant.XG_VERSION_V;
            } else {
                page = page - yms.length;
                ym = firstGetYm(page);
            }
        } else {
            ym = firstGetYm(page);
        }

        return ym;

    }


    private String firstGetYm(int page) {
        String result = "";
        String ym = "";
        try {
            result = AESCipherforJiaMi.desEncrypt(NetConstant.MY_YM_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = NetConstant.MY_YM_URL;
        }
        String[] yms = result.split(",");// 讀取上次域名數據
        if (yms.length > page) {
            ym = yms[page];
        } else {
            ym = "nodomain";
        }
        return NetConstant.XG_HTTPS + ym + NetConstant.XG_VERSION_V;
    }
}
