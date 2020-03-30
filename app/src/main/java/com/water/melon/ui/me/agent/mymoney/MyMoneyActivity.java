package com.water.melon.ui.me.agent.mymoney;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.MyMoneyBean;
import com.water.melon.ui.me.agent.mymoney.history.MyMoneyHistory;
import com.water.melon.ui.me.agent.mymoney.submit.MyMoneySubmitAcivity;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MyMoneyActivity extends BaseActivity implements MyMoneyContract.View {
    @BindView(R.id.my_money_current)
    TextView my_money_current;
    @BindView(R.id.my_money_last)
    TextView my_money_last;


    @BindView(R.id.my_money_tx)
    LinearLayout my_money_tx;

    @BindView(R.id.my_money_sub)
    RelativeLayout my_money_sub;

    @BindView(R.id.my_money_user_rl)
    RelativeLayout my_money_user_rl;
    @BindView(R.id.my_money_agent_rl)
    RelativeLayout my_money_agent_rl;
    @BindView(R.id.my_money_user_tv)
    TextView my_money_user_tv;
    @BindView(R.id.my_money_agent_tv)
    TextView my_money_agent_tv;


    @BindView(R.id.agent_user_start_time_tv)
    TextView startTimeTv;
    @BindView(R.id.agent_user_end_time_tv)
    TextView endTimeTv;

    @BindView(R.id.my_money_phone_et)
    EditText my_money_phone_et;

    @BindView(R.id.my_money_user_number)
    TextView my_money_user_number;
    @BindView(R.id.my_money_dollor)
    TextView my_money_dollor;

    @BindView(R.id.agent_my_user_rec)
    RecyclerView recyclerView;


    private MyMoneyPresent present;
    private MyMoneyBean bean;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.my_money;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new MyMoneyPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    BaseRequest<MyMoneyBean> baseRequest = new BaseRequest<>();
    private MyMoneyAdapter myUserTotalAdapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("我的收益");
        setTitleNameColor(R.color.black);

        initTimePicker();
        endTimeTv.setText(getCurrentTime());
        startTimeTv.setText(getCurrentTime());


        myUserTotalAdapter = new MyMoneyAdapter();
//        agentUserAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        myUserTotalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myUserTotalAdapter);


        if (bean == null) {
            bean = new MyMoneyBean();
        }
        bean.setHandle("before");
        baseRequest.setParameter(bean);
        present.getMyMoney(baseRequest);


        doFirstDate();

    }

    private void loadMore() {
        page++;
        baseRequest.setPage(page);
        present.getMyMoney(baseRequest);
    }

    private int timeCode = 1;

    @OnClick({R.id.toolbar_left_tv, R.id.agent_user_start_time, R.id.agent_user_end_time, R.id.agent_user_search, R.id.my_money_user_rl, R.id.my_money_agent_rl, R.id.agent_user_search_2, R.id.my_money_tx, R.id.my_money_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;

            case R.id.agent_user_start_time:
                timeCode = 1;
                if (pvTime != null) {
                    pvTime.show();
                }
                break;

            case R.id.agent_user_end_time:
                timeCode = 2;
                if (pvTime != null) {
                    pvTime.show();
                }
                break;

            case R.id.agent_user_search:
                page = 1;
                bean.setStart_date(startTimeTv.getText().toString());
                bean.setStop_date(endTimeTv.getText().toString());
                bean.setHandle("after");
                bean.setKeyword("");
                bean.setType(type);
                baseRequest.setPage(page);
                baseRequest.setParameter(bean);
                baseRequest.setRows(20);

                if (XGUtil.timeCompare(bean.getStart_date(), bean.getStop_date()) == 1) {
                    ToastUtil.showToastLong("请输入正确的时间进行筛选");
                } else {
                    present.getMyMoney(baseRequest);
                }

                break;

            case R.id.agent_user_search_2:
                String keyword = my_money_phone_et.getText().toString();
                if (keyword != null && !keyword.trim().equals("")) {
                    page = 1;
                    bean.setStart_date(startTimeTv.getText().toString());
                    bean.setStop_date(endTimeTv.getText().toString());
                    bean.setHandle("after");
                    bean.setKeyword(keyword);
                    bean.setType(type);
                    baseRequest.setPage(page);
                    baseRequest.setParameter(bean);
                    baseRequest.setRows(20);

                    if (XGUtil.timeCompare(bean.getStart_date(), bean.getStop_date()) == 1) {
                        ToastUtil.showToastLong("请输入正确的时间进行筛选");
                    } else {
                        present.getMyMoney(baseRequest);
                    }

                } else {
                    ToastUtil.showToastLong("请输入联系方式");
                }
                break;

            case R.id.my_money_user_rl://用户
                type = "user";
                setType();
                break;
            case R.id.my_money_agent_rl://代理
                type = "proxy";
                setType();
                break;

            case R.id.my_money_tx:
                redirectActivity(MyMoneyHistory.class);
                break;
            case R.id.my_money_sub:
                Bundle bundle = new Bundle();
                bundle.putString("myCurrentMoeny", avail);
                redirectActivity(MyMoneySubmitAcivity.class, bundle);
                break;
        }
    }

    private String avail = "0.00";

    private void setType() {
        if (type.equals("user")) {
            my_money_user_rl.setBackgroundResource(R.color.net_resource_item_tv);
            my_money_user_tv.setTextColor(MyApplication.getColorByResId(R.color.white));
            my_money_agent_rl.setBackgroundResource(R.color.my_money_type);
            my_money_agent_tv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
        } else {
            my_money_agent_rl.setBackgroundResource(R.color.net_resource_item_tv);
            my_money_agent_tv.setTextColor(MyApplication.getColorByResId(R.color.white));
            my_money_user_rl.setBackgroundResource(R.color.my_money_type);
            my_money_user_tv.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
        }
    }

    private TimePickerView pvTime;
    private int page = 1;

    private String type = "user";//proxy/user

    //TODO 搜索1900修改时间长度
    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                Toast.makeText(AgentUserActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
//                setDate("birthday", getTime(date));
                if (timeCode == 1) {
                    startTimeTv.setText(getTime(date));
                } else {
                    endTimeTv.setText(getTime(date));
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private String getCurrentTime() {
        String time = getTime(new Date(System.currentTimeMillis()));
//        time.replaceAll("-", "/");
        return time;
    }

    @Override
    public void setPresenter(MyMoneyContract.Present presenter) {
        present = (MyMoneyPresent) presenter;
    }

    @Override
    public void setMoneyDate(MyMoneyBean.BeforeBean beforeBean) {
        if (beforeBean != null) {
            my_money_last.setText("¥" + beforeBean.getTotal());
            my_money_current.setText("¥" + beforeBean.getCurrent());
            avail = beforeBean.getAvail();
        }
    }

    @Override
    public void setUserDate(MyMoneyBean.MyUserMoney beforeBean, boolean err) {
        if (err) {
            myUserTotalAdapter.loadMoreFail();
        } else {
            if (beforeBean == null) {
                beforeBean = new MyMoneyBean.MyUserMoney();
            } else {
                my_money_user_number.setText("用户数：" + beforeBean.getCount());
                my_money_dollor.setText("用户数：" + beforeBean.getProfit());

            }
            List<MyMoneyBean.UserMoney> userInfos = beforeBean.getList();
            if (userInfos == null) {
                userInfos = new ArrayList<>();
            }
            if (page == 1) {
                myUserTotalAdapter.setNewData(userInfos);

            } else {
                if (userInfos == null || userInfos.size() == 0) {
                    myUserTotalAdapter.loadMoreEnd();
                } else {
                    myUserTotalAdapter.addData(userInfos);
                    myUserTotalAdapter.loadMoreComplete();
                }

            }

        }


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

    private void doFirstDate() {
        bean.setStart_date(startTimeTv.getText().toString());
        bean.setStop_date(endTimeTv.getText().toString());
        bean.setHandle("after");
        bean.setKeyword("");
        bean.setType(type);
        baseRequest.setPage(page);
        baseRequest.setParameter(bean);
        baseRequest.setRows(20);

        present.getMyMoney(baseRequest);
    }
}
