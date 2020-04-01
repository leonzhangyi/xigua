package com.water.melon.ui.me.dowload.download_down;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequest;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.ClickTooQucik;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.MessageButtonDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadDoneAdapter extends BaseRVListAdapter<LocalVideoInfo> implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private boolean isEditMoudle;//是否开启编辑模式
    private List<String> checkPosition;//选中那个Item
    private boolean clickSelectAll = false;

    public void setEditMoudle(boolean editMoudle) {
        checkPosition = new ArrayList<>();
        isEditMoudle = editMoudle;
        notifyDataSetChanged();
    }

    public boolean isEditMoudle() {
        return isEditMoudle;
    }

    //全选
    public void selectAllItem(boolean b) {
        if (checkPosition == null) {
            checkPosition = new ArrayList<>();
        }
        clickSelectAll = b;
        if (!b) {
            checkPosition.clear();
        } else {
            //全部选中
            int size = getDatas().size();
            checkPosition.clear();
            for (int i = 0; i < size; i++) {
                checkPosition.add(i + "");
            }
        }
        notifyDataSetChanged();
    }

    public boolean isClickSelectAll() {
        return clickSelectAll;
    }

    public void setClickSelectAll(boolean clickSelectAll) {
        this.clickSelectAll = clickSelectAll;
    }

    //获取选中的Item下标
    public List<String> getCheckPosition() {
        return checkPosition;
    }

    public DownloadDoneAdapter(List<LocalVideoInfo> datas) {
        super(datas);
        setNoBottomView(true);
        setEmptyMsg(MyApplication.getStringByResId(R.string.download_manage_done_empty));
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_done, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        mViewHolder.setData(getDatas().get(position));
        //删除按钮
        mViewHolder.itemDownloadDoneDelete.setTag(R.id.tag_id1, getDatas().get(position));
        mViewHolder.itemDownloadDoneDelete.setTag(R.id.tag_id2, position);
        mViewHolder.itemDownloadDoneDelete.setOnClickListener(this);
        //播放按钮
        mViewHolder.itemDownloadDoneImageLayout.setTag(R.id.tag_id1, getDatas().get(position));
        mViewHolder.itemDownloadDoneImageLayout.setOnClickListener(this);
        if (isEditMoudle) {
            //编辑模式
            mViewHolder.itemDownloadDoneDelete.setVisibility(View.INVISIBLE);
            mViewHolder.itemDownloadDoneCheck.setTag(position + "");
            mViewHolder.itemDownloadDoneCheck.setOnCheckedChangeListener(this);
            mViewHolder.itemDownloadDoneCheck.setVisibility(View.VISIBLE);
            mViewHolder.itemDownloadDoneLayout.setTag(mViewHolder.itemDownloadDoneCheck);
            mViewHolder.itemDownloadDoneLayout.setOnClickListener(this);
            if (checkPosition.contains(position + "")) {
                mViewHolder.itemDownloadDoneCheck.setChecked(true);
            } else {
                mViewHolder.itemDownloadDoneCheck.setChecked(false);
            }
        } else {
            mViewHolder.itemDownloadDoneDelete.setVisibility(View.VISIBLE);
            mViewHolder.itemDownloadDoneCheck.setVisibility(View.GONE);
            mViewHolder.itemDownloadDoneLayout.setOnClickListener(null);
            mViewHolder.itemDownloadDoneLayout.setTag(null);
        }
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String position = (String) compoundButton.getTag();
        if (b && !checkPosition.contains(position)) {
            checkPosition.add(position);
        } else if (!b && checkPosition.contains(position)) {
            checkPosition.remove(position);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickTooQucik.isFastClick()) {
            return;
        }
        if (isEditMoudle) {
            if (view.getId() == R.id.item_download_done_layout) {
                ((CheckBox) view.getTag()).performClick();
            }
            return;
        }
        LocalVideoInfo mapData = (LocalVideoInfo) view.getTag(R.id.tag_id1);

        switch (view.getId()) {
            case R.id.item_download_done_delete:
                //删除
                int position = (int) view.getTag(R.id.tag_id2);
                deleteVideo(mapData, position, view.getContext());
                break;
            case R.id.item_download_done_image_layout:
                //播放
                if (!isEditMoudle) {
                    playVideo(mapData, view.getContext());
                }
                break;
        }
    }

    //删除影片
    private void deleteVideo(final LocalVideoInfo videoInfo, final int position, final Context mContext) {
        new MessageButtonDialog(mContext, mContext.getString(R.string.message_dialog_title),
                String.format(mContext.getString(R.string.message_dialog_delete_video), videoInfo.getTitle()), false, new MessageButtonDialog.MyDialogOnClick() {
            @Override
            public void btnOk(MessageButtonDialog dialog) {
                try {
                    MyApplication.getp2p().P2Pdoxdel(videoInfo.getUrl().getBytes("GBK"));
                    deleteData(position);//移除当前item
                    //重新保存已下载的电影信息
                    XGUtil.saveCacheList(getDatas());
                    //刷新列表
                    notifyItemRemoved(position);
                    XGConstant.showSDSizeByUserClear = true;//通知更新手机内存大小
                } catch (UnsupportedEncodingException e) {
                    ToastUtil.showToastShort(MyApplication.getStringByResId(R.string.do_error));
                    e.printStackTrace();
                }
            }

            @Override
            public void btnNo(MessageButtonDialog dialog) {

            }
        }).show();
    }

    private void playVideo(LocalVideoInfo mapData, Context mContext) {
        mapData.setDone(true);
        XGUtil.playXG(mapData, (Activity) mContext);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_download_done_layout)
        LinearLayout itemDownloadDoneLayout;
        @BindView(R.id.item_download_done_check)
        CheckBox itemDownloadDoneCheck;
        @BindView(R.id.item_download_done_image)
        ImageView itemDownloadDoneImage;
        @BindView(R.id.item_download_done_image_layout)
        CardView itemDownloadDoneImageLayout;
        @BindView(R.id.item_download_done_delete)
        ImageView itemDownloadDoneDelete;
        @BindView(R.id.item_download_done_name)
        TextView itemDownloadDoneName;
        @BindView(R.id.item_download_done_free)
        TextView itemDownloadDoneFree;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(LocalVideoInfo mapData) {
            if (mapData.getInfo().equals("0.0 KB")) {
                mapData.setInfo("");
            }
            itemDownloadDoneFree.setText(mapData.getInfo());
            String name = mapData.getTitle();
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf("."));
            }
            itemDownloadDoneName.setText(name);

            String image = mapData.getVideoImage();
            GlideRequest glideRequest;
            if (TextUtils.isEmpty(image)) {
                glideRequest = GlideApp.with(itemDownloadDoneImage.getContext()).load(mapData.getVideoImageByResour());
            } else {
                glideRequest = GlideApp.with(itemDownloadDoneImage.getContext()).load(image);
            }
            glideRequest.placeholder(R.mipmap.bg_video_plact_horizontal)
                    .override(300, 200);
            glideRequest.into(itemDownloadDoneImage);
        }
    }
}
