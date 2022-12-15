package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.j256.ormlite.stmt.query.In;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanPaymentDetailsBinding;
import cn.mofufin.morf.databinding.ActivityProductPaymentDetailsBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.LoanProduct;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.mine.BalanceTransferStatusActivity;
import cn.mofufin.morf.ui.mine.RechargeToBalanceActivity;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.ContactUtil;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.FlowRadioGroup;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 产品申贷
 */
public class LoanPayMentDetailsActivity extends BaseActivity {
    private ActivityLoanPaymentDetailsBinding binding;
    private int paddings;
    private int loansInterestWay=-1,loansPeriod=0;

    private String json="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_payment_details);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        binding.setIsAgree(true);
        paddings = (int) getResources().getDimension(R.dimen.default_padding);
        LoanProduct.ProductListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setDetails(bean);
        addRadio(bean.loansPeriod);
        setInterestsCalculationRadioButtion(bean.loansInterestWay);
        rxManager.onRxEvent(RxEvent.PRODUCT_PAYMENT_FINISH)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //根据贷款月数动态添加选项
    public void addRadio(String str){
        if (!TextUtils.isEmpty(str)){
            String[] buffer;
            if (str.contains("-")){//TODO 两个或以上分期
                buffer = str.split("-");
            }else {//TODO 一个以内分期
                buffer=new String[]{str};
            }

            for (String s:buffer){
                creatRatioButton(s);
            }
        }
    }

    //生成radioButton
    @SuppressLint("SetTextI18n")
    public void creatRatioButton(String time){
        RadioButton button=getRadioButton();
        button.setTag(Integer.valueOf(time));
        String str = /*(Integer.valueOf(time)<10?"  ":"")+*/time+"个月";
        button.setText(str);
        binding.productGroup.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loansPeriod = (int) v.getTag();
            }
        });
    }

    //计息方式选项
    public void setInterestsCalculationRadioButtion(int type){
        if (type < 2){
            RadioButton button = getRadioButton();
            if (type < 1)
                button.setText("等额本息");
            else
                button.setText("先息后本");

            button.setTag(type);
            binding.interestCalculation.addView(button);
        }else {
            for (int i=0; i<type; i++){
                RadioButton button = getRadioButton();
                button.setTag(i);
                if (i==0){
                    button.setText("等额本息");
                }else {
                    button.setText("先息后本");
                }
                binding.interestCalculation.addView(button);
            }
        }
    }

    public RadioButton getRadioButton(){
        RadioButton button=new RadioButton(this);
        button.setButtonDrawable(null);
        Drawable drawable=ContextCompat.getDrawable(this,R.drawable.loan_check);
        button.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        button.setCompoundDrawablePadding(paddings);
        button.setTextColor(ContextCompat.getColor(this,R.color.dark_gray));
        button.setPadding(0,0,paddings,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loansInterestWay = (int) v.getTag();
            }
        });
        return button;
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    //协议
    public void financialAgreement(View view){
        Intent intent = new Intent(this,LoanAgreeMentActivity.class);
        startActivity(intent);
    }

    public void agree(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    public void submitLoans(View view){
        if (TextUtils.isEmpty(binding.getAmount())){
            showTips("请输入贷款金额");
            return;
        }else if (Double.valueOf(binding.getAmount()) > binding.getDetails().loansMaxMoney){
            showTips("最高只能贷"+binding.getDetails().loansMaxMoney/10000+"万");
            return;
        }else if (loansInterestWay <0){
            showTips("请选择计息方式");
            return;
        }else if (loansPeriod <=0){
            showTips("请选择贷款周期");
            return;
        }

        new RxPermissions(this).request(Manifest.permission.READ_CONTACTS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
                            showLoading();
                            try {
                                json = ContactUtil.getInstance(LoanPayMentDetailsActivity.this).getContactInfo();
                                Logger.e(json);
                                submits();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            submits();
                        }
                    }
                });

    }

    public void submits(){
        Subscription subscription = LoanImAPI.sumitApplyLoans(HttpParam.LOANS_SUBMIT_APPLY_LOAN_KEY,
                HttpParam.OFFICE_CODE,MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION,
                String.valueOf(binding.getDetails().lpCode),String.valueOf(binding.getAmount()),
                String.valueOf(loansPeriod),String.valueOf(loansInterestWay),json)
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        Intent intent = new Intent(LoanPayMentDetailsActivity.this,LoanApplyStatusActivity.class);
                        if (baseResult.result_Code==0){
                            intent.putExtra(IntentParam.STATUS,1);
                            intent.putExtra(IntentParam.AMOUNT,binding.getAmount());
                            intent.putExtra(IntentParam.RATIO,binding.getDetails().loansRate);
                            intent.putExtra(IntentParam.PERIOD,loansPeriod);
                            intent.putExtra(IntentParam.BANKCARD_NO,baseResult.debitCardCode);
                        }else{
                            intent.putExtra(IntentParam.STATUS,0);
                            intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        }
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void showPlan(View view){
        if (TextUtils.isEmpty(binding.getAmount())){
            showTips("请输入贷款金额");
            return;
        }else if (Double.valueOf(binding.getAmount()) > binding.getDetails().loansMaxMoney){
            showTips("最高只能贷"+binding.getDetails().loansMaxMoney/10000+"万");
            return;
        }else if (loansInterestWay <0){
            showTips("请选择计息方式");
            return;
        }else if (loansPeriod <=0){
            showTips("请选择贷款周期");
            return;
        }

        Intent intent = new Intent(this,LoanRepayShowPlanActivity.class);
        intent.putExtra(IntentParam.AMOUNT,Integer.parseInt(binding.getAmount()));
        intent.putExtra(IntentParam.RATIO,binding.getDetails().loansRate);
        intent.putExtra(IntentParam.WAY,loansInterestWay);
        intent.putExtra(IntentParam.PERIOD,loansPeriod);
        startActivity(intent);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.setAmount(s.toString());
    }

}
