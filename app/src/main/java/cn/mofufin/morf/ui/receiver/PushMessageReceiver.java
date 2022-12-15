package cn.mofufin.morf.ui.receiver;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cc.ruis.lib.util.Logger;
import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import cn.mofufin.morf.ui.HomeActivity;
import cn.mofufin.morf.ui.LaucherActivity;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.manager.NotifyManager;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";

    /**
     * 收到自定义消息回调
     * @param context
     * @param customMessage
     */
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Logger.e(TAG,"onMessage =="+customMessage.toString());
        NotifyManager.getInstance().constuctorNotify(context,customMessage);
    }

    /**
     * 点击通知回调
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG,"onNotifyMessageOpened");
        if (null!= BaseApplication.activityStackManager){
            Activity activity = BaseApplication.activityStackManager.getActivity(HomeActivity.class);
            if (activity==null){//已经退出
                Intent intent = new Intent(context, LaucherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else {
                Toast.makeText(context,"打开通知：\n"+notificationMessage.notificationContent,Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 收到通知回调
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG,"onNotifyMessageArrived ："+notificationMessage.toString());
    }

    /**
     * 清除通知回调
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG,"onNotifyMessageDismiss");
    }

    /**
     * 注册成功回调
     * @param context
     * @param s
     */
    @Override
    public void onRegister(Context context, String s) {
        Logger.e(TAG,"onRegister");
    }


    /**
     * 长连接状态回调
     * @param context
     * @param b
     */
    @Override
    public void onConnected(Context context, boolean b) {
        Logger.e(TAG,"onConnected");
    }

    /**
     * 注册失败回调
     * @param context
     * @param cmdMessage
     */
    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Logger.e(TAG,"onCommandResult");
    }

    /**
     * tag 增删查改的操作会在此方法中回调结果。
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG,"onTagOperatorResult");
    }

    /**
     * 查询某个 tag 与当前用户的绑定状态的操作会在此方法中回调结果。
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG,"onCheckTagOperatorResult");
    }

    /**
     * alias 相关的操作会在此方法中回调结果。
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG,"onAliasOperatorResult");
    }

    /**
     * 设置手机号码会在此方法中回调结果
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.e(TAG,"onMobileNumberOperatorResult");
    }

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Logger.e(TAG,"getNotification");
        return super.getNotification(context, notificationMessage);
    }

}
