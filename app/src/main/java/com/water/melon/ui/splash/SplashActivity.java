package com.water.melon.ui.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.presenter.WelcomPresenter;
import com.water.melon.presenter.contract.WelcomeContract;
import com.water.melon.ui.main.MainActivity;
import com.water.melon.utils.ToastUtil;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity implements WelcomeContract.View {
    private WelcomPresenter welcomPresenter;

    @BindView(R.id.help_iv)
    ImageView help_iv;
    @BindView(R.id.help_rl)
    RelativeLayout help_rl;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
    }

    @Override
    public void setPresenter(WelcomeContract.Presenter presenter) {
        welcomPresenter = (WelcomPresenter) presenter;
    }

    @Override
    public void doFinishInit() {
        help_rl.setVisibility(View.GONE);
        redirectActivity(MainActivity.class);
        finish();
    }

    @Override
    public void doErrCode(int code) {
//        showLoadingDialog(false);
        help_rl.setVisibility(View.GONE);
        if(code == 1){
            ToastUtil.showToastLong("无可用通道");
        }else{
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
}
