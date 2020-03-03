package com.water.melon.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.water.melon.R;
import com.water.melon.constant.XGConstant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class UpProgressDialog extends Dialog {
    private TextView upAppSpeed, joke;
    private ProgressBar progressBar;
    private Disposable mdDisposable;

    private String[] joke_content = {"问：熊把指甲剪了变成了什么？", "答：变成”能“ o(*￣V￣*)o"
            , "问：一块姜，切成四片变成什么", "答：姜姜姜姜 o(*￣V￣*)o"
            , "问：站在树上唱Rap，打一个字。", "答：”桑“ o(*￣V￣*)o"
            , "问：皮卡丘站起来变成了什么？", "答：”皮卡兵“ o(*￣V￣*)o"
            , "问：甲乙丙丁，那个字酷？", "答：丁字裤（酷） o(*￣V￣*)o"
            , "问：金木水火土，那个腿长？", "答：火腿长 o(*￣V￣*)o"
            , "问：发射用英语怎么说？", "答：biu,biu,biu o(*￣V￣*)o"
            , "问：世界上最没力气的花是什么？", "答：茉莉花，好一朵没力的茉莉花(*￣V￣*)o"
    };
    private int jokePosition = 0;
    private int jokeSize = joke_content.length;

    public UpProgressDialog(@NonNull Context context) {
        this(context, 0);
    }

    public UpProgressDialog(@NonNull Context context, int themeResId) {
        this(context, false, null);
    }

    protected UpProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_up_app_progress);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setWindowAnimations(R.style.DialogBottomAnimation);
        dialogWindow.setGravity(Gravity.CENTER);
//        ViewGroup.LayoutParams params = findViewById(R.id.dialog_up_img_lay).getLayoutParams();
        lp.width = (int) (XGConstant.Screen_Width * 0.85); // 宽度
//        lp.height = params.height; // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);
        initView();
    }

    private void initView() {
        upAppSpeed = findViewById(R.id.up_app_progress_speed);
        joke = findViewById(R.id.up_app_progress_joke);
        progressBar = findViewById(R.id.up_app_progress);

        Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mdDisposable = disposable;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (jokePosition == jokeSize) {
                            jokePosition = 0;
                        }
                        joke.setText(joke_content[jokePosition]);
                        jokePosition++;

                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void setProgress(int progress) {
        if (progress >= 98 && null != mdDisposable) {
            mdDisposable.dispose();
            mdDisposable = null;
        }
        progressBar.setProgress(progress);
        upAppSpeed.setText(progress + "%");
    }

}
