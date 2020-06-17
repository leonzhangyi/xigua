package com.water.melon.ui.me.vip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.ui.in.PayDialogClick;
import com.water.melon.ui.in.VipPayItemClick;
import com.water.melon.ui.in.VipPayItemClick2;
import com.water.melon.ui.login.RegistActivity;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.PayDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class VipActivity extends BaseActivity implements VipContract.View {

    public static boolean isRefresh = false;

    @BindView(R.id.recycleview_vip)
    RecyclerView recyclerView;

    @BindView(R.id.layout_pay_recy)
    RecyclerView layout_pay_recy;

    @BindView(R.id.layout_agent_phone_et)
    EditText layout_agent_phone_et;

    @BindView(R.id.layout_vip_vip_name)
    TextView layout_vip_vip_name;
    @BindView(R.id.layout_vip_vip_time)
    TextView layout_vip_vip_time;

    @BindView(R.id.status_bar_view)
    View status_bar_view;

    @BindView(R.id.layout_agent_vip_sure)
    View layout_agent_vip_sure;


    private VipPresent vipPresent;

    private VipPayAdapter1 vipPayAdapter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_vip_1;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {

        new VipPresent(this, this);
        vipPresent.start();
    }

    @OnClick({R.id.toolbar_left_tv, R.id.layout_agent_vip_sure})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toolbar_left_tv:
                onClickTitleBack();
                break;

            case R.id.layout_agent_vip_sure:
                String text = layout_agent_phone_et.getText().toString();
                if (text != null && !text.trim().equals("")) {
                    CreateCodeBean.UserCodeBean userCodeBean = new CreateCodeBean.UserCodeBean();
                    userCodeBean.setCode(text.trim());
                    vipPresent.doBdVip(userCodeBean);
                } else {
                    ToastUtil.showToastLong("请输入激活码");
                }

                break;
        }
    }

    @Override
    protected void onClickTitleBack() {
        this.finish();
    }

    @Override
    protected void onClickTitleRight() {

    }

    private PayDialogAdapter1 payDialogAdapter;
    private String order;

    @Override
    public void initView() {
        vipPayAdapter = new VipPayAdapter1();
        vipPayAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        vipPayAdapter.setItemClick(new VipPayItemClick() {
            @Override
            public void onItemClick(VipBean item) {
//                UserBean userBean1 = XGUtil.getMyUserInfo();
//                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
//                    ToastUtil.showToastShort("请先绑定手机号");
//                    Intent intent = new Intent(VipActivity.this, RegistActivity.class);
//                    redirectActivityForResult(intent, 1);
//                } else {
                for (int i = 0; i < vipBeans.size(); i++) {
                    if (vipBeans.get(i).getId().equals(item.getId())) {
                        vipBeans.get(i).setSelect(true);
                    } else {
                        vipBeans.get(i).setSelect(false);
                    }
                }
                vipPayAdapter.setNewData(vipBeans);
                vipPresent.doPay(item);
//                }


            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(vipPayAdapter);

        payDialogAdapter = new PayDialogAdapter1();
        payDialogAdapter.setOnItemClick(new VipPayItemClick2() {
            @Override
            public void onItemClick(VipBean.PayMethod item) {
                UserBean userBean1 = XGUtil.getMyUserInfo();
                if (userBean1 == null || userBean1.getGroup_id().trim().equals("0")) { //游客
                    ToastUtil.showToastShort("请先绑定手机号");
                    Intent intent = new Intent(VipActivity.this, RegistActivity.class);
                    redirectActivityForResult(intent, 1);
                } else {
                    vipPresent.doCPay(order, item.getMethod());
                }

            }
        });

        layout_pay_recy.setLayoutManager(new LinearLayoutManager(this));
        layout_pay_recy.setAdapter(payDialogAdapter);


        setBaseDate();

        vipPresent.getVipDate();
    }

    @Override
    public void setPresenter(VipContract.Present presenter) {
        this.vipPresent = (VipPresent) presenter;
    }

    List<VipBean> vipBeans = new ArrayList<>();

    @Override
    public void setVipDate(List<VipBean> vipBeans) {
        this.vipBeans.clear();
        if (vipBeans != null && vipBeans.size() > 0) {
            vipBeans.get(0).setSelect(true);
            this.vipBeans = vipBeans;
            vipPayAdapter.setNewData(vipBeans);
//            vipPayAdapter.addData(vipBeans);
            vipPresent.doPay(vipBeans.get(0));
        }
    }

    private PayDialog payDialog;

    @Override
    public void setPayMethod(VipBean vipBean) {
        if (vipBean != null) {
            order = vipBean.getOrder_id();
            payDialogAdapter.setNewData(vipBean.getMethod());
        }


//        if (payDialog == null) {
//            payDialog = new PayDialog(this, R.style.dialog);
//            payDialog.setBuyClick(new PayDialogClick() {
//                @Override
//                public void onItemClick(String order, String method) {
//                    vipPresent.doCPay(order, method);
//                }
//            });
//
//        }
//        payDialog.show();
//        payDialog.setData(vipBean);
    }

    @Override
    public void updata() {
        setBaseDate();
    }

    public void setBaseDate() {
        String baseInfo = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_WATER_USER_INFO, "");
        if (baseInfo != null && !baseInfo.trim().equals("") && !baseInfo.trim().equals("[]")) {
            UserBean userBean = (UserBean) GsonUtil.toClass(baseInfo, UserBean.class);
            if (userBean != null) {
                setDateInfo(userBean);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        vipPresent.updataUserInfo();
    }

    String url;

    @Override
    public void doPay(String result) {
        if (result != null && !result.equals("")) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.has("url")) {
                    url = obj.getString("url");
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setDateInfo(UserBean userBean) {
        int vip = Integer.parseInt(userBean.getVip().trim());
        String invite = userBean.getInvitation_code();
//        if (invite != null && !invite.trim().equals("")) {
//            layout_agent_phone_et.setText(invite);
//            layout_agent_phone_et.setEnabled(false);
//            layout_agent_vip_sure.setEnabled(false);
//        }
        String time = userBean.getEnd_date().split(" ")[0];
        switch (vip) {
            case 1:
                layout_vip_vip_name.setText("临时会员");
                layout_vip_vip_time.setText(time);
                break;
            case 2:
                layout_vip_vip_name.setText("周卡会员");
                layout_vip_vip_time.setText(time);
                break;
            case 3:
                layout_vip_vip_name.setText("月卡会员");
                layout_vip_vip_time.setText(time);
                break;
            case 4:
                layout_vip_vip_name.setText("季卡会员");
                layout_vip_vip_time.setText(time);
                break;
            case 5:
                layout_vip_vip_name.setText("年卡会员");
                layout_vip_vip_time.setText(time);
                break;
            case 6:
                layout_vip_vip_name.setText("永久卡会员");
                layout_vip_vip_time.setText(time);
                break;
            default:
                layout_vip_vip_name.setText("游客用户");
                layout_vip_vip_time.setText("会员失效");
                break;

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.e("MeFragment", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1001) {
            setBaseDate();
            isRefresh = true;
        }
    }

    @Override
    public void initImmersionBar() {
//        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarView(status_bar_view)
                .init();
//        ImmersionBar.with(this)
//                .hideBar(BarHide.FLAG_HIDE_BAR)
//                .init();
    }
}
