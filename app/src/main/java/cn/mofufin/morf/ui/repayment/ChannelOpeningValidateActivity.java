package cn.mofufin.morf.ui.repayment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityChannelOpeningValidateBinding;
import cn.mofufin.morf.databinding.ActivityModifyTranspasswordBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 开通通道验证
 */
public class ChannelOpeningValidateActivity extends BaseActivity {
    private ActivityChannelOpeningValidateBinding binding;
    public static final int VALIDATE_CHANNEL = 1;
    public static final int VALIDATE_CREDIT = 2;
    private Timer timer;
    private int count = 60;
    private String phone;
    private int type = 0;
    private RepayChannel.ChannelListBean channel;
    private String numcode;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int second = (int)msg.obj;
            if (second>0){
                binding.setSecond(getString(R.string.modifytrans2,String.valueOf(second)));
            }else {
                if (timer!=null){
                    timer.cancel();
                    timer = null;
                }

                binding.setSecond(getString(R.string.channelopen_2));
                binding.smsCode.setEnabled(true);
                binding.smsCode.setTextColor(ContextCompat.getColor(ChannelOpeningValidateActivity.this,R.color.app_blue));
                count = 60;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_channel_opening_validate);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        channel = getIntent().getParcelableExtra(IntentParam.BEAN);
        timer = new Timer();
        phone = DataUtils.coverPhoneNum(MerchanInfoDB.queryInfo().merchantPhone);
        binding.setPhone(getString(R.string.channelopen_3,phone));
        type = getIntent().getIntExtra(IntentParam.TYPE,type);
        if (type == VALIDATE_CHANNEL){
            binding.setTips(getString(R.string.channel_val_1));
            binding.setTitle(getString(R.string.channel_val_title));
        }else {
            binding.setTips(getString(R.string.credit_val_1));
            binding.setTitle(getString(R.string.credit_val_title));
        }
        getSmsCode();
    }

    public void changeSmsType(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(1,count-=1));
            }
        };

        timer = new Timer();
        timer.schedule(task,0,1000);

        binding.smsCode.setEnabled(false);
        binding.smsCode.setTextColor(ContextCompat.getColor(
                ChannelOpeningValidateActivity.this,R.color.dark_gray));

    }

    /**
     * 获取验证码
     */
    public void getSmsCode(){
        Subscription subscription = RepayMentImpAPI.initRefundChannelBindCardSmsSend(HttpParam.OFFICE_CODE,
                HttpParam.INIT_BINDCARD_SMS_SEND_KEY,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                getIntent().getStringExtra(IntentParam.BANKCARD_NO),String.valueOf(channel.rcNumber))
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
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        showTips(mofuResult.result_Msg);
                        if (mofuResult.result_Code == 0){
                            phone = DataUtils.coverPhoneNum(MerchanInfoDB.queryInfo().merchantPhone);
                            binding.setPhone(getString(R.string.channelopen_1,phone));
                            changeSmsType();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable ,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void getCode(View view){
        getSmsCode();
    }


    public void next(View view){
        String smscode = binding.modfyTransSms.getText().toString();

        if (TextUtils.isEmpty(smscode)){
            showTips("请输入验证码");
            return;
        }

        Subscription subscription = RepayMentImpAPI.initRefundChannelBindCardSmsAffirm(HttpParam.OFFICE_CODE,
                HttpParam.INIT_BINDCARD_SMS_CONFIRM_KEY,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                getIntent().getStringExtra(IntentParam.BANKCARD_NO),String.valueOf(channel.rcNumber),
                smscode)
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
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        showTips(mofuResult.result_Msg);
                        if (mofuResult.result_Code == 0){
                            jumpIntent();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },1000);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable ,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.next.setEnabled(s.length() == 6);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseTimer();
    }


    public void releaseTimer(){
        count = 5;
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void jumpIntent(){
        Intent intent = new Intent(ChannelOpeningValidateActivity.this, GenerationProjectActivity.class);
        intent.putExtra(IntentParam.REPAY_DAYS,getIntent().getIntArrayExtra(IntentParam.REPAY_DAYS));
        intent.putExtra(IntentParam.BEAN,channel);
        intent.putExtra(IntentParam.BANKCARD_NO,getIntent().getStringExtra(IntentParam.BANKCARD_NO));
        startActivity(intent);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
