package cn.mofufin.morf.ui.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.db.DBHelper;
import cn.mofufin.morf.ui.entity.SelfTextInfo.MerSelfTextBean;

/**
 * author：created by Liubd on 2018/8/31 16:38
 * e-mail:79528323@qq.com
 */
public class MerSelfTextBeanDao {
    private Dao<MerSelfTextBean,Integer> dao;
    private DBHelper dbHelper;

    public MerSelfTextBeanDao(Context context) {
        try {
            dbHelper = DBHelper.getHelper(context);
            dao = dbHelper.getDao(MerSelfTextBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBean(MerSelfTextBean obj){
        try {
            MerSelfTextBean bean = queryBean();
            if (bean != null){
                dao.update(obj);
                Logger.e("update");
            }else{
                dao.create(obj);
                Logger.e("create");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBean(){
        try {
            List<MerSelfTextBean> list = dao.queryForAll();
            if (list.size() > 0){
                dao.delete(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public MerSelfTextBean queryBean(){
        MerSelfTextBean bean=null;
        try {
            List<MerSelfTextBean> list = dao.queryForAll();
            if (list!=null && list.size() > 0)
                bean = list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return bean;
    }
}
