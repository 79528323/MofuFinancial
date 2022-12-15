package cn.mofufin.morf.ui.base;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.hope.paysdk.core.SelectDeviceTypeData;
import com.hope.paysdk.core.UiEnvService;
import com.hope.paysdk.framework.mposdriver.MposDriverService;
import com.hope.paysdk.framework.mposdriver.daqu.DaQuBtDfbService;
import com.hope.paysdk.framework.mposdriver.model.BtDeviceModel;
import com.hope.paysdk.framework.mposdriver.model.DeviceEnum;
import com.hope.paysdk.framework.mposdriver.model.DeviceModel;
import com.hope.paysdk.framework.mposdriver.model.ModelDrawable;
import com.hope.paysdk.framework.mposdriver.posflow.FlowEnum;
//import com.tencent.tinker.entry.ApplicationLike;
//import com.tinkerpatch.sdk.TinkerPatch;
//import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.ruis.lib.base.LibBaseApplication;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.ActivityStackManager;
import cc.ruis.lib.util.Logger;
//import cn.mofufin.morf.BuildConfig;
import cn.jpush.android.api.JPushInterface;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.Listener.MyLifecycleHandler;
import cn.mofufin.morf.ui.manager.ForegroundActivityManager;
import cn.mofufin.morf.ui.framework.update.UpdateConfig;
import cn.mofufin.morf.ui.framework.update.custom.AllDialogShowStrategy;
import cn.mofufin.morf.ui.framework.update.custom.CustomNeedUpdateCreator;
import cn.mofufin.morf.ui.framework.update.custom.NotificationDownloadCreator;
import cn.mofufin.morf.ui.framework.update.custom.OkhttpCheckWorker;
import cn.mofufin.morf.ui.framework.update.custom.OkhttpDownloadWorker;
import cn.mofufin.morf.ui.framework.update.custom.VersionNameUpdateChecker;
import cn.mofufin.morf.ui.framework.update.model.CheckEntity;
import cn.mofufin.morf.ui.framework.update.model.Update;
import cn.mofufin.morf.ui.framework.update.model.UpdateParser;
import cn.mofufin.morf.ui.util.Common;
//import cn.mofufin.morf.ui.util.FetchPatchHandler;
import cn.mofufin.morf.ui.util.HttpParam;

/**
 * Created by yhz on 2015/5/22.
 */
public class BaseApplication extends LibBaseApplication {
    public static ActivityStackManager activityStackManager;
    public static ActivityStackManager topicactivityStackManager;
    public static ForegroundActivityManager foregroundActivityManager;
    public static Context context;
//    public static int PHONE_RING_MODE;
    public static Uri uri;
    private static int SYSTEM = 2;// 2 表示 Android
    private RxManager rxManager = new RxManager();
    public static BaseApplication instance;
    public static boolean PAY_SDK_INIT = false;
//    private ApplicationLike tinkerApplicationLike;
    private ActivityManager activityManager;


//    public static int statementDate=0;
//    public static int statementMonth=0;
//    public static int repaymentDate=0;
//    public static int repaymentMonth=0;
//    public static boolean isPost = false;
//    public static boolean isPostStateD = false;


    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.TINKER_ENABLE) {
//            // 我们可以从这里获得Tinker加载过程的信息
//            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
//
//            // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
//            TinkerPatch.init(tinkerApplicationLike)
//                    .reflectPatchLibrary()
//                    .setPatchRollbackOnScreenOff(true)
//                    .setPatchRestartOnSrceenOff(true);
//
//            new FetchPatchHandler().fetchPatchWithInterval(3);
//        }

        JPushInterface.setDebugMode(BuildConfig.FLAVOR.equals(Common.TYPE_DEVELOP));
        JPushInterface.init(this);


        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            return;
        }
        PAY_SDK_INIT = false;
        activityStackManager = new ActivityStackManager();
        topicactivityStackManager = new ActivityStackManager();
        foregroundActivityManager = new ForegroundActivityManager();
        context = this;
        instance = this;
//        PHONE_RING_MODE = ((AudioManager) context.getSystemService(AUDIO_SERVICE)).getMode();

//        if (!BuildConfig.FLAVOR.equals(Common.TYPE_PRODUCE)) {
//            CrashHandler.logWhenCrash(this);
//        }
        initClipboardUIManager();
        //DataUtils.circleTransform=new CircleTransform(this);
        setTextScaleNormal();
        initFile();
//        initListener();
        initUploadConfig();
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());//监测所有Activity状态
//        moduleBlock();
//        getAndroidAuditVersion();
//        InitializationPaySDK();

        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long TotalMemory = memoryInfo.totalMem;
        long avlMem = memoryInfo.availMem/1000000;
        long thMem = memoryInfo.threshold/1000000;
        Logger.e("333","avlMem="+avlMem);
        Logger.e("333","thMem="+thMem);
    }

    public String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    private void initUploadConfig() {
        Map<String,String> param = new HashMap<>();
        param.put("officeCode",HttpParam.OFFICE_CODE);
        UpdateConfig.getConfig()
                // 必填：数据更新接口,url与checkEntity两种方式任选一种填写
//                .url(Common.SYSTEM_APK + "/queryApkFileVersion")
                .checkEntity(new CheckEntity().setParams(param).setMethod("POST")
                        .setUrl(Common.DOWNLOAD_APK_HOST))
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        /* 此处根据上面url或者checkEntity设置的检查更新接口的返回数据response解析出
                         * 一个update对象返回即可。更新启动时框架内部即可根据update对象的数据进行处理
                         */
                        Logger.e( "UpdateConfig response = " + response);
                        JSONObject object = new JSONObject(response);
                        Update update = new Update();
                        // 此apk包的下载地址
                        update.setUpdateUrl(object.optString("vPath")+object.optString("vName"));

//                        update.setUpdateUrl("http://172.20.10.57:8080/JFBank/upload/officeapk/mofu_1.5.1_release1.apk");


                        // 此apk包的版本号
//                        update.setVersionCode(UpdateVersionUtil.getAppCurrVersionCode() + 1);
                        // 此apk包的版本名称
                        update.setVersionName(object.optString("vCode"));
                        // 此apk包的更新内容
                        update.setUpdateContent("");
                        // 此apk包是否为强制更新
                        update.setForced(true);
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(false);
                        return update;
                    }
                })
                .checkWorker(new OkhttpCheckWorker())//使用okHttp检查更新
                .downloadWorker(new OkhttpDownloadWorker())//使用okHttp下载
                .updateDialogCreator(new CustomNeedUpdateCreator())//设置更新提示弹窗
                .downloadDialogCreator(new NotificationDownloadCreator())//设置通知栏和弹窗显示下载进度
                .updateChecker(new VersionNameUpdateChecker())//设置版本判断方式
                .strategy(new AllDialogShowStrategy());//设置弹窗提醒
    }


    /**
     * 初始化设备驱动服务
     */
    public static MposDriverService initMposDriverService() {
        //设备驱动服务
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        BtDeviceModel dq_dfb = new BtDeviceModel(
                new DeviceEnum.EnumDeviceType[]{
                        DeviceEnum.EnumDeviceType.DeviceType_Daqu_DfbBluetooth_GENWK
                },//适配设备类型
                DaQuBtDfbService.class,//设备驱动
                "蓝牙点付宝-M2",//名称
                new ModelDrawable(R.drawable.dfb_m2, false),//图标
                new String[]{"DQM2"});//蓝牙名称
        Map<FlowEnum.EnumAnim, ModelDrawable> anim_dq_dfb = new HashMap<FlowEnum.EnumAnim, ModelDrawable>();
        anim_dq_dfb.put(FlowEnum.EnumAnim.PlsPlugOrOnDev, new ModelDrawable(R.drawable.anim_plugdev_dfb_m2, true));
        anim_dq_dfb.put(FlowEnum.EnumAnim.PlsConnectDev, new ModelDrawable(R.drawable.anim_plugdev_dfb_m2, true));
        anim_dq_dfb.put(FlowEnum.EnumAnim.DevSigning, new ModelDrawable(R.drawable.anim_plugdev_dfb_m2, true));
        anim_dq_dfb.put(FlowEnum.EnumAnim.PlsSwiper, new ModelDrawable(R.drawable.anim_swiper_dfb_m2, true));
        anim_dq_dfb.put(FlowEnum.EnumAnim.PlsSwiperPwd, new ModelDrawable(R.drawable.anim_swiperpwd_dfb_m2, true));
        anim_dq_dfb.put(FlowEnum.EnumAnim.PlsNfc, new ModelDrawable(R.drawable.anim_nfc_dfb_m2, true));
        dq_dfb.setDevAnim(anim_dq_dfb);
        devices.add(dq_dfb);

        MposDriverService mposDriverService = new MposDriverService(devices);

        //初始化选择设备页数据
        //单列
        UiEnvService.SelectDeviceTypeData = new SelectDeviceTypeData(devices);
        return mposDriverService;
    }


    public enum TYPE_ENVIRONMENT {
        DEVELOP,
        PRODUCT
    }


}