package cn.mofufin.morf.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAboutMineBinding;
import cn.mofufin.morf.databinding.ActivityOverRiskManagementBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 跨境风控说明
 */
public class OverRiskManagementActivity extends BaseActivity {
    private ActivityOverRiskManagementBinding binding;
    private Timer timer;
    private int count = 4;
    private Overseans.OverListBean bean;
    private MerchantBag.DataBean.ListBean merchanbag;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            int second = (int)msg.obj;
            if (second > 0){
                binding.next.setText(second+"秒");
                binding.next.setTextColor(ContextCompat.getColor(OverRiskManagementActivity.this,R.color.app_blue));
            }else {
                binding.next.setText(getString(R.string.register6));
                binding.next.setTextColor(ContextCompat.getColor(OverRiskManagementActivity.this,R.color.white));
                binding.next.setBackgroundResource(R.drawable.login_button);
                binding.next.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_over_risk_management);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        merchanbag = getIntent().getParcelableExtra(IntentParam.MERCHANT_BAG);
        binding.setIsAgree(false);
        binding.next.setBackgroundResource(R.drawable.login_disable_button);
        binding.next.setEnabled(false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(1,count-=1));
            }
        };

        timer = new Timer();
        timer.schedule(task,0,1000);
    }

    public void agree(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }


    public void next(View view){
        if (!binding.getIsAgree()){
            showTips("请同意说明");
            return;
        }

        String money = getIntent().getStringExtra(IntentParam.AMOUNT);
        Subscription subscription = QueryChannelImpAPI.proceedPayNew(HttpParam.OFFICE_CODE,
                HttpParam.PROCEEDPAY_NEW_APPKEY,money,
                MerchanInfoDB.queryInfo().merchantCode,bean.channelNumber,
                merchanbag!=null?merchanbag.mcbGoodsNumber:"", Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code == 0){
                            if (bean.channelKind==0){//TODO 0:h5 , 1:扫码

                                startActivity(new Intent(
                                        OverRiskManagementActivity.this,OverseansWebActivity.class)
                                        .putExtra("HTML",baseResult.url)
                                        .putExtra(IntentParam.ACTIVITY_FLAG,OverRiskManagementActivity.this.TAG));

                            }else {
                                startActivity(new Intent(
                                        OverRiskManagementActivity.this,ScanQRReceiVablesActivity.class)
                                        .putExtra(IntentParam.QR_URL,baseResult.url)
                                        .putExtra(IntentParam.QR_TOTAL,Float.valueOf(getIntent().getStringExtra(IntentParam.QR_TOTAL)))
                                        .putExtra(IntentParam.QR_TYPE,ScanQRReceiVablesActivity.QR_CODE_OVER));

                            }
                        }else {
                            showTips(baseResult.result_Msg);
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
    protected void onDestroy() {
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
