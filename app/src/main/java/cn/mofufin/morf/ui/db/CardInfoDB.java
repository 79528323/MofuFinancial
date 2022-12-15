package cn.mofufin.morf.ui.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.User;

public class CardInfoDB {

    public static void SaveCardInfo(List<User.DataBean.CardInfoBean> cardInfoBeanList){
        String SQL = "Select * from Tab_CardInfo where cardCode=?";
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            for (int index=0; index < cardInfoBeanList.size(); index++){
                ContentValues values = new ContentValues();

                User.DataBean.CardInfoBean bean= cardInfoBeanList.get(index);
                values.put("repaymentDay",bean.repaymentDay);
                values.put("accountDay",bean.accountDay);
                values.put("cardPhone",bean.cardPhone);
                values.put("cardBankName",bean.cardBankName);
                values.put("cardDef",bean.cardDef);
                values.put("cardCode",bean.cardCode);
                values.put("cardReDate",bean.cardReDate);
                values.put("cardBackNo",bean.cardBackNo);
                values.put("cardType",bean.cardType);

                Cursor cursor = db.rawQuery(SQL,new String[]{bean.cardCode});
                if (cursor!=null && cursor.getCount() > 0){
                    User.DataBean.CardInfoBean exits = queryCardforNum(bean.cardCode);
                    if (exits==null){
                        db.insert("Tab_CardInfo",null,values);
                    }else {
                        db.update("Tab_CardInfo",values,"cardCode=?",new String[]{bean.cardCode});
                    }
                }else {
                    db.insert("Tab_CardInfo",null,values);
                }

            }

        }
    }

    public static List<User.DataBean.CardInfoBean> queryCardALL(){
        String SQL = "Select * from Tab_CardInfo";
        List<User.DataBean.CardInfoBean> temp=null;
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            Cursor cursor = db.rawQuery(SQL,null);
            temp = new ArrayList<>();
            while (cursor.moveToNext()){
                User.DataBean.CardInfoBean bean = new User.DataBean.CardInfoBean();
                bean.repaymentDay = cursor.getString(cursor.getColumnIndex("repaymentDay"));
                bean.accountDay = cursor.getString(cursor.getColumnIndex("accountDay"));
                bean.cardPhone = cursor.getString(cursor.getColumnIndex("cardPhone"));
                bean.cardBankName = cursor.getString(cursor.getColumnIndex("cardBankName"));
                bean.cardDef = cursor.getInt(cursor.getColumnIndex("cardDef"));
                bean.cardCode = cursor.getString(cursor.getColumnIndex("cardCode"));
                bean.cardReDate = cursor.getString(cursor.getColumnIndex("cardReDate"));
                bean.cardBackNo = cursor.getString(cursor.getColumnIndex("cardBackNo"));
                bean.cardType = cursor.getInt(cursor.getColumnIndex("cardType"));

                temp.add(bean);
            }

            cursor.close();
//            db.close();
        }

        return temp;
    }

    public static User.DataBean.CardInfoBean queryCardforNum(String cardNum){
        String SQL = "Select * from Tab_CardInfo where cardCode=?";
        User.DataBean.CardInfoBean temp = null;
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            Cursor cursor = db.rawQuery(SQL,new String[]{cardNum});
            if (cursor!=null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    temp = new User.DataBean.CardInfoBean();
                    temp.repaymentDay = cursor.getString(cursor.getColumnIndex("repaymentDay"));
                    temp.accountDay = cursor.getString(cursor.getColumnIndex("accountDay"));
                    temp.cardPhone = cursor.getString(cursor.getColumnIndex("cardPhone"));
                    temp.cardBankName = cursor.getString(cursor.getColumnIndex("cardBankName"));
                    temp.cardDef = cursor.getInt(cursor.getColumnIndex("cardDef"));
                    temp.cardCode = cursor.getString(cursor.getColumnIndex("cardCode"));
                    temp.cardReDate = cursor.getString(cursor.getColumnIndex("cardReDate"));
                    temp.cardBackNo = cursor.getString(cursor.getColumnIndex("cardBackNo"));
                    temp.cardType = cursor.getInt(cursor.getColumnIndex("cardType"));

                }
                cursor.close();
            }



//            db.close();
        }
        return temp;
    }

    public static void deleteBankCard(String code){
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            db.delete("Tab_CardInfo","cardCode=?",new String[]{code});
        }
    }

    public static void clearAllCard(){
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            db.delete("Tab_CardInfo",null,null);
        }
    }
}
