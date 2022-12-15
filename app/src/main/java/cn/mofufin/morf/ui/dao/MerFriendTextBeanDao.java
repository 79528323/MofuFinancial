package cn.mofufin.morf.ui.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.db.DBHelper;
import cn.mofufin.morf.ui.entity.SelfTextInfo;

/**
 * author：created by Liubd on 2018/8/31 16:38
 * e-mail:79528323@qq.com
 */
public class MerFriendTextBeanDao {
    private Dao<SelfTextInfo.MerFriendText,Integer> dao;
    private DBHelper dbHelper;

    public MerFriendTextBeanDao(Context context) {
        try {
            dbHelper = DBHelper.getHelper(context);
            dao = dbHelper.getDao(SelfTextInfo.MerFriendText.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBean(SelfTextInfo.MerFriendText obj){
        try {
            SelfTextInfo.MerFriendText bean = queryBean();
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
            List<SelfTextInfo.MerFriendText> list = dao.queryForAll();
            if (list.size() > 0){
                dao.delete(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SelfTextInfo.MerFriendText queryBean(){
        SelfTextInfo.MerFriendText bean=null;
        try {
            List<SelfTextInfo.MerFriendText> list = dao.queryForAll();
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
