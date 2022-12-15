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
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityChangeAccountBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChangeAccountActivity extends BaseActivity {
    private ActivityChangeAccountBinding binding;
    private Timer timer;
    private int count = 60;
    private int RESET_NUMBER=0;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_account);
        binding.setHandlers(this);
        timer = new Timer();
        binding.setTips(getString(R.string.change_account_1,RESET_NUMBER));
        updateMerchantInfo();
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
        String orgPhone = binding.orgPhone.getText().toString();
        String idCard = binding.idCard.getText().toString();
        String newPhone = binding.newPhone.getText().toString();
        String smsCode = binding.smsCode.getText().toString();

        if (TextUtils.isEmpty(orgPhone)){
            showTips(getString(R.string.change_account_2));
            return;
        }else if (TextUtils.isEmpty(idCard)){
            showTips("请输入身份证号");
            return;
        }else if (TextUtils.isEmpty(newPhone)){
            showTips("请输入新手机号");
            return;
        }else if (TextUtils.isEmpty(smsCode)){
            showTips(getString(R.string.modfyCode5));
            return;
        }else if (RESET_NUMBER <= 0){
            showTips("当前剩余更换次数为0");
            return;
        }

        Subscription subscription = UserImpAPI.updateAccountName(HttpParam.UPDATE_ACCOUNT_KEY,HttpParam.OFFICE_CODE,
                orgPhone,newPhone,idCard,smsCode)
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
                .subscribe(new Action1<BaseResponse<GeneralResponse>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse> response) {
                        showTips(response.data.reason);
                        if (response.bool){
                            DataUtils.Logout();
                            overridePendingTransition(0,R.anim.login_enter_anims);
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
        String orgPhone = binding.orgPhone.getText().toString();
        String phone = binding.newPhone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            showTips("请输入新手机号");
            return;
        }else if (phone.length()!=11){
            showTips("您输入的新手机号不正确，请重新输入");
            return;
        }

        Subscription subscription = UserImpAPI.getSMSCode(
                orgPhone,HttpParam.SMS_CODE_CHANGEACCOUNT_APPKEY,HttpParam.OFFICE_CODE,null,phone)
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


    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
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
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        if (dataBeanBaseResponse.bool){
                            RESET_NUMBER = dataBeanBaseResponse.data.getMerchantInfo().resetPhoneNumber;
                            binding.setTips(getString(R.string.change_account_1,RESET_NUMBER));
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
}
