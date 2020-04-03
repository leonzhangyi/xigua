package com.water.melon.ui.me.agent.setting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.water.melon.R;
import com.water.melon.base.ui.BaseActivity;
import com.water.melon.net.bean.MyAgentBean;
import com.water.melon.ui.in.AdapterItemClick;
import com.water.melon.ui.me.agent.create.CreateCodeAdapter;
import com.water.melon.utils.LoadingUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.views.PayDialog;
import com.water.melon.views.SaveAgentDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SettingAgentActivity extends BaseActivity implements SettingAgentContract.View {
    @BindView(R.id.setting_agent_wx)
    EditText setting_agent_wx;

    @BindView(R.id.setting_agent_phone)
    EditText setting_agent_phone;

    @BindView(R.id.setting_agent_ryc)
    RecyclerView recyclerView;

    @BindView(R.id.setting_agent_huifu)
    RelativeLayout setting_agent_huifu;
    @BindView(R.id.setting_agent_save)
    RelativeLayout setting_agent_save;

    private SettingAgentPresent present;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.layout_setting_agent;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        new SettingAgentPresent(this, this);
        present.start();
    }

    @Override
    protected void onClickTitleBack() {

    }

    @Override
    protected void onClickTitleRight() {

    }

    MyAgentBean request;
    private SettingAgentAdapter adapter;

    @Override
    public void initView() {
        setToolBarLeftView(R.mipmap.back_left);
        setTitleName("代理设置");
        setTitleNameColor(R.color.black);

        adapter = new SettingAgentAdapter();
        adapter.setEnableLoadMore(true);//这里的作用是防止下拉刷新的时候还可以上拉加载
        adapter.setListener(new SettingAgentAdapter.SaveEditListener() {
            @Override
            public void SaveEdit(MyAgentBean.Vips item, String string) {
                int k = -1;
                MyAgentBean.Vips vip = new MyAgentBean.Vips();
                vip.setType(item.getType());
                vip.setPrice(string);
                vip.setTitle(item.getTitle());
                if (vipsList == null) {
                    vipsList = new ArrayList<>();
                }
                for (int i = 0; i < vipsList.size(); i++) {
                    if (vipsList.get(i).getType().equals(item.getType())) {
                        vipsList.remove(i);
                        k = i;
                        break;
                    }
                }
                if (k != -1) {
                    vipsList.add(k, vip);
                }else{
                    vipsList.add( vip);
                }


//                changeList.add(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        LoadingUtil.init(this);
        if (request == null) {
            request = new MyAgentBean();
        }
        request.setHandle("before");
        present.setDate(request);
    }


    //    private Set<MyAgentBean.Vips> vipsList = new HashSet<>();
    private List<MyAgentBean.Vips> vipsList = new ArrayList<>();
//    private Set<MyAgentBean.Vips> defList = new HashSet<>();

    SaveAgentDialog dialog;
    SaveAgentDialog dialog1;

    @OnClick({R.id.toolbar_left_tv, R.id.setting_agent_save, R.id.setting_agent_huifu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_left_tv:
                this.finish();
                break;

            case R.id.setting_agent_save:
                if (dialog == null) {
                    dialog = new SaveAgentDialog(this, R.style.dialog);
                    dialog.setOnClickListener(new AdapterItemClick() {
                        @Override
                        public void onItemClick(int position) {
                            request.setHandle("afeter");
                            request.setPrices(vipsList.toString());
                            request.setTel(setting_agent_phone.getText().toString());
                            request.setWx(setting_agent_wx.getText().toString());
                            request.setDef(false);
                            present.setDate(request);
                        }
                    });
                }
                dialog.show();

                LogUtil.e("SettingAgent", "list = " + vipsList.toString());
                break;
            case R.id.setting_agent_huifu:
//                adapter.setNewData(mVips);
//                adapter.notifyDataSetChanged();

                if (dialog1 == null) {
                    dialog1 = new SaveAgentDialog(this, R.style.dialog);
                    dialog1.setOnClickListener(new AdapterItemClick() {
                        @Override
                        public void onItemClick(int position) {
                            request.setHandle("afeter");
                            request.setPrices(defArrayList.toString());
                            request.setTel(setting_agent_phone.getText().toString());
                            request.setWx(setting_agent_wx.getText().toString());
                            request.setDef(true);
                            present.setDate(request);
                        }
                    });
                }
                dialog1.show();
                break;
        }
    }


    @Override
    public void setPresenter(SettingAgentContract.Present presenter) {
        present = (SettingAgentPresent) presenter;
    }


    public List<MyAgentBean.Vips> mVips = new ArrayList<>();


    List<MyAgentBean.Vips> saveArrayList = new ArrayList<>();
    List<MyAgentBean.Vips> defArrayList = new ArrayList<>();

    @Override
    public void setDate(MyAgentBean myAgentBean) {
        mVips.clear();
//        defList.clear();
        defArrayList.clear();
        saveArrayList.clear();
        if (myAgentBean != null) {
            setting_agent_wx.setText(myAgentBean.getWx());
            setting_agent_phone.setText(myAgentBean.getTel());
            adapter.setNewData(myAgentBean.getVips());
            mVips.addAll(myAgentBean.getVips());
            saveArrayList.addAll(myAgentBean.getVips());

//            defList.addAll(myAgentBean.getDef());
            defArrayList.addAll(myAgentBean.getDef());

        }
    }

    @Override
    public void setSucDate(boolean def) {
        if (def) {
            adapter.setNewData(defArrayList);
        } else {
            adapter.setNewData(vipsList);
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
}
