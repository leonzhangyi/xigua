package com.water.melon.presenter;

import android.util.SparseArray;
import android.widget.RadioGroup;

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
import com.water.melon.net.utils.AESCipherforJiaMi;
import com.water.melon.presenter.contract.MainContract;
import com.water.melon.ui.home.MainFragment;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.ui.me.MeFragment;
import com.water.melon.ui.netresource.NetResouceFragment;
import com.water.melon.ui.welfare.WelfareFragment;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.update.AppVersionInfo;
import com.water.melon.utils.update.MyApkFileDownloadPath;
import com.water.melon.utils.update.MyDownloadNotifier;
import com.water.melon.utils.update.MyUpdateNotifier;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.List;

import androidx.fragment.app.Fragment;

public class MainPresent extends BasePresenterParent implements MainContract.Present {
    public static final String TAG = "MainPresent";
    private MainContract.View mView;
    private SparseArray<Fragment> fragments;
    private int oldPositon = -1;

    public MainPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        fragments = new SparseArray<>();
        mView = (MainContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        String updateUrl = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_DOMAIN, NetConstant.XG_RUL) + NetConstant.XG_APP_WEL_UPDATE + ApiImp.getEcond(ApiImp.getDefMap());
        LogUtil.e("update", "updateUrl ===" + updateUrl);
        //初始化更新组件
        UpdateConfig.getConfig()
                .setUrl(updateUrl)// 配置检查更新的API接口
                .setUpdateParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
//                        LogUtil.e("update", "response === " + response);
                        BaseApiResultData data = (BaseApiResultData) GsonUtil.toClass(response, BaseApiResultData.class);
                        String result = data.getResult();
                        LogUtil.e("update", "result === " + result);
                        Update update = new Update();
                        if (result != null && !result.equals("") && !result.equals("[]")) {
                            AppVersionInfo info = (AppVersionInfo) GsonUtil.toClass(result, AppVersionInfo.class);
                            if (null == info) {
                                return update;
                            }
                            LogUtil.e("update", info.toString() + "===");
//                            AppVersionInfo info = baseInfo.getData();
                            if (null == info) {
                                return update;
                            }
                            // 此apk包的下载地址
//                            update.setUpdateUrl(info.getDownload());
                            update.setUpdateUrl("http://gdown.baidu.com/data/wisegame/cfdb6ba461b2c8ad/baidu_97519360.apk");
                            // 此apk包的版本号
                            update.setVersionCode(Integer.parseInt(info.getVersionCode().trim()));
                            // 此apk包的版本名称
                            update.setVersionName(info.getVersion());
                            // 此apk包的版本大小
//                        update.setAppSize(info.getAndroid_app_size());
                            // 此apk包的更新内容
                            update.setUpdateContent(info.getDesc());
                            // 此apk包是否为强制更新
                            update.setForced("1".equals(info.getForce()));//"is_up": 1//0:不强制；1：强制升级
                            // 是否显示忽略此次版本更新按钮
                            update.setIgnore(false);

                        }
                        return update;
                    }
                }).setCheckNotifier(new MyUpdateNotifier())
                .setDownloadNotifier(new MyDownloadNotifier())
                .setFileCreator(new MyApkFileDownloadPath());
        mView.initView();
    }

    @Override
    public void selectTab(int position, RadioGroup tablayout) {
        if (oldPositon == position) {
            return;
        }
        switch (position) {
            case 0:
                if (fragments.get(position) == null) {
                    fragments.put(position, new MainFragment());
                }
                break;
            case 1:
                if (fragments.get(position) == null) {
                    fragments.put(position, new NetResouceFragment());
                }
                break;
            case 2:
                if (fragments.get(position) == null) {
                    fragments.put(position, new WelfareFragment());
                }
                break;
            case 3:
                if (fragments.get(position) == null) {
                    fragments.put(position, new MeFragment());
                }
                break;
        }


        switchFragment(position);
        oldPositon = position;
    }

    @Override
    public void getUserInfo() {
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


                  }

            }
        });

    }

    @Override
    public void doAdvClick(AdvBean advBean) {
        ApiImp.getInstance().doClickAdv(advBean, getLifecycleTransformerByStopToActivity(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
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

    private void switchFragment(int selectPosition) {
        //功能页面切换
        if (null == fragments.get(selectPosition)) {
            return;
        }
        Fragment oldFragment = null;
        if (oldPositon >= 0) {
            oldFragment = fragments.get(oldPositon);
        }
        mView.selectTab(fragments.get(selectPosition), oldFragment);
    }
}
