package cn.mofufin.morf.ui.loan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.databinding.FragmentPersonalStepFourBinding;
import cn.mofufin.morf.databinding.FragmentPersonalStepOneBinding;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.loan.LoanPersonalActivity;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.SinglePicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class PersonalStepFourFragment extends BaseFragment {
    private FragmentPersonalStepFourBinding binding;
    private String mutiple="" ,longer="" ,isLegal="";
    private SinglePicker jobsPicker,postPicker,legalPicker;
    private String jobs="",post="";
    private TimePickerView timePickerView;

    public static PersonalStepFourFragment newInstance(){
        return new PersonalStepFourFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_step_four,container,false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    @Override
    public void onResume() {
        String privater = LoanPersonalActivity.params.get("isBizEntit");
        binding.setIsPrivater(privater.equals("Y"));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(){
        Bundle bundle = getArguments();
        SelfTextInfo.MerSelfTextBean bean = bundle.getParcelable(IntentParam.BEAN);
        setViewHolder(bean);

        binding.setIsLonger(true);
        binding.setLegalClickListener(legalClickListener);
        binding.setLongerClickListener(longerClickListener);
        binding.setMultipleClickListener(multipleClickListener);

        timePickerView = new TimePickerView(getActivity(),TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1900,2100);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                String dates = new SimpleDateFormat("yyyy-MM-dd").format(date);
                binding.regExpiry.setText(dates);
            }
        });
    }

    public void selectDate(View view){
        if (!timePickerView.isShowing())
            timePickerView.show();
        else
            timePickerView.dismiss();
    }

    public void jobSector(View view){
        jobsPicker = new SinglePicker(getActivity(),SinglePicker.TYPE_JOBS);
        jobsPicker.setTitle("职业");
        jobsPicker.codeSelectListener = new SinglePicker.onSingleCodeSelectListener() {
            @Override
            public void singleSelect(String value, String code) {
                binding.jobsector.setText(value);
                jobs = code;
            }
        };
        jobsPicker.show();
    }

    public void postSector(View view){
        postPicker = new SinglePicker(getActivity(),SinglePicker.TYPE_POST);
        postPicker.setTitle("职位");
        postPicker.codeSelectListener = new SinglePicker.onSingleCodeSelectListener() {
            @Override
            public void singleSelect(String value, String code) {
                Logger.e("postSector ="+value);
                binding.postSector.setText(value);
                post = code;
            }
        };
        postPicker.show();
    }

    public void next(View view){
        if (!binding.getIsPrivater()){
            if (TextUtils.isEmpty(binding.workUnit.getText().toString())){
                showTips("请填写工作单位");
                return;
            }else if (TextUtils.isEmpty(binding.jobsector.getText().toString())){
                showTips("请选择职业");
                return;
            }else if (TextUtils.isEmpty(binding.postSector.getText().toString())){
                showTips("请选择职位");
                return;
            }else if (TextUtils.isEmpty(binding.unitPhone.getText().toString())){
                showTips("请填写工作单位电话");
                return;
            }

            LoanPersonalActivity.params.put("employerNm",binding.workUnit.getText().toString());
            LoanPersonalActivity.params.put("mtJobSectorCd",jobs);
            LoanPersonalActivity.params.put("mtJobSectorCdName",binding.jobsector.getText().toString());
            LoanPersonalActivity.params.put("mtPosHeldCd",post);
            LoanPersonalActivity.params.put("mtPosHeldCdName",binding.postSector.getText().toString());
            LoanPersonalActivity.params.put("employerPhone",binding.unitPhone.getText().toString());

        }else {
            if (TextUtils.isEmpty(mutiple)){
                showTips("请选择是否多证合一");
                return;
            }else if (TextUtils.isEmpty(binding.regNo.getText().toString())){
                showTips("请填写营业执照号");
                return;
            }else if (TextUtils.isEmpty(binding.regAddress.getText().toString())){
                showTips("请填写经营地址");
                return;
            }else if (TextUtils.isEmpty(longer)){
                showTips("请选择营业执照是否为长期");
                return;
            }else if (!TextUtils.isEmpty(longer) && !binding.getIsLonger()){
                showTips("请填写营业执照到期日");
                return;
            }else if (TextUtils.isEmpty(isLegal)){
                showTips("请选择是否为法定代表人");
                return;
            }else if (TextUtils.isEmpty(binding.currenTotal.getText().toString())){
                showTips("请填写近一年流水");
                return;
            }else if (TextUtils.isEmpty(binding.saleMargin.getText().toString())){
                showTips("请填写利润");
                return;
            }else if (TextUtils.isEmpty(binding.ratal.getText().toString())){
                showTips("请填写纳税额");
                return;
            }

            LoanPersonalActivity.params.put("bizRegNo",binding.regNo.getText().toString());
            LoanPersonalActivity.params.put("bizAddr",binding.regAddress.getText().toString());
            if (!binding.getIsLonger())
                LoanPersonalActivity.params.put("bizRegDtExpiry",binding.regExpiry.getText().toString());

            String currentTotal = binding.currenTotal.getText().toString();
            if (!TextUtils.isEmpty(currentTotal)){
                double total = Double.valueOf(currentTotal);
                if (total <= 0){
                    showTips("请填写正确的流水");
                    return;
                }
                LoanPersonalActivity.params.put("currentTotal",currentTotal);
            }


            String rate = binding.saleMargin.getText().toString();
            if (!TextUtils.isEmpty(rate)){
                double total = Double.valueOf(rate);
                if (total <= 0){
                    showTips("请填写正确的利润");
                    return;
                }
                LoanPersonalActivity.params.put("yearSaleMarginsRate",rate);
            }

            String ratal = binding.ratal.getText().toString();
            if (!TextUtils.isEmpty(ratal)){
                double total = Double.valueOf(ratal);
                if (total <= 0){
                    showTips("请填写正确的纳税额");
                    return;
                }
                LoanPersonalActivity.params.put("ratal",ratal);
            }
        }

        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,5);
    }

    View.OnClickListener multipleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mutiple = (String) v.getTag();
            LoanPersonalActivity.params.put("isComb",mutiple);
        }
    };


    View.OnClickListener longerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            longer = (String) v.getTag();
            binding.setIsLonger(longer.equals("Y")?true:false);
            LoanPersonalActivity.params.put("isBussLongEffec",longer);
        }
    };

    View.OnClickListener legalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isLegal = (String) v.getTag();
            LoanPersonalActivity.params.put("isLegalRep",isLegal);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 隐藏时调用
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        String privater = LoanPersonalActivity.params.get("isBizEntit");
        binding.setIsPrivater(privater.equals("Y"));
        binding.executePendingBindings();
    }


    //数据回读
    public void setViewHolder(SelfTextInfo.MerSelfTextBean bean){
        if (bean!=null && !TextUtils.isEmpty(bean.isBizEntit)){
            if (bean.isBizEntit.equals("Y")){
                mutiple = bean.isComb;
                if (!TextUtils.isEmpty(mutiple)){
                    binding.mutipleGroup.check(mutiple.equals("Y")?R.id.mutiple_radio1:R.id.mutiple_radio2);
                    LoanPersonalActivity.params.put("isComb",mutiple);
                }

                longer = bean.isBussLongEffec;
                if (!TextUtils.isEmpty(longer)){
                    binding.setIsLonger(longer.equals("Y")?true:false);
                    binding.islongGroup.check(longer.equals("Y")?R.id.islong_radio1:R.id.islong_radio2);
                    LoanPersonalActivity.params.put("isBussLongEffec",longer);
                }


                isLegal = bean.isLegalRep;
                if (!TextUtils.isEmpty(isLegal)){
                    binding.legalGroup.check(isLegal.equals("Y")?R.id.legal_radio1:R.id.legal_radio2);
                    LoanPersonalActivity.params.put("isLegalRep",isLegal);
                }

                binding.regNo.setText(bean.bizRegNo);
                binding.regAddress.setText(bean.bizAddr);
                if (!binding.getIsLonger())
                    binding.regExpiry.setText(bean.bizRegDtExpiry);
                binding.saleMargin.setText(String.valueOf(bean.yearSaleMarginsRate));
                binding.currenTotal.setText(String.valueOf(bean.currentTotal));
                binding.ratal.setText(String.valueOf(bean.ratal));
            }else {
                binding.workUnit.setText(bean.employerNm);
                binding.jobsector.setText(bean.mtJobSectorCdName);
                jobs = bean.mtJobSectorCd;
                binding.postSector.setText(bean.mtPosHeldCdName);
                post = bean.mtPosHeldCd;
                binding.unitPhone.setText(bean.employerPhone);
            }
        }
    }


    @Override
    public void onStop() {
        if (!binding.getIsPrivater()){
            LoanPersonalActivity.params.put("employerNm",binding.workUnit.getText().toString());
            LoanPersonalActivity.params.put("mtJobSectorCd",jobs);
            LoanPersonalActivity.params.put("mtJobSectorCdName",binding.jobsector.getText().toString());
            LoanPersonalActivity.params.put("mtPosHeldCd",post);
            LoanPersonalActivity.params.put("mtPosHeldCdName",binding.postSector.getText().toString());
            LoanPersonalActivity.params.put("employerPhone",binding.unitPhone.getText().toString());
        }else {
            LoanPersonalActivity.params.put("isComb",mutiple);
            LoanPersonalActivity.params.put("isBussLongEffec",longer);
            LoanPersonalActivity.params.put("isLegalRep",isLegal);
            LoanPersonalActivity.params.put("bizRegNo",binding.regNo.getText().toString());
            LoanPersonalActivity.params.put("bizAddr",binding.regAddress.getText().toString());
            if (!binding.getIsLonger())
                LoanPersonalActivity.params.put("bizRegDtExpiry",binding.regExpiry.getText().toString());
            LoanPersonalActivity.params.put("currentTotal",binding.currenTotal.getText().toString());
            LoanPersonalActivity.params.put("yearSaleMarginsRate",binding.saleMargin.getText().toString());
            LoanPersonalActivity.params.put("ratal",binding.ratal.getText().toString());
        }
        super.onStop();
    }
}
