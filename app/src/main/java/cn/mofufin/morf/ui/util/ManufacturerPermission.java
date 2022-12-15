package cn.mofufin.morf.ui.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

import cn.mofufin.morf.BuildConfig;

public class ManufacturerPermission {

    public static void IntentsPermission(Context context ,String flag){
        if (TextUtils.equals(flag,Brands.Huawei.toString())){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.Meizu.toString())){
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.Xiaomi.toString())){
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.setComponent(componentName);
            intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.Sony.toString())){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.OPPO.toString())){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.LG.toString())){
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            context.startActivity(intent);

        }else if (TextUtils.equals(flag,Brands.Letv.toString())){
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            context.startActivity(intent);

        }else {
            if (TextUtils.equals(flag,"360")){
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
                ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
                intent.setComponent(comp);
                context.startActivity(intent);
            }else {
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        }
    }
}
