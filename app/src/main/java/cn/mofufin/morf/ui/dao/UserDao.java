//package cn.mofufin.morf.ui.dao;
//
//import android.content.Context;
//
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.stmt.Where;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import cc.ruis.lib.util.Logger;
//import cn.mofufin.morf.ui.db.DBHelper;
//import cn.mofufin.morf.ui.entity.User;
//
///**
// * author：created by Liubd on 2018/8/31 16:38
// * e-mail:79528323@qq.com
// */
//public class UserDao {
//    private Dao<User.DataBean.MerchantInfoBean,Integer> dao;
//    private DBHelper dbHelper;
//
//    public UserDao(Context context) {
//        try {
//            dbHelper = DBHelper.getHelper(context);
//            dao = dbHelper.getDao(UserDao.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addUser(User.DataBean.MerchantInfoBean obj){
//        try {
//            dao.create(obj);
//            Logger.e("addUser");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public User.DataBean.MerchantInfoBean queryMerChantedId(int id) {
//        User.DataBean.MerchantInfoBean bean = null;
//        QueryBuilder<User.DataBean.MerchantInfoBean ,Integer> queryBuilder = dao.queryBuilder();
//        Where<User.DataBean.MerchantInfoBean ,Integer> where = queryBuilder.where();
//        try {
//            Where<User.DataBean.MerchantInfoBean ,Integer> beanWhere = where.eq("merchantId",id);
//            List<User.DataBean.MerchantInfoBean> query = beanWhere.query();
//            if (query.size() > 0) {
//                bean = query.get(0);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }
//
//    public User.DataBean.MerchantInfoBean queryInfo(){
//        User.DataBean.MerchantInfoBean bean=null;
//        try {
//            List< User.DataBean.MerchantInfoBean> list = dao.queryForAll();
//            if (list.size() > 0)
//                bean = list.get(0);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }
//}
