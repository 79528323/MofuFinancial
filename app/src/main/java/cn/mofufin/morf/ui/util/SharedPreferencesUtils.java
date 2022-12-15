package cn.mofufin.morf.ui.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 陈燕明
 * @time 2017/4/14 0014  上午 9:43
 * @desc ${TODD}
 */

public class SharedPreferencesUtils {
    public static final String PREF_NAME = "config";
    private static final String TOPIC_SEARCH_KEYWORD = "topic_search_keyword";
    private static final String TOPIC_RECORD = "topic_record";


    /**
     * 向SharePreferences中封装boolean类型数据
     */

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharePreferences中取出boolean类型数据
     *
     * @param ctx          上下文对象
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 向SharePreferences中封装String类型数据
     */
    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 从SharePreferences中取出String类型数据
     */
    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 向SharePreferences中封装Long类型数据
     */
    public static void setLong(Context ctx, String key, long value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();
    }

    /**
     * 从SharePreferences中取出Long类型数据
     */
    public static long getLong(Context ctx, String key, long defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    /**
     * 向SharePreferences中封装Int类型数据
     */
    public static void setInt(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 从SharePreferences中取出Int类型数据
     */
    public static int getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 保存话题搜索关键字
     *
     * @param ctx           上下文
     * @param searchContent 关键字
     * @param maxSize       最大保存条数
     */
    public static void addTopicKeyword(Context ctx, String searchContent, int maxSize) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        List<String> list = getTopicKeywordList(ctx);
        if (!list.contains(searchContent)) {
            list.add(0, searchContent);
        } else {
            int index = list.indexOf(searchContent);
            String temp = list.get(0);
            list.set(0, searchContent);
            list.set(index, temp);
        }
        if (list.size() > maxSize)
            list.remove(maxSize);
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        sp.edit().putString(TOPIC_SEARCH_KEYWORD, Arrays.toString(arr)).commit();
    }

    /**
     * 获取话题历史搜索关键字集合
     *
     * @param ctx 上下文
     * @return 返回List集合
     */
    public static List<String> getTopicKeywordList(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String arrStr = sp.getString(TOPIC_SEARCH_KEYWORD, "").replace("[", "").replace("]", "").replace(" ", "");
        List<String> list;
        if (!TextUtils.isEmpty(arrStr)) {
            list = new ArrayList<>(Arrays.asList(arrStr.split(",")));
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 清除话题搜索记录
     *
     * @param ctx 上下文
     * @return 成功则返回true
     */
    public static boolean removeTopicSearchHistory(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.edit().putString(TOPIC_SEARCH_KEYWORD, "").commit();
    }

    /**
     * 保存话题浏览记录
     *
     * @param ctx     上下文
     * @param topicId 话题ID
     */
    public static void addTopicRecord(Context ctx, String topicId) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String recordStr = getTopicRecord(ctx).replace("[", "").replace("]", "").replace(" ", "");
        List<String> list;
        if (!TextUtils.isEmpty(recordStr)) {
            list = new ArrayList<>(Arrays.asList(recordStr.split(",")));
        } else {
            list = new ArrayList<>();
        }
        if (!list.contains(topicId)) {
            list.add(0, topicId);
        } else {
            int index = list.indexOf(topicId);
            String temp = list.get(0);
            list.set(0, topicId);
            list.set(index, temp);
        }
        String[] arr = new String[list.size()];
        arr = list.toArray(arr);
        sp.edit().putString(TOPIC_RECORD, Arrays.toString(arr)).commit();//保存字符串格式为 [1, 3, 2, 6]
    }

    /**
     * 获取话题浏览记录
     *
     * @param ctx 上下文
     * @return 返回格式为 [1, 3, 2, 6] 的字符串
     */
    public static String getTopicRecord(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(TOPIC_RECORD, "");
    }
}
