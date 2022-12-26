package cn.mofufin.morf.ui;

import android.Manifest;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.tbruyelle.rxpermissions.RxPermissions;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.CardInfoDB;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.Notifys;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.framework.update.UpdateBuilder;
import cn.mofufin.morf.ui.framework.update.UpdateVersionUtil;
import cn.mofufin.morf.ui.framework.update.custom.CustomApkFileCreator;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.LauncherUtil;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;
import cn.mofufin.morf.ui.widget.TipsDialog;
import cn.mofufin.morf.databinding.ActivityLaucherBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LaucherActivity extends BaseActivity {

    private ActivityLaucherBinding binding;
    private RxPermissions rxPermissions;
    private Notifys notifys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_laucher);
        setStatusBar(ContextCompat.getColor(this,R.color.app_blue));
        binding.setHandlers(this);
        if (BuildConfig.FLAVOR.equals(Common.TYPE_DEVELOP)) {
            binding.beta.setVisibility(View.VISIBLE);
        }else {
            binding.beta.setVisibility(View.GONE);
        }
        rxPermissions = new RxPermissions(this);

        CardInfoDB.clearAllCard();
//        queryVersion();
        JumpNext();
        notifys = getIntent().getParcelableExtra(IntentParam.NOTIFYS);
    }

    @Override
    protected boolean enableSliding() {
        return false;
    }

    /**
     * 创建快捷方式
     */
    private void initShortCut(){
        boolean isExits = LauncherUtil.isShortCutExist(this,getString(R.string.app_name));
        if (!isExits){
            LauncherUtil.installShortCut(
                    this,getString(R.string.app_name),R.mipmap.ic_launcher,new Intent());
        }
    }


    /**
     * 检查新版本
     */
    private void checkNewVersion(boolean permisson) {
        UpdateBuilder builder = UpdateBuilder.create();
        if (permisson) {
            builder.fileCreator(new CustomApkFileCreator());
        }
        builder.check();
    }

    public void JumpNext(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        },500);
    }

    public void queryVersion(){
        Subscription subscription = UtilsImpAPI.QuerySystemAPK(HttpParam.OFFICE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult result) {
                        Logger.e("result_Code="+result.result_Code);
                        if (result.result_Code == 1){
                            JumpNext();
                        }else {
                            String localVersion = UpdateVersionUtil.getAppCurrVersionName();
                            String serviceVersion = result.vCode;

                            int local = Integer.valueOf(localVersion.replace(".",""));
                            int service = Integer.valueOf(serviceVersion.replace(".",""));
                            if (local < service) {
                                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        .subscribe(new Action1<Boolean>() {
                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean) {
                                                    checkNewVersion(aBoolean);
                                                } else {
                                                    DataUtils.setPermissionDailog(
                                                            LaucherActivity.this,getString(R.string.permissions_location_1));
                                                }
                                            }
                                        });
                            } else {
                                JumpNext();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                        Intent intent = new Intent(LaucherActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overrideTransaction();
                    }
                });
        rxManager.add(subscription);
    }

    public void overrideTransaction(){
        overridePendingTransition(R.anim.pop_enter_anims,R.anim.activity_open);
    }


    public void login(){
        String loginnum = SharedPreferencesUtils.getString(this,LoginActivity.ACCOUNTNAME,"");
        String password = SharedPreferencesUtils.getString(this,LoginActivity.PASSWORD,"");
        String currentFacility=DataUtils.getPhoneInfo();
        Logger.e("PhoneInfo="+currentFacility);
        Subscription subscription = UserImpAPI.Logins(HttpParam.LOGIN_APPKEY, HttpParam.OFFICE_CODE,loginnum,password,currentFacility)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        Intent intent = null;
                        if (dataBeanBaseResponse.bool){
                            User.DataBean dataBean = dataBeanBaseResponse.data;
                            MerchanInfoDB.saveMerchanInfo(dataBean.getMerchantInfo());
//                            CardInfoDB.SaveCardInfo(dataBean.getCardInfo());
                            intent = new Intent(LaucherActivity.this,HomeActivity.class);
                            intent.putExtra(IntentParam.NOTIFYS,notifys);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in,0);
                        }else {
                            intent = new Intent(LaucherActivity.this,LoginActivity.class);
                            intent.putExtra(IntentParam.NOTIFYS,notifys);
                            startActivity(intent);
                            finish();
                            overrideTransaction();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                        startActivity(new Intent(LaucherActivity.this,LoginActivity.class));
                        finish();
                        overrideTransaction();
                    }
                });
        rxManager.add(subscription);
    }
}
