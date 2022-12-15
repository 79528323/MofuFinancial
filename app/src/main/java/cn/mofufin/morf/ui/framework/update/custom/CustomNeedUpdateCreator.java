package cn.mofufin.morf.ui.framework.update.custom;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Process;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.framework.update.creator.DialogCreator;
import cn.mofufin.morf.ui.framework.update.model.Update;
import cn.mofufin.morf.ui.framework.update.util.ActivityManager;
import cn.mofufin.morf.ui.framework.update.util.SafeDialogOper;

public class CustomNeedUpdateCreator extends DialogCreator {
    @Override
    public Dialog create(final Update update, final Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        final Dialog dialog = new AlertDialog
                .Builder(context, R.style.AlertDialogCustom)
                .setView(view)
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView updateTitle = (TextView) view.findViewById(R.id.updateTitle);
        TextView tvUpdateContent = (TextView) view.findViewById(R.id.tv_update_content);
//        if (update.isForced()) {
//            tvUpdateContent.setText(context.getString(R.string.update_force, update.getVersionName()));
//        } else {
//            tvUpdateContent.setText(context.getString(R.string.update_not_force, update.getVersionName()));
//        }
        updateTitle.setText(String.format("发现新版本(v%s)", update.getVersionName()));
        tvUpdateContent.setText(update.getUpdateContent());
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDownloadRequest(update);
                SafeDialogOper.safeDismissDialog(dialog);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserCancel();
                SafeDialogOper.safeDismissDialog(dialog);
                if (update.isForced()) {
                    Toast.makeText(context, "即将退出应用", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 释放Activity资源。避免进程被杀后导致自动重启
                            ActivityManager.get().finishAll();
                            // 两种kill进程方式一起使用。双管齐下！
                            Process.killProcess(Process.myPid());
                            System.exit(0);
                        }
                    }, 2000);

                }
            }
        });
        return dialog;
    }
}
