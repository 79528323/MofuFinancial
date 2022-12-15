package cn.mofufin.morf.ui.loan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanPersonalBinding;
import cn.mofufin.morf.databinding.ActivityLoanSpouseBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.dao.MerSpouseTextBeanDao;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepFourFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepOneFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepThreeFragment;
import cn.mofufin.morf.ui.loan.fragment.PersonalStepTwoFragment;
import cn.mofufin.morf.ui.loan.fragment.SpouseStepOneFragment;
import cn.mofufin.morf.ui.loan.fragment.SpouseStepThreeFragment;
import cn.mofufin.morf.ui.loan.fragment.SpouseStepTwoFragment;
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

public class LoanSpouseActivity extends BaseActivity {
    private ActivityLoanSpouseBinding binding;
    public static Map<String,String> params = new HashMap<>();
    private FragmentManager fragmentManager;
    public static boolean isPrivater = false;

    SpouseStepOneFragment oneFragment;
    SpouseStepTwoFragment twoFragment;
    SpouseStepThreeFragment threeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_spouse);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        params.put("appKey",HttpParam.LOANS_SPOUSETEXT_UPLOAD_KEY);
        params.put("officeCode",HttpParam.OFFICE_CODE);
        params.put("merchantCode",MerchanInfoDB.queryInfo().merchantCode);
        params.put("version",Common.LOAN_VERSION);

        SelfTextInfo.MerSpouseTextBean bean = null;
        if (getIntent().getIntExtra(IntentParam.STATUS,0) == 1){
//            initFragment(null);
            new queryBeanAsynTask().execute(this);
        }else {
            bean = getIntent().getParcelableExtra(IntentParam.BEAN);
            initFragment(bean);
        }
    }


    public void initFragment(SelfTextInfo.MerSpouseTextBean info){
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentParam.BEAN,info);

        oneFragment = SpouseStepOneFragment.newInstance();
        oneFragment.setArguments(bundle);
        twoFragment= SpouseStepTwoFragment.newInstance();
        twoFragment.setArguments(bundle);
        threeFragment = SpouseStepThreeFragment.newInstance();
        threeFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.spouse_container,oneFragment,"1");
        transaction.add(R.id.spouse_container,twoFragment,"2");
        transaction.add(R.id.spouse_container,threeFragment,"3");
        transaction.commitAllowingStateLoss();

        rxManager.onRxEvent(RxEvent.SCAN_QR_POSITION).subscribe(new Action1<Object>() {

            @Override
            public void call(Object o) {
                int pos = (int) o;
                if (pos <= 3){
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
                    oneFragment = SpouseStepOneFragment.newInstance();
                    transaction.add(R.id.spouse_container,oneFragment,"1");
                }
                break;

            case 2:
                if (twoFragment!=null){
                    transaction.show(twoFragment);
                }else {
                    twoFragment = SpouseStepTwoFragment.newInstance();
                    transaction.add(R.id.spouse_container,twoFragment,"2");
                }
                break;

            case 3:
                if (threeFragment!=null){
                    transaction.show(threeFragment);
                }else {
                    threeFragment = SpouseStepThreeFragment.newInstance();
                    transaction.add(R.id.spouse_container,threeFragment,"3");
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
    public void onBackPressed() {
        if (binding.getPosition()>1){
            binding.setPosition(binding.getPosition()-1);
            selectFragment(binding.getPosition());
        }else {
            super.onBackPressed();
        }
    }


    public void submit(){
        Subscription subscription = LoanImAPI.uploadSpouseText(params.get("appKey"),params.get("officeCode"),params.get("merchantCode"),
                params.get("version"),params.get("nm"),params.get("idNo"),params.get("mtCityCd"),params.get("mtCityCdName"),params.get("mtStateCd"),
                params.get("mtStateCdName"),params.get("dtIssue"),params.get("dtExpiry"),params.get("isLongEffec"),params.get("dtRegistered"),
                params.get("mtGenderCd"),params.get("mtMaritalStsCd"),params.get("mtEduLvlCd"),params.get("mtResidenceStsCd"),params.get("mobileNo"),
                params.get("mtIndvMobileUsageStsCd"),
                params.get("email"),params.get("qq"),params.get("weChat"),params.get("yearIncAmt"),params.get("isFamily"),
                params.get("hasCar"),params.get("plateNo"),params.get("hasCreditCard"),params.get("creditCardLines"))
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
                        Intent intent = new Intent(LoanSpouseActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,2);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        startActivity(intent);
                        if (baseResult.result_Code==0){
                            new MerSpouseTextBeanDao(LoanSpouseActivity.this).deleteBean();
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
            SelfTextInfo.MerSpouseTextBean bean = new SelfTextInfo.MerSpouseTextBean();
            bean.nm = params.get("nm");
            bean.idNo = params.get("idNo");
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
            bean.mobileNo = params.get("mobileNo");

            MerSpouseTextBeanDao dao = new MerSpouseTextBeanDao(this);
            dao.insertBean(bean);
        }
    }


    class queryBeanAsynTask extends AsyncTask<Context,Void, SelfTextInfo.MerSpouseTextBean>{

        @Override
        protected void onPreExecute() {
            showLoading();
        }


        @Override
        protected void onPostExecute(SelfTextInfo.MerSpouseTextBean merSpouseTextBean) {
            initFragment(merSpouseTextBean);
            hiddenLoading();
        }

        @Override
        protected SelfTextInfo.MerSpouseTextBean doInBackground(Context... contexts) {
            MerSpouseTextBeanDao dao = new MerSpouseTextBeanDao(contexts[0]);
            return dao.queryBean();
        }
    }
}
