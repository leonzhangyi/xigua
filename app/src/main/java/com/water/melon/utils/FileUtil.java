package com.water.melon.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.water.melon.application.MyApplication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import androidx.annotation.NonNull;

public class FileUtil {
    /**
     * App拍照图片路径
     */
    public static final String CamerPath = FileUtil.getSdcardRootDirectory("camer");
    public static final String HttpCache = FileUtil.getSdcardRootDirectory("HttpCache");
    public static final String ServiceCache = FileUtil.getSdcardRootDirectory("ServiceCache");
    public static final String ScreenPathCache = FileUtil.getSdcardRootDirectory("screenPath");
    public static final String GlidePathCache = FileUtil.getSdcardRootDirectory("GlidePathCache");
    public static final String XiguaPathCache = FileUtil.getSdcardRootDirectory("xigua");

    public static String getSdcardRootDirectory(@NonNull String addFlod) {
        if (null == MyApplication.getContext()) {
            return "";
        }
        String rootPath = "";
        try {
            if (null != MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)) {
                //内部存储
                rootPath = Objects.requireNonNull(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getAbsolutePath() + "/" + addFlod + "/";
            } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //外部存储
                rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + addFlod + "/";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(rootPath)) {
            File file = new File(rootPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return rootPath;
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final String url) {
        Uri uri = Uri.parse(url);
        return getRealFilePathFromUri(uri);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将文件转换为字节
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] fileToBytes(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        BufferedInputStream in = new BufferedInputStream(inputStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] content = out.toByteArray();
        return content;
    }

    public static String getSize(long lSize) {//LS:大小判断（数据大小转换）
        if (lSize < 0) {
            lSize = Math.abs(lSize);
        }
        if (lSize == 0) {
            lSize = 1;
        }
        float size = lSize;
        if (size == 0) return "0.0 KB";
        if (size < 1024 * 1024)
            return String.valueOf((float) (Math.round((size / 1024) * 10)) / 10) + " KB";
        if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024)
            return String.valueOf((float) (Math.round((size / 1024 / 1024) * 10)) / 10) + " MB";
        if (size >= 1024 * 1024 * 1024)
            return String.valueOf((float) (Math.round((size / 1024 / 1024 / 1024) * 100)) / 100) + " GB";
        return "0.0 KB";
    }


    /**
     * 带压缩的保存图片
     *
     * @param image
     * @param outPath
     * @param options 压缩比例
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, int options) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        LogUtil.e("FileUtil", "compressAndGenImage--->文件大小：" + os.size() + "，压缩比例：" + options);
        fos.flush();
        fos.close();
    }

    /**
     * 删空文件夹内的文件
     *
     * @param folderName
     */
    public static void deleteFileInFolder(String folderName) {
        File file = new File(folderName);
        if (file.exists()) {
            if (file.isDirectory()) {
                if (null != file.listFiles() && file.listFiles().length != 0) {
                    for (File f : file.listFiles()) {
                        if (f.isDirectory()) {
                            deleteFileInFolder(f.getPath());
                        } else {
                            f.delete();
                        }
                    }
                }
            } else {
                file.delete();
            }
        }
    }


    public static void viewSaveToImage(View view, Context context) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        FileOutputStream fos;
        String imagePath = "";
        File file = null;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
//                File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis()+".png");
                file = new File(getAppDir1(), "/vipxg.jpg");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else {
//                Logger.t("ShareActivity").e("创建文件失败!");
                throw new Exception("创建文件失败!");
            }
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        Logger.t("ShareActivity").e("imagePath="+imagePath);

        view.destroyDrawingCache();
        ToastUtil.showToastLong("保存成功");
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), "/vipxg.jpg", null);
//            MyToastUtils.showShortToast(context, "保存成功");
        } catch (FileNotFoundException e) {
//            MyToastUtils.showShortToast(context, "保存失败");
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
    }

    private static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    public static String getAppDir1() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
    }
}
