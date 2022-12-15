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
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.ActivityModifyTranspasswordBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ModifyTransPasswordActivity extends BaseActivity {
    private ActivityModifyTranspasswordBinding binding;
    private Timer timer;
    private int count = 60;
    private String phone;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int second = (int)msg.obj;
            if (second>0){
                binding.setSecond(getString(R.string.modifytrans2,String.valueOf(second)));
            }else {
                binding.setSecond("");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_modify_transpassword);
        binding.setHandlers(this);
        binding.setIsNext(false);
        initView();
    }

    public void initView(){
        timer = new Timer();
        phone = MerchanInfoDB.queryInfo().merchantPhone;
        binding.setPhone(getString(R.string.modifytrans1,phone));
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
    }

    /**
     * 获取验证码
     */
    public void getSmsCode(){
        String phone = MerchanInfoDB.queryInfo().merchantPhone;
        changeSmsType();
        Subscription subscription = UserImpAPI.getSMSCode(
                phone, HttpParam.SMS_TRANS_MODIFY_APPKEY,HttpParam.OFFICE_CODE,null,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse.DataBean> dataBeanBaseResponse) {
                        showTips(dataBeanBaseResponse.data.reason);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable ,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void next(View view){
        String smscode = binding.modfyTransSms.getText().toString();

        if (TextUtils.isEmpty(smscode)){
            showTips("请输入验证码");
            return;
        }

        if (!binding.getIsNext()){
            binding.setIsNext(true);
        }else {
            String newCode = binding.modfyTransNewCode.getText().toString();
            String repeatCode = binding.modfyTransRepeat.getText().toString();
            String idname = binding.idname.getText().toString();
            String idnum = binding.idnum.getText().toString();
            if (TextUtils.isEmpty(idname)){
                showTips("请填写真实姓名");
                return;
            }else if (TextUtils.isEmpty(idnum)){
                showTips("请填写身份证");
                return;
            }else if (TextUtils.isEmpty(newCode)){
                showTips("请填写密码");
                return;
            }else if (TextUtils.isEmpty(repeatCode)){
                showTips("请填写密码");
                return;
            }else if (!TextUtils.equals(newCode ,repeatCode)){
                showTips("密码不一致");
                return;
            }

            Subscription subscription = UserImpAPI.resetTransPassword(
                    HttpParam.MODIFY_TRANS_PASSWROD_APPKEY,HttpParam.OFFICE_CODE,
                    phone,smscode
                    ,newCode,repeatCode,idname,idnum)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            showLoading();
                        }
                    }).subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                        @Override
                        public void call(BaseResponse<GeneralResponse.DataBean> dataBeanBaseResponse) {
                            showTips(dataBeanBaseResponse.data.reason);
                            if (dataBeanBaseResponse.bool){
                                finish();
                            }
                            hiddenLoading();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            onError(throwable,true);
                            hiddenLoading();
                        }
                    });
            rxManager.add(subscription);
        }
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

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
