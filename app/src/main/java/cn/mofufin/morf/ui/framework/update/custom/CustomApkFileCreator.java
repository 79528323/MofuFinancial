package cn.mofufin.morf.ui.framework.update.custom;

import android.os.Environment;

import java.io.File;

import cn.mofufin.morf.ui.framework.update.creator.ApkFileCreator;
import cn.mofufin.morf.ui.framework.update.creator.DefaultFileCreator;

/**
 * 生成下载apk文件的文件地址
 * 默认使用参考 {@link DefaultFileCreator}
 */
public class CustomApkFileCreator implements ApkFileCreator {
    private final static String AUTH_NAME="Mofu";
    @Override
    public File create(String versionName) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        File path = new File(Environment.getExternalStorageDirectory().getPath() + "/"+AUTH_NAME);
        path.mkdirs();
        return new File(path, AUTH_NAME+"_" + versionName + ".apk");
    }

}
