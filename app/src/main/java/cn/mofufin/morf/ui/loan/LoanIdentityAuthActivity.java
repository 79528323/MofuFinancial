package cn.mofufin.morf.ui.loan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanIdentityAuthBinding;
import cn.mofufin.morf.databinding.ActivityModifyLogPassWordBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.dao.MerFriendTextBeanDao;
import cn.mofufin.morf.ui.dao.MerSelfTextBeanDao;
import cn.mofufin.morf.ui.dao.MerSpouseTextBeanDao;
import cn.mofufin.morf.ui.db.DBHelper;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.LoansControlInfo;
import cn.mofufin.morf.ui.entity.SelfImgInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoanIdentityAuthActivity extends BaseActivity {
    private ActivityLoanIdentityAuthBinding binding;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                showLoading();
            }else{
                hiddenLoading();
                Intent intent = (Intent) msg.obj;
                startActivity(intent);
            }
        }
    };

    int blue,red,ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_identity_auth);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        blue =ContextCompat.getColor(LoanIdentityAuthActivity.this,R.color.app_blue);
        red = ContextCompat.getColor(LoanIdentityAuthActivity.this,R.color.loan_red);
        ok  = ContextCompat.getColor(LoanIdentityAuthActivity.this,R.color.ok);

        rxManager.onRxEvent(RxEvent.REFRESH_LOAN_IDENTITY_STATUS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        getStatus();
                    }
                });

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_LOAN_IDENTITY_STATUS);
    }

//    selfTextState 本人文本资料提交状态: 0:已提交,1:未提交,2:未通过
//    selfImgState 本人图片资料提交状态: 0:已提交,1:未提交,2:未通过
//    selfPrivateImgState 本人私营业主资料提交状态: 0:已提交,1:未提交,2:未通过,3:本人不是私营业主无需提交
//    spouseTextState 配偶文本资料提交状态: 0:已提交,1:未提交,2:未通过,3:本人无配偶信息无需提交
//    spouseImgState 配偶图片资料提交状态：0:已提交,1:未提交,2:未通过,3:本人无配偶信息无需提交
//    friendTextState  亲朋文本资料提交状态: 0:已提交,1:未提交,2:未通过
//    businessImgState 业务图片资料提交状态: 0:已提交,1:未提交,2:未通过

    public void selfText(View view){//TODO 个人资料
        if (DataUtils.isFastClick())
            return;

        if (binding.getSelfTextState() == 1){
            handler.sendEmptyMessage(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    MerSelfTextBeanDao dao = new MerSelfTextBeanDao(LoanIdentityAuthActivity.this);
                    Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanPersonalActivity.class);
                    intent.putExtra(IntentParam.BEAN,dao.queryBean());
                    intent.putExtra(IntentParam.TYPE,binding.getSelfTextState());
                    handler.sendMessage(handler.obtainMessage(1,intent));
                    long end = System.currentTimeMillis() - start;
                    Logger.e("用时："+end+"秒");
                }
            }).start();
        }else
            getPersonText();
    }

    public void selfImg(View view){
        getPersonImg();
    }//TODO 个人图片

    public void spouseText(View view){//TODO 配偶资料
        if (DataUtils.isFastClick())
            return;

        if (binding.getSpouseTextState() == 1){
            handler.sendEmptyMessage(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MerSpouseTextBeanDao dao = new MerSpouseTextBeanDao(LoanIdentityAuthActivity.this);
                    Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanSpouseActivity.class);
                    intent.putExtra(IntentParam.BEAN,dao.queryBean());
                    intent.putExtra(IntentParam.TYPE,binding.getSpouseTextState());
                    handler.sendMessage(handler.obtainMessage(1,intent));
                }
            }).start();
        }else
            getSpouseText(binding.getSpouseTextState());
    }

    public void friendText(View view){//TODO 朋友资料
        if (DataUtils.isFastClick())
            return;

        if (binding.getFriendTextState() == 1){
            handler.sendEmptyMessage(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MerFriendTextBeanDao dao = new MerFriendTextBeanDao(LoanIdentityAuthActivity.this);
                    Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanFriendActivity.class);
                    intent.putExtra(IntentParam.BEAN,dao.queryBean());
                    intent.putExtra(IntentParam.TYPE,binding.getFriendTextState());
//                    startActivity(intent);
                    handler.sendMessage(handler.obtainMessage(1,intent));
                }
            }).start();
        }else
            getFriendText();
    }

    public void spouseImg(View view){
        getSpouseImg();
    }//TODO 配偶图片

    public void privaterImg(View view){
        getPrivaterImg();
    }//TODO 私营图片

    public void businessImg(View view){
        getBusinessImg();
    }//TODO 业务图片

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBHelper dbHelper = DBHelper.getHelper(this);
        dbHelper.close();
    }

    public void getStatus(){
        Subscription subscription = LoanImAPI.querySelfSubmitState(HttpParam.LOANS_SELF_STATUS_KEY,HttpParam.OFFICE_CODE, MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
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
                .subscribe(new Action1<LoansControlInfo>() {
                    @Override
                    public void call(LoansControlInfo controlInfo) {
                        if (controlInfo.result_Code == 0) {
                            LoansControlInfo.ControlInfo info = controlInfo.controlInfo;
                            int selfTextState = info.selfTextState;
                            binding.setSelfTextState(selfTextState);
                            binding.selftextStatus.setStrokeColor(selfTextState==0?blue:selfTextState==1?ok:red);

                            int businessImgState = info.businessImgState;
                            binding.setBusinessImgState(businessImgState);
                            binding.businessimgStatus.setStrokeColor(businessImgState==0?blue:businessImgState==1?ok:red);

                            int friendTextState = info.friendTextState;
                            binding.setFriendTextState(friendTextState);
                            binding.friendtextStatus.setStrokeColor(friendTextState==0?blue:friendTextState==1?ok:red);

                            int selfImgState = info.selfImgState;
                            binding.setSelfImgState(selfImgState);
                            binding.selfimgStatus.setStrokeColor(selfImgState==0?blue:selfImgState==1?ok:red);

                            int selfPrivateImgState = info.selfPrivateImgState;
                            binding.setSelfPrivateImgState(selfPrivateImgState);
                            binding.privateimgStatus.setStrokeColor(selfPrivateImgState==0?blue:selfPrivateImgState==1?ok:red);

                            int spouseTextState =info.spouseTextState;
                            binding.setSpouseTextState(spouseTextState);
                            binding.spousetextStatus.setStrokeColor(spouseTextState==0?blue:spouseTextState==1?ok:red);

                            int spouseImgState = info.spouseImgState;
                            binding.setSpouseImgState(spouseImgState);
                            binding.spouseimgStatus.setStrokeColor(spouseImgState==0?blue:spouseImgState==1?ok:red);
                            binding.executePendingBindings();
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

    public void getPersonText(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanPersonalActivity.class);
        Subscription subscription = LoanImAPI.queryMerSelfText(HttpParam.LOANS_SELFTEXT_QUERY_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
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
                .subscribe(new Action1<SelfTextInfo>() {
                    @Override
                    public void call(SelfTextInfo selfTextInfo) {
                        if (selfTextInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfTextInfo.merSelfText);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void getPersonImg(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,PersonalUploadPhotoActivity.class);
        Subscription subscription = LoanImAPI.queryMerSelfImg(HttpParam.LOANS_QUERY_SELFIMG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfImgInfo>() {
                    @Override
                    public void call(SelfImgInfo selfImgInfo) {
                        if (selfImgInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfImgInfo.merSelfImg);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void getSpouseText(int state){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanSpouseActivity.class);
        intent.putExtra(IntentParam.STATUS,state);
        Subscription subscription = LoanImAPI.queryMerSpouseText(HttpParam.LOANS_QUERY_SPOUSE_TEXT_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfTextInfo>() {
                    @Override
                    public void call(SelfTextInfo selfTextInfo) {
                        if (selfTextInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfTextInfo.merSpouseText);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void getFriendText(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,LoanFriendActivity.class);
        Subscription subscription = LoanImAPI.queryMerFriendText(HttpParam.LOANS_QUERY_FRIEND_TEXT_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfTextInfo>() {
                    @Override
                    public void call(SelfTextInfo selfTextInfo) {
                        if (selfTextInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfTextInfo.merFriendText);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void getSpouseImg(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,SpouseUploadPhotoActivity.class);
        Subscription subscription = LoanImAPI.queryMerSpouseImg(HttpParam.LOANS_QUERY_SPOUSEIMG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfImgInfo>() {
                    @Override
                    public void call(SelfImgInfo selfImgInfo) {
                        if (selfImgInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfImgInfo.merSpouseImg);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void getPrivaterImg(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,PrivaterUploadPhotoActivity.class);
        Subscription subscription = LoanImAPI.queryMerSelfPrivateImg(HttpParam.LOANS_QUERY_PRIVATERIMG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfImgInfo>() {
                    @Override
                    public void call(SelfImgInfo selfImgInfo) {
                        if (selfImgInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfImgInfo.merSelfPrivateImg);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }



    public void getBusinessImg(){
        final Intent intent = new Intent(LoanIdentityAuthActivity.this,BusinessUploadPhotoActivity.class);
        Subscription subscription = LoanImAPI.queryBusinessImg(HttpParam.LOANS_QUERY_BUSINESSIMG_KEY,HttpParam.OFFICE_CODE,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SelfImgInfo>() {
                    @Override
                    public void call(SelfImgInfo selfImgInfo) {
                        if (selfImgInfo.result_Code == 0){
                            intent.putExtra(IntentParam.BEAN,selfImgInfo.merBusinessImg);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        startActivity(intent);
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }
}
