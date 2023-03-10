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
    private static int SYSTEM = 2;// 2 ?????? Android
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
//            // ???????????????????????????Tinker?????????????????????
//            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
//
//            // ?????????TinkerPatch SDK, ?????????????????????API????????????,?????????SDK
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
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());//????????????Activity??????
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
                // ???????????????????????????,url???checkEntity??????????????????????????????
//                .url(Common.SYSTEM_APK + "/queryApkFileVersion")
                .checkEntity(new CheckEntity().setParams(param).setMethod("POST")
                        .setUrl(Common.DOWNLOAD_APK_HOST))
                // ???????????????????????????????????????????????????response???????????????Update?????????????????????????????????
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        /* ??????????????????url??????checkEntity??????????????????????????????????????????response?????????
                         * ??????update????????????????????????????????????????????????????????????update???????????????????????????
                         */
                        Logger.e( "UpdateConfig response = " + response);
                        JSONObject object = new JSONObject(response);
                        Update update = new Update();
                        // ???apk??????????????????
                        update.setUpdateUrl(object.optString("vPath")+object.optString("vName"));

//                        update.setUpdateUrl("http://172.20.10.57:8080/JFBank/upload/officeapk/mofu_1.5.1_release1.apk");


                        // ???apk???????????????
//                        update.setVersionCode(UpdateVersionUtil.getAppCurrVersionCode() + 1);
                        // ???apk??????????????????
                        update.setVersionName(object.optString("vCode"));
                        // ???apk??????????????????
                        update.setUpdateContent("");
                        // ???apk????????????????????????
                        update.setForced(true);
                        // ??????????????????????????????????????????
                        update.setIgnore(false);
                        return update;
                    }
                })
                .checkWorker(new OkhttpCheckWorker())//??????okHttp????????????
                .downloadWorker(new OkhttpDownloadWorker())//??????okHttp??????
                .updateDialogCreator(new CustomNeedUpdateCreator())//????????????????????????
                .downloadDialogCreator(new NotificationDownloadCreator())//??????????????????????????????????????????
                .updateChecker(new VersionNameUpdateChecker())//????????????????????????
                .strategy(new AllDialogShowStrategy());//??????????????????
    }


    /**
     * ???????????????????????????
     */
    public static MposDriverService initMposDriverService() {
        //??????????????????
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        BtDeviceModel dq_dfb = new BtDeviceModel(
                new DeviceEnum.EnumDeviceType[]{
                        DeviceEnum.EnumDeviceType.DeviceType_Daqu_DfbBluetooth_GENWK
                },//??????????????????
                DaQuBtDfbService.class,//????????????
                "???????????????-M2",//??????
                new ModelDrawable(R.drawable.dfb_m2, false),//??????
                new String[]{"DQM2"});//????????????
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

        //??????????????????????????????
        //??????
        UiEnvService.SelectDeviceTypeData = new SelectDeviceTypeData(devices);
        return mposDriverService;
    }


    public enum TYPE_ENVIRONMENT {
        DEVELOP,
        PRODUCT
    }


}