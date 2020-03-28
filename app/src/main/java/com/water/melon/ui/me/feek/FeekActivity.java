package com.water.melon.ui.me.feek;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.ui.me.feek.history.FeekHistoryActivity;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class FeekActivity extends BaseActivity implements FeekContract.View {

    @BindView(R.id.layout_feek_weixin_tv)
    TextView layout_feek_weixin_tv;
    @BindView(R.id.layout_feek_qq_tv)
    TextView layout_feek_qq_tv;

    @BindView(R.id.layout_feek_et)
    EditText layout_feek_et;

    @BindView(R.id.layout_feek_sub)
    RelativeLayout layout_feek_sub;
    @BindView(R.id.layout_feek_weixin_copy)
    RelativeLayout layout_feek_weixin_copy;
    @BindView(R.id.layout_feek_qq_copy)
    RelativeLayout layout_feek_qq_copy;


    private FeekPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_feek;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new FeekPresent(this, this);
        present.start();

        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("意见反馈");
        setTitleNameColor(R.color.black);
        setToolBarRightView("反馈记录", R.color.net_resource_item_tv);

    }

    @OnClick({R.id.toolbar_left_tv, R.id.layout_feek_sub, R.id.layout_feek_weixin_copy, R.id.layout_feek_qq_copy, R.id.toolbar_right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
            case R.id.layout_feek_sub: //提交反馈
                String text = layout_feek_et.getText().toString();
                if (text != null && !text.trim().equals("")) {
                    showLoadingDialog(true);
                    present.subMessage(text);
                } else {
                    ToastUtil.showToastShort("请输入您宝贵的意见");
                }

                break;

            case R.id.layout_feek_weixin_copy:
                String weixin = layout_feek_weixin_tv.getText().toString();
                if (weixin != null && !weixin.trim().equals("")) {
                    XGUtil.copyText(this, weixin);
                }
                break;
            case R.id.layout_feek_qq_copy:
                String qq = layout_feek_qq_tv.getText().toString();
                if (qq != null && !qq.trim().equals("")) {
                    XGUtil.copyText(this, qq);
                }
                break;

            case R.id.toolbar_right_tv:
                redirectActivity(FeekHistoryActivity.class);
                break;
        }

    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    @Override
    public void initView() {
        layout_feek_weixin_tv.setText(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_WEIXIN, ""));
        layout_feek_qq_tv.setText(SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_QQ, ""));
    }

    @Override
    public void setPresenter(FeekContract.Present presenter) {
        present = (FeekPresent) presenter;
    }

    @Override
    public void subSucc(boolean isSuc) {
        showLoadingDialog(false);
        XGUtil.hideSoftKeyboard(this);
        if (isSuc) {
            ToastUtil.showToastShort("提交成功,感谢您宝贵意见！");
        }

        layout_feek_et.setText("");
    }
}
