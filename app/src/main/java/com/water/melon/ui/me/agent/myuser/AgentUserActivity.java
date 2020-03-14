package com.water.melon.ui.me.agent.myuser;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.AgentUserBean;
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

public class AgentUserActivity extends BaseActivity implements AgentUserContract.View {

    @BindView(R.id.agent_my_user_rec)
    RecyclerView recyclerView;

    @BindView(R.id.agent_user_start_time)
    RelativeLayout startTime;

    @BindView(R.id.agent_user_end_time)
    RelativeLayout endTime;

    @BindView(R.id.agent_my_user_all)
    TextView agent_my_user_all;

    @BindView(R.id.agent_user_total)
    TextView agent_user_total;

    @BindView(R.id.agent_user_end_time_tv)
    TextView endTimeTv;
    @BindView(R.id.agent_user_start_time_tv)
    TextView startTimeTv;

    @BindView(R.id.agent_use_et)
    EditText keywordEt;

    private AgentUserPresent present;


    @Override
    public int getContentViewByBase(Bundle savedInstance) {
        return R.layout.layout_agent_my_user;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("我的用户");
        setTitleNameColor(R.color.black);
        setToolBarRightView("用户统计", R.color.net_resource_item_tv);

        new AgentUserPresent(this, this);
        present.start();

        initTimePicker();
        endTimeTv.setText(getCurrentTime());
        startTimeTv.setText(getCurrentTime());
    }

    private int timeCode = 1;
    private AgentUserBean userBean = new AgentUserBean();
    private BaseRequest<AgentUserBean> baseRequest = new BaseRequest<>();

    @OnClick({R.id.toolbar_left_tv, R.id.agent_user_start_time, R.id.agent_user_end_time, R.id.agent_my_user_all, R.id.agent_user_search,R.id.agent_user_one_search_rl})
    public void onClick(View view) {
        switch (view.getId()) {
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

            case R.id.toolbar_left_tv:
                this.finish();
                break;

            case R.id.agent_my_user_all:
                setAllBtn();
                break;

            case R.id.agent_user_search://日期搜索
                page = 1;
                if (agent_my_user_all.isSelected()) {
                    userBean.setStart_date("");
                    userBean.setEnd_date("");
                    userBean.setKeyword("");
                } else {
                    userBean.setStart_date(startTimeTv.getText().toString());
                    userBean.setEnd_date(endTimeTv.getText().toString());
                    userBean.setKeyword("");
                }
                baseRequest.setPage(page);
                baseRequest.setParameter(userBean);
                baseRequest.setRows(20);

                if (XGUtil.timeCompare(userBean.getStart_date(), userBean.getEnd_date()) == 1) {
                    ToastUtil.showToastLong("请输入正确的时间进行筛选");
                }else{
                    present.getAgentUser(baseRequest);
                }


                break;
            case R.id.agent_user_one_search_rl:  //精确搜索
                String keyword = keywordEt.getText().toString();
                if (keyword != null && !keyword.trim().equals("")) {
                    page = 1;
                    userBean.setStart_date("");
                    userBean.setEnd_date("");
                    userBean.setKeyword(keyword);
                    baseRequest.setPage(page);
                    baseRequest.setParameter(userBean);
                    baseRequest.setRows(20);
                    present.getAgentUser(baseRequest);
                } else {
                    ToastUtil.showToastLong("请输入关键字");
                }

                break;
        }
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private int page = 1;
    private AgentUserAdapter agentUserAdapter;

    @Override
    public void initView() {
        agentUserAdapter = new AgentUserAdapter();
//        agentUserAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        agentUserAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(agentUserAdapter);


        agent_my_user_all.setSelected(true);
        setAllBtn();
    }


    private void loadMore() {
        page++;
        baseRequest.setPage(page);
        present.getAgentUser(baseRequest);
    }


    @Override
    public void setPresenter(AgentUserContract.Present presenter) {
        present = (AgentUserPresent) presenter;
    }

    private TimePickerView pvTime;


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

    private void setAllBtn() {
        agent_my_user_all.setSelected(!agent_my_user_all.isSelected());

        if (agent_my_user_all.isSelected()) {
            agent_my_user_all.setBackgroundColor(MyApplication.getColorByResId(R.color.net_resource_item_tv));
            agent_my_user_all.setTextColor(MyApplication.getColorByResId(R.color.white));
        } else {
            agent_my_user_all.setBackgroundColor(MyApplication.getColorByResId(R.color.color_bac_un_sel));
            agent_my_user_all.setTextColor(MyApplication.getColorByResId(R.color.black_D9));
        }


    }

    @Override
    public void setAgentUser(AgentUserBean agentUserBean, boolean err) {
        if (err) {
            agentUserAdapter.loadMoreFail();
        } else {
            if (agentUserBean == null) {
                agentUserBean = new AgentUserBean();
            }
            List<AgentUserBean.UserInfo> userInfos = agentUserBean.getList();
            if (page == 1) {
                String total = agentUserBean.getCount();
                if (userInfos == null) {
                    userInfos = new ArrayList<>();
                }
                agentUserAdapter.setNewData(userInfos);
                if (total == null) {
                    total = "0";
                }
                agent_user_total.setText("总用户：" + total);

            } else {
                if (userInfos == null || userInfos.size() == 0) {
                    agentUserAdapter.loadMoreEnd();
                } else {
                    agentUserAdapter.addData(userInfos);
                    agentUserAdapter.loadMoreComplete();
                }

            }

        }


    }
}
