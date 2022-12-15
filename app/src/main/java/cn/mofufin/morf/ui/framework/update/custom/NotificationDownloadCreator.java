package cn.mofufin.morf.ui.framework.update.custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import java.io.File;
import java.util.UUID;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.framework.update.callback.UpdateDownloadCB;
import cn.mofufin.morf.ui.framework.update.creator.DefaultNeedDownloadCreator;
import cn.mofufin.morf.ui.framework.update.creator.DownloadCreator;
import cn.mofufin.morf.ui.framework.update.model.Update;
import cn.mofufin.morf.ui.framework.update.util.SafeDialogOper;
import cn.mofufin.morf.ui.widget.NumberProgressBar;

/**
 * <p>
 * 很多小伙伴提意见说需要一个下载时在通知栏进行进度条显示更新的功能。
 * 此类用于提供此种需求的解决方案。以及如何对其进行定制。满足任意场景使用
 * 默认使用参考：{@link DefaultNeedDownloadCreator}
 * </p>
 */
public class NotificationDownloadCreator implements DownloadCreator {
    @Override
    public UpdateDownloadCB create(Update update, Activity activity) {
        // 返回一个UpdateDownloadCB对象用于下载时使用来更新界面。
        return new NotificationCB(activity);
    }

    private static class NotificationCB implements UpdateDownloadCB {

        NotificationManager manager;
        NotificationCompat.Builder builder;
        int id;
        int preProgress;

        Dialog dialog;
        NumberProgressBar npb;


        NotificationCB(Activity activity) {
            this.manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(activity);
            builder.setProgress(100, 0, false)
                    .setSmallIcon(activity.getApplicationInfo().icon)
                    .setAutoCancel(false)
//                    .setContentTitle(activity.getString(R.string.update_app))
                    .setContentText("下载中...")
                    .build();
            id = Math.abs(UUID.randomUUID().hashCode());

            View downloadProgressView = View.inflate(activity, R.layout.download_progress_layout, null);
            npb = (NumberProgressBar) downloadProgressView.findViewById(R.id.number_bar);
            dialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                    .setView(downloadProgressView)
                    .setMessage(R.string.download_progress)
                    .setCancelable(false)
                    .create();
            SafeDialogOper.safeShowDialog(dialog);
        }

        @Override
        public void onDownloadStart() {
            // 下载开始时的通知回调。运行于主线程
            manager.notify(id, builder.build());
        }

        @Override
        public void onDownloadComplete(File file) {
            // 下载完成的回调。运行于主线程
            manager.cancel(id);
        }

        @Override
        public void onDownloadProgress(long current, long total) {
            // 下载过程中的进度信息。在此获取进度信息。运行于主线程
            int progress = (int) (current * 1f / total * 100);
            // 过滤不必要的刷新进度
            if (preProgress < progress) {
                preProgress = progress;
                builder.setProgress(100, progress, false);
                builder.setContentText(String.format("下载进度：%d%%", progress));
                manager.notify(id, builder.build());
            }
            npb.setProgress(progress);
        }

        @Override
        public void onDownloadError(Throwable t) {
            // 下载时出错。运行于主线程
            manager.cancel(id);
            SafeDialogOper.safeDismissDialog(dialog);
        }
    }
}
