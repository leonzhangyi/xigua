package com.water.melon.ui.me.dowload.play_record;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.ClickTooQucik;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.MessageButtonDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayRecordAdapter extends BaseRVListAdapter<LocalVideoInfo> implements OnClickListener, CompoundButton.OnCheckedChangeListener {

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

    public PlayRecordAdapter(List<LocalVideoInfo> datas) {
        super(datas);
        setNoBottomView(true);
        setEmptyMsg(MyApplication.getStringByResId(R.string.download_manage_play_record_empty));
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;
        mViewHolder.setData(getDatas().get(position));
        //删除按钮
        mViewHolder.itemHistoryDelete.setTag(R.id.tag_id1, getDatas().get(position).getTitle());
        mViewHolder.itemHistoryDelete.setTag(R.id.tag_id2, position);
        mViewHolder.itemHistoryDelete.setOnClickListener(this);
        //播放按钮
        mViewHolder.itemHistoryImageLayout.setTag(R.id.tag_id1, getDatas().get(position));
        mViewHolder.itemHistoryImageLayout.setOnClickListener(this);
        if (isEditMoudle) {
            //编辑模式
            mViewHolder.itemHistoryDelete.setVisibility(View.INVISIBLE);
            mViewHolder.itemHistoryCheck.setTag(position + "");
            mViewHolder.itemHistoryCheck.setOnCheckedChangeListener(this);
            mViewHolder.itemHistoryCheck.setVisibility(View.VISIBLE);
            mViewHolder.itemHistoryLayout.setTag(mViewHolder.itemHistoryCheck);
            mViewHolder.itemHistoryLayout.setOnClickListener(this);
            if (checkPosition.contains(position + "")) {
                mViewHolder.itemHistoryCheck.setChecked(true);
            } else {
                mViewHolder.itemHistoryCheck.setChecked(false);
            }
        } else {
            mViewHolder.itemHistoryDelete.setVisibility(View.VISIBLE);
            mViewHolder.itemHistoryCheck.setVisibility(View.GONE);
            mViewHolder.itemHistoryLayout.setOnClickListener(null);
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
            if (view.getId() == R.id.item_history_layout) {
                ((CheckBox) view.getTag()).performClick();
            }
            return;
        }
        switch (view.getId()) {
            case R.id.item_history_delete:
                //删除
                String name = (String) view.getTag(R.id.tag_id1);
                int position = (int) view.getTag(R.id.tag_id2);
                deleteVideo(name, position, view.getContext());
                break;
            case R.id.item_history_image_layout:
                //播放
                if (!isEditMoudle) {
                    playVideo((LocalVideoInfo) view.getTag(R.id.tag_id1), view.getContext());
                }
                break;
        }
    }

    private void deleteVideo(final String name, final int position, final Context mContext) {
        new MessageButtonDialog(mContext, mContext.getString(R.string.message_dialog_title),
                String.format(mContext.getString(R.string.message_dialog_delete_video), name), false, new MessageButtonDialog.MyDialogOnClick() {
            @Override
            public void btnOk(MessageButtonDialog dialog) {
                dialog.dismiss();
                deleteData(position);
                //重新保存历史记录信息
                XGUtil.saveHistoryList(getDatas());
                //刷新列表
                notifyItemRemoved(position);
            }

            @Override
            public void btnNo(MessageButtonDialog dialog) {

            }
        }).show();
    }

    private void playVideo(LocalVideoInfo data, Context mContext) {
//        data.setVod_id("");
        String id = data.getVod_id();
//        if (TextUtils.isEmpty(id)) {
        XGUtil.playXG(data, (Activity) mContext);
//        } else {
        //网络资源播放过来的
//            VlcVideoBean vlcVideoBean = new VlcVideoBean();
//            vlcVideoBean.setVideoHttpUrl(data.getUrl());
//            vlcVideoBean.setVideoName(data.getTid());
//            vlcVideoBean.setVideoId(id);
//            XGUtil.playXG(vlcVideoBean, (Activity) mContext, false);
//        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_history_layout)
        LinearLayout itemHistoryLayout;
        @BindView(R.id.item_history_check)
        CheckBox itemHistoryCheck;
        @BindView(R.id.item_history_image)
        ImageView itemHistoryImage;
        @BindView(R.id.item_history_image_layout)
        CardView itemHistoryImageLayout;
        @BindView(R.id.item_history_delete)
        ImageView itemHistoryDelete;
        @BindView(R.id.item_history_name)
        TextView itemHistoryName;
        @BindView(R.id.item_history_play_position)
        TextView itemHistoryPlayPosition;
        @BindView(R.id.item_history_size)
        TextView itemHistorySize;

        private LinearLayout layout_delete;
        private TextView title, time;
        private String address;
        private Button layout_play;
        private String movieId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(LocalVideoInfo mapData) {
            address = mapData.getUrl();
            itemHistorySize.setText(mapData.getInfo());
            itemHistoryName.setText(mapData.getTitle());
            String dur = TextUtils.isEmpty(mapData.getDuration()) ? "0" : mapData.getDuration();
            int duration = Integer.parseInt(dur);
            if (duration > 0) {
                itemHistoryPlayPosition.setVisibility(View.VISIBLE);
                itemHistoryPlayPosition.setText("上次播放位置:" + caluteTime(duration));
            } else {
//                itemHistoryPlayPosition.setVisibility(View.GONE);
                itemHistoryPlayPosition.setText("上次播放位置：00:00");
            }

            String image = mapData.getVideoImage();
            GlideRequest glideRequest = GlideApp.with(itemHistoryImage.getContext())
                    .load(image);

            if (TextUtils.isEmpty(image)) {
                glideRequest.placeholder(R.mipmap.bg_local_video_default);
            } else {
                glideRequest.placeholder(R.mipmap.bg_video_plact_horizontal);
            }

            glideRequest.override(300, 200);
            glideRequest.into(itemHistoryImage);
        }

        private String caluteTime(int time) {
            StringBuilder stringBuilder = new StringBuilder();
            if (time <= 60) {
                stringBuilder.append("00:00:");
                if (time < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(time);
                } else {
                    stringBuilder.append(time);
                }
                return stringBuilder.toString();
            } else if (time <= 3600) {
                int mm = time / 60;
                int ss = time % 60;
                stringBuilder.append("00:");
                if (mm < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(mm);
                } else {
                    stringBuilder.append(mm);
                }
                stringBuilder.append(":");
                if (ss < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(ss);
                } else {
                    stringBuilder.append(ss);
                }
                return stringBuilder.toString();
            } else if (time < 3600 * 24) {
                int hh = time / 3600;
                int mm = (time % 3600) / 60;
                int ss = ((time % 3600) % 60) % 60;
                if (hh < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(hh);
                } else {
                    stringBuilder.append(hh);
                }
                stringBuilder.append(":");
                if (mm < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(mm);
                } else {
                    stringBuilder.append(mm);
                }
                stringBuilder.append(":");
                if (ss < 10) {
                    stringBuilder.append("0");
                    stringBuilder.append(ss);
                } else {
                    stringBuilder.append(ss);
                }
                return stringBuilder.toString();
            } else {
                return "00:00:00";
            }
        }
    }

}
