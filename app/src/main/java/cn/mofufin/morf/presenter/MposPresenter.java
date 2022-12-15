package cn.mofufin.morf.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.hope.paysdk.PaySdkEnvionment;
import com.hope.paysdk.PaySdkListener;
import com.hope.paysdk.framework.BusiInfo;
import com.hope.paysdk.framework.mposdriver.MposDriverService;

import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.contract.MposContract;

public class MposPresenter implements MposContract.Presenter {

    private MposContract.View view;
    private RxManager rxManager;


    public MposPresenter(MposContract.View view) {
        this.view = view;
        this.rxManager = view.getRxManager();
    }

    @Override
    public void InitializationSDK(String flag) {
        view.showLoading();
        String appkey = "109017";//TODO:生产环境appkey
        String appsecret = "23e66db7-435f-427c-a9e7-6424e0d408ac";//TODO:生产环境appsecret
        String appFlag = "mofusdk";//TODO:资源目录
        final boolean debug = false;

//        if (BaseApplication.TYPE_ENVIRONMENT.PRODUCT.name().equals(flag)) {
//            appkey = "109017";//TODO:生产环境appkey
//            appsecret = "23e66db7-435f-427c-a9e7-6424e0d408ac";//TODO:生产环境appsecret
//            debug = false;
//            appFlag = "mofusdk";//TODO:资源目录
//        } else {
//            appkey = "100226";//TODO:测试环境appkey
//            appsecret = "1aea0787-a982-4c23-b2fd-081bebff7bd2";//TODO:测试环境appsecret
//            debug = true;
//            appFlag = "ceshisdk";//TODO:资源目录
//        }
        if (TextUtils.isEmpty(appkey)) {
            Logger.e("未找到接入平台");
        } else {
            MposDriverService mposDriverService = BaseApplication.initMposDriverService();
            PaySdkEnvionment.initSdk(BaseApplication.instance,
                    appkey,
                    appsecret,
                    appFlag,
                    -1,//adv_home_text_id
                    -1,// adv_home_banner_id
                    Common.PAYSDK_CALLBACK_URL,
                    mposDriverService,//mposDriverService,
                    new PaySdkListener() {

                        @Override
                        public void initedSdkListener() {
                            Logger.e("initedSdkListener");
                            view.paySdkInitializationSuccess();
                            view.hiddenLoading();
                        }

                        @Override
                        public void bindDeviceListener(boolean success, String msg, String termId, String posId) {
//                            Logger.e("bindDeviceListener,success:" + success + ",msg:" + msg + ",termId:" + termId + ",posId:" + posId);
                            Toast.makeText(BaseApplication.context, "绑定设备成功", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void startFlowListener(BusiInfo busiInfo) {
                            Logger.e("startPayListener,busiInfo:" + busiInfo);
                        }

                        public void interruptFlowListener(BusiInfo busiInfo) {
                            Logger.e("interruptFlowListener,busiInfo:" + busiInfo);
                        }

                        @Override
                        public void finishPayListener(BusiInfo busiInfo) {
                            Logger.e("finishPayListener,busiInfo:" + busiInfo.toString());
                        }

                        @Override
                        public void destoryedSdkListener() {
                            Logger.e("destoryedSdkListener");
                        }
                    }, debug);
        }
    }

}
