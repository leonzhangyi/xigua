package com.water.melon.ui.me.feek.history;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.FeedBean;

import butterknife.BindView;
import butterknife.OnClick;

public class FeekItemActivity extends BaseActivity {
    public static final String FEEK_BEAN = "feek_bean";

    @BindView(R.id.feek_item_et1)
    EditText feek_item_et1;

    @BindView(R.id.feek_item_et2)
    EditText feek_item_et2;

    private FeedBean.FeekItemBean feekItemBean;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_feek_item;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("平台回复");
        setTitleNameColor(R.color.black);

        feekItemBean = (FeedBean.FeekItemBean) getIntent().getSerializableExtra(FEEK_BEAN);

        if (feekItemBean != null) {
            feek_item_et1.setText(feekItemBean.getContent());
            feek_item_et2.setText(feekItemBean.getAnswer());
        }
    }

    @OnClick(R.id.toolbar_left_tv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }
}
