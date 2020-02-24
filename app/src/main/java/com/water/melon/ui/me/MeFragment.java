package com.water.melon.ui.me;

import android.os.Bundle;
import android.view.View;

import com.water.melon.R;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.presenter.MePresenter;
import com.water.melon.presenter.contract.MeContract;
import com.water.melon.ui.in.AdapterItemClick;
import com.water.melon.ui.me.dowload.DownLoadActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class MeFragment extends BaseFragment implements MeContract.View, AdapterItemClick {
    @BindView(R.id.me_recy)
    RecyclerView recyclerView;

    private MePresenter mePresenter;

    private MePageAdapter mePageAdapter;

    protected int getLayoutId() {
        return R.layout.me_fragment;
    }

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        new MePresenter(this, this);
        mePresenter.start();
    }

    @Override
    public void initView() {
        setDefData();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mePageAdapter = new MePageAdapter(pageBeans, context);
        mePageAdapter.setAdapterItemClick(this);
        recyclerView.setAdapter(mePageAdapter);

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
    public void onItemClick(int position) {
        switch (position) {
            case 0: //观看历史

                break;
            case 1://视频下载
                redirectActivity(DownLoadActivity.class);
                break;
        }
    }

    private List<PageBean> pageBeans = new ArrayList<>();

    private void setDefData() {
        PageBean bean1 = new PageBean();
        bean1.setName("观看历史");
        bean1.setText("好片继续看");
        pageBeans.add(bean1);

        PageBean bean2 = new PageBean();
        bean2.setName("视频下载");
        bean2.setText("视频云缓存，播放更流畅");
        pageBeans.add(bean2);

        PageBean bean3 = new PageBean();
        bean3.setName("常见问题");
        bean3.setText("更多");
        pageBeans.add(bean3);

        PageBean bean4 = new PageBean();
        bean4.setName("检查更新");
        bean4.setText("更多");
        pageBeans.add(bean4);

        PageBean bean5 = new PageBean();
        bean5.setName("清理缓存");
        bean5.setText("当前版本V1.0.0");
        pageBeans.add(bean5);

        PageBean bean6 = new PageBean();
        bean6.setName("意见反馈");
        bean6.setText("反馈赢好礼");
        pageBeans.add(bean6);

        PageBean bean7 = new PageBean();
        bean7.setName("关于我们");
        bean7.setText("新西瓜");
        pageBeans.add(bean7);
    }


}
