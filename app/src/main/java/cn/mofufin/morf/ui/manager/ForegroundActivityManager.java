package cn.mofufin.morf.ui.manager;

import android.app.Activity;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.ArrayMap;


import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yhz on 2016/5/6.
 */
public class ForegroundActivityManager {
    private List<Activity> activityList = new LinkedList<>();

    public void pushForegroundActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    public void popForegroundActivity(Activity activity) {
        activityList.remove(activity);
    }

    public boolean hasForegroundActivities() {
        return !activityList.isEmpty();
    }

    /**
     * 是否存在聊天界面
     *
     * @return
     */
//    public boolean isChatting() {
//        boolean chat = false;
//        for (Activity activity : activityList) {
//            if (TextUtils.equals(activity.getClass().getName(), ChatActivity.class.getName())
//                    || TextUtils.equals(activity.getClass().getName(), AssistantActivity.class.getName())) {
//                chat = true;
//                break;
//            }
//        }
//        return chat;
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Activity GameTopActivity() {
        Activity activity = getRunningActivity();
        return activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Activity getRunningActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread")
                    .invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
//        throw new RuntimeException("Didn't find the running activity");
    }
}
