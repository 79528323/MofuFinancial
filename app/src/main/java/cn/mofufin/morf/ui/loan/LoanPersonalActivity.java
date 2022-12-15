package cn.mofufin.morf.ui.loan;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanIdentityAuthBinding;
import cn.mofufin.morf.databinding.ActivityLoanPersonalBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.dao.MerSelfTextBeanDao;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.LoansControlInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepFourFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepOneFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepThreeFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepTwoFragment;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoanPersonalActivity extends BaseActivity {
    private ActivityLoanPersonalBinding binding;
    public static Map<String,String> params = new HashMap<>();
    private FragmentManager fragmentManager;
//    public static boolean isPrivater = false;

    PersonalStepOneFragment oneFragment;
    PersonalStepTwoFragment twoFragment;
    PersonalStepThreeFragment threeFragment;
    PersonalStepFourFragment fourFragment;

    private Bundle bundle =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_personal);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        params.put("appKey",HttpParam.LOANS_SELFTEXT_UPLOAD_KEY);
        params.put("officeCode",HttpParam.OFFICE_CODE);
        params.put("merchantCode",MerchanInfoDB.queryInfo().merchantCode);
        params.put("version",Common.LOAN_VERSION);
        SelfTextInfo.MerSelfTextBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        initFragment(bean);
    }


    public void initFragment(SelfTextInfo.MerSelfTextBean info){
        bundle = new Bundle();
        bundle.putParcelable(IntentParam.BEAN,info);

        oneFragment = PersonalStepOneFragment.newInstance();
        oneFragment.setArguments(bundle);
//        twoFragment= PersonalStepTwoFragment.newInstance();
//        twoFragment.setArguments(bundle);
//        threeFragment = PersonalStepThreeFragment.newInstance();
//        threeFragment.setArguments(bundle);
//        fourFragment = PersonalStepFourFragment.newInstance();
//        fourFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.personal_container,oneFragment,"1");
//        transaction.add(R.id.personal_container,twoFragment,"2");
//        transaction.add(R.id.personal_container,threeFragment,"3");
//        transaction.add(R.id.personal_container,fourFragment,"4");
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

        binding.setPosition(1);
        selectFragment(binding.getPosition());
    }

    public void hideFragmentAll(FragmentTransaction ft){
        if (oneFragment!=null)
            ft.hide(oneFragment);
        if (twoFragment!=null)
            ft.hide(twoFragment);
        if (threeFragment!=null)
            ft.hide(threeFragment);
        if (fourFragment!=null)
            ft.hide(fourFragment);
    }

    public void selectFragment(int posititon){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragmentAll(transaction);
        switch (posititon){
            case 1:
                if (oneFragment!=null){
                    transaction.show(oneFragment);
                }else {
                    oneFragment = PersonalStepOneFragment.newInstance();
                    transaction.add(R.id.personal_container,oneFragment,"1");
                }
                break;

            case 2:
                if (twoFragment!=null){
                    transaction.show(twoFragment);
                }else {
                    twoFragment = PersonalStepTwoFragment.newInstance();
                    twoFragment.setArguments(bundle);
                    transaction.add(R.id.personal_container,twoFragment,"2");
                }
                break;

            case 3:
                if (threeFragment!=null){
                    transaction.show(threeFragment);
                }else {
                    threeFragment = PersonalStepThreeFragment.newInstance();
                    threeFragment.setArguments(bundle);
                    transaction.add(R.id.personal_container,threeFragment,"3");
                }
                break;

            case 4:
                if (fourFragment!=null){
                    transaction.show(fourFragment);
                }else {
                    fourFragment = PersonalStepFourFragment.newInstance();
                    fourFragment.setArguments(bundle);
                    transaction.add(R.id.personal_container,fourFragment,"4");
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        setControllSave();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
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

    public void submit(){
        Subscription subscription = LoanImAPI.uploadSelfText(params.get("appKey"),params.get("officeCode"),params.get("merchantCode"),
                params.get("version"),params.get("debitCardCode"),params.get("mtCityCd"),params.get("mtCityCdName"),params.get("mtStateCd"),
                params.get("mtStateCdName"),params.get("dtIssue"),params.get("dtExpiry"),params.get("isLongEffec"),params.get("dtRegistered"),
                params.get("mtGenderCd"),params.get("mtMaritalStsCd"),params.get("mtEduLvlCd"),params.get("mtResidenceStsCd"),params.get("mtIndvMobileUsageStsCd"),
                params.get("email"),params.get("qq"),params.get("weChat"),params.get("yearIncAmt"),params.get("isFamily"),
                params.get("hasCar"),params.get("plateNo"),params.get("hasCreditCard"),params.get("creditCardLines"),params.get("isBizEntit"),
                params.get("employerNm"),params.get("mtJobSectorCd"),params.get("mtJobSectorCdName"),params.get("mtPosHeldCd"),params.get("mtPosHeldCdName"),params.get("employerPhone"),
                params.get("isComb"),params.get("bizRegNo"),params.get("bizAddr"),params.get("isBussLongEffec"),params.get("bizRegDtExpiry"),params.get("isLegalRep"),
                params.get("currentTotal"),params.get("yearSaleMarginsRate"),params.get("ratal"))
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        Intent intent = new Intent(LoanPersonalActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,1);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        startActivity(intent);
                        if (baseResult.result_Code==0){
                            new MerSelfTextBeanDao(LoanPersonalActivity.this).deleteBean();
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


    public void setControllSave(){
        int type = getIntent().getIntExtra(IntentParam.TYPE,1);
        if (type == 1){
            SelfTextInfo.MerSelfTextBean bean = new SelfTextInfo.MerSelfTextBean();
            bean.debitCardCode = params.get("debitCardCode");
            bean.mtCityCd = params.get("mtCityCd");
            bean.mtCityCdName = params.get("mtCityCdName");
            bean.mtStateCd = params.get("mtStateCd");
            bean.mtStateCdName = params.get("mtStateCdName");
            bean.dtIssue = params.get("dtIssue");
            bean.dtExpiry = params.get("dtExpiry");
            bean.isLongEffec = params.get("isLongEffec");
            bean.dtRegistered = params.get("dtRegistered");
            bean.mtGenderCd = params.get("mtGenderCd");
            bean.mtMaritalStsCd = params.get("mtMaritalStsCd");
            bean.mtEduLvlCd = params.get("mtEduLvlCd");
            bean.mtResidenceStsCd = params.get("mtResidenceStsCd");
            bean.mtIndvMobileUsageStsCd = params.get("mtIndvMobileUsageStsCd");
            bean.email = params.get("email");
            bean.qq = params.get("qq");
            bean.weChat = params.get("weChat");
            bean.yearIncAmt = params.get("yearIncAmt");
            bean.isFamily = params.get("isFamily");
            bean.hasCar = params.get("hasCar");
            bean.plateNo = params.get("plateNo");
            bean.hasCreditCard = params.get("hasCreditCard");
            bean.creditCardLines = params.get("creditCardLines");
            bean.isBizEntit = params.get("isBizEntit");
            bean.employerNm = params.get("employerNm");
            bean.mtJobSectorCd = params.get("mtJobSectorCd");
            bean.mtJobSectorCdName = params.get("mtJobSectorCdName");
            bean.mtPosHeldCd = params.get("mtPosHeldCd");
            bean.mtPosHeldCdName = params.get("mtPosHeldCdName");
            bean.employerPhone = params.get("employerPhone");
            bean.isComb = params.get("isComb");
            bean.bizRegNo = params.get("bizRegNo");
            bean.bizAddr = params.get("bizAddr");
            bean.isBussLongEffec = params.get("isBussLongEffec");
            bean.bizRegDtExpiry = params.get("bizRegDtExpiry");
            bean.isLegalRep = params.get("isLegalRep");
            if (!TextUtils.isEmpty(bean.isBizEntit) && bean.isBizEntit.equals("Y")){
                String currentTotal = params.get("currentTotal");
                if (!TextUtils.isEmpty(currentTotal))
                    bean.currentTotal = Double.valueOf(currentTotal);

                String yearSaleMarginsRate = params.get("yearSaleMarginsRate");
                if (!TextUtils.isEmpty(yearSaleMarginsRate))
                    bean.yearSaleMarginsRate = Double.valueOf(yearSaleMarginsRate);

                String ratal = params.get("ratal");
                if (!TextUtils.isEmpty(ratal))
                    bean.ratal = Double.valueOf(ratal);
            }

            MerSelfTextBeanDao dao = new MerSelfTextBeanDao(this);
            dao.insertBean(bean);
        }
    }
}
