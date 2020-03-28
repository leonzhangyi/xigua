package com.water.melon.ui.me;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.net.bean.UserBean;
import com.water.melon.presenter.MePresenter;
import com.water.melon.presenter.contract.MeContract;
import com.water.melon.ui.in.AdapterItemClick;
import com.water.melon.ui.login.LoginActivity;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.ui.me.agent.AgentActivity;
import com.water.melon.ui.me.dowload.DownLoadActivity;
import com.water.melon.ui.me.feek.FeekActivity;
import com.water.melon.ui.me.history.VideoHistroryActivity;
import com.water.melon.ui.me.ques.MeQueActivity;
import com.water.melon.ui.me.share.ShareActivity;
import com.water.melon.ui.me.vip.VipActivity;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.AboutDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MeFragment extends BaseFragment implements MeContract.View, AdapterItemClick {
    @BindView(R.id.me_recy)
    RecyclerView recyclerView;

    private MePresenter mePresenter;

    private MePageAdapter mePageAdapter;

    @BindView(R.id.app_versions_right_progress)
    ProgressBar appVersionProgress;

    @BindView(R.id.me_phone)
    TextView me_phone;

    @BindView(R.id.me_vip)
    TextView me_vip;

    @BindView(R.id.me_fragment_vip_tv1)
    TextView me_fragment_vip_tv1;
    @BindView(R.id.me_fragment_vip_tv2)
    TextView me_fragment_vip_tv2;

    @BindView(R.id.login_out_ll)
    LinearLayout login_out_ll;


    protected int getLayoutId() {
        return R.layout.me_fragment_1;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new MePresenter(this, this);
        mePresenter.start();
    }

    @Override
    public void initView() {
        setDefData();
        mePresenter.getUserInfo();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mePageAdapter = new MePageAdapter(pageBeans, context);
        mePageAdapter.setAdapterItemClick(this);
        recyclerView.setAdapter(mePageAdapter);


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setBaseDate();
    }

    @Override
    public void setBaseDate() {
        String baseInfo = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        if (baseInfo != null && !baseInfo.trim().equals("") && !baseInfo.trim().equals("[]")) {
            UserBean userBean = (UserBean) GsonUtil.toClass(baseInfo, UserBean.class);
            if (userBean != null) {
                String mobile = userBean.getMobile();
                if (mobile == null || mobile.trim().equals("")) {
                    me_phone.setText("游客用户");
//                    me_phone.setText(userBean.getImei());
                    login_out_ll.setVisibility(View.GONE);
                } else {
                    me_phone.setText(mobile);
                    login_out_ll.setVisibility(View.VISIBLE);
                }

                int groupID = Integer.parseInt(userBean.getGroup_id().trim());
                setVipDate(groupID);

                int vip = Integer.parseInt(userBean.getVip().trim());
                String time = userBean.getEnd_date().trim();
                if (time.contains(" ")) {
                    time = time.split(" ")[0];
                }

                setVip(vip, time);
            }
        }
    }

    private void setVip(int vip, String time) {
        // 0非会员 1体验卡, 2周卡, 3月卡, 4季卡, 5年卡, 6永久卡 7 代理卡
        switch (vip) {
            case 0:
                me_fragment_vip_tv1.setText("VIP已过期");
                me_fragment_vip_tv2.setText("VIP已过期，请及时续费 >");
                break;
            case 1:
                me_fragment_vip_tv1.setText("体验卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 2:
                me_fragment_vip_tv1.setText("周卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 3:
                me_fragment_vip_tv1.setText("月卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 4:
                me_fragment_vip_tv1.setText("季卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 5:
                me_fragment_vip_tv1.setText("年卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 6:
                me_fragment_vip_tv1.setText("永久卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;
            case 7:
                me_fragment_vip_tv1.setText("代理卡");
                me_fragment_vip_tv2.setText("有效至：" + time);
                break;

        }
    }


    //    0游客,1,注册会员,2,代理,3,总代理
    private void setVipDate(int groupID) {
        switch (groupID) {
            case 0:
                me_vip.setText("游客");
                break;
            case 1:
                me_vip.setText("注册会员");
                break;
            case 2:
                me_vip.setText("代理");
                break;
            case 3:
                me_vip.setText("总代理");
                break;
        }
    }


    @Override
    public void setPresenter(MeContract.Presenter presenter) {
        this.mePresenter = (MePresenter) presenter;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void checkAppVersion(boolean has, String msg) {
        appVersionProgress.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.showToastShort(msg);
        }
        if (has) {
//            appVersion.setText(getStringByRes(R.string.up_app_new));
        }
    }

    private AboutDialog aboutDialog;

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0: //观看历史
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    redirectActivity(VideoHistroryActivity.class);
                }

                break;
            case 1://视频下载
                UserBean userBean2 = XGUtil.getMyUserInfo();
                if (userBean2 == null || userBean2.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    redirectActivity(DownLoadActivity.class);
                }
                break;
            case 2: //常见问题
                redirectActivity(MeQueActivity.class);
                break;
            case 3: //检查更新
                appVersionProgress.setVisibility(View.VISIBLE);
                mePresenter.checkAppVersion();
                break;
            case 4: //意见反馈
                redirectActivity(FeekActivity.class);
                break;
            case 5: //关于我们
                if (aboutDialog == null) {
                    aboutDialog = new AboutDialog(context, R.style.dialog);
                }
                aboutDialog.show();
                break;
        }
    }


    @OnClick({R.id.me_fragment_agent, R.id.me_fragment_vip, R.id.me_fragment_share, R.id.me_go_to_login, R.id.login_out_ll, R.id.me_fragment_vip_tv2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_fragment_agent:
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    redirectActivity(AgentActivity.class);
                }

                break;
            case R.id.me_fragment_vip:
                redirectActivity(VipActivity.class);
                break;
            case R.id.me_fragment_share:
                redirectActivity(ShareActivity.class);
                break;
            case R.id.me_go_to_login: //登录
                UserBean userBean = XGUtil.getMyUserInfo();
                if (userBean == null) {
                    Intent intent = new Intent(getContext(), RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    String grop = userBean.getGroup_id();
                    if (grop.trim().equals("0")) { //游客
                        Intent intent = new Intent(getContext(), RegistActivity.class);
                        redirectActivityForResult(intent, 1);
                    } else {
                        redirectActivity(VipActivity.class);
                    }
                }

                break;

            case R.id.login_out_ll:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                redirectActivityForResult(intent, 2);
                break;

            case R.id.me_fragment_vip_tv2:
                if (me_fragment_vip_tv2.getText().toString().contains("VIP已过期")) {
                    redirectActivity(VipActivity.class);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.e("MeFragment", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1001) {
            setBaseDate();
        } else if (requestCode == 2 && resultCode == 1002) {
            setBaseDate();
        }
    }

    private List<PageBean> pageBeans = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        if (VipActivity.isRefresh) {
            setBaseDate();
            VipActivity.isRefresh = false;
        }
    }

    private void setDefData() {
        PageBean bean1 = new PageBean();
        bean1.setName("观看历史");
        bean1.setText("好片继续看");
        bean1.setBac(R.mipmap.me_history);
        pageBeans.add(bean1);

        PageBean bean2 = new PageBean();
        bean2.setName("视频下载");
        bean2.setText("视频云缓存，播放更流畅");
        bean2.setBac(R.mipmap.me_download);
        pageBeans.add(bean2);

        PageBean bean3 = new PageBean();
        bean3.setName("常见问题");
        bean3.setText("更多");
        bean3.setBac(R.mipmap.me_problem);
        pageBeans.add(bean3);

        PageBean bean4 = new PageBean();
        bean4.setName("检查更新");
        bean4.setText("当前版本V" + XGUtil.getCurrentAppVersion(context));
        bean4.setBac(R.mipmap.me_refresh);
        pageBeans.add(bean4);

//        PageBean bean5 = new PageBean();
//        bean5.setName("清理缓存");
//        bean5.setText("当前版本V1.0.0");
//        bean5.setBac(R.mipmap.me_history);
//        pageBeans.add(bean5);

        PageBean bean6 = new PageBean();
        bean6.setName("意见反馈");
        bean6.setText("反馈赢好礼");
        bean6.setBac(R.mipmap.me_distance);
        pageBeans.add(bean6);

        PageBean bean7 = new PageBean();
        bean7.setName("关于我们");
        bean7.setText("VIP你妹");
        bean7.setBac(R.mipmap.me_info);
        pageBeans.add(bean7);
    }


}
