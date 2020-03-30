package com.water.melon.ui.me.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sunfusheng.util.Utils;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.ShareBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.ScreenUtils;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareContract.View {
    private SharePresent sharePresent;

    @BindView(R.id.me_share_ewm)
    ImageView me_share_ewm;


    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 去掉窗口标题
        // 隐藏顶部的状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.layout_me_share;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= 28) {
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            getWindow().setAttributes(lp);
//        }

        viewShare = getWindow().getDecorView();
        new SharePresent(this, this);
        sharePresent.start();

//        setEwm("http://www.baidu.com");
    }

    @OnClick({R.id.me_share_back, R.id.me_share_save, R.id.me_share_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_share_back:
                this.finish();
                break;
            case R.id.me_share_save:
                savePath();
                break;
            case R.id.me_share_share:
                XGUtil.sharePic(this, content, shareUrl);
                break;
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    public void setEwm(String url) {
//        url = obj.getString("url");
//        if(url.contains("channelCode")) {
//            url = url+"&invitecode="+AppConfig.getUserInfo(mContext).getInvitation_code();
//        }else{
//            url = url+"?invitecode="+AppConfig.getUserInfo(mContext).getInvitation_code();
//        }
        Bitmap bitmap = ZXingUtils.createQRCodeWithLogo(url, Utils.dp2px(this, 150),
                BitmapFactory.decodeResource(getResources(), R.mipmap.def_icon));
        me_share_ewm.setImageBitmap(bitmap);
    }

    View viewShare;

    private void savePath() {
        if (viewShare != null) {
            FileUtil.viewSaveToImage(viewShare, this);
        } else {
            ToastUtil.showToastShort("操作異常，清稍後再試");
        }
    }

    @Override
    public void initView() {
        sharePresent.getShareDate();
    }

    @Override
    public void setPresenter(ShareContract.Present presenter) {
        this.sharePresent = (SharePresent) presenter;
    }

    private String shareUrl = "";
    private String content = "";

    @Override
    public void setShareDate(ShareBean shareBean) {
        if (shareBean != null) {
            shareUrl = shareBean.getUrl();
            content = shareBean.getContent();
        }
        String baseInfo = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        if (baseInfo != null && !baseInfo.trim().equals("") && !baseInfo.trim().equals("[]")) {
            UserBean userBean = (UserBean) GsonUtil.toClass(baseInfo, UserBean.class);
            if (userBean != null) {
                if (shareUrl.contains("channelCode")) {
                    shareUrl = shareUrl + "&invitecode=" + userBean.getInvitation_code();
                } else {
                    shareUrl = shareUrl + "?invitecode=" + userBean.getInvitation_code();
                }
            }
        }
        setEwm(shareUrl);
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .navigationBarColor(R.color.main_botton_bac)
//                .statusBarDarkFont(true)
//                .statusBarColor(R.color.main_botton_bac)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init();
    }
}

