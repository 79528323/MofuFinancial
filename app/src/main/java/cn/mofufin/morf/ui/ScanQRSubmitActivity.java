package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.BaseInfoFragment;
import cn.mofufin.morf.ui.fragment.ManagementInfoFragment;
import cn.mofufin.morf.ui.fragment.MerchantEntryFragment;
import cn.mofufin.morf.ui.fragment.SettleMentFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityScanQrSubmitBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 扫码进件
 */
public class ScanQRSubmitActivity extends BaseActivity {
    private ActivityScanQrSubmitBinding binding;
    private BaseInfoFragment baseInfoFragment;
    private SettleMentFragment settleMentFragment;
    private ManagementInfoFragment managementInfoFragment;
    private MerchantEntryFragment merchantEntryFragment;
    public static Map<String,String> params = new HashMap<>();
    public static Map<String,File> fileMap = new HashMap<>();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_scan_qr_submit);
        binding.setHandlers(this);

        params.put("appKey", HttpParam.IDENTITY_SCANPAY_APPKEY);
        params.put("officeCode",HttpParam.OFFICE_CODE);
        User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        params.put("merchantCode",bean.merchantCode);

        baseInfoFragment = BaseInfoFragment.newInstance();
        settleMentFragment = SettleMentFragment.newInstance();
        managementInfoFragment = ManagementInfoFragment.newInstance();
        merchantEntryFragment = MerchantEntryFragment.newInstance();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
        transaction.add(R.id.scan_qr_container,baseInfoFragment,"1");
        transaction.add(R.id.scan_qr_container,settleMentFragment,"2");
        transaction.add(R.id.scan_qr_container,managementInfoFragment,"3");
        transaction.add(R.id.scan_qr_container,merchantEntryFragment,"4");
        transaction.commitAllowingStateLoss();

        rxManager.onRxEvent(RxEvent.SCAN_QR_POSITION).subscribe(new Action1<Object>() {

            @Override
            public void call(Object o) {
                int pos = (int) o;
                if (pos <= 4){
                    binding.setPosition(pos);
                    selectFragment(pos);
                }else {
                    submit();
                }
            }
        });

        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,1);
    }

    public void hideFragmentAll(FragmentTransaction ft){
        if (baseInfoFragment!=null)
            ft.hide(baseInfoFragment);
        if (settleMentFragment!=null)
            ft.hide(settleMentFragment);
        if (managementInfoFragment!=null)
            ft.hide(managementInfoFragment);
        if (merchantEntryFragment!=null)
            ft.hide(merchantEntryFragment);
    }

    public void selectFragment(int posititon){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragmentAll(transaction);
        switch (posititon){
            case 1:
                if (baseInfoFragment!=null){
                    transaction.show(baseInfoFragment);
                }else {
                    baseInfoFragment = BaseInfoFragment.newInstance();
                    transaction.add(R.id.scan_qr_container,baseInfoFragment,"1");
                }
                break;

            case 2:
                if (settleMentFragment!=null){
                    transaction.show(settleMentFragment);
                }else {
                    settleMentFragment = SettleMentFragment.newInstance();
                    transaction.add(R.id.scan_qr_container,settleMentFragment,"2");
                }
                break;

            case 3:
                if (managementInfoFragment!=null){
                    transaction.show(managementInfoFragment);
                }else {
                    managementInfoFragment = ManagementInfoFragment.newInstance();
                    transaction.add(R.id.scan_qr_container,managementInfoFragment,"3");
                }
                break;

            case 4:
                if (merchantEntryFragment!=null){
                    transaction.show(merchantEntryFragment);
                }else {
                    merchantEntryFragment = MerchantEntryFragment.newInstance();
                    transaction.add(R.id.scan_qr_container,merchantEntryFragment,"4");
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (binding.getPosition()>1){
            binding.setPosition(binding.getPosition()-1);
            selectFragment(binding.getPosition());
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void submit(){
        Subscription subscription = SubMissionImpAPI.identityScanPay(params,fileMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        showTips(baseResult.result_Msg);
                        if (baseResult.result_Code==0){
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

    public void selectFragment(View view){
        int position = Integer.valueOf((String) view.getTag());
        binding.setPosition(position);
        selectFragment(position);
    }
}
