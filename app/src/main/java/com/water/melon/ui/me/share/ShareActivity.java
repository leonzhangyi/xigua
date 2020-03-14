package com.water.melon.ui.me.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sunfusheng.util.Utils;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.ScreenUtils;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.me_share_ewm)
    ImageView me_share_ewm;


    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.layout_me_share;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        viewShare = getWindow().getDecorView();

        setEwm("http://www.baidu.com");
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
                XGUtil.sharePic(this, "欢迎进入VIP你妹", "http://www.baidu.com");
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
}
