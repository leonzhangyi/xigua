package com.water.melon.ui.me.ques;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.home.h5.AgentWebFragment;
import com.water.melon.utils.SharedPreferencesUtil;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

public class MeQueActivity extends BaseActivity {
    @BindView(R.id.question_flayout)
    FrameLayout frameLayout;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_me_question;
    }

    private FragmentManager mFragmentManager;

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("常见问题");
        setTitleNameColor(R.color.black);

        mFragmentManager = this.getSupportFragmentManager();
        openFragment();
    }

    private QuestionWebFragment mQuestionWebFragment;

    private void openFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;
        ft.add(R.id.question_flayout, mQuestionWebFragment = QuestionWebFragment.getInstance(mBundle = new Bundle()), QuestionWebFragment.class.getName());
        mBundle.putString(AgentWebFragment.URL_KEY, SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_HELP, "http://www.baidu.com"));
        ft.commit();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @OnClick({R.id.toolbar_left_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    @Override
    protected void onClickTitleRight() {

    }
    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.main_botton_bac)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.main_botton_bac)
                .init();
    }
}
