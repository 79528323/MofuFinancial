package cn.mofufin.morf.ui.framework.update.custom;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import cn.mofufin.morf.ui.framework.update.model.Update;
import cn.mofufin.morf.ui.framework.update.model.UpdateChecker;
import cn.mofufin.morf.ui.framework.update.util.ActivityManager;
import cn.mofufin.morf.ui.framework.update.util.UpdatePreference;

public class VersionNameUpdateChecker implements UpdateChecker {
    @Override
    public boolean check(Update update) throws Exception {
        String curVersion = getApkVersion(ActivityManager.get().getApplicationContext());
        return !curVersion.equals(update.getVersionName()) &&
                (update.isForced() ||
                        !UpdatePreference.getIgnoreVersions().contains(update.getVersionName()));
    }

    public String getApkVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName;
    }
}
