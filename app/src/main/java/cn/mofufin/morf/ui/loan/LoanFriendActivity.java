package cn.mofufin.morf.ui.loan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanFriendBinding;
import cn.mofufin.morf.databinding.ActivityLoanPersonalBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.dao.MerFriendTextBeanDao;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
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
import cn.mofufin.morf.ui.widget.SinglePicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoanFriendActivity extends BaseActivity {
    private ActivityLoanFriendBinding binding;
    public static Map<String,String> params = new HashMap<>();
    public String sexy;
    private SinglePicker relatePicker , incomePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_friend);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        params.put("appKey",HttpParam.LOANS_FRIENDTEXT_UPLOAD_KEY);
        params.put("officeCode",HttpParam.OFFICE_CODE);
        params.put("merchantCode",MerchanInfoDB.queryInfo().merchantCode);
        params.put("version",Common.LOAN_VERSION);
        SelfTextInfo.MerFriendText bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        setViewHolder(bean);
        binding.setOnClicklistener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sexy = (String) v.getTag();
            params.put("mtGenderCd",sexy);
        }
    };

    public void incomeSelect(View view){
        incomePicker = new SinglePicker(this,SinglePicker.TYPE_INCOME);
        incomePicker.setTitle("收入来源");
        incomePicker.codeSelectListener = new SinglePicker.onSingleCodeSelectListener() {
            @Override
            public void singleSelect(String value, String code) {
                binding.income.setText(value);
                params.put("mtIncSourceCdName",value);
                params.put("mtIncSourceCd",code);
            }
        };
        incomePicker.show();
    }

    public void relateSelect(View view){
        relatePicker = new SinglePicker(this,SinglePicker.TYPE_RELATION);
        relatePicker.setTitle("关系");
        relatePicker.codeSelectListener = new SinglePicker.onSingleCodeSelectListener() {
            @Override
            public void singleSelect(String value, String code) {
                binding.relationship.setText(value);
                params.put("mtCifRelCdName",value);
                params.put("mtCifRelCd",code);
            }
        };
        relatePicker.show();
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


    public void submit(View view){
        if (TextUtils.isEmpty(binding.relationship.getText().toString())){
            showTips("请选择关联人关系");
            return;
        }else if (TextUtils.isEmpty(binding.relateName.getText().toString())){
            showTips("请输入关联人姓名");
            return;
        }else if (TextUtils.isEmpty(binding.idCard.getText().toString())){
            showTips("请输入身份证号码");
            return;
        }else if (TextUtils.isEmpty(sexy)){
            showTips("请选择性别");
            return;
        }else if (TextUtils.isEmpty(binding.phone.getText().toString())){
            showTips("请输入关联人号码");
            return;
        }else if (TextUtils.isEmpty(binding.amt.getText().toString())){
            showTips("请输入近一年收入");
            return;
        }

        params.put("nm",binding.relateName.getText().toString());
        params.put("idNo",binding.idCard.getText().toString());
        params.put("mobileNo",binding.phone.getText().toString());
        params.put("yearIncAmt",binding.amt.getText().toString());

        Subscription subscription = LoanImAPI.uploadFriend(params.get("appKey"),params.get("officeCode"),params.get("merchantCode"),
                params.get("version"),params.get("mtCifRelCd"),params.get("mtCifRelCdName"),params.get("nm"),params.get("idNo"),
                params.get("mtGenderCd"),params.get("mobileNo"),params.get("mtIncSourceCd"),params.get("mtIncSourceCdName"),params.get("yearIncAmt"))
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
                        Intent intent = new Intent(LoanFriendActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,3);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        startActivity(intent);
                        if (baseResult.result_Code==0){
                            new MerFriendTextBeanDao(LoanFriendActivity.this).deleteBean();
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

    public void setViewHolder(SelfTextInfo.MerFriendText merFriendText){
        if (merFriendText!=null){
            binding.amt.setText(merFriendText.yearIncAmt);
            binding.idCard.setText(merFriendText.idNo);
            binding.phone.setText(merFriendText.mobileNo);
            binding.relateName.setText(merFriendText.nm);
            binding.relationship.setText(merFriendText.mtCifRelCdName);
            params.put("mtCifRelCd",merFriendText.mtCifRelCd);
            params.put("mtCifRelCdName",merFriendText.mtCifRelCdName);
            sexy = merFriendText.mtGenderCd;
            if (!TextUtils.isEmpty(sexy)){
                binding.sexyGroup.check(sexy.equals("M")?R.id.radio1:R.id.radio2);
                params.put("mtGenderCd",sexy);
            }
            binding.income.setText(merFriendText.mtIncSourceCdName);
            params.put("mtIncSourceCd",merFriendText.mtIncSourceCd);
            params.put("mtIncSourceCdName",merFriendText.mtIncSourceCdName);
        }
    }

    public void setControllSave(){
        int type = getIntent().getIntExtra(IntentParam.TYPE,1);
        if (type == 1){
            SelfTextInfo.MerFriendText bean = new SelfTextInfo.MerFriendText();

            bean.mtCifRelCd = params.get("mtCifRelCd");
            bean.mtCifRelCdName = params.get("mtCifRelCdName");
            bean.mtGenderCd = sexy;
            bean.mtIncSourceCd = params.get("mtIncSourceCd");
            bean.mtIncSourceCdName=params.get("mtIncSourceCdName");

            bean.idNo = binding.idCard.getText().toString();
            bean.nm = binding.relateName.getText().toString();
            bean.mobileNo = binding.phone.getText().toString();
            bean.yearIncAmt = binding.amt.getText().toString();

            MerFriendTextBeanDao dao = new MerFriendTextBeanDao(this);
            dao.insertBean(bean);
        }
    }
}
