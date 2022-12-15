package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.ModifyLogPassWordActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;
import cn.mofufin.morf.databinding.ActivityLoginBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity{
//    public static final String APPKEY ="1C607B535B4A416B661490AB5EC786A1";
    private ActivityLoginBinding binding;
//    public static final String ACCOUNTNAME="accountname";
    public static final String ACCOUNTNAME= BaseApplication.context.getPackageName()+"_mofu_account";
//    public static final String PASSWORD = "password";
    public static final String PASSWORD = BaseApplication.context.getPackageName()+"_mofu_password";
//    public static final String REMEMBER_PASSWORD="remember_password";
    public static final String REMEMBER_PASSWORD=BaseApplication.context.getPackageName()+"_mofu_remember_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);

        boolean isSavePwd = SharedPreferencesUtils.getBoolean(this,REMEMBER_PASSWORD,true);
        binding.setIsSavePwd(isSavePwd);

        String loginnum = SharedPreferencesUtils.getString(this,ACCOUNTNAME,"");
        binding.loginNumber.setText(loginnum);
        String password = SharedPreferencesUtils.getString(this,PASSWORD,"");
        binding.loginPwd.setText(password);
    }

    @Override
    protected boolean enableSliding() {
        return false;
    }


    public void savePwd(View view){
        binding.setIsSavePwd(!binding.getIsSavePwd());
    }

    public void logins(View view){
        String number = binding.loginNumber.getText().toString();
        String password = binding.loginPwd.getText().toString();

        if (TextUtils.isEmpty(number)){
            DataUtils.toastTips(this,"请输入手机号");
            return;
        }else if (TextUtils.isEmpty(password)){
            DataUtils.toastTips(this,"请输入密码");
            return;
        }else if (number.length()< 11){
            DataUtils.toastTips(this,"请输入正确的手机号");
            return;
        }

        String currentFacility=DataUtils.getPhoneInfo();

        Subscription subscription = UserImpAPI.Logins(HttpParam.LOGIN_APPKEY, HttpParam.OFFICE_CODE,number,password,currentFacility)
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
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        showTips(dataBeanBaseResponse.data.getReason());
                        if (dataBeanBaseResponse.bool){
                            SharedPreferencesUtils.setString(LoginActivity.this,ACCOUNTNAME,binding.loginNumber.getText().toString());
                            if (binding.getIsSavePwd()){
                                SharedPreferencesUtils.setString(LoginActivity.this,PASSWORD,binding.loginPwd.getText().toString());
                            }else
                                SharedPreferencesUtils.setString(LoginActivity.this,PASSWORD,"");


                            User.DataBean dataBean = dataBeanBaseResponse.data;
                            MerchanInfoDB.saveMerchanInfo(dataBean.getMerchantInfo());
//                            CardInfoDB.SaveCardInfo(dataBean.getCardInfo());

//                            User.DataBean.MerchantInfoBean beans = MerchanInfoDB.queryInfo();
//                            Logger.e("MerchantPhone============"+beans.getMerchantPhone());

                            startActivity(new Intent(
                                    LoginActivity.this,HomeActivity.class).putExtra(
                                            IntentParam.NOTIFYS,getIntent().getParcelableExtra(IntentParam.NOTIFYS)));
                            overridePendingTransition(R.anim.anim_slide_in,R.anim.login_exit_anims);
                            finish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hiddenLoading();
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }





    /**
     * 注册
     * @param view
     */
    public void register(View view){
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void forgetPwd(View view){
        startActivity(new Intent(this,ModifyLogPassWordActivity.class));
    }

    public void isSelectorPassWord(View view){
        binding.setIsSavePwd(!binding.getIsSavePwd());
        SharedPreferencesUtils.setBoolean(this,REMEMBER_PASSWORD,binding.getIsSavePwd());
    }
}



