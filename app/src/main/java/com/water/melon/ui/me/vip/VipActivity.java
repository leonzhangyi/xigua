package com.water.melon.ui.me.vip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.net.bean.UserBean;
import com.water.melon.ui.in.VipPayItemClick;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.ToastUtil;
import com.water.melon.views.PayDialog;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class VipActivity extends BaseActivity implements VipContract.View {
    @BindView(R.id.recycleview_vip)
    RecyclerView recyclerView;

    @BindView(R.id.layout_agent_phone_et)
    EditText layout_agent_phone_et;

    @BindView(R.id.layout_vip_vip_name)
    TextView layout_vip_vip_name;
    @BindView(R.id.layout_vip_vip_time)
    TextView layout_vip_vip_time;


    private VipPresent vipPresent;

    private VipPayAdapter vipPayAdapter;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_vip;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setToolBarLeftView(R.mipmap.back_left);
//        setToolBarRightView("充值记录", R.color.net_resource_item_tv);
        setTitleName("会员中心");
        setTitleNameColor(R.color.black);
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

    @Override
    public void initView() {
        vipPayAdapter = new VipPayAdapter();
        vipPayAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        vipPayAdapter.setItemClick(new VipPayItemClick() {
            @Override
            public void onItemClick(VipBean item) {
                vipPresent.doPay(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vipPayAdapter);


        setBaseDate();

        vipPresent.getVipDate();
    }

    @Override
    public void setPresenter(VipContract.Present presenter) {
        this.vipPresent = (VipPresent) presenter;
    }

    @Override
    public void setVipDate(List<VipBean> vipBeans) {
        if (vipBeans != null && vipBeans.size() > 0) {
            vipPayAdapter.addData(vipBeans);
        }
    }

    private PayDialog payDialog;

    @Override
    public void setPayMethod(VipBean vipBean) {
        if (payDialog == null) {
            payDialog = new PayDialog(this, R.style.dialog);
        }
        payDialog.show();
        payDialog.setData(vipBean);
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

    private void setDateInfo(UserBean userBean) {
        int vip = Integer.parseInt(userBean.getVip().trim());
        switch (vip) {
            case 1:
                layout_vip_vip_name.setText("临时会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            case 2:
                layout_vip_vip_name.setText("周卡会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            case 3:
                layout_vip_vip_name.setText("月卡会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            case 4:
                layout_vip_vip_name.setText("季卡会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            case 5:
                layout_vip_vip_name.setText("年卡会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            case 6:
                layout_vip_vip_name.setText("永久卡会员");
                layout_vip_vip_time.setText(userBean.getEnd_date());
                break;
            default:
                layout_vip_vip_name.setText("游客用户");
                layout_vip_vip_time.setText("会员失效");
                break;

        }
    }
}
