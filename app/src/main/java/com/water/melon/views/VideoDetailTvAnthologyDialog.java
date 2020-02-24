package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.water.melon.R;
import com.water.melon.constant.XGConstant;
import com.water.melon.ui.netresource.SearchVideoInfoBean;
import com.water.melon.ui.player.adapter.TvAnthologyCountAdapter;
import com.water.melon.utils.ui.recyler_view_item.GridSpacingItemDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 创建者： awa.pi 在 2019/7/5.
 * 视频电视剧选集弹窗
 */

public class VideoDetailTvAnthologyDialog extends DialogFragment {
    @BindView(R.id.dialog_video_detail_tv_close)
    TextView dialogVideoDetailTvClose;
    @BindView(R.id.dialog_video_detail_tv_rv)
    RecyclerView dialogVideoDetailTvRv;

    private Unbinder unbinder;
    private int dilogHeight;
    private TvAnthologyCountAdapter tvAnthologyCountAdapter;
    private TvAnthologyCountAdapter.AdapterLsiten adapterLsiten;

    public void setTvPositon(int tvPositon) {
        if (null != tvAnthologyCountAdapter) {
            tvAnthologyCountAdapter.setCheckPositon(tvPositon);
        }
    }

    public void setAdapterLsiten(TvAnthologyCountAdapter.AdapterLsiten adapterLsiten) {
        this.adapterLsiten = adapterLsiten;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //去除标题栏
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = inflater.inflate(R.layout.dialog_video_detail_tv_antthology, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getArguments()) {
            return;
        }
        List<SearchVideoInfoBean.Torrents> tvAnthologys = (List<SearchVideoInfoBean.Torrents>) getArguments().getSerializable(XGConstant.KEY_LIST_DATA);
        if (tvAnthologys == null) {
            return;
        }
        String tvCount = getArguments().getString(XGConstant.KEY_DATA);
        dilogHeight = getArguments().getInt(XGConstant.KEY_DATA_2);
        setdata(tvCount, tvAnthologys);
    }

    private void setdata(String counts, List<SearchVideoInfoBean.Torrents> tvAnthologys) {
        dialogVideoDetailTvClose.setText(String.format(getString(R.string.video_tv_count), counts));
        dialogVideoDetailTvRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        dialogVideoDetailTvRv.addItemDecoration(new GridSpacingItemDecoration(4, 30, true));
        tvAnthologyCountAdapter = new TvAnthologyCountAdapter(tvAnthologys, adapterLsiten, 2);
        int tvPositon = getArguments().getInt(XGConstant.KEY_DATA_3);
        tvAnthologyCountAdapter.initCheckPositon(tvPositon);
        dialogVideoDetailTvRv.setAdapter(tvAnthologyCountAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
//            dialog.setCanceledOnTouchOutside(false);//点击空白页无效
//            dialog.setCancelable(false);//按返回按钮无效
            Window window = dialog.getWindow(); //得到对话框
            window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
//            window.setWindowAnimations(R.style.DialogTopAnimation);
            window.setDimAmount(0.5f);
            WindowManager.LayoutParams wl = window.getAttributes();
            //根据x，y坐标设置窗口需要显示的位置
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = dilogHeight;
//            wl.y = -280; //y小于0上移，大于0下移
            wl.gravity = Gravity.BOTTOM; //设置重力
            window.setAttributes(wl);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick(R.id.dialog_video_detail_tv_close)
    public void onViewClicked() {
        dismiss();
    }
}
