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
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.databinding.FragmentPersonalStepOneBinding;
import cn.mofufin.morf.databinding.FragmentSpouseStepOneBinding;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.loan.LoanSpouseActivity;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.CityPicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class SpouseStepOneFragment extends BaseFragment {
    private FragmentSpouseStepOneBinding binding;
    private CityPicker cityPicker;
    private String regProvId,regCityId;
    private TimePickerView timePickerView;
    private String selectTag="";
    private String proCodes="",cityCodes="";

    public static SpouseStepOneFragment newInstance(){
        return new SpouseStepOneFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spouse_step_one,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(){
        binding.setOnClickListener(onClickListener);
        binding.setIsLongrange("");

        Bundle bundle = getArguments();
        SelfTextInfo.MerSpouseTextBean bean = bundle.getParcelable(IntentParam.BEAN);
        setViewHolder(bean);

        cityPicker = new CityPicker(getActivity(),true);
        cityPicker.setTitle("选择省市");
        cityPicker.listener = new CityPicker.OnSelectListener() {
            @Override
            public void Select(String s1, String s2, String s3) {
                regProvId=s1;
                regCityId=s2;
                binding.providerCity.setText(regProvId +" "+ regCityId);
            }
        };
        cityPicker.setCoderListener(new CityPicker.onCoderListener() {
            @Override
            public void getCode(String Procode, String cityCode) {
                proCodes = Procode;
                cityCodes = cityCode;
            }
        });

        timePickerView = new TimePickerView(getActivity(),TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1900,2100);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                String dates = new SimpleDateFormat("yyyy-MM-dd").format(date);
                if (TextUtils.equals(selectTag,"birthDay")){
                    binding.birthDay.setText(dates);
                }else if (TextUtils.equals(selectTag,"issue")){
                    binding.issue.setText(dates);
                }else if (TextUtils.equals(selectTag,"limit")){
                    binding.expiry.setText(dates);
                }
            }
        });
    }

    public void SelectProCity(final View view){
        cityPicker.show();
    }

    public void selectDate(View view){
        if (timePickerView.isShowing()){
            timePickerView.dismiss();
        }else{
            selectTag = (String) view.getTag();
            timePickerView.setTitle(selectTag.equals("birthDay")?
                    "出生日期":selectTag.equals("issue")?"签发日期":"到期日期");
            timePickerView.show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            binding.setIsLongrange((String)v.getTag());
        }
    };

    public void next(View view){
        if (TextUtils.isEmpty(binding.name.getText().toString())){
            showTips("请填写姓名");
            return;
        }else if (TextUtils.isEmpty(binding.idCard.getText().toString())){
            showTips("请填写身份证");
            return;
        }else if (TextUtils.isEmpty(binding.providerCity.getText().toString())){
            showTips("请选择常住省市");
            return;
        }else if (TextUtils.isEmpty(binding.issue.getText().toString())){
            showTips("请填写身份证签发日期");
            return;
        }else if (TextUtils.isEmpty(binding.getIsLongrange())){
            showTips("请选择身份证是否长期");
            return;
        }else if (binding.getIsLongrange().equals("N")){
            if (TextUtils.isEmpty(binding.expiry.getText().toString())){
                showTips("请填写身份证到期日期");
                return;
            }
        }else if (TextUtils.isEmpty(binding.birthDay.getText().toString())){
            showTips("请填写出生日期");
            return;
        }else if (TextUtils.isEmpty(binding.phone.getText().toString())){
            showTips("请填写手机号");
            return;
        }else if (TextUtils.isEmpty(binding.email.getText().toString())){
            showTips("请填写邮箱");
            return;
        }else if (TextUtils.isEmpty(binding.QQ.getText().toString())){
            showTips("请填写QQ号");
            return;
        }else if (TextUtils.isEmpty(binding.wechat.getText().toString())){
            showTips("请填写微信号");
            return;
        }

        LoanSpouseActivity.params.put("nm",binding.name.getText().toString());
        LoanSpouseActivity.params.put("idNo",binding.idCard.getText().toString());
        LoanSpouseActivity.params.put("mtCityCdName",regCityId);
        LoanSpouseActivity.params.put("mtCityCd",cityCodes);
        LoanSpouseActivity.params.put("mtStateCd",proCodes);
        LoanSpouseActivity.params.put("mtStateCdName",regProvId);
        LoanSpouseActivity.params.put("dtIssue",binding.issue.getText().toString());
        LoanSpouseActivity.params.put("isLongEffec",binding.getIsLongrange());
        if (binding.getIsLongrange().equals("N"))
            LoanSpouseActivity.params.put("dtExpiry",binding.expiry.getText().toString());
        LoanSpouseActivity.params.put("dtRegistered",binding.birthDay.getText().toString());
        LoanSpouseActivity.params.put("mobileNo",binding.phone.getText().toString());
        LoanSpouseActivity.params.put("email",binding.email.getText().toString());
        LoanSpouseActivity.params.put("qq",binding.QQ.getText().toString());
        LoanSpouseActivity.params.put("weChat",binding.wechat.getText().toString());
        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,2);
    }

    //数据回读
    public void setViewHolder(SelfTextInfo.MerSpouseTextBean bean){
        if (bean!=null){
            binding.wechat.setText(bean.weChat);
            binding.QQ.setText(bean.qq);
            binding.email.setText(bean.email);
            if (!TextUtils.isEmpty(bean.dtRegistered))
                binding.birthDay.setText(bean.dtRegistered.substring(0,10));
            binding.phone.setText(bean.mobileNo);
            binding.idCard.setText(bean.idNo);

            regProvId=bean.mtStateCdName;
            regCityId=bean.mtCityCdName;
            if (!TextUtils.isEmpty(regProvId)&&!TextUtils.isEmpty(regCityId))
                binding.providerCity.setText(regProvId+" "+regCityId);

            proCodes = bean.mtStateCd;
            cityCodes = bean.mtCityCd;
            binding.name.setText(bean.nm);

            if (!TextUtils.isEmpty(bean.dtIssue))
                binding.issue.setText(bean.dtIssue.substring(0,10));
            binding.setIsLongrange(bean.isLongEffec);

            if (!TextUtils.isEmpty(binding.getIsLongrange())){
                binding.cardGroup.check(binding.getIsLongrange().equals("Y")?R.id.radio1:R.id.radio2);
                if (binding.getIsLongrange().equals("N")){
                    if (!TextUtils.isEmpty(bean.dtExpiry))
                        binding.expiry.setText(bean.dtExpiry.substring(0,10));
                }
            }

            LoanSpouseActivity.params.put("isLongEffec",binding.getIsLongrange());
        }
    }

    @Override
    public void onStop() {
        LoanSpouseActivity.params.put("nm",binding.name.getText().toString());
        LoanSpouseActivity.params.put("idNo",binding.idCard.getText().toString());
        LoanSpouseActivity.params.put("mtCityCdName",regCityId);
        LoanSpouseActivity.params.put("mtCityCd",cityCodes);
        LoanSpouseActivity.params.put("mtStateCd",proCodes);
        LoanSpouseActivity.params.put("mtStateCdName",regProvId);
        LoanSpouseActivity.params.put("dtIssue",binding.issue.getText().toString());
        LoanSpouseActivity.params.put("isLongEffec",binding.getIsLongrange());
        if (!TextUtils.isEmpty(binding.getIsLongrange()) && binding.getIsLongrange().equals("N"))
            LoanSpouseActivity.params.put("dtExpiry",binding.expiry.getText().toString());
        LoanSpouseActivity.params.put("dtRegistered",binding.birthDay.getText().toString());
        LoanSpouseActivity.params.put("mobileNo",binding.phone.getText().toString());
        LoanSpouseActivity.params.put("email",binding.email.getText().toString());
        LoanSpouseActivity.params.put("qq",binding.QQ.getText().toString());
        LoanSpouseActivity.params.put("weChat",binding.wechat.getText().toString());

        super.onStop();
    }
}
