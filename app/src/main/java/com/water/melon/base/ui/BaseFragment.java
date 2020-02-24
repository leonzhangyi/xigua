package com.water.melon.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle3.components.support.RxFragment;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.ErrorResponse;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.fragment_back_listen.HandleBackInterface;

import java.net.HttpURLConnection;

import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends RxFragment implements com.water.melon.base.mvp.BaseFragment , HandleBackInterface {

    protected abstract void onMyCreateView(View rootView, Bundle savedInstanceState);

    //获取布局文件ID
    protected abstract int getLayoutId();

    protected Context context;

    public boolean isVisible = false;//懒加载
    public boolean isPrepared = false;//初始化控件完成
    private Unbinder unbinder;
    public View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, rootView);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        View toolbarStatusVew = rootView.findViewById(R.id.toolbar_status_bg);
        if (null != toolbar) {
//            if (this instanceof NewHomeFragment) {
//                //首页
//                ((FrameLayout.LayoutParams) toolbar.getLayoutParams()).setMargins(0, (int) (MyApplication.Screen_Height * 0.035),
//                        0, -50);
//            } else {
            //自适应各种屏幕
            toolbar.setPadding(0, (int) (XGConstant.Screen_Height * 0.05),
                    0, (int) (XGConstant.Screen_Height * 0.015));
//            }
        }
        if (null != toolbarStatusVew) {
            //状态栏颜色
            toolbarStatusVew.setBackgroundResource(R.color.black);
//            toolbarStatusVew.setBackgroundResource(R.drawable.me_fragment_top_bac);
        }
        onMyCreateView(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target) {
        redirectActivity(target, null, -1);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target, Bundle bundle) {
        redirectActivity(target, bundle, -1);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target, int flags) {
        redirectActivity(target, null, flags);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivity(Class<? extends Activity> target,
                                    Bundle data, int flags) {
        Intent it = new Intent(getContext(), target);
        if (null != data) {
            it.putExtras(data);
        }
        if (flags > -1) {
            it.setFlags(flags);
        }
        redirectActivity(it);
    }

    /**
     * Intent跳转
     *
     * @param it
     */
    protected void redirectActivity(Intent it) {
        startActivity(it);
    }

    /**
     * Intent跳转
     *
     * @param target
     */
    protected void redirectActivityForAnima(Class<? extends Activity> target, Bundle
            bundle, int animaEnterResId) {
        Intent it = new Intent(getContext(), target);
        if (null != bundle) {
            it.putExtras(bundle);
        }
        startActivity(it);
        // 进入Activity动画
//        getActivity().overridePendingTransition(animaEnterResId, R.anim.push_center);
    }

    /**
     * IntentForResult跳转
     *
     * @param it
     */
    protected void redirectActivityForResult(Intent it, int requestCode) {
        startActivityForResult(it, requestCode);
        // 进入Activity动画
//        getActivity().overridePendingTransition(R.anim.anim_activity_right_in, R.anim.anim_activity_left_out);
    }

    @Override
    public void showLoadingDialog(boolean b) {
        if (isVisible) {
            LoadingUtil.showLoadingDialog(b);
        }
    }

    @Override
    public void showLoadingDialog(boolean b, String msg) {
        if (isVisible) {
            LoadingUtil.showLoadingDialog(b, msg);
        }
    }

    @Override
    public void catchApiSubscriberError(ErrorResponse error) {
        if (error.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED && !BaseActivity.goLogin) {
            //goLogin 防止多个请求跳转
            BaseActivity.goLogin = true;
            ToastUtil.showToastShort(error.getMessage());
            //Token失效
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(LoginActivity.Key_Login_Success_go_back, true);
//            SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_USER_INFO,"");
//            redirectActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
//            MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
        } else {
            isVisible = false;
            onInvisible();
//            MobclickAgent.onPageEnd(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = !hidden;
        if (isVisible) {
            onVisible();
//            MobclickAgent.onPageStart(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
        } else {
            onInvisible();
//            MobclickAgent.onPageEnd(getClass().getSimpleName()); //统计页面("MainScreen"为页面名称，可自定义)
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }

    public String getStringByRes(int resId) {
        return MyApplication.getStringByResId(resId);
    }
}
