package cn.mofufin.morf.ui.mine;

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
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityModifyLogPassWordBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ModifyLogPassWordActivity extends BaseActivity {
    private ActivityModifyLogPassWordBinding binding;
    private Timer timer;
    private int count = 60;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int second = (int)msg.obj;
            if (second>0){
                binding.btnSmscode.setText(second+"S");
                binding.btnSmscode.setEnabled(false);
            }else {
                timer.cancel();
                binding.btnSmscode.setText("验证码");
                binding.btnSmscode.setEnabled(true);
                count = 60;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_modify_log_pass_word);
        binding.setHandlers(this);
        binding.setIsNext(false);
        timer = new Timer();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void next(View view){
        String merchantPhone = binding.modfyLogPhone.getText().toString();
        String smscode = binding.modfyLogSms.getText().toString();
        String newCode = binding.modfyLogNewCode.getText().toString();
        String repeatCode = binding.modfyLogRepeat.getText().toString();

        if (binding.getIsNext()){
            Subscription subscription = UserImpAPI.resetLoginPassword(
                    HttpParam.MODIFY_LOGIN_PASSWROD_APPKEY,HttpParam.OFFICE_CODE,
                    merchantPhone,smscode
                    ,newCode,repeatCode)
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
                            if (dataBeanBaseResponse.bool){
                                finish();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            onError(throwable,true);
                        }
                    });
            rxManager.add(subscription);
        }else {
            if (TextUtils.isEmpty(merchantPhone)){
                showTips("请输入您的注册手机号");
                return;
            }else if (merchantPhone.length()!=11){
                showTips("您输入的注册手机号不正确，请重新输入");
                binding.modfyLogPhone.setText("");
                return;
            }else if (TextUtils.isEmpty(smscode)){
                showTips("请输入验证码");
//                binding.modfyLogPhone.setText("");
                return;
            }
            binding.setIsNext(true);
        }

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
    }

    /**
     * 获取验证码
     * @param view
     */
    public void getSmsCode(View view){
        String phone = binding.modfyLogPhone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showTips("请输入您的注册手机号");
            return;
        }else if (phone.length()!=11){
            showTips("您输入的注册手机号不正确，请重新输入");
//            binding.modfyLogPhone.setText("");
            return;
        }

        Subscription subscription = UserImpAPI.getSMSCode(
                phone,HttpParam.SMS_CODE_MODIFY_APPKEY,HttpParam.OFFICE_CODE,null,null)
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
                        if (dataBeanBaseResponse.bool) {
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
}
