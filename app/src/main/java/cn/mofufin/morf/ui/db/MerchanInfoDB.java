package cn.mofufin.morf.ui.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.User;

/**
 * author：created by Liubd on 2018/9/3 09:57
 * e-mail:79528323@qq.com
 */
public class MerchanInfoDB {
    /**
     * merchantCode : M20180808161435320
     * merchantImg : null
     * indirectNum : 0
     * RMB : 0.0
     * fdMerIdentityCardName : 杜丰铭
     * agentName : null
     * merchantState : 0
     * merchantName : 高大上
     * realName : 2
     * merchantPhone : 13927450890
     * merchantId : 26
     * mposFeedPart : 4
     * directlyNum : 5
     * memberType : 3
     * USDollar : 0.0
     * email : dufengming2008@qq.com
     * HKDollar : 0.0
     */
    public static void saveMerchanInfo(User.DataBean.MerchantInfoBean merchantInfoBean){
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("merchantCode",merchantInfoBean.getMerchantCode());
            values.put("merchantImg",merchantInfoBean.getMerchantImg());
            values.put("indirectNum",merchantInfoBean.getIndirectNum());
            values.put("RMB",merchantInfoBean.getRMB());
            values.put("fdMerIdentityCardName",merchantInfoBean.getFdMerIdentityCardName());
            values.put("agentName",merchantInfoBean.getAgentName());
            values.put("merchantState",merchantInfoBean.getMerchantState());
            values.put("merchantName",merchantInfoBean.getMerchantName());
            values.put("realName",merchantInfoBean.getRealName());
            values.put("merchantPhone",merchantInfoBean.getMerchantPhone());
            values.put("merchantId",merchantInfoBean.getMerchantId());
            values.put("mposFeedPart",merchantInfoBean.getMposFeedPart());
            values.put("directlyNum",merchantInfoBean.getDirectlyNum());
            values.put("memberType",merchantInfoBean.getMemberType());
            values.put("USDollar",merchantInfoBean.getUSDollar());
            values.put("email",merchantInfoBean.getEmail());
            values.put("HKDollar",merchantInfoBean.getHKDollar());

            values.put("remitPayFeedPart",merchantInfoBean.getRemitPayFeedPart());
            values.put("lastLoginIP",merchantInfoBean.getLastLoginIP());
            values.put("lastEnterDate",merchantInfoBean.getLastEnterDate());
            values.put("enterDate",merchantInfoBean.getEnterDate());
            values.put("currentLoginIP",merchantInfoBean.getCurrentLoginIP());

            //1.7.2新增参数  注册时间
            values.put("merchantDateTime",merchantInfoBean.getMerchantDateTime());
            db.insert("Tab_MerchantInfo",null,values);
        }
    }


    public static void updateMerchanInfo(User.DataBean.MerchantInfoBean merchantInfoBean){
        final String SQL = "Select * from Tab_MerchantInfo";
        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db.isOpen()){
            Cursor cursor = db.rawQuery(SQL,null);
            boolean isExist = cursor.moveToNext();
            cursor.close();
            if (isExist){
                ContentValues values = new ContentValues();
                values.put("merchantCode",merchantInfoBean.getMerchantCode());
                values.put("merchantImg",merchantInfoBean.getMerchantImg());
                values.put("indirectNum",merchantInfoBean.getIndirectNum());
                values.put("RMB",merchantInfoBean.getRMB());
                values.put("fdMerIdentityCardName",merchantInfoBean.getFdMerIdentityCardName());
                values.put("agentName",merchantInfoBean.getAgentName());
                values.put("merchantState",merchantInfoBean.getMerchantState());
                values.put("merchantName",merchantInfoBean.getMerchantName());
                values.put("realName",merchantInfoBean.getRealName());
                values.put("merchantPhone",merchantInfoBean.getMerchantPhone());
                values.put("merchantId",merchantInfoBean.getMerchantId());
                values.put("mposFeedPart",merchantInfoBean.getMposFeedPart());
                values.put("directlyNum",merchantInfoBean.getDirectlyNum());
                values.put("memberType",merchantInfoBean.getMemberType());
                values.put("USDollar",merchantInfoBean.getUSDollar());
                values.put("email",merchantInfoBean.getEmail());
                values.put("HKDollar",merchantInfoBean.getHKDollar());
                //Dataversion 2 新增参数
                values.put("remitPayFeedPart",merchantInfoBean.getRemitPayFeedPart());
                values.put("lastLoginIP",merchantInfoBean.getLastLoginIP());
                values.put("lastEnterDate",merchantInfoBean.getLastEnterDate());
                values.put("enterDate",merchantInfoBean.getEnterDate());
                values.put("currentLoginIP",merchantInfoBean.getCurrentLoginIP());
                //1.7.2 新增参数
                values.put("merchantDateTime",merchantInfoBean.merchantDateTime);


                db.update("Tab_MerchantInfo",values,null,null);
            }
        }
    }


    public static int deleteMerchantInfo(){
        int num=0;
        DbOpenHelper dbHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()){
            try {
                num = db.delete("Tab_MerchantInfo",null,null);
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }
        return num;
    }

    public static User.DataBean.MerchantInfoBean queryInfo(){
        final String SQL = "Select * from Tab_MerchantInfo";
        User.DataBean.MerchantInfoBean bean = null;
        DbOpenHelper dbHelper = DbOpenHelper.getInstance(BaseApplication.context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(SQL,null);
            while (cursor.moveToNext()){
                bean = new User.DataBean.MerchantInfoBean();
                bean.setMerchantCode(cursor.getString(cursor.getColumnIndex("merchantCode")));
                bean.setMerchantImg(cursor.getString(cursor.getColumnIndex("merchantImg")));
                bean.setIndirectNum(cursor.getInt(cursor.getColumnIndex("indirectNum")));
                bean.setRMB(cursor.getDouble(cursor.getColumnIndex("RMB")));
                bean.setFdMerIdentityCardName(cursor.getString(cursor.getColumnIndex("fdMerIdentityCardName")));
                bean.setAgentName(cursor.getString(cursor.getColumnIndex("agentName")));
                bean.setMerchantState(cursor.getInt(cursor.getColumnIndex("merchantState")));
                bean.setMerchantName(cursor.getString(cursor.getColumnIndex("merchantName")));
                bean.setRealName(cursor.getInt(cursor.getColumnIndex("realName")));
                bean.setMerchantPhone(cursor.getString(cursor.getColumnIndex("merchantPhone")));
                bean.setMerchantId(cursor.getInt(cursor.getColumnIndex("merchantId")));
                bean.setMposFeedPart(cursor.getInt(cursor.getColumnIndex("mposFeedPart")));
                bean.setDirectlyNum(cursor.getInt(cursor.getColumnIndex("directlyNum")));
                bean.setMemberType(cursor.getInt(cursor.getColumnIndex("memberType")));
                bean.setUSDollar(cursor.getDouble(cursor.getColumnIndex("USDollar")));
                bean.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                bean.setHKDollar(cursor.getDouble(cursor.getColumnIndex("HKDollar")));
                //Dataversion 2 新增参数
                bean.setRemitPayFeedPart(cursor.getInt(cursor.getColumnIndex("remitPayFeedPart")));
                bean.setCurrentLoginIP(cursor.getString(cursor.getColumnIndex("currentLoginIP")));
                bean.setLastEnterDate(cursor.getString(cursor.getColumnIndex("lastEnterDate")));
                bean.setLastLoginIP(cursor.getString(cursor.getColumnIndex("lastLoginIP")));
                bean.setEnterDate(cursor.getString(cursor.getColumnIndex("enterDate")));
                //1.7.2
                bean.setMerchantDateTime(cursor.getString(cursor.getColumnIndex("merchantDateTime")));
            }
            cursor.close();
        }

        return bean;
    }
}
