package com.water.melon.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SDUtils {

	/** 
     * 获得SD卡总大小 
     *  
     * @return 
     */  
    public static long getSDTotalSize(Context context) {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return blockSize*totalBlocks;  
    }  
  

}
