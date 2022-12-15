package cn.mofufin.morf.ui.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.db.DBHelper;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo.MerSelfTextBean;

/**
 * author：created by Liubd on 2018/8/31 16:38
 * e-mail:79528323@qq.com
 */
public class MerSpouseTextBeanDao {
    private Dao<SelfTextInfo.MerSpouseTextBean,Integer> dao;
    private DBHelper dbHelper;

    public MerSpouseTextBeanDao(Context context) {
        try {
            dbHelper = DBHelper.getHelper(context);
            dao = dbHelper.getDao(SelfTextInfo.MerSpouseTextBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBean(SelfTextInfo.MerSpouseTextBean obj){
        try {
            SelfTextInfo.MerSpouseTextBean bean = queryBean();
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
            List<SelfTextInfo.MerSpouseTextBean> list = dao.queryForAll();
            if (list.size() > 0){
                dao.delete(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SelfTextInfo.MerSpouseTextBean queryBean(){
        SelfTextInfo.MerSpouseTextBean bean=null;
        try {
            List<SelfTextInfo.MerSpouseTextBean> list = dao.queryForAll();
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
