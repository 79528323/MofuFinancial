/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.mofufin.morf.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;//TODO 数据库版本，v1.7.1版本为5
    private static DbOpenHelper instance;

    private static final String USER_INFO_TABLE_CREATE ="CREATE TABLE IF NOT EXISTS Tab_MerchantInfo("
            +"merchantCode varchar,"
            +"merchantImg varchar,"
            +"indirectNum int,"
            +"RMB double,"
            +"fdMerIdentityCardName varchar,"
            +"agentName varchar,"
            +"merchantState int,"
            +"merchantName varchar,"
            +"realName int,"
            +"merchantPhone varchar,"
            +"merchantId int,"
            +"mposFeedPart int,"
            +"directlyNum int,"
            +"memberType int,"
            +"USDollar double,"
            +"email int,"
            +"HKDollar double," +
            "remitPayFeedPart int," +
            "lastLoginIP varchar," +
            "lastEnterDate varchar," +
            "enterDate varchar," +
            "currentLoginIP varchar," +
            "merchantDateTime varchar);";

    private static final String USER_BANK_CARD_TABLE_CREATE="CREATE TABLE IF NOT EXISTS Tab_CardInfo(" +
            "repaymentDay varchar," +
            "accountDay varchar," +
            "cardPhone varchar," +
            "cardBankName varchar," +
            "cardDef int," +
            "cardCode varchar," +
            "cardReDate varchar," +
            "cardBackNo varchar," +
            "cardType int);";


    private DbOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(new DatabaseContext(context.getApplicationContext()));
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return "MofuFinancial.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_INFO_TABLE_CREATE);
        db.execSQL(USER_BANK_CARD_TABLE_CREATE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3){
            db.execSQL("alter table Tab_MerchantInfo add remitPayFeedPart int");
            db.execSQL("alter table Tab_MerchantInfo add lastLoginIP varchar");
            db.execSQL("alter table Tab_MerchantInfo add lastEnterDate varchar");
            db.execSQL("alter table Tab_MerchantInfo add enterDate varchar");
            db.execSQL("alter table Tab_MerchantInfo add currentLoginIP varchar");
        }else if (oldVersion < 4){
            db.execSQL("drop table if exists Tab_MerchantInfo");
            db.execSQL(USER_INFO_TABLE_CREATE);
        }else if (oldVersion < 5){
            db.execSQL(USER_BANK_CARD_TABLE_CREATE);
        }else if ((oldVersion < 6)){//TODO 1.7.2
            db.execSQL("alter table Tab_MerchantInfo add merchantDateTime varchar");
        }
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}