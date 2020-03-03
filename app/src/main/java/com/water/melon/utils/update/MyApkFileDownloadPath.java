package com.water.melon.utils.update;


import com.water.melon.utils.FileUtil;

import org.lzh.framework.updatepluginlib.base.FileCreator;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;

public class MyApkFileDownloadPath extends FileCreator {
    @Override
    public File create(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        File path = new File(FileUtil.getSdcardRootDirectory("/updateWaterMelon"));
        return new File(path,"waterMelon" + update.getVersionName());
    }

    @Override
    public File createForDaemon(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        File path = new File(FileUtil.getSdcardRootDirectory("/updateWaterMelon"));
        return new File(path,"waterMelon_daemon_" + update.getVersionName());
    }

}
