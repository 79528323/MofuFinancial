package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityAlipayBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 支付宝页面
 */
public class AlipayActivity extends BaseActivity{
    private ActivityAlipayBinding binding;
    private String orderInfo="";
    private static final int SDK_PAY_FLAG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_alipay);
        binding.setHandlers(this);

        getOrder();
    }

    public void getOrder(){
        Subscription subscription = UtilsImpAPI.createAliOrder("da934072afb6a2fb5f0831af2a63de36", HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode,"0.01")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code==0){
                            orderInfo = baseResult.sign;
                            Logger.e("orderInfo  ==== "+orderInfo);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void alipay(View view){
        //异步处理
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //新建任务
                PayTask alipay = new PayTask(AlipayActivity.this);
                //获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread thread = new Thread(payRunnable);
        thread.start();

    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_PAY_FLAG:
                    String resultStatus="" , result="" ,memo="";
                    Map<String, String> rawResult = (Map<String, String>) msg.obj;
                    if (rawResult == null) {
                        return;
                    }

                    for (String key : rawResult.keySet()) {
                        if (TextUtils.equals(key, "resultStatus")) {
                            resultStatus = rawResult.get(key);
                        } else if (TextUtils.equals(key, "result")) {
                            result = rawResult.get(key);
                        } else if (TextUtils.equals(key, "memo")) {
                            memo = rawResult.get(key);
                        }
                    }

                    Logger.e("resultStatus="+resultStatus);
                    Logger.e("result="+result);
                    Logger.e("memo="+memo);

                    if (TextUtils.equals(resultStatus, "9000")) {
                        showTips("支付成功");
                    } else {
                        showTips("支付失败");
                    }

            }
        }
    };

}
