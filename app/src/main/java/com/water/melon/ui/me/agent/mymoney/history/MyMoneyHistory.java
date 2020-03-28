package com.water.melon.ui.me.agent.mymoney.history;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.MyMoneyBean;
import com.water.melon.ui.me.agent.mymoney.MyMoneyAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MyMoneyHistory extends BaseActivity implements MyMoneyHistoryContract.View {

    @BindView(R.id.my_money_history_all_rl)
    RelativeLayout my_money_history_all_rl;
    @BindView(R.id.my_money_history_apply_rl)
    RelativeLayout my_money_history_apply_rl;
    @BindView(R.id.my_money_history_finish_rl)
    RelativeLayout my_money_history_finish_rl;
    @BindView(R.id.my_money_history_unfinish_rl)
    RelativeLayout my_money_history_unfinish_rl;

    @BindView(R.id.agent_my_user_rec)
    RecyclerView recyclerView;

    private MyMoneyHistoryPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_my_money_history;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new MyMoneyHistoryPresent(this, this);
        present.start();
    }

    private int page = 1;
    MyMoneyBean request;
    private int type = -1;

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    private MyMoneyHistoryAdapter myUserTotalAdapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("提现记录");
        setTitleNameColor(R.color.black);

        myUserTotalAdapter = new MyMoneyHistoryAdapter();
//        agentUserAdapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        myUserTotalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myUserTotalAdapter);


        type = -1;
        setType();
        if (request == null) {
            request = new MyMoneyBean();
            request.setPage(page + "");
            request.setRows("20");
            request.setType(type + "");
        }
        present.getHistoryData(request);


    }

    private void loadMore() {
        page++;
        request.setPage(page + "");
        present.getHistoryData(request);
    }


    @OnClick({R.id.toolbar_left_tv, R.id.my_money_history_all_rl, R.id.my_money_history_apply_rl, R.id.my_money_history_finish_rl, R.id.my_money_history_unfinish_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;
            case R.id.my_money_history_all_rl:
                type = -1;
                doItemClick();
                break;
            case R.id.my_money_history_apply_rl:
                type = 0;
                doItemClick();
                break;
            case R.id.my_money_history_finish_rl:
                type = 1;
                doItemClick();
                break;
            case R.id.my_money_history_unfinish_rl:
                type = 2;
                doItemClick();
                break;
        }
    }

    private void doItemClick() {
        setType();
        page = 1;
        request.setPage(page + "");
        request.setType(type + "");
        present.getHistoryData(request);
    }

    @Override
    public void setPresenter(MyMoneyHistoryContract.Present presenter) {
        present = (MyMoneyHistoryPresent) presenter;
    }

    private void setType() {
        switch (type) {
            case -1:
                my_money_history_all_rl.setSelected(true);
                my_money_history_apply_rl.setSelected(false);
                my_money_history_finish_rl.setSelected(false);
                my_money_history_unfinish_rl.setSelected(false);
                break;
            case 0:
                my_money_history_all_rl.setSelected(false);
                my_money_history_apply_rl.setSelected(true);
                my_money_history_finish_rl.setSelected(false);
                my_money_history_unfinish_rl.setSelected(false);
                break;
            case 1:
                my_money_history_all_rl.setSelected(false);
                my_money_history_apply_rl.setSelected(false);
                my_money_history_finish_rl.setSelected(true);
                my_money_history_unfinish_rl.setSelected(false);
                break;
            case 2:
                my_money_history_all_rl.setSelected(false);
                my_money_history_apply_rl.setSelected(false);
                my_money_history_finish_rl.setSelected(false);
                my_money_history_unfinish_rl.setSelected(true);
                break;
        }

    }


    @Override
    public void setUserDate(List<MyMoneyBean.MyMoneyHistory> userInfos, boolean err) {
        if (err) {
            myUserTotalAdapter.loadMoreFail();
        } else {
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
}
