/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package cn.mofufin.morf.ui.util;

import android.content.Context;

import java.io.File;

public class FileUtil {
    public static File getSaveFile(Context context,int which) {
        String name = "";
        String dir = "";
        switch (which){
            case 0:
                name="positive.jpg";
                dir = "A/";
                break;
            case 1:
                name="back.jpg";
                dir = "B/";
                break;
            case 2:
                name="bank.jpg";
                dir = "";
                break;

            case 3:
                name="backbank.jpg";
                break;
        }
        File file = new File(context.getFilesDir(), name);
        return file;
    }

    public static void deleteAllFile(Context context){
        String path = context.getFilesDir().getAbsolutePath();
        File dirs = new File(path);
        if (dirs!=null && dirs.isDirectory()){
            for (File file :dirs.listFiles()){
                if (file.isFile()){
                    file.delete();
                }
            }
        }
    }
}
