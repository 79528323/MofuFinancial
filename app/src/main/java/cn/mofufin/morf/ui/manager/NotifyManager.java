package cn.mofufin.morf.ui.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import androidx.core.app.NotificationCompat;
import cn.jpush.android.api.CustomMessage;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.CreditApplyActivity;
import cn.mofufin.morf.ui.HomeActivity;
import cn.mofufin.morf.ui.LaucherActivity;
import cn.mofufin.morf.ui.MerChantScanQRActivity;
import cn.mofufin.morf.ui.MofuMallActivity;
import cn.mofufin.morf.ui.MposChanceActivity;
import cn.mofufin.morf.ui.OverseasChancesActivity;
import cn.mofufin.morf.ui.ScanQRReceiVablesActivity;
import cn.mofufin.morf.ui.SelectionChannelActivity;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.Notifys;
import cn.mofufin.morf.ui.home.InvitationActivity;
import cn.mofufin.morf.ui.loan.MofuLoanHomeActivity;
import cn.mofufin.morf.ui.mine.AboutMineActivity;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.mine.BalanceActivity;
import cn.mofufin.morf.ui.mine.CardManagerActivity;
import cn.mofufin.morf.ui.mine.CommissionActivity;
import cn.mofufin.morf.ui.mine.MofuCoinActivity;
import cn.mofufin.morf.ui.mine.MoreActivity;
import cn.mofufin.morf.ui.mine.MyInfoActivity;
import cn.mofufin.morf.ui.repayment.RepayMentManagerActivity;
import cn.mofufin.morf.ui.util.IntentParam;

public class NotifyManager {

    private static NotifyManager instance;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    public static NotifyManager getInstance(){
        if (instance==null){
            instance = new NotifyManager();
        }

        return instance;
    }

    @SuppressLint("WrongConstant")
    public void constuctorNotify(Context context, CustomMessage customMessage){
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = context.getPackageName();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel advertChannel = new NotificationChannel(
//                    context.getPackageName(),"摩富通知_1", NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(advertChannel);
//            NotificationChannelGroup
            NotificationChannel noticeChannel  = new NotificationChannel(
                    CHANNEL_ID ,"摩富通知", NotificationManager.IMPORTANCE_HIGH);
            noticeChannel.setShowBadge(true);
            manager.createNotificationChannel(noticeChannel);

            builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        }else
            builder = new NotificationCompat.Builder(context);

        Notifys notifys = JSONObject.parseObject(customMessage.extra, Notifys.class);
        notifys.setTitle(customMessage.title);

        Class<?> mClass = null;
        if (null!= BaseApplication.activityStackManager){
            Activity activity = BaseApplication.activityStackManager.getActivity(HomeActivity.class);
            if (activity==null){//已经退出
                mClass =LaucherActivity.class;
            }else {
                mClass = HomeActivity.class;
            }
        }

        Intent intent = new Intent(context,mClass);
        intent.putExtra(IntentParam.NOTIFYS,notifys);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(IntentParam.BEAN,notifys);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setAutoCancel(true)//点击消息后自动清除
                .setSmallIcon(R.mipmap.noti_logo)//设置状态栏小图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round))
                .setContentTitle(customMessage.title)//下拉后显示的标题
                .setTicker(customMessage.title)//状态栏标题
                .setWhen(System.currentTimeMillis())
                .setContentText(customMessage.message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            builder.setFullScreenIntent(pendingIntent,false);
        }

        manager.notify(1,builder.build());
    }



//    public Class<?> wichActivityClass(Notifys notifys){
//        Class<?> mClass = null;
//        if (null!= BaseApplication.activityStackManager){
//            Activity activity = BaseApplication.activityStackManager.getActivity(HomeActivity.class);
//            if (activity==null){//已经退出
//                mClass = LaucherActivity.class;
//            }else {
//                int params = Integer.valueOf(notifys.params);
//                if (params == 1){
//                    mClass = MofuMallActivity.class;
//                }else if (params == 2){
//                    mClass = MofuCoinActivity.class;
//                }else if (params == 3){
//                    mClass = CommissionActivity.class;
//                }else if (params == 4){
//                    mClass = BalanceActivity.class;
//                }else if (params == 5){
//                    mClass = BackpackActivity.class;
//                }else if (params == 6){
//                    mClass = CardManagerActivity.class;
//                }else if (params == 7){
//                    mClass = MoreActivity.class;
//                }else if (params == 8){
//                    mClass = MofuLoanHomeActivity.class;
//                }else if (params == 9){
//                    mClass = RepayMentManagerActivity.class;
//                }else if (params == 10){
//                    mClass = InvitationActivity.class;
//                }else if (params == 11){
//                    mClass = MyInfoActivity.class;
//                }else if (params == 12){//TODO 订单查询
//                    mClass = RepayMentManagerActivity.class;
//                }else if (params == 13){
//                    mClass = CreditApplyActivity.class;
//                }else if (params == 14){
//                    mClass = MposChanceActivity.class;
//                }else if (params == 15){
//                    mClass = SelectionChannelActivity.class;
//                }else if (params == 16){
//                    mClass = ScanQRReceiVablesActivity.class;
//                }else if (params == 17){
//                    mClass = OverseasChancesActivity.class;
//                }else
//                    mClass = HomeActivity.class;
//            }
//        }
//        return mClass;
//    }
}
