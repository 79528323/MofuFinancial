package cn.mofufin.morf.ui;

import android.Manifest;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityMposChanceBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Audit;
import cn.mofufin.morf.ui.entity.MposRatio;
import cn.mofufin.morf.ui.mine.DepositActivity;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.PaySDKListener;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * MPOS -选择界面
 */
public class MposChanceActivity extends BaseActivity{
    private ActivityMposChanceBinding binding;
    private int use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mpos_chance);
        binding.setHandlers(this);
        conditSDK();
        getMposRate();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //跳转
    public void jumpIntent(View view){
        final String tag= (String) view.getTag();

        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
                            PaySDKListener.InitializationSDK();
                            Intent intent = new Intent(MposChanceActivity.this,MposActivity.class);
                            intent.putExtra(IntentParam.PAY_ENNUM_MODE,tag);
                            intent.putExtra(IntentParam.TYPE,use);
                            startActivity(intent);
                        }else {
                            DataUtils.setPermissionDailog(MposChanceActivity.this,getString(R.string.permissions_location_1));
                        }
                    }
                });

    }


    @Override
    public void submit(View view) {
        super.submit(view);
        startActivity(new Intent(this, DepositActivity.class));
    }

    public void conditSDK(){
        Subscription subscription = UserImpAPI.iosAudit(HttpParam.OFFICE_CODE,HttpParam.QUERY_MPOS_AUDIT_KEY,
                "0", Common.LOAN_VERSION,null,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Audit>() {
                    @Override
                    public void call(Audit audit) {
                        if (audit.result_Code==0){
                            use = audit.parameter.appUseMposSdk;
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


    public void getMposRate(){
        Subscription subscription = Observable.zip(UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION, "0")
                , UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                        HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY,
                        MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION, "1"),
                new Func2<MposRatio, MposRatio, List<Object>>() {
                    @Override
                    public List<Object> call(MposRatio mposRatio, MposRatio mposRatio2) {
                        List<Object> objectList = new ArrayList<>();
                        objectList.add(mposRatio);
                        objectList.add(mposRatio2);
                        return objectList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Object>>() {
                    @Override
                    public void call(List<Object> objects) {
                        MposRatio mpos = (MposRatio) objects.get(0);
                        binding.setMpos(getString(R.string.mpos_chance_4,DataUtils.converOverPercent(mpos.minRatio))+"+"+DataUtils.numFormat(mpos.fee));

                        MposRatio pay = (MposRatio) objects.get(1);
                        binding.setPay(getString(R.string.mpos_chance_2,DataUtils.converOverPercent(pay.minRatio))+"+"+DataUtils.numFormat(pay.fee));
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
