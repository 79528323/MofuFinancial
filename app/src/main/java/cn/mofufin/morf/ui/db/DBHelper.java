package cn.mofufin.morf.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.ui.entity.CityBean;
import cn.mofufin.morf.ui.entity.IndustryRateInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.entity.User;

/**
 * author：created by Liubd on 2018/8/31 16:26
 * e-mail:79528323@qq.com
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {


    /**
     * 数据库名字
     */
    private static String getUserDatabaseName() {
        String dbName = BuildConfig.FLAVOR +"_"+ MerchanInfoDB.queryInfo().merchantCode + "_1.db";
//        Logger.e("dbName="+dbName);
        return dbName;
    }


    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 171;//TODO 数据库版本

    /**
     * 用来存放Dao的地图
     */
    private Map<String, Dao> daos = new HashMap<String, Dao>();


    private static DBHelper instance;

    public static void destoryDBHelper() {
        instance = null;
    }


    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(new DatabaseContext(context));
                }
            }
        }
        return instance;
    }


    /**
     * 构造方法
     *
     * @param context
     */
    private DBHelper(Context context) {
        super(context, getUserDatabaseName(), null, DB_VERSION);
    }

    /**
     * 这里创建表
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {

        // 创建表
        try {
            TableUtils.createTable(connectionSource, SelfTextInfo.MerSelfTextBean.class);
            TableUtils.createTable(connectionSource, SelfTextInfo.MerSpouseTextBean.class);
            TableUtils.createTable(connectionSource, SelfTextInfo.MerFriendText.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 这里进行更新表操作
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, SelfTextInfo.MerSelfTextBean.class, true);
            TableUtils.dropTable(connectionSource, SelfTextInfo.MerSpouseTextBean.class, true);
            TableUtils.dropTable(connectionSource, SelfTextInfo.MerFriendText.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过类来获得指定的Dao
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {

        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            daos.put(className, dao);

        }
        return dao;
    }


    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
        destoryDBHelper();//TODO 若不清除实例，无法调用实例方法读取其它用户数据库
    }

}
