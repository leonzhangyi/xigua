package com.p2p;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;

import com.water.melon.application.MyApplication;
import com.water.melon.utils.GetPath;
import com.water.melon.utils.LogUtil;

import java.io.File;

public class P2PClass {
    private static final String TAG = "P2PClass";
    public static final String appFilePath = "xigua";
    //        private static String cachefile = File.separator + appFilePath;//外部存储
    private static String cachefile = MyApplication.getContext().getFilesDir() + "/" + appFilePath;//内部存储
    //内部存储需要清除的文件夹
    public static String clearCachefile = Environment.getExternalStorageDirectory().getPath() + "/" + appFilePath + "/Downloads/";
    public static int port = 8087;

    static {//TODO 记得保证com.p2p路径一直
        System.loadLibrary("p2p");
    }

    public P2PClass() {
    }

    public void InitP2PServer() {
        String oldsdcardpath = GetPath.getNormalSDCardPath();
//        String sdcardpath = GetPath.getSDCardPath();

        String key = "TEST3E63BAAECDAA79BEAA91853490A69F08";
//        LogUtil.e(TAG, "oldPath==" + oldsdcardpath + "...newPath==" + sdcardpath);

//        File oldpath = new File(oldsdcardpath + cachefile);
//        File newpath = new File(sdcardpath + cachefile);
//        if (!sdcardpath.equals(oldsdcardpath) && oldpath.exists() && !newpath.exists()) {
        port = doxstarthttpd(key.getBytes(), oldsdcardpath.getBytes());
        LogUtil.e("p2p", "p2p版本：" + getVersion());
//        } else {
//            LogUtil.e(TAG, "P2PClass: " + sdcardpath.getBytes());
//            port = doxstarthttpd(key.getBytes(), sdcardpath.getBytes());
//            LogUtil.e(TAG, "p2p started:" + GetPath.getSDCardPath());
//        }
//        LogUtil.e(TAG, "P2PClass: " + "---oldsdcardpath==" + oldpath.getAbsolutePath() + "====newpath=" + newpath.getAbsolutePath());
    }


    public long P2PGetFree() {//LS：获取手机剩余的内存容量

//        LogUtil.e(TAG, "P2PGetFree: " + "---11");
//        String oldsdcardpath = GetPath.getNormalSDCardPath();
//        String sdcardpath = GetPath.getSDCardPath();
//        File oldpath = new File(oldsdcardpath + cachefile);
//        File newpath = new File(sdcardpath + cachefile);
//        LogUtil.e(TAG, "P2PGetFree: " + "---oldsdcardpath==" + oldpath.getAbsolutePath() + "====newpath=" + newpath.getAbsolutePath());
//
//
//        if (!sdcardpath.equals(oldsdcardpath) && oldpath.exists()
//                && !newpath.exists()) {
//            sdcardpath = oldsdcardpath;
//        }
//
//        StatFs sf = new StatFs(sdcardpath);
//        long availCount = sf.getAvailableBlocks();
//        availCount = availCount * sf.getBlockSize();
//        // long availCount = sf.getAvailableBytes();
//        return availCount;

        String state = Environment.getExternalStorageState();
        long fresSzie = 0;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
//            info[0] = blockSize * blockCount;//总大小
            fresSzie = availCount * blockSize;//剩余大小
//            Log.e("size", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
//            Log.e("size", "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
        }
        return fresSzie;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public int P2Pdoxstart(byte[] url) {
        return doxstart(url);
    }

    public int P2Pdoxdownload(byte[] url) {
        return doxdownload(url);
    }

    public int P2Pdoxterminate() {
        return doxterminate();
    }

    public int P2Pdosetupload(int t) {
        return dosetupload(t);
    }

    public int P2Pdoxcheck(byte[] url) {
        return doxcheck(url);
    }

    public int P2Pdoxadd(byte[] url) {
        return doxadd(url);
    }

    public int P2Pdoxpause(byte[] url) {
        return doxpause(url);
    }

    public int P2Pdoxdel(byte[] url) {
        return doxdel(url);
    }

    public int P2PdoxdelAll() {
        return doxdelall();
    }

    public long P2Pgetspeed(int t) {
        return getspeed(t);
    }

    public long P2Pgetdownsize(int t) {//LS：获取已经下载的数据的大小
        return getdownsize(t);
    }

    public long P2Pgetfilesize(int t) {//LS:获取要下载的文件的大小
        return getfilesize(t);
    }

    public int P2Pgetpercent() {
        return getpercent();
    }

    public long P2Pgetlocalfilesize(byte[] url) {
        return getlocalfilesize(url);
    }

    public long P2Pdosetduration(int dur) {
        return doxsetduration(dur);
    }

    public String getServiceAddress() {
        return doxgethostbynamehook("xx0.github.com");
    }

    public int P2Pdoxstarthttpd(byte[] key, byte[] path) {

        return doxstarthttpd(key, path);
    }

    public int P2Pdoxsave() {
        return doxsave();
    }

    public int P2Pdoxendhttpd() {
        return doxendhttpd();
    }

    public String getVersion() {
        return doxgetVersion();
    }


    private native final int doxsave();

    private native final int doxstart(byte[] url);

    private native final int doxadd(byte[] url);

    private native final int dosetupload(int t);

    private native final int doxdownload(byte[] url);

    private native final int doxpause(byte[] url);

    private native final int doxdel(byte[] url);

    private native final int doxdelall();

    private native final int doxcheck(byte[] url);

    private native final int doxterminate();


    private native final int doxstarthttpd(byte[] key, byte[] path);

    private native final int doxendhttpd();

    private native final int doxsetduration(int dur);

    private native final long getspeed(int t);//LS：缓冲速度

    private native final long getdownsize(int t);

    private native final long getfilesize(int t);

    private native final int getpercent();

    private native final long getlocalfilesize(byte[] url);


    //9.4号    //添加参数网速的接口
    public String P2Pdoxgettaskstat(int t) {
        return doxgettaskstat(t);
    }

    private native final String doxgettaskstat(int t);

    private native final String doxgethostbynamehook(String t);

    private native final String doxgetVersion();
}
