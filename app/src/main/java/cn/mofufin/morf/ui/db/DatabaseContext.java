package cn.mofufin.morf.ui.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

import cc.ruis.lib.util.AppManager;
import cn.mofufin.morf.ui.base.BaseApplication;


/**
 * @author 陈燕明
 * @time 2017/4/19 0019  下午 2:03
 * @desc ${TODD}
 */

public class DatabaseContext extends ContextWrapper {
    public static File rootDirectory;
    public static File cacheDirectory;
    public static File dbFile;

    /**
     * 构造函数
     *
     * @param base 上下文环境
     */
    public DatabaseContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     * @return
     */
    @Override
    public File getDatabasePath(String name) {
        //判断是否存在sd卡
//        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
//        if(!sdExist){//如果不存在,
//            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
//            return null;
//        }
//        else{//如果存在
        //获取sd卡路径
        String cache = AppManager.getDiskCacheDir(BaseApplication.context);
        cacheDirectory = new File(cache);
        rootDirectory = cacheDirectory.getParentFile();
        dbFile = new File(rootDirectory.getAbsolutePath() + "/database/");
        // String dbDir=android.os.Environment.getExternalStorageDirectory().toString();
        //   dbDir += "/scexam";//数据库所在目录
        // String dbPath = dbDir+"/"+name;//数据库路径
        //判断目录是否存在，不存在则创建该目录
        //  File dirFile = new File(dbDir);
        if (!dbFile.exists())
            dbFile.mkdirs();

        //数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        //判断文件是否存在，不存在则创建该文件
        File dbFile = new File(rootDirectory.getAbsolutePath() + "/database/" + name);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();//创建文件
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            isFileCreateSuccess = true;

        //返回数据库文件对象
        if (isFileCreateSuccess)
            return dbFile;
        else
            return null;
        //  }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
                                               SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     * android.database.sqlite.SQLiteDatabase.CursorFactory,
     * android.database.DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}