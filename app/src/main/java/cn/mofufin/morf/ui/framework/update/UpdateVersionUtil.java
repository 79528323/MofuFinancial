package cn.mofufin.morf.ui.framework.update;//package com.ddpkcc.dingding.framework.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.VersionInfo;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;


/**
 * 检测新版本
 */

public class UpdateVersionUtil {

    public interface VersionCallBack {
        void callBack(VersionInfo versionInfo);
    }

    /**
     * 获取版本信息
     */
//    public static void checkServiceVersion(RxManager rxManager, final VersionCallBack callBack) {
//        Subscription version = VersionimplAPI.version("2")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(new Action1<VersionInfo>() {
//                    @Override
//                    public void call(VersionInfo versionInfo) {
//                        if (callBack != null)
//                            callBack.callBack(versionInfo);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                    }
//                });
//        rxManager.add(version);
//    }

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

    /**
     * 弹出新版本提示
     *
     * @param context     上下文
     * @param versionInfo 更新内容
     */
    public static void showTipDialog(final Activity context, final VersionInfo versionInfo,
                                     final OnOkClickListener okClickListener,
                                     final OnCancleClickListener cancleClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        final Dialog dialog = new AlertDialog
                .Builder(context, R.style.AlertDialogCustom)
                .setView(view)
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
//        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
//        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
//        TextView tvUpdateMsgSize = (TextView) view.findViewById(R.id.tv_update_msg_size);

//        tvContent.setText(versionInfo.getVersionDesc());
//        if ("1".equals(versionInfo.isforce)) {
//            tvUpdateTile.setText(context.getString(R.string.update_force, versionInfo.version));
//        } else {
//            tvUpdateTile.setText(context.getString(R.string.update_not_force, versionInfo.version));
//        }
//        tvUpdateMsgSize.setText("新版本大小：" + versionInfo.getVersionSize());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (okClickListener != null) {
                    okClickListener.okClicked();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (cancleClickListener != null) {
                    cancleClickListener.cancleClicked();
                }
            }
        });
        dialog.show();
    }

    public interface OnOkClickListener {
        void okClicked();
    }

    public interface OnCancleClickListener {
        void cancleClicked();
    }
}
