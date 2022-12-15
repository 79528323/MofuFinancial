package cn.mofufin.morf.ui.util;

import cn.mofufin.morf.BuildConfig;

/**
 * author：created by Liubd on 2018/8/29 17
 * e-mail:79528323@qq.com
 */
public class Common {
    public final static int IMAGE_MAX_SIZE = 1080;
    public final static int IMAGE_QUALITY = 85;
    public final static int ICON_MAX_SIZE = IMAGE_MAX_SIZE;
    public final static int ICON_QUALITY = IMAGE_QUALITY;
    public final static String SERVICE_NUMBER="4008915100";




    //编译类型，与build.gradle的productFlavors对应
    public static final String TYPE_DEVELOP = "develop";
    public static final String TYPE_PER_PRODUCE = "perProduce";
    public static final String TYPE_PRODUCE = "produce";

//    public final static String SERVERTYPE_DEV = "dev";
//    public final static String SERVERTYPE_TEST = "test";
//    public final static String SERVERTYPE_PRO = "pro";

    public final static String WECHAT_APP_ID="wx35e0d75a6cdd9a30";
    public final static String WECHAT_SECRET_ID="daea558fc3ec5a8da6d4505d99c44a5f";

    public final static String PAYSDK_CALLBACK_URL="http://mofufin.cn/system/mpos/cardFriendOrder";//卡友支付订单回调
    public final static String MOFU_LICAI = "http://mofu.limapay.com.cn";//摩富理财

    public final static int SingleCommission = 0;
    public final static String PAYSDK_PASSWORD = "123456";

    public final static String UPDATE_CONTENT = "";

    public final static String LOAN_VERSION = "1.0";


    public static final String HOST;
    public static final String DOWNLOAD_APK_HOST;
    public static final String BANNER_HOST;
//    public static final String HOS_SYSTEM_TYPE="/JFBank/system";
        public static final String HOS_SYSTEM_TYPE="/system";
//
//    public static final String HOSTTYPE="/JFBank/mobile";
        public static final String HOSTTYPE="/mobile";
//
//    public static final String SYSTEM_APK="/JFBank/system/app";
    public static final String SYSTEM_APK="/system/app";
//
    static {
//        HOST = "http://172.20.10.78:8080";
        HOST = "http://120.78.213.181";
//
        BANNER_HOST = HOST +"/";
//        BANNER_HOST = HOST +"/JFBank/";
//
        DOWNLOAD_APK_HOST = HOST+SYSTEM_APK+"/queryApkFileVersion";
    }





//    public final static String HOST;
//    public final static String DOWNLOAD_APK_HOST;
//    public final static String BANNER_HOST;
//    public final static String HOS_SYSTEM_TYPE;
//    //        public static final String HOS_SYSTEM_TYPE="/system";
////
//    public final static String HOSTTYPE;
////        public static final String HOSTTYPE="/mobile";
//
//    public final static String SYSTEM_APK;
//    //    public static final String SYSTEM_APK="/system/app";
////
//    static {
////        if (BuildConfig.FLAVOR.equals(TYPE_DEVELOP)){
//        HOST = "http://172.20.10.57:8080";
//        BANNER_HOST = HOST +"/JFBank/";
//        HOS_SYSTEM_TYPE="/JFBank/system";
//        HOSTTYPE="/JFBank/mobile";
//        SYSTEM_APK="/JFBank/system/app";
////        }else {//TODO 生产环境
////
////            HOST = "http://120.78.213.181";
////            BANNER_HOST = HOST +"/";
////            HOS_SYSTEM_TYPE="/system";
////            HOSTTYPE="/mobile";
////            SYSTEM_APK="/system/app";
////        }
////
//
//        DOWNLOAD_APK_HOST = HOST+Common.SYSTEM_APK+"/queryApkFileVersion";
//    }



    public final static int LIFE_BIZCODE=3301;
    public final static int STANDARD_BIZCODE=3110;
    public final static int BUSINESS_BIZCODE=3111;
    public final static int COMMODITY_BIZCODE=3113;
    public final static int RESTAURANT_BIZCODE=3112;
    public final static int JEWELLERY_BIZCODE=3114;
    public final static int HOUSE_BIZCODE=3302;

    public final static int UN_LIFE_BIZCODE=3301;
    public final static int UN_STANDARD_BIZCODE=4302;
    public final static int UN_BUSINESS_BIZCODE=4303;
    public final static int UN_COMMODITY_BIZCODE=4304;
    public final static int UN_RESTAURANT_BIZCODE=4305;
    public final static int UN_JEWELLERY_BIZCODE=4306;
    public final static int UN_HOUSE_BIZCODE=4307;


    /**
     * 便民生活（普通）
     */
    public final static double RATE_MPOS_ORDINARY_LIFE = 0.56;
    /**
     * 标准商户
     */
    public final static double RATE_MPOS_ORDINARY_STANDARD = 0.56;

    /**
     * 便民生活（黄金）
     */
    public final static double RATE_MPOS_GOLDEM_LIFE = 0.55;
    /**
     * 标准商户
     */
    public final static double RATE_MPOS_GOLDEM_STANDARD = 0.55;

    /**
     * 便民生活（钻石）
     */
    public final static double RATE_MPOS_DIAMONDS_LIFE = 0.54;
    /**
     * 标准商户
     */
    public final static double RATE_MPOS_DIAMONDS_STANDARD = 0.54;
    /**
     * MPOS手续费
     */
    public final static int MPOS_CHARGE = 0;

    /**
     * 扫码（支付宝）
     */
    public final static double RATE_SCAN_ALIPAY= 0.0038;
    /**
     * 扫码（微信）
     */
    public final static double RATE_SCAN_WEIXIN= 0.0038;


    /**
     * MPOS刷卡单笔好额
     */
    public final static int QUOTA_LIFE = 1000;//mpos便民生活额度
    public final static int QUOTA_STANDARD = 50000;//标准生活
    public final static int QUOTA_BUSINESS = 20000;//商旅酒店
    public final static int QUOTA_COMMODITY = 10000;//日用百货
    public final static int QUOTA_RESTAURANT = 20000;//餐饮娱乐
    public final static int QUOTA_JEWELLERY = 50000;//珠宝古玩
    public final static int QUOTA_HOUSE = 50000;//汽车房产






}
