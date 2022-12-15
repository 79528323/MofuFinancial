package cn.mofufin.morf.ui.loan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.databinding.FragmentPersonalStepOneBinding;
import cn.mofufin.morf.databinding.FragmentSpouseStepTwoBinding;
import cn.mofufin.morf.ui.ProductDetailActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.loan.LoanPersonalActivity;
import cn.mofufin.morf.ui.loan.LoanSpouseActivity;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class SpouseStepTwoFragment extends BaseFragment {
    private FragmentSpouseStepTwoBinding binding;
    private String sexy="",married="",phoneUse="",education="",address="";

    public static SpouseStepTwoFragment newInstance(){
        return new SpouseStepTwoFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spouse_step_two,container,false);
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

        Bundle bundle = getArguments();
        SelfTextInfo.MerSpouseTextBean bean = bundle.getParcelable(IntentParam.BEAN);
        setViewHolder(bean);

        binding.setAddressClick(addressClick);
        binding.setEducationClick(educationClick);
        binding.setMarriedClick(marreidClick);
        binding.setPhoneUsedClick(phoneClick);
        binding.setSexyClick(sexyClick);
    }

    public void next(View view){
        if (TextUtils.isEmpty(sexy)){
            showTips("请选择性别");
            return;
        }else if (TextUtils.isEmpty(married)){
            showTips("请选择婚姻情况");
            return;
        }else if (TextUtils.isEmpty(phoneUse)){
            showTips("请选择手机号使用年限");
            return;
        }else if (TextUtils.isEmpty(education)){
            showTips("请选择最高学历");
            return;
        }else if (TextUtils.isEmpty(address)){
            showTips("请选择本地居住地址");
            return;
        }

        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,3);
    }


    View.OnClickListener sexyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sexy=(String)v.getTag();
            LoanSpouseActivity.params.put("mtGenderCd",sexy);
        }
    };

    View.OnClickListener marreidClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            married =(String)v.getTag();
            LoanSpouseActivity.params.put("mtMaritalStsCd",married);
        }
    };

    View.OnClickListener phoneClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            phoneUse = (String)v.getTag();
            LoanSpouseActivity.params.put("mtIndvMobileUsageStsCd",phoneUse);
        }
    };

    View.OnClickListener educationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            education =  (String)v.getTag();
            LoanSpouseActivity.params.put("mtEduLvlCd",education);
        }
    };

    View.OnClickListener addressClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            address =  (String)v.getTag();
            LoanSpouseActivity.params.put("mtResidenceStsCd",address);
        }
    };

    //数据回读
    public void setViewHolder(SelfTextInfo.MerSpouseTextBean bean){
        if (bean!=null){
            sexy = bean.mtGenderCd;
            if (!TextUtils.isEmpty(sexy))
                binding.sexyGroup.check(sexy.equals("M")?R.id.sexy_radio1:R.id.sexy_radio2);

            married = bean.mtMaritalStsCd;
            if (!TextUtils.isEmpty(married))
                binding.marriedGroup.check(married.equals("01")?R.id.married_radio1:married.equals("02")?R.id.married_radio2:R.id.married_radio3);

            phoneUse = bean.mtIndvMobileUsageStsCd;
            if (!TextUtils.isEmpty(phoneUse))
                binding.usePhoneNumGroup.check(phoneUse.equals("01")?R.id.usePhoneNum_radio1:phoneUse.equals("02")?
                    R.id.usePhoneNum_radio2:phoneUse.equals("03")?R.id.usePhoneNum_radio3:phoneUse.equals("04")?
                    R.id.usePhoneNum_radio4:R.id.usePhoneNum_radio5);

            education = bean.mtEduLvlCd;
            if (!TextUtils.isEmpty(education))
                binding.educationGroup.check(education.equals("01")?R.id.education_radio1:education.equals("02")?
                    R.id.education_radio2:education.equals("03")?R.id.education_radio3:education.equals("04")?
                    R.id.education_radio4:R.id.education_radio5);

            address = bean.mtResidenceStsCd;
            if (!TextUtils.isEmpty(address))
                binding.addressGroup.check(address.equals("01")?R.id.address_radio1:address.equals("02")?
                    R.id.address_radio2:address.equals("03")?R.id.address_radio3:address.equals("04")?
                    R.id.address_radio4:R.id.address_radio5);


            LoanSpouseActivity.params.put("mtGenderCd",sexy);
            LoanSpouseActivity.params.put("mtMaritalStsCd",married);
            LoanSpouseActivity.params.put("mtIndvMobileUsageStsCd",phoneUse);
            LoanSpouseActivity.params.put("mtEduLvlCd",education);
            LoanSpouseActivity.params.put("mtResidenceStsCd",address);
        }
    }

    @Override
    public void onStop() {
        LoanSpouseActivity.params.put("mtGenderCd",sexy);
        LoanSpouseActivity.params.put("mtMaritalStsCd",married);
        LoanSpouseActivity.params.put("mtIndvMobileUsageStsCd",phoneUse);
        LoanSpouseActivity.params.put("mtEduLvlCd",education);
        LoanSpouseActivity.params.put("mtResidenceStsCd",address);
        super.onStop();
    }
}
