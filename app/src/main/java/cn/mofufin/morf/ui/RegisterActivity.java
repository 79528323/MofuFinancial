package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.AgreeMentActivity;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityRegisterBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding binding;
    private Timer timer;
    private int count = 60;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int second = (int)msg.obj;
            if (second>0){
                binding.smsCode.setText(second+"S");
                binding.smsCode.setEnabled(false);
            }else {
                timer.cancel();
                binding.smsCode.setText("验证码");
                binding.smsCode.setEnabled(true);
            }
        }
    };

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        binding.setHandlers(this);
        binding.setIsNext(false);
        binding.setIsAgree(false);
        timer = new Timer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
        }
    }

    public void Agreement(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    /**
     * 下一步
     * @param view
     */
    public void next(View view){
        String number = binding.registerNumber.getText().toString();
        String code = binding.registerValCode.getText().toString();
        if (TextUtils.isEmpty(number)){
            DataUtils.toastTips(this,"请输入手机号");
            return;
        }else if (TextUtils.isEmpty(code)){
            DataUtils.toastTips(this,"请输入验证码");
            return;
        }else if (!binding.getIsAgree()){
            DataUtils.toastTips(this,"请同意相关协议");
            return;
        }else if (number.length()!= 11){
            DataUtils.toastTips(this,"请输入正确的手机号");
            return;
        }else if (code.length()!=6){
            DataUtils.toastTips(this,"请输入正确的验证码");
            return;
        }

        binding.setIsNext(true);

    }


    /**
     * 提交注册
     * @param view
     */
    public void registers(View view){
        String merchantPhone = binding.registerNumber.getText().toString();
        String spreadPhone = binding.loginNumber.getText().toString();
        String password = binding.loginPwd.getText().toString();
        String smsCode = binding.registerValCode.getText().toString();

        if (TextUtils.isEmpty(merchantPhone)){
            DataUtils.toastTips(this,"请输入手机号");
            return;
        }else if (merchantPhone.length() < 11){
            DataUtils.toastTips(this,"请输入正确的手机号");
            return;
        }else if (TextUtils.isEmpty(password)){
            DataUtils.toastTips(this,"请输入密码");
            return;
        }else if (!TextUtils.isEmpty(spreadPhone) && spreadPhone.length() < 11){
            DataUtils.toastTips(this,"请输入正确的推荐人手机号");
            return;
        }else if (password.length()<6 || password.length() > 15){
            DataUtils.toastTips(this,"请输入由6~15位数组成的密码");
            return;
        }

        Subscription subscription = UserImpAPI.register(
                HttpParam.OFFICE_CODE,HttpParam.USER_REGISTER_APP_KEY,merchantPhone,smsCode,password,
                TextUtils.isEmpty(spreadPhone)?"18620573888":spreadPhone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse.DataBean> registerBaseResponse) {
                        final GeneralResponse.DataBean dataBean = registerBaseResponse.data;
                        showTips(dataBean.reason);

                        if (registerBaseResponse.bool){
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
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


    /**
     * 获取验证码
     * @param view
     */
    public void getSmsCode(View view){
        String merchantPhone = binding.registerNumber.getText().toString();
        String spreadPhone = binding.loginNumber.getText().toString();
        if (TextUtils.isEmpty(merchantPhone)){
            DataUtils.toastTips(this,"请输入手机号");
            return;
        }else if (merchantPhone.length()!= 11){
            DataUtils.toastTips(this,"请输入正确的手机号");
            return;
        }

        Subscription subscription = UserImpAPI.getSMSCode(
                merchantPhone,HttpParam.SMS_CODE_REGISTER_APPKEY,HttpParam.OFFICE_CODE,spreadPhone,null)
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
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse.DataBean> dataBeanBaseResponse) {
                        showTips(dataBeanBaseResponse.data.reason);
                        changeSmsType();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable ,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void changeSmsType(){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(handler.obtainMessage(1,count-=1));
            }
        };

        timer.schedule(task,1000,1000);
    }

    public void readAgreement(View view){
        startActivity(new Intent(this,AgreeMentActivity.class));
    }

}
