//package cn.mofufin.morf.ui.db;
//
//import android.annotation.SuppressLint;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.mofufin.morf.ui.base.BaseApplication;
//import cn.mofufin.morf.ui.entity.Address;
//
//public class AddressDB {
//
//    public static long saveAddress(Address address){
//        long result = -1;
//        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        if (db.isOpen()){
//            Cursor cursor = db.rawQuery("Select * from Tab_Address where _id=?",new String[]{String.valueOf(address._id)});
//            ContentValues values = new ContentValues();
////            values.put("_id",address._id);
//            values.put("receiName",address.receiName);
//            values.put("receiPhone",address.receiPhone);
//            values.put("receiAddress",address.receiAddress);
//            result = db.insert("Tab_Address",null,values);
//
//            cursor.close();
//        }
//
//        return result;
//    }
//
//
//
//    public static List<Address> queryAddressALL(){
//        String SQL = "Select * from Tab_Address order by _id desc";
//        List<Address> temp=null;
//        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        if (db.isOpen()){
//            Cursor cursor = db.rawQuery(SQL,null);
//            temp = new ArrayList<>();
//            while (cursor.moveToNext()){
//                Address bean = new Address();
//                bean._id = cursor.getInt(cursor.getColumnIndex("_id"));
//                bean.receiName = cursor.getString(cursor.getColumnIndex("receiName"));
//                bean.receiPhone = cursor.getString(cursor.getColumnIndex("receiPhone"));
//                bean.receiAddress = cursor.getString(cursor.getColumnIndex("receiAddress"));
//                temp.add(bean);
//            }
//
//            cursor.close();
//        }
//
//        return temp;
//    }
//
//    public static long updateAddress(Address address){
//        long result = -1;
//        String SQL = "Select * from Tab_Address where _id=?";
//        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        if (db.isOpen()){
//            deleteAddress(address);
//
//            ContentValues values = new ContentValues();
//            values.put("receiName",address.receiName);
//            values.put("receiPhone",address.receiPhone);
//            values.put("receiAddress",address.receiAddress);
//            result = db.insert("Tab_Address",null,values);
////            result = db.update("Tab_Address",values,"_id=?",new String[]{String.valueOf(address._id)});
//        }
//
//        return result;
//    }
//
//    public static int deleteAddress(Address address){
//        int result=-1;
//        DbOpenHelper dbOpenHelper = DbOpenHelper.getInstance(BaseApplication.context);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        if (db.isOpen()){
//            result = db.delete("Tab_Address","_id=?",new String[]{String.valueOf(address._id)});
//        }
//        return result;
//    }
//
//}
