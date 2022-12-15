package cn.mofufin.morf.ui.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.ImageUtils;
import cn.jpush.android.api.JPushInterface;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.LoginActivity;
import cn.mofufin.morf.ui.RealNameIdentityActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.CardInfoDB;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DataUtils {
    public static final String DECIMAL_FORMAT="######0.00";


    public static String getPhoneInfo(){
        StringBuffer brand = new StringBuffer();
        brand.append(Build.BRAND).append("/")//品牌
                .append(Build.MODEL).append("/")//型号
//                .append(Build.VERSION.CODENAME).append("/")
                .append(Build.VERSION.RELEASE).append("/")//版本号通用
                .append(Build.VERSION.SDK_INT);
        return brand.toString();
    }



    public static void setPermissionDailog(final Context context,String warning){
//        String content = "";
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(warning)
                .setCancelable(true);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ManufacturerPermission.IntentsPermission(context, Build.BRAND);

                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                }
                context.startActivity(localIntent);

            }
        }).setNegativeButton("以后", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
    /**
     * 实名登记提示
     * @param context
     * @param content
     * @param cancel
     * @param confirm
     */
    public static void realNameTipsDailog(final Context context, String content, String cancel, String confirm){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(content)
                .setCancelable(true);
        if (!TextUtils.isEmpty(cancel)){
            builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

        if (!TextUtils.isEmpty(confirm)){
            builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    jumpToRealName(context);
                }
            });
        }

        final Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 提示弹窗
     * @param context
     * @param content
     * @param cancel
     * @param confirm
     * @param listener
     */
    public static void TipsDailog(final Context context, String content, String cancel, String confirm
            , DialogInterface.OnClickListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(content)
                .setCancelable(true);
        if (!TextUtils.isEmpty(cancel)){
            builder.setNegativeButton(cancel, null);
        }

        if (!TextUtils.isEmpty(confirm)){
            builder.setPositiveButton(confirm, listener);
        }

        final Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 提示弹窗
     * @param context
     * @param title
     * @param content
     * @param cancel
     * @param confirm
     * @param listener
     */
    public static void TipsDailog(final Context context,String title, String content, String cancel, String confirm
            , DialogInterface.OnClickListener listener){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(true);
        if (!TextUtils.isEmpty(cancel)){
            builder.setNegativeButton(cancel, null);
        }

        if (!TextUtils.isEmpty(confirm)){
            builder.setPositiveButton(confirm, listener);
        }

        final Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     *
     * @param context
     * @param content
     * @param cancel
     * @param confirm
     * @param listener
     * @param isCancelable
     */
    public static void TipsDailog(final Context context, String content, String cancel, String confirm
            , DialogInterface.OnClickListener listener,boolean isCancelable){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(content)
                .setCancelable(isCancelable);

        if (!TextUtils.isEmpty(confirm)){
            builder.setPositiveButton(confirm, listener);
        }

        final Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 即将上线
     * @param context
     */
    public static void comingSoon(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.comingsoon))
                .setCancelable(true)
                .setNegativeButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    public static void jumpToRealName(Context context){
        context.startActivity(new Intent(context, RealNameIdentityActivity.class));
    }

    public static void toastTips(Context context,String res){
        Toast.makeText(context,res,Toast.LENGTH_SHORT).show();
    }


    /**
     * 复制文件
     * @param fromFile
     * @param toFile
     * @return
     */
    public int copy(String fromFile, String toFile)
    {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if(!root.exists())
        {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if(!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for(int i= 0;i<currentFiles.length;i++)
        {
            if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            }else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    /**
     * 文件拷贝
     //要复制的目录下的所有非子目录(文件夹)文件拷贝
     * @param fromFile
     * @param toFile
     * @return
     */
    public int CopySdcardFile(String fromFile, String toFile)
    {

        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex)
        {
            return -1;
        }
    }

    public static String GetNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static void Logout(){
        JPushInterface.stopPush(BaseApplication.context);
        JPushInterface.setAlias(BaseApplication.context,0,"");
        MerchanInfoDB.deleteMerchantInfo();
        CardInfoDB.clearAllCard();
        Intent intent = new Intent(BaseApplication.context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.context.startActivity(intent);
        BaseApplication.activityStackManager.cleanAllActivityWithout(LoginActivity.class);
    }

    public static String numFormat(float num){
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        return df.format(num);
    }

    public static String numFormat(double num){
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        return df.format(num);
    }

    public static String numCut(String s1,String s2){
        String result = "";
        if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)){
            float f1 = Float.valueOf(s1);
            float f2 = Float.valueOf(s2);
            f1 -= f2;
            result = String.valueOf(f1);
        }
        return result;
    }

    public static int getAppCurrVersionCode() {
        try {
            PackageManager packageManager = BaseApplication.context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(BaseApplication.context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAppCurrVersionName() {
        try {
            PackageManager packageManager = BaseApplication.context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(BaseApplication.context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String createSerialNo(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static String GetDateOrTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap=view.getDrawingCache();
        return bitmap;
    }


    public static void saveBitmap(Context context, Bitmap mBitmap,String fileName) {
        File filePic=null;
        String galleryPath= Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                +File.separator+"QR"+File.separator;
        try {
            filePic = new File(galleryPath + fileName+".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                mBitmap, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(filePic);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Toast.makeText(context,"保存图片成功",Toast.LENGTH_LONG).show();
        mBitmap.recycle();
        mBitmap=null;
    }

    public static String construcBankcode(String code){
        if (TextUtils.isEmpty(code)){
            return "";
        }else if (code.length() <= 4){
            return code;
        }else
            return "**** **** **** " + code.substring(code.length()-4,code.length());
    }

    public static String formatDate(String str){

        return str.substring(0,str.lastIndexOf(":"));
    }

    /**
     * 压缩文件
     * @param path
     * @param quality
     * @return
     */
    public static String compressFile(String path,int quality){
        String target = getTempPath();
        try {
            ImageUtils.compress(path,quality,Common.IMAGE_MAX_SIZE,Common.ICON_MAX_SIZE,target);
            if (!TextUtils.isEmpty(target)){
                path = target;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private static String getTempPath() {
        return BaseApplication.imageDirectory.getAbsolutePath() + "/" + System.currentTimeMillis()+"摩富测试图样" + ".jpg";
    }


    /**
     * 查看文件大小
     * @param size
     * @return
     */
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 截取字符串
     * @param s1
     * @param l1
     * @return
     */
    public static String subString(String s1,int l1){
        if (TextUtils.isEmpty(s1)){
            return "";
        }else if (s1.length() <= l1){
            return s1;
        }else {
            return s1.substring(0,l1);
        }
    }

    /**
     * 截取字符串
     * @param s1
     * @param l1
     * @param l2
     * @return
     */
    public static String subString(String s1,int l1,int l2){
        if (TextUtils.isEmpty(s1)){
            return "";
        }else if (s1.length() < l2){
            return "";
        }else if (l1 >= l2){
            return "";
        }else {
            return s1.substring(l1,l2);
        }
    }

    private static final int MIN_DELAY_TIME= 2000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 字符串全角转换为半角
     * @param input
     * @return
     */
    public static String fullToHalf(String input) {
        if (TextUtils.isEmpty(input))
            return "";
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++)
        {
            if (c[i] == 12288) //全角空格
            {
                c[i] = (char) 32;
                continue;
            }

            if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 通道（）
     * @param str
     * @param money
     * @return
     */
    public static SpannableString spannerStr(String str,int money){
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(money > 20000?"#DA323A":"#333333"));
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public static boolean spannerRatio(double ratio){
        return ratio < 0.0050;
    }


    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String bankNumStr(String num,int type){
        if (TextUtils.isEmpty(num))
            return "";

        String str = num.substring(num.length()-4,num.length());
        return "尾号为"+str+"的"+(type == 0?"借记卡":"信用卡");
    }


    //跨境手续费率转换
    public static String converOverPercent(double ratio){
        String result = DataUtils.numFormat(ratio*100);
        return result + "%";
    }


    public static String converOverPercent(String ratio){
        String result = DataUtils.numFormat(Float.valueOf(ratio)*100);
        return result + "%";
    }

    public static String converOverNOPercent(String ratio){
        String result = DataUtils.numFormat(Float.valueOf(ratio)*100);
        return result;
    }

    public static String converOverNOPercent(float ratio){
        String result = DataUtils.numFormat(ratio*100);
        return result;
    }

    public static String converOverNOPercent(double ratio){
        String result = DataUtils.numFormat(ratio*100);
        return result;
    }


    /**
     * 理财百分转换
     * @param ratio
     * @return
     */
    public static String converOverNOPercent2financial(double ratio){
        ratio = ratio*100;

        NumberFormat nf = NumberFormat.getInstance();
        // 设置此格式中不使用分组
        nf.setGroupingUsed(false);
        // 设置数的小数部分所允许的最大位数。
        nf.setMaximumFractionDigits(2);
        String result = nf.format(ratio);
        return result;
    }


    /**
     * 小数位自定
     * @param d
     * @param newValue
     * @return
     */
    public static String scientificNotation2String(Double d,int newValue){
        String value = null;
        NumberFormat nf = NumberFormat.getInstance();
        // 设置此格式中不使用分组
        nf.setGroupingUsed(false);
        // 设置数的小数部分所允许的最大位数。
        nf.setMaximumFractionDigits(newValue);
        value = nf.format(d);
        return value;
    }


    public static String scientificMatchPercent(Double d,int newValue){
        String value = null;
        NumberFormat nf = NumberFormat.getInstance();
        // 设置此格式中不使用分组
        nf.setGroupingUsed(false);
        // 设置数的小数部分所允许的最大位数。
        nf.setMaximumFractionDigits(newValue);
        value = nf.format(d);
        double temp = Double.valueOf(value);
        temp = temp * 100;
        value = nf.format(temp);
        return value+"%";
    }


    /**
     * 还款计划日期
     * @param refundDay
     * @return
     */
    public static String converDateforPlan(String refundDay){
        String result="";
        if (!TextUtils.isEmpty(refundDay)){
            String[] arry = refundDay.split("-");
            StringBuffer buffer = new StringBuffer();
            buffer.append(arry[0]).append("年").append(arry[1]).append("月").append(arry[2]).append("日");
            result = buffer.toString();
        }

        return result;
    }



    public static final int FINANCIAL_TIME = 15;
    public static final int YEAR_OF_DAYS = 365;
    /**
     *
     * @param amount 本金
     * @return
     *
     *
     * 年率化收益计算公式：本金*年利率*投资天数/365
    例如：本金1万 年利率15.8% 投资31天
    10000*0.158*31/365  收益=134.19(四舍五入保留两位小数)
     */
    /**
     * 理财预期收益算法
     * @param amount 本金
     * @param ratio 年利率
     * @param date 投资周期
     * @param interest 优惠卷（加息）
     * @return
     */
    public static String matchExpIncome(double amount,double ratio ,int date,double interest){
        String result ="";

        if (amount <= 0){
            return  "0.00";
        }

        if (!isGreaterThanPM()){
            //TODO 判断当前时间是否大于PM 3：00，如是需要在投资期限里-1天
            date -=1;
        }

        ratio += interest;//优惠卷 利率加息

        result = scientificNotation2String(((amount * ratio) * Float.valueOf(date))/Float.valueOf(YEAR_OF_DAYS),2);
        return result;
    }



    public static boolean isGreaterThanPM(){
        long current = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(current));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= FINANCIAL_TIME){//已经大于3点
            return false;
        }

        return true;
    }


    /**
     * 手机掩码
     * @param phone 13760832152
     * @return
     */
    public static String coverPhoneNum(String phone){
        if (TextUtils.isEmpty(phone))
            return phone;

        StringBuffer buffer = new StringBuffer();
        buffer.append(phone.substring(0,3))
                .append(" **** ")
                .append(phone.substring(7,11));

        return buffer.toString();
    }



    public static String conversationDate(String pattern,String day){
        DateFormat outformat = new SimpleDateFormat(pattern);
        DateFormat informat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = informat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outformat.format(date);
    }


    public static String dateDiff( String endTime) {
        String result ="";
        // 按照传入的格式生成一个simpledateformate对象
        String format ="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sd = new SimpleDateFormat(format);
//        String startTime = sd.format(endTime);
        long start = 0;
        try {
            start = sd.parse(endTime).getTime();
            format = "yyyy-MM-dd HH:mm";
            sd = new SimpleDateFormat(format);
            result = sd.format(new Date(start));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        SimpleDateFormat sd = new SimpleDateFormat(format);
//        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
//        long nh = 1000 * 60 * 60;// 一小时的毫秒数
//        long nm = 1000 * 60;// 一分钟的毫秒数
//        long ns = 1000;// 一秒钟的毫秒数
//        long diff;
//        long day = 0;
//        try {
//            // 获得两个时间的毫秒时间差异
//            diff = sd.parse(endTime).getTime()
//                    - sd.parse(startTime).getTime();
//            day = diff / nd;// 计算差多少天
//            long hour = diff % nd / nh;// 计算差多少小时
//            long min = diff % nd % nh / nm;// 计算差多少分钟
//            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
//            // 输出结果
//            System.out.println("时间相差：" + day + "天" + hour + "小时" + min
//                    + "分钟" + sec + "秒。");
//            if (day>0) {
//                return day+"天后到期";
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return result+" 到期";
    }


    public static String converLoansPeriod(String period){
        String temp ="";
        if (TextUtils.isEmpty(period))
            return temp;

        int index = period.lastIndexOf("-");
        if (index < 0)
            return period;

        return period.substring(index+1,period.length());
    }

    /**
     * 设置顶部状态栏
     * @param activity
     */
    public static void setStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true,activity);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    /*| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.WHITE);
        }else {
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(19)
    private  static void setTranslucentStatus(boolean on,Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

//    public interface OnRefreshMerchantInfoListener{
//        void onSuccess(User.DataBean user);
//        void onErrors(Throwable throwable);
//    }

    //刷新商户信息
    public static void refreshMerchantInfo(RxManager rxManager, final OnRefreshMerchantInfoListener listener){

        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> response) {
                        if (response.bool){
                            listener.onSuccess(response.data);
                        }else{
                            listener.onToast(response.result_Msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onErrors(throwable);
                    }
                });
        rxManager.add(subscription);
    }



    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 掩名字
     * @param str
     * @return
     */
    public static String coverStr(String str){
        if (TextUtils.isEmpty(str))
            return "";

        if (str.length() <= 2){
            return str.substring(0,1) + "*";
        }else if (str.length() <= 3){
            return str.substring(0,1) + "**";
        }else
            return str.substring(0,2) + "**";
    }
}
