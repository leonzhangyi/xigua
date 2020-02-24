package com.water.melon.ui.me.dowload.offline_download;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.base.ui.BaseFragment;
import com.water.melon.constant.XGConstant;
import com.water.melon.evenbus.EvenBusEven;
import com.water.melon.ui.player.LocalVideoInfo;
import com.water.melon.utils.FileUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.NetworkUtils;
import com.water.melon.utils.SpacesItemDecoration;
import com.water.melon.utils.ToastUtil;
import com.water.melon.utils.XGUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 下载界面
 */
public class OffLineDownFragment extends BaseFragment implements OffLineDownAdapter.AdapterListen {
    @BindView(R.id.loading_lay)
    LinearLayout loadingLay;
    @BindView(R.id.fragment_offline_lv)
    RecyclerView fragmentOfflineLv;
    @BindView(R.id.edit_stop_all_btn)
    Button editStopAllBtn;
    @BindView(R.id.edit_select_all_btn)
    Button editSelectAllBtn;
    @BindView(R.id.edit_delete_btn)
    Button editDeleteBtn;
    @BindView(R.id.edit_btn)
    Button editBtn;
    @BindView(R.id.download_edit_layout)
    LinearLayout downloadEditLayout;

    private static final int MSG_UPDATE_TASK = 4;
    private long dsize = 0;// 当前所下载下来的文件大小
    private long fsize = 0;// 当前所下载文件的大小
    public static Disposable threadRun;
    public boolean flag = false;// 是否向handler发送消息
    private RxPermissions rxPermissions;
    //用来更新下载速度
    private LinearLayoutManager myLayoutManager;
    private OffLineDownAdapter mAdapter;

    @Override
    protected void onMyCreateView(View rootView, Bundle savedInstanceState) {
        // 注册广播
        EventBus.getDefault().register(this);

        initData();
        initListView();
    }

    private void initData() {
        List<LocalVideoInfo> mData = XGUtil.loadList();// 获取下载列表信息
        List<LocalVideoInfo> mNewDownList = new ArrayList<>();// 查看是否有下载完的数据没有更新到
        //为了防止从任务管理器中结束应用导致的数据不能保存
        for (LocalVideoInfo item : mData) {
            if (item.getPercent().equals("1000")) {
                item.setSpeed_info("下载完成");
                item.setRunning(LocalVideoInfo.running_finish);
//                    currentData.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
                if (TextUtils.isEmpty(item.getInfo()) || item.getInfo().equals("0.0 KB")) {
                    item.setInfo(FileUtil.getSize(fsize));
                }
                item.setSize(fsize + "");
                item.setPercent("1000");
                item.setTid("-1");
                item.setDone(true);

                // 将数据保存到已下载列表中mData
                ArrayList<LocalVideoInfo> mCacheData = XGUtil.loadCacheList();
                if (!mCacheData.contains(item)) {
                    mCacheData.add(0, item);
                    XGUtil.saveCacheList(mCacheData);
                }
            } else {
                String running = item.getRunning();
                boolean isRunning = false;
                if (running.equals(LocalVideoInfo.running_Star)) {
                    item.setRunning(LocalVideoInfo.running_Stop);
                }
                //防止未打开下载界面造成播放的电影，再次进来被自动暂停
                if (XGConstant.userSeeVideoId.equals(item.getTitle())) {
                    item.setRunning(LocalVideoInfo.running_Star);
                    isRunning = true;
                }
                XGConstant.userSeeVideoId = "";

                try {
                    int tid = MyApplication.getp2p().P2Pdoxadd(item.getUrl().getBytes("GBK"));
                    item.setTid(tid + "");
                    if (isRunning && !MyApplication.downTaskPositionList.contains(item)) {
                        MyApplication.downTaskPositionList.add(item);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mNewDownList.add(item);
            }
        }
        XGUtil.saveList(mNewDownList);
    }

    private void refreshData() {
        boolean hasChange = false;
        List<LocalVideoInfo> mData = XGUtil.loadList();// 获取下载列表信息
        List<LocalVideoInfo> mNewDownList = new ArrayList<>();// 查看是否有下载完的数据没有更新到
        //为了防止从任务管理器中结束应用导致的数据不能保存
        for (LocalVideoInfo item : mData) {
            if (item.getPercent().equals("1000")) {
                item.setSpeed_info(getStringByRes(R.string.download_over));
                item.setRunning(LocalVideoInfo.running_finish);
//                    currentData.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
                if (TextUtils.isEmpty(item.getInfo()) || item.getInfo().equals("0.0 KB")) {
                    item.setInfo(FileUtil.getSize(fsize));
                }
                item.setSize(fsize + "");
                item.setPercent("1000");
                item.setTid("-1");
                item.setDone(true);

                // 将数据保存到已下载列表中mData
                ArrayList<LocalVideoInfo> mCacheData = XGUtil.loadCacheList();
                if (!mCacheData.contains(item)) {
                    mCacheData.add(0, item);
                    XGUtil.saveCacheList(mCacheData);
                }
                hasChange = true;
            } else {
                mNewDownList.add(item);
            }
        }
        LogUtil.e("ffffffffffffff", "111111111111111111111111111");
        if (hasChange && null != mAdapter) {
            XGUtil.saveList(mNewDownList);
            mAdapter.setDatas(mNewDownList);
            // 发送广播更新下载完数据
            EventBus.getDefault().post(new EvenBusEven.DownLoadDoneEven());
            LogUtil.e("ffffffffffffff", "22222222222222222222222");
        }
    }

    @Override
    protected void lazyLoad() {
        if (isVisible && fragmentOfflineLv != null && null == mAdapter) {
            mAdapter = new OffLineDownAdapter(XGUtil.loadList(), this);
            fragmentOfflineLv.setAdapter(mAdapter);
            fragmentOfflineLv.post(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter.getDatas().size() > 0 && null != downloadEditLayout) {
                        editStopAllBtn.setVisibility(View.VISIBLE);
                        downloadEditLayout.setVisibility(View.VISIBLE);
                    }
                    if (null != loadingLay) {
                        loadingLay.setVisibility(View.GONE);
                    }
                }
            });
        }
        if (isVisible) {
            starOrStopNotifinDownLoadSpeed(true);
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        starOrStopNotifinDownLoadSpeed(false);
        if (null != mAdapter && mAdapter.isEditMoudle()) {
            editBtn.setText(MyApplication.getStringByResId(R.string.download_edit));
            mAdapter.setEditMoudle(false);
            mAdapter.setClickSelectAll(false);
            editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
            editSelectAllBtn.setVisibility(View.GONE);
            editDeleteBtn.setVisibility(View.GONE);
            editStopAllBtn.setVisibility(View.VISIBLE);
        }
    }

    public void starOrStopNotifinDownLoadSpeed(boolean flag) {
        if (flag) {
            if (null == threadRun) {
                startThread();// 开启线程
            }
        } else {
            if (null != threadRun) {
                threadRun.dispose();
                threadRun = null;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_offlinedown;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * init listview
     */
    private void initListView() {
        myLayoutManager = new LinearLayoutManager(getContext());
        fragmentOfflineLv.setLayoutManager(myLayoutManager);
        fragmentOfflineLv.setItemAnimator(null);
        fragmentOfflineLv.addItemDecoration(new SpacesItemDecoration(2));
    }


    private synchronized void updateProgressPartly2() {
        Iterator<LocalVideoInfo> iterator = MyApplication.downTaskPositionList.iterator();
        while (iterator.hasNext()) {
            LocalVideoInfo positionS = iterator.next();
            int size = mAdapter.getDatas().size();
            int position = -1;
            for (int i = 0; i < size; i++) {
                if (mAdapter.getDatas().get(i).getTid().equals(positionS.getTid())) {
                    position = i;
                    break;
                }
            }
            int firstItemPosition = myLayoutManager.findFirstVisibleItemPosition();
            int lastItemPosition = myLayoutManager.findLastVisibleItemPosition();
            if (position < firstItemPosition || position > lastItemPosition) {
                //当前更新的数据不在屏幕可见区域
                continue;
            }
            View view = myLayoutManager.findViewByPosition(position);
            if (null == view) {
                MyApplication.downTaskPositionList.remove(positionS);
                continue;
            }
            if (view.getTag(R.id.tag_id4) instanceof OffLineDownAdapter.MyHolder) {
                OffLineDownAdapter.MyHolder vh = (OffLineDownAdapter.MyHolder) view.getTag(R.id.tag_id4);
                int tid = Integer.parseInt(positionS.getTid());
                dsize = MyApplication.getp2p().P2Pgetdownsize(tid);
                fsize = MyApplication.getp2p().P2Pgetfilesize(tid);
                if (fsize == 0) {
                    try {
                        MyApplication.getp2p().P2Pdoxadd(positionS.getUrl().getBytes("GBK"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dsize = MyApplication.getp2p().P2Pgetdownsize(tid);
                    fsize = MyApplication.getp2p().P2Pgetfilesize(tid);
                }
                LogUtil.e("下载速度", "speed_info==" + dsize + "/" + fsize + "..." + MyApplication.getp2p().P2Pgetspeed(tid));
                LogUtil.e("下载速度", "更新位置" + position + "===tid=" + tid);
                int checSize = 0;
                try {
                    checSize = MyApplication.getp2p().P2Pdoxcheck(positionS.getUrl().getBytes("GBK"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LogUtil.e("下载是否完成", "===checSize=" + checSize);
                if (fsize == 0 && dsize == 0 && checSize == 0) {
//                    iterator.remove();
                    LocalVideoInfo currentData = mAdapter.getDatas().get(position);
                    currentData.setSpeed_info(getStringByRes(R.string.download_get_info));
                    vh.itemOfflineSpeed.setText(getStringByRes(R.string.download_get_info));
//                    currentData.setRunning(LocalVideoInfo.running_Stop);
//                    currentData.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
//                    if (TextUtils.isEmpty(currentData.getInfo()) || currentData.getInfo().equals("0.0 KB")) {
//                        currentData.setInfo(FileUtil.getSize(fsize));
//                    }
//                    currentData.setPercent("0");
//                    currentData.setTid(tid + "");
//                    mAdapter.notifyItemChanged(position);
                } else if (fsize != 0 && dsize == fsize || tid == -1 || checSize == 1) {
                    // 电影下载完成
                    flag = false;// 停止下载
                    iterator.remove();
                    LogUtil.e("下载完成", mAdapter.getDatas().get(position).getTitle() + "==============" + mAdapter.getDatas().size());
                    LocalVideoInfo currentData = mAdapter.getDatas().get(position);
                    currentData.setSpeed_info(getStringByRes(R.string.download_over));
                    currentData.setRunning(LocalVideoInfo.running_finish);
//                    currentData.setInfo(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
                    if (TextUtils.isEmpty(currentData.getInfo()) || currentData.getInfo().equals("0.0 KB")) {
                        currentData.setInfo(FileUtil.getSize(fsize));
                    }
                    currentData.setSize(fsize + "");
                    currentData.setPercent("1000");
                    currentData.setTid("-1");
                    currentData.setDone(true);

                    // 将数据保存到已下载列表中mData
                    ArrayList<LocalVideoInfo> mCacheData = XGUtil.loadCacheList();
                    if (!mCacheData.contains(currentData)) {
                        mCacheData.add(0, currentData);
                        XGUtil.saveCacheList(mCacheData);
                    }
                    // 删除正在下载列表中的item
                    mAdapter.deleteData(position);
                    XGUtil.saveList(mAdapter.getDatas());
                    // 发送广播更新数据
                    EventBus.getDefault().post(new EvenBusEven.DownLoadDoneEven());
                } else {
                    //下载中
                    LocalVideoInfo videoInfo = mAdapter.getDatas().get(position);
                    if (!videoInfo.getRunning().equals(LocalVideoInfo.running_Star)) {
                        videoInfo.setRunning(LocalVideoInfo.running_Star);
                    }
                    vh.itemOfflineSpeed.setText(FileUtil.getSize(MyApplication.getp2p().P2Pgetspeed(tid)) + "/s");
                    if (fsize == 0) {
                        fsize = 1;
                    }
                    int profress = (int) (1000 * dsize / fsize);
                    if (profress >= vh.itemOfflineProgress.getProgress()) {
                        vh.itemOfflineProgress.setProgress(profress);
                        videoInfo.setPercent(profress + "");
                    }
//                    vh.itemOfflineFree.setText(FileUtil.getSize(dsize) + "/" + FileUtil.getSize(fsize));
                    videoInfo.setSpeed_info(vh.itemOfflineSpeed.getText().toString());
                    if (TextUtils.isEmpty(videoInfo.getInfo()) || videoInfo.getInfo().equals("0.0 KB")) {
                        videoInfo.setInfo(FileUtil.getSize(fsize));
                        vh.itemOfflineFree.setText(FileUtil.getSize(fsize));
                    }
//                    mAdapter.getDatas().get(position).setInfo(vh.info.getText().toString());
//                        }
                }
            }
        }
    }

    /**
     * TODO
     * 开启线程，监听下载状态
     */
    private void startThread() {
        if (null == threadRun) {
            refreshData();
            Observable.interval(1500, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            threadRun = disposable;
                        }
                    }).map(new Function<Long, String>() {
                @Override
                public String apply(Long aLong) throws Exception {
                    LogUtil.e("下载数量", MyApplication.downTaskPositionList.size() + "=====================");
                    if (MyApplication.downTaskPositionList.size() > 0) {
                        if (!NetworkUtils.isWifiConnected()) {
                            //非Wifi环境
                        }
                        return "1";
                    }
                    return "0";
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String aLong) {
                            if ("1".equals(aLong)) {
                                updateProgressPartly2();
                            }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存当前进度状态
        if (null != mAdapter) {
            XGUtil.saveList(mAdapter.getDatas());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        flag = false;
        if (null != threadRun) {
            threadRun.dispose();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 接受广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EvenBusEven.OffLineDownEven event) {
        if (null == mAdapter) {
            return;
        }
        if (event.getType() == 3) {
            //手机内存满了
            for (LocalVideoInfo data : mAdapter.getDatas()) {
                data.setRunning(LocalVideoInfo.running_Stop);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            List<LocalVideoInfo> mNewData = XGUtil.loadList();
            int oldSize = mAdapter.getDatas().size();
            int newSize = mNewData.size();
            if (null != downloadEditLayout) {
                //显示编辑布局
                if (mNewData.size() > 0 && downloadEditLayout.getVisibility() == View.GONE) {
                    downloadEditLayout.setVisibility(View.VISIBLE);
                } else if (mNewData.size() == 0 && downloadEditLayout.getVisibility() == View.VISIBLE) {
                    downloadEditLayout.setVisibility(View.GONE);
                }
            }
            if (newSize > 0) {
                if (newSize > oldSize && MyApplication.downTaskPositionList.size() < MyApplication.downLoadingMax) {
                    //添加新的电影，并开始下载
                    MyApplication.downTaskPositionList.add(mNewData.get(0));
                    try {
                        MyApplication.getp2p().P2Pdoxadd(mNewData.get(0).getUrl().getBytes("GBK"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mAdapter.setDatas(mNewData);
                } else {
                    //刷新界面
                    for (LocalVideoInfo localVideoInfo : MyApplication.downTaskPositionList) {
                        for (int i = 0; i < oldSize; i++) {
                            if (localVideoInfo.getTid().equals(mAdapter.getDatas().get(i).getTid())) {
                                mAdapter.getDatas().get(i).setRunning(LocalVideoInfo.running_Star);
                            } else {
                                mAdapter.getDatas().get(i).setRunning(LocalVideoInfo.running_Stop);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                mAdapter.setDatas(new ArrayList<>());
            }
        }
    }

    @OnClick({R.id.edit_stop_all_btn, R.id.edit_select_all_btn, R.id.edit_delete_btn, R.id.edit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_stop_all_btn:
                //暂停全部
                if (MyApplication.downTaskPositionList.size() > 0) {
                    XGUtil.stopAll("", true);
                    for (LocalVideoInfo data : mAdapter.getDatas()) {
                        data.setRunning(LocalVideoInfo.running_Stop);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.edit_select_all_btn:
                //选中全部
                mAdapter.selectAllItem(!mAdapter.isClickSelectAll());
                if (mAdapter.isClickSelectAll()) {
                    editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all_cancle));
                } else {
                    editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
                }

                break;
            case R.id.edit_delete_btn:
                //删除选中的Item
                if (mAdapter.getCheckPosition().size() > 0) {
                    List<LocalVideoInfo> newData = new ArrayList<>();
                    int size = mAdapter.getDatas().size();
                    for (int i = 0; i < size; i++) {
                        if (!mAdapter.getCheckPosition().contains(i + "")) {
                            newData.add(mAdapter.getDatas().get(i));
                        } else {
                            //删除选中的本地下载文件
                            try {
                                // 暂停当前影片的下载
                                MyApplication.getp2p().P2Pdoxpause(mAdapter.getDatas().get(i).getUrl().getBytes("GBK"));
                                // 删除影片
                                MyApplication.getp2p().P2Pdoxdel(mAdapter.getDatas().get(i).getUrl().getBytes("GBK"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mAdapter.getCheckPosition().clear();
                    mAdapter.setDatas(newData);
                    //重新保存历史记录信息
                    XGUtil.saveList(mAdapter.getDatas());
                    if (newData.size() == 0) {
                        downloadEditLayout.setVisibility(View.GONE);
                        editStopAllBtn.setVisibility(View.GONE);
                    }
                    mAdapter.setEditMoudle(false);
                    mAdapter.setClickSelectAll(false);
                    XGConstant.showSDSizeByUserClear = true;//通知更新手机内存大小
                } else {
                    ToastUtil.showToastShort(MyApplication.getStringByResId(R.string.download_edit_delete_empty));
                }
                break;
            case R.id.edit_btn:
                //开启编辑
                if (mAdapter != null) {
                    if (mAdapter.isEditMoudle()) {
                        editBtn.setText(MyApplication.getStringByResId(R.string.download_edit));
                        mAdapter.setEditMoudle(false);
                        mAdapter.setClickSelectAll(false);
                        editSelectAllBtn.setText(MyApplication.getStringByResId(R.string.download_edit_select_all));
                        editSelectAllBtn.setVisibility(View.GONE);
                        editDeleteBtn.setVisibility(View.GONE);
                        editStopAllBtn.setVisibility(View.VISIBLE);
                    } else {
                        editBtn.setText(MyApplication.getStringByResId(R.string.button_cancel));
                        mAdapter.setEditMoudle(true);
                        editSelectAllBtn.setVisibility(View.VISIBLE);
                        editDeleteBtn.setVisibility(View.VISIBLE);
                        editStopAllBtn.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void checkPermission() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean && null != mAdapter) {
                            mAdapter.setHasPermission(true);
                        } else {
                            ToastUtil.showToastLong(getStringByRes(R.string.permission_sd));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(getStringByRes(R.string.permission_sd));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
