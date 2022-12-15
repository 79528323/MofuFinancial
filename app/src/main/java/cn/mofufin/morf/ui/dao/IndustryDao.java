//package cn.mofufin.morf.ui.dao;
//
//import android.content.Context;
//
//import com.j256.ormlite.dao.Dao;
//
//import java.sql.SQLException;
//
//import cn.mofufin.morf.ui.db.DBHelper;
//
///**
// * author：created by Liubd on 2018/8/31 16:38
// * e-mail:79528323@qq.com
// */
//public class IndustryDao {
//    private Dao<IndustryDao,Integer> dao;
//    private DBHelper dbHelper;
//
//    public IndustryDao(Context context) {
//        try {
//            dbHelper = DBHelper.getHelper(context);
//            dao = dbHelper.getDao(IndustryDao.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
