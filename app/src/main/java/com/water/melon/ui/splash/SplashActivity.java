package com.water.melon.ui.splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gyf.immersionbar.ImmersionBar;
import com.sunfusheng.progress.GlideApp;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AdvBean;
import com.water.melon.presenter.WelcomPresenter;
import com.water.melon.presenter.contract.WelcomeContract;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity implements WelcomeContract.View {
    public static final int GET_NO_ADV = -2;//無廣告
    public static final int GET_ADV_FINISH = -1;//廣播播放完

    private WelcomPresenter welcomPresenter;

    @BindView(R.id.help_iv)
    ImageView help_iv;
    @BindView(R.id.adv_activity_ext_bac)
    ImageView adv_activity_ext_bac;
    @BindView(R.id.adv_activity_time_bac)
    TextView adv_activity_time_bac;
    @BindView(R.id.activity_splash_iv)
    ImageView activity_splash_iv;

    @BindView(R.id.help_rl)
    RelativeLayout help_rl;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }

        new WelcomPresenter(this, this, this);
        welcomPresenter.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private boolean isSure = false;
    private AnimationDrawable animationDrawable;

    @Override
    public void initView() {
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    help_rl.setVisibility(View.VISIBLE);
                    animationDrawable = (AnimationDrawable) help_iv.getBackground();
                    animationDrawable.start();
                    welcomPresenter.getMyYm();

//                    redirectActivity(MainActivity.class);
//                    finish();
                } else {
//                    ToastUtil.showToastLong(getStringByResId(R.string.permission_sd));
                    if (!isSure) {
                        isSure = true;
                        showNormalDialog();
                    } else {
                        doMyFinish();
                    }
//                    Logger.t(TAG).e("READ_PHONE_STATE Permission Denied");
                }
            }

            @Override
            public void onError(Throwable e) {
//                ToastUtil.showToastLong(getStringByResId(R.string.permission_sd));
                if (!isSure) {
                    isSure = true;
                    showNormalDialog();
                } else {
                    doMyFinish();
                }
            }

            @Override
            public void onComplete() {

            }
        });

        initHandler();
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        welcomPresenter = (WelcomPresenter) presenter;
    }

    @Override
    public void doFinishInit() {
//        help_rl.setVisibility(View.GONE);
//        redirectActivity(MainActivity.class);
//        finish();


        welcomPresenter.getOpenAdv();

    }

    AdvBean advBean;

    @Override
    public void init1(final Boolean haveAdv, AdvBean advBeans) {
        help_rl.setVisibility(View.GONE);
        if (haveAdv) {
            this.advBean = advBeans;
            GlideApp.with(this)
                    .asBitmap()
                    .load(advBeans.getUrl())
                    .placeholder(R.mipmap.bg_welcome)
//                    .override(200, 300)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(activity_splash_iv);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (haveAdv) {
                    for (int i = 5; i >= 0; i--) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(i);
                    }
                }


                if (!haveAdv) {
                    mHandler.sendEmptyMessage(GET_NO_ADV);
                } else {
                    mHandler.sendEmptyMessage(GET_ADV_FINISH);
                }
            }
        }).start();
    }


    @Override
    public void doErrCode(int code) {
//        showLoadingDialog(false);
        help_rl.setVisibility(View.GONE);
        if (code == 1) {
            ToastUtil.showToastLong("无可用通道");
        } else {
            ToastUtil.showToastLong("APP异常，请重新启动");
        }

    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("打开程序，方可正常使用哦");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initView();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doMyFinish();
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void doMyFinish() {
        this.finish();
    }

    protected void initImmersionBar() {
        //设置共同沉浸式样式
//        ImmersionBar.with(this).navigationBarColor(R.color.colorAccent).init();
    }


    private Handler mHandler;

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GET_ADV_FINISH) {
                    adv_activity_time_bac.setVisibility(View.GONE);
                    adv_activity_ext_bac.setVisibility(View.VISIBLE);
                } else if (msg.what == GET_NO_ADV) {
                    doFinish();
                } else {
                    adv_activity_time_bac.setText(msg.what + "s");
                }
            }
        };
    }

    private void doFinish() {
        help_rl.setVisibility(View.GONE);
        redirectActivity(MainActivity.class);
        finish();
    }

    @OnClick({R.id.adv_activity_ext_bac, R.id.activity_splash_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adv_activity_ext_bac:
                doFinish();
                break;
            case R.id.activity_splash_iv:
                if (advBean != null) {
                    XGUtil.openAdv(advBean, this);
                }
                break;
        }
    }
}
