package com.water.melon.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.mvp.BaseActivityView;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.ApiImp;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.net_status.NetType;
import com.water.melon.net.net_status.NetworkLiveData;
import com.water.melon.utils.BarTextColorUtils;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SetTextViewDrawable;
import com.water.melon.utils.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends RxAppCompatActivity implements BaseActivityView, View.OnClickListener {
    /**
     * 当前activity是否是活动状态
     */
    protected boolean isActive;

    protected boolean hasMenu;//是否显示菜单

    public static boolean goLogin;//防止多次跳转

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(MyApplication.getColorByResId(R.color.transparent));

        }
//        setStausColor(R.color.white_1); //设置导航栏颜色值

//        BarTextColorUtils.StatusBarLightMode(this);

        initImmersionBar();

        //监听网络全局
        monitorNetStatus();
        setContentView(getContentViewByBase(savedInstanceState));
        //绑定控件
        ButterKnife.bind(this);

        createdViewByBase(savedInstanceState);
//        Toolbar toolbar = findViewById(R.id.toolbar);
        View toolbarStatusVew = findViewById(R.id.toolbar_status_bg);
//        if (null != toolbar) {
////            int padding = (int) (XGConstant.Screen_Height * 0.015);
////            toolbar.setPadding(0, XGConstant.Screen_Status_Height + padding,
////                    0, padding);
////            toolbar.setBackground(MyApplication.getDrawableByResId(R.drawable.me_fragment_top_bac));
////            toolbar.setBackground(MyApplication.getDrawableByResId(R.color.video_download_top_bac));
//        }
//        if (null != toolbarStatusVew) {
//            //自适应各种屏幕
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //状态栏字体设置为黑色
////                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
////            } else {
//            //状态栏颜色
////            toolbarStatusVew.setBackgroundResource(R.color.black);
////            toolbarStatusVew.setBackgroundResource(R.drawable.me_fragment_top_bac);
////            }
//            //设置高度
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbarStatusVew.getLayoutParams();
//            params.height = XGConstant.Screen_Status_Height;
//        }
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)
//                .statusBarColor(R.color.black)
                .init();
    }

    private void monitorNetStatus() {
        NetworkLiveData.get(this).observe(this, new androidx.lifecycle.Observer<NetType>() {
            @Override
            public void onChanged(NetType netType) {
                switch (netType) {
                    case NET_UNKNOW:
                        break;
                    case NET_4G:
                        break;
                    case NET_3G:
                        break;
                    case NET_2G:
                        //有网络
                        break;
                    case WIFI:
                        break;
                    case NOME:
                        ToastUtil.showToastShort(getStringByResId(R.string.net_error_for_link_exception));
                        //没有网络，提示用户跳转到设置
                        break;
                    default:
                        break;
                }
            }
        });
    }


    //获取视图布局，该方法不能获取布局控件
    public abstract int getContentViewByBase(Bundle savedInstanceState);

    //设置完视图后调用，该方法可以获取布局控件并操作
    public abstract void createdViewByBase(Bundle savedInstanceState);

    /**
     * 5.0以上系统设置手机状态栏颜色
     *
     * @param colorId
     */
    public void setStausColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(MyApplication.getColorByResId(colorId));
            //底部虚拟按键颜色
//            window.setNavigationBarColor(MyApplication.getColorByResId(colorId));
            //判断当前手机品牌
            String phoneSystem = Build.MANUFACTURER;
            if ("Xiaomi".equalsIgnoreCase(phoneSystem)) {
                MIUI(colorId);
            } else if (("samsung".equalsIgnoreCase(phoneSystem) || "huawei".equalsIgnoreCase(phoneSystem))
                    && colorId == R.color.white) {
                //三星华为手机如果设置状态栏为白色，则统一改成系统色
                window.setStatusBarColor(MyApplication.getColorByResId(R.color.colorPrimaryDark));
            }
        }
    }

    public Bundle getBundle() {
        if (null == getIntent().getExtras()) {
            return new Bundle();
        } else {
            return getIntent().getExtras();
        }
    }

    //更改小米系统的状态栏颜色
    public boolean MIUI(int colorId) {
        boolean result = false;
        Window window = getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (colorId == R.color.white) {//状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {//清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;
            } catch (Exception e) {
                LogUtil.e("MIUI", e.toString());
            }
        }
        return result;
    }

    /**
     * 退出Activity动画
     */
    public void finishActivity(Activity activity) {
        activity.finish();
//        overridePendingTransition(R.anim.anim_activity_left_in, R.anim.anim_activity_right_out);
    }

    /**
     * 退出Activity,指定动画
     */
    public void finishActivity(Activity activity, int animaEnterResId, int animaEexitResId) {
        activity.finish();
        overridePendingTransition(animaEnterResId, animaEexitResId);
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
        Intent it = new Intent(this, target);
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
        // 进入Activity动画
//        overridePendingTransition(R.anim.anim_activity_right_in, R.anim.anim_activity_left_out);
    }

    /**
     * Intent跳转
     *
     * @param target 带指定动画
     */
    protected void redirectActivityForAnima(Class<? extends Activity> target, Bundle bundle, int animaEnterResId, int animaEexitResId) {
        Intent it = new Intent(this, target);
        if (null != bundle) {
            it.putExtras(bundle);
        }
        startActivity(it);
        // 进入Activity动画
        overridePendingTransition(animaEnterResId, animaEexitResId);
    }

    /**
     * IntentForResult跳转
     *
     * @param it
     */
    protected void redirectActivityForResult(Intent it, int requestCode) {
        startActivityForResult(it, requestCode);
        // 进入Activity动画
//        overridePendingTransition(R.anim.anim_activity_right_in, R.anim.anim_activity_left_out);
    }

    /**
     * 延时返回
     */
    protected void goBackBySlowly() {
        //延迟，让点击的效果飘一伙~
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        finishActivity(BaseActivity.this);
                    }
                });

    }

    /**
     * 延时返回
     *
     * @param time 延时时间
     */
    protected void goBackBySlowly(long time) {
        //延迟，让点击的效果飘一伙~
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        finishActivity(BaseActivity.this);
                    }
                });

    }

    /**
     * 返回
     */
    protected void goBackByQuick() {
        finishActivity(this);
    }

    /**
     * 点击标题返回按钮
     */
    protected abstract void onClickTitleBack();

    /**
     * 点击标题返回按钮
     */
    protected abstract void onClickTitleRight();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !LoadingUtil.isShowing()) {
            goBackByQuick();
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                } else {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        //弹窗
        LoadingUtil.init(this);
        super.onResume();
//        if (!"SplashActivity".equals(getClass().getSimpleName())
//                && !"WelcomeActivity".equals(getClass().getSimpleName())
//                && !"LoginActivity".equals(getClass().getSimpleName())
//                && !"ForgetPassWordActivty".equals(getClass().getSimpleName())) {
        //启动页，欢迎页，登录和忘记密码不用验证
//            checkSystemClearCatchInfo();
//        }
        isActive = true;
    }

    @Override
    protected void onPause() {
        //弹窗的释放
        LoadingUtil.destory();
        super.onPause();
        isActive = false;
    }


    @Override
    public boolean isActive() {
        return isActive;
    }

    //进度弹窗
    @Override
    public void showLoadingDialog(boolean b) {
        LoadingUtil.showLoadingDialog(b);
    }

    //进度弹窗 带自定义提示文字
    @Override
    public void showLoadingDialog(boolean b, String msg) {
        LoadingUtil.showLoadingDialog(b, msg);
    }

    @Override
    public void catchApiSubscriberError(ErrorResponse error) {
        if (error.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED && !goLogin) {
            //goLogin 防止多个请求跳转
            goLogin = true;
            ToastUtil.showToastShort(error.getErr());
            //Token失效
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiImp.getInstance().stopRequst();
//        ImmersionBar.with(this).destroy();
    }

    //============初始化ToolBar==========================

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     * @param hasBack     是否显示返回按钮
     * @param hasMenu     是否显示菜单按钮
     */
    public void initToolBar(String back_name, String center_name, boolean hasBack, boolean hasMenu) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //返回按钮旁边的标题
        toolbar.setTitle(back_name);
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.title_name)).setText(center_name);
        }
        if (hasBack) {
            //替换toolbar系统的返回按钮
            toolbar.setNavigationIcon(R.mipmap.icon_toleft);
            setSupportActionBar(toolbar);
            //设置返回按钮的监听事件
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            setSupportActionBar(toolbar);
        }
        this.hasMenu = hasMenu;
    }

    /**
     * 改变toolBar字体色
     */
    public void setToolBarTitleColor(int colorId) {
        ((TextView) findViewById(R.id.title_name)).setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar原生返回按钮图标
     */
    public void setToolBarBackView(int drawableId) {
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(drawableId);
    }

    /**
     * 改变toolBar自定义的左边按钮图标
     */
    public void setToolBarLeftView(int resId) {
        TextView toolbarLeft = findByID(R.id.toolbar_left_tv);
        SetTextViewDrawable.setLeftView(toolbarLeft, resId);
    }


    public void setToolLine(boolean isVisible) {
        View v = findByID(R.id.toolbar_split_line);
        v.setVisibility(isVisible == true ? View.VISIBLE : View.GONE);
    }

    /**
     * 改变toolBar自定义的右边按钮图标
     */
    public void setToolBarRightView(int resId) {
        TextView toolbarRight = findByID(R.id.toolbar_right_tv);
        SetTextViewDrawable.setLeftView(toolbarRight, resId);
    }

    /**
     * 改变toolBar自定义的右边按钮图标
     */
    public void setToolBarRightView(String text,int colorId) {
        TextView toolbarRight = findByID(R.id.toolbar_right_tv);
        toolbarRight.setText(text);
        toolbarRight.setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar状态栏的底色
     */
    public void setToolBarStatusBgColor(int colorId) {
        findViewById(R.id.toolbar_status_bg).setBackgroundColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 改变toolBar背景色
     */
    public void setToolBarBackground(int resID) {
        ((Toolbar) findViewById(R.id.toolbar)).setBackgroundResource(resID);
    }

    /**
     * 改变Tilte字体色
     */
    public void setTitleNameColor(int colorId) {
        ((TextView) findViewById(R.id.title_name)).setTextColor(MyApplication.getColorByResId(colorId));
    }

    /**
     * 设置title文字
     */
    public void setTitleName(String title) {
        ((TextView) findViewById(R.id.title_name)).setText(title);
    }

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     */
    public void initToolBar(String back_name, String center_name) {
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.title_name)).setText(center_name);
        }
        TextView toolbarLeft = ((TextView) findViewById(R.id.toolbar_left_tv));
        if (!TextUtils.isEmpty(back_name)) {
            //返回按钮旁边的标题
            toolbarLeft.setText(back_name);
        }
        SetTextViewDrawable.setLeftView(toolbarLeft, R.mipmap.icon_toleft);
        //加大点击区域
        toolbarLeft.setOnClickListener(this);
    }

    /**
     * 初始化ToolBar
     *
     * @param back_name   返回按钮旁边的标题
     * @param center_name 中间标题
     * @param hasBack     是否显示返回按钮
     * @param hasMenu     是否显示菜单按钮
     */
    public void initToolBar(String back_name, String center_name, boolean hasBack, boolean hasMenu, int resColor) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //返回按钮旁边的标题
        toolbar.setTitle(back_name);
        toolbar.setBackgroundColor(MyApplication.getColorByResId(resColor));
        if (!TextUtils.isEmpty(center_name)) {
            //中间标题
            ((TextView) findViewById(R.id.title_name)).setText(center_name);
        }
        if (hasBack) {
            //替换toolbar系统的返回按钮
            toolbar.setNavigationIcon(R.mipmap.icon_toleft);
            setSupportActionBar(toolbar);
            //设置返回按钮的监听事件
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            setSupportActionBar(toolbar);
        }
        this.hasMenu = hasMenu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (hasMenu) {
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//            return true;
//        } else {
//            return super.onCreateOptionsMenu(menu);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回按钮
                onClickTitleBack();
                break;
//            case R.id.action_item1:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //左键返回
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_left_tv) {
            onClickTitleBack();
        } else if (v.getId() == R.id.toolbar_right_tv) {
            onClickTitleRight();
        }
    }
    //============初始化ToolBar End==========================

    public String getStringByResId(int str) {
        return getResources().getString(str);
    }

    //切换语言====================================

    /**
     * 切换语言
     *
     * @param
     */
//    public void switchLanguage(Locale locale) {
//        Resources res = getResources();
//        Configuration config = res.getConfiguration();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Locale currentLocal;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            currentLocal = config.getLocales().get(0);
//            config.setLocale(locale);
//            res.updateConfiguration(config, dm);
//            // 如果切换了语言
//            if (!currentLocal.equals(config.getLocales().get(0))) {
//                // 这里需要重新刷新当前页面中使用到的资源
//                MyApplication.language = locale;
//                recreate();
//            }
//        } else {
//            currentLocal = config.locale;
//            config.locale = locale;
//            res.updateConfiguration(config, dm);
//            // 如果切换了语言
//            if (!currentLocal.equals(config.locale)) {
//                // 这里需要重新刷新当前页面中使用到的资源
//                MyApplication.language = locale;
//                recreate();
//            }
//        }
//
//    }
    public <T> T findByID(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //必须要重写acitvity此方法，否则友盟分享不会回调
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Resources getResources() {
        //设置后，app的字体大小不会与系统设置的大小有关
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
