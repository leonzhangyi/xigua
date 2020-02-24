package com.water.melon.ui.me.dowload.offline_download;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequest;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.ClickTooQucik;
import com.water.melon.utils.NetworkUtils;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;
import com.water.melon.views.MessageButtonDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OffLineDownAdapter extends BaseRVListAdapter<LocalVideoInfo> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private boolean isEditMoudle = false;//是否开启编辑模式
    private List<String> checkPosition;//选中那个Item
    private boolean clickSelectAll = false;
    private AdapterListen adapterListen;
    private boolean hasPermission = false;

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

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
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

    public interface AdapterListen {
        void checkPermission();

    }

    public OffLineDownAdapter(List<LocalVideoInfo> datas, AdapterListen adapterListen) {
        super(datas);
        setNoBottomView(true);
        setEmptyMsg(MyApplication.getStringByResId(R.string.download_manage_offline_empty));
        this.adapterListen = adapterListen;
    }


    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloading, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder mHolder = (MyHolder) holder;
        mHolder.setData(getDatas().get(position), position);
        // 暂停或继续
        mHolder.itemView.setTag(R.id.tag_id1, getDatas().get(position));
        mHolder.itemView.setTag(R.id.tag_id2, position);
        mHolder.itemView.setTag(R.id.tag_id3, mHolder.itemOfflineSpeed);
        mHolder.itemView.setTag(R.id.tag_id4, mHolder);
        mHolder.itemView.setOnClickListener(this);

        // 删除
        mHolder.itemOfflineDelete.setTag(R.id.tag_id1, getDatas().get(position));
        mHolder.itemOfflineDelete.setTag(R.id.tag_id2, position);
        mHolder.itemOfflineDelete.setOnClickListener(this);

        //点击按钮 ：播放
        mHolder.itemOfflineImageLayout.setTag(R.id.tag_id1, getDatas().get(position));
        mHolder.itemOfflineImageLayout.setTag(R.id.tag_id2, position);
        mHolder.itemOfflineImageLayout.setTag(R.id.tag_id3, mHolder.itemOfflineSpeed);
        mHolder.itemOfflineImageLayout.setOnClickListener(this);
        if (isEditMoudle) {
            //编辑模式
            mHolder.itemOfflineDelete.setVisibility(View.INVISIBLE);
            mHolder.itemOfflineCheck.setTag(position + "");
            mHolder.itemOfflineCheck.setOnCheckedChangeListener(this);
            mHolder.itemOfflineCheck.setVisibility(View.VISIBLE);
            mHolder.itemView.setTag(mHolder.itemOfflineCheck);
            if (checkPosition.contains(position + "")) {
                mHolder.itemOfflineCheck.setChecked(true);
            } else {
                mHolder.itemOfflineCheck.setChecked(false);
            }
        } else {
            mHolder.itemOfflineDelete.setVisibility(View.VISIBLE);
            mHolder.itemOfflineCheck.setVisibility(View.GONE);
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
        if (ClickTooQucik.isFastClick() || null == adapterListen) {
            return;
        }
        adapterListen.checkPermission();
        if (!hasPermission) {
            ToastUtil.showToastShort(MyApplication.getStringByResId(R.string.permission_sd));
            return;
        }
        if (isEditMoudle) {
            //编辑模式
            if (view.getId() == R.id.item_offline_layout) {
                ((CheckBox) view.getTag()).performClick();
            }
            return;
        }
        LocalVideoInfo mapData = (LocalVideoInfo) view.getTag(R.id.tag_id1);
        int position = (int) view.getTag(R.id.tag_id2);
        if (null == mapData) {
            return;
        }
        switch (view.getId()) {
            case R.id.item_offline_layout:
                //开始/暂停下载
                starOrStopDownLoad(mapData, position, (TextView) view.getTag(R.id.tag_id3));
                break;
            case R.id.item_offline_delete:
                //删除
                deleteVedio(mapData, position, view.getContext());
                break;
            case R.id.item_offline_image_layout:
                //播放,编辑中不能播放
                if (!isEditMoudle) {
                    TextView speed = (TextView) view.getTag(R.id.tag_id3);
                    playView(mapData, position, speed.getText().toString(), view.getContext());
                }
                break;
        }
    }

    private void starOrStopDownLoad(final LocalVideoInfo mapData, final int position, final TextView speed) {
        String runState = mapData.getRunning();
        if (runState.equals(LocalVideoInfo.running_Star) && !"正在暂停中...".equals(speed.getText().toString())) {
            speed.setText("正在暂停中...");
            // 暂停当前下载的影片
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    try {
                        MyApplication.getp2p().P2Pdoxpause(mapData.getUrl().getBytes("GBK"));
                        emitter.onNext("stop");
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onNext("error");
                    }
                }
            }).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String o) {
                    if (o.equals("error")) {
                        ToastUtil.showToastShort("暂停失败");
                        return;
                    }
                    for (LocalVideoInfo item : MyApplication.downTaskPositionList) {
                        if (item.getTid().equals(mapData.getTid())) {
                            MyApplication.downTaskPositionList.remove(item);
                            break;
                        }
                    }
                    int tid = Integer.valueOf(mapData.getTid());
                    long dsize = MyApplication.getp2p().P2Pgetdownsize(tid);
                    long fsize = MyApplication.getp2p().P2Pgetfilesize(tid);
                    LocalVideoInfo data = getDatas().get(position);
                    data.setRunning(LocalVideoInfo.running_Stop);
                    data.setSpeed_info(MyApplication.getStringByResId(R.string.download_manage_stop));
//            data.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
//                    data.setInfo(FileUtil.getSize(fsize));
                    if (fsize == 0) {
                        data.setPercent(0 + "");
                    } else {
                        data.setPercent((int) (1000 * dsize / fsize) + "");
                    }
                    // 保存并刷新列表
                    XGUtil.saveList(getDatas());
                    notifyItemChanged(position);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                }
            });

        } else if (runState.equals(LocalVideoInfo.running_Stop) && !"正在连接中...".equals(speed.getText().toString())) {
            //是否开启手机网络
            int netType = NetworkUtils.getConnectionType();
            switch (netType) {
                case 0:
                    ToastUtil.showToastShort("当前暂无网络~");
                    return;
                case 1:
                    ToastUtil.showToastShort("请注意，当前使用手机网络~");
                    break;
                case 2:
                    break;
            }
            if (MyApplication.downTaskPositionList.size() >= MyApplication.downLoadingMax) {
                ToastUtil.showToastShort("最多同时下载" + MyApplication.downLoadingMax + "个哦");
                speed.setText(MyApplication.getStringByResId(R.string.download_manage_stop));
                return;
            }
            speed.setText("正在连接中...");
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    try {
                        MyApplication.getp2p().P2Pdoxdownload(mapData.getUrl().getBytes("GBK"));
                        emitter.onNext("star");
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onNext("error");
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String o) {
                            if (o.equals("error")) {
                                ToastUtil.showToastShort("连接失败");
                                return;
                            } else if (getDatas().size() <= position) {
                                return;
                            }
                            if (!MyApplication.downTaskPositionList.contains(mapData)) {
                                MyApplication.downTaskPositionList.add(mapData);
                            }
                            LocalVideoInfo data = getDatas().get(position);
                            data.setRunning(LocalVideoInfo.running_Star);
                            data.setSpeed_info("正在连接中...");
                            // 保存并刷新列表
                            XGUtil.saveList(getDatas());
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    private void deleteVedio(final LocalVideoInfo mapData, final int position, final Context context) {
        new MessageButtonDialog(context, context.getString(R.string.message_dialog_title),
                String.format(context.getString(R.string.message_dialog_delete_video), mapData.getTitle()), false, new MessageButtonDialog.MyDialogOnClick() {
            @Override
            public void btnOk(MessageButtonDialog dialog) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                        try {
                            // 暂停当前影片的下载
                            MyApplication.getp2p().P2Pdoxpause(mapData.getUrl().getBytes("GBK"));
                            // 删除影片
                            MyApplication.getp2p().P2Pdoxdel(mapData.getUrl().getBytes("GBK"));
                            emitter.onNext(true);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            emitter.onNext(false);
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public synchronized void onNext(Boolean t) {
                                for (LocalVideoInfo item : MyApplication.downTaskPositionList) {
                                    if (item.getTid().equals(mapData.getTid())) {
                                        MyApplication.downTaskPositionList.remove(item);
                                        break;
                                    }
                                }
                                // 移除map集合中被删除影片的item
                                Iterator<LocalVideoInfo> iterator = getDatas().iterator();
                                while (iterator.hasNext()) {
                                    if (iterator.next().getTid().equals(mapData.getTid())) {
                                        iterator.remove();
                                        break;
                                    }
                                }
//                                deleteData(mapData);
                                // 保存数据
                                XGUtil.saveList(getDatas());
                                notifyDataSetChanged();
                                XGConstant.showSDSizeByUserClear = true;//通知更新手机内存大小
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void btnNo(MessageButtonDialog dialog) {

            }
        }).show();
    }

    private void playView(LocalVideoInfo mapData, int position, String speed, Context context) {
        int size = getDatas().size();
        for (int i = 0; i < size; i++) {
            LocalVideoInfo data = getDatas().get(i);
            if (i != position) {
                data.setRunning(LocalVideoInfo.running_Stop);
            } else {
                data.setRunning(LocalVideoInfo.running_Star);
            }
        }
        //进入全屏播放，点击返回就退回到下载管理,所以讲id置空，逻辑会更本地视频一样
//        mapData.setVod_id("");
        mapData.setSpeed_info(speed);
        XGUtil.playXG(mapData, (Activity) context);
        MyApplication.downTaskPositionList.clear();
        MyApplication.downTaskPositionList.add(mapData);
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_offline_check)
        CheckBox itemOfflineCheck;
        @BindView(R.id.item_offline_image)
        ImageView itemOfflineImage;
        @BindView(R.id.item_offline_image_layout)
        CardView itemOfflineImageLayout;
        @BindView(R.id.item_offline_name)
        TextView itemOfflineName;
        @BindView(R.id.item_offline_progress)
        ProgressBar itemOfflineProgress;
        @BindView(R.id.item_offline_speed)
        TextView itemOfflineSpeed;
        @BindView(R.id.item_offline_free)
        TextView itemOfflineFree;
        @BindView(R.id.item_offline_layout)
        LinearLayout itemOfflineLayout;
        @BindView(R.id.item_offline_delete)
        ImageView itemOfflineDelete;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(LocalVideoInfo mapData, int position) {
            itemOfflineName.setText(mapData.getTitle());
            itemOfflineFree.setText(mapData.getInfo());
            itemOfflineProgress.setProgress(Integer.valueOf(mapData.getPercent()));

            final String runState = mapData.getRunning();
            if (runState.equals(LocalVideoInfo.running_Stop)) {
                if (mapData.getSpeed_info().contains("失败")) {
                    itemOfflineSpeed.setText(mapData.getSpeed_info());
                } else {
                    itemOfflineSpeed.setText(MyApplication.getStringByResId(R.string.download_manage_stop));
                }

            } else if (runState.equals(LocalVideoInfo.running_Star)) {
                itemOfflineSpeed.setText(mapData.getSpeed_info());
            }

            String image = mapData.getVideoImage();
            GlideRequest glideRequest;
            if (TextUtils.isEmpty(image)) {
                glideRequest = GlideApp.with(itemOfflineImage.getContext()).load(mapData.getVideoImageByResour());
            } else {
                glideRequest = GlideApp.with(itemOfflineImage.getContext()).load(image);
            }
            glideRequest.placeholder(R.mipmap.bg_video_plact_horizontal)
                    .override(300, 200);
            glideRequest.into(itemOfflineImage);

        }
    }
}
