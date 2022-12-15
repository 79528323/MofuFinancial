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
import cn.mofufin.morf.databinding.FragmentPersonalStepThreeBinding;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 贷款
 */
public class PersonalStepThreeFragment extends BaseFragment {
    private FragmentPersonalStepThreeBinding binding;
    private String car="" , privates="" , family="" , credit="";

    public static PersonalStepThreeFragment newInstance(){
        return new PersonalStepThreeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_step_three,container,false);
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

        binding.setCarClickListener(carClickListener);
        binding.setCreditClickListener(creditClickListener);
        binding.setFamilyClickListener(familyClickListener);
        binding.setPrivateClickListener(privateClickListener);
    }


    public void next(View view){
        if (TextUtils.isEmpty(binding.amt.getText().toString())){
            showTips("请填写税后收入");
            return;
        }else if (TextUtils.isEmpty(family)){
            showTips("请选择房产");
            return;
        }else if (TextUtils.isEmpty(car)){
            showTips("请选择是否有车");
            return;
        }else if (TextUtils.isEmpty(credit)){
            showTips("请选择是否有信用卡");
            return;
        }else if (TextUtils.isEmpty(privates)){
            showTips("请选择是否为私营业主");
            return;
        }

        if (binding.getHasCar()){
            if (TextUtils.isEmpty(binding.plateNo.getText().toString())){
                showTips("请输入车牌号");
                return;
            }
        }

        if (binding.getHasCredit()){
            if (TextUtils.isEmpty(binding.credit.getText().toString())){
                showTips("请输入额度");
                return;
            }
        }

        LoanPersonalActivity.params.put("yearIncAmt",binding.amt.getText().toString());
        LoanPersonalActivity.params.put("plateNo",binding.plateNo.getText().toString());
        LoanPersonalActivity.params.put("creditCardLines",binding.credit.getText().toString());
        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,4);
    }


    View.OnClickListener familyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            family = (String)v.getTag();
            LoanPersonalActivity.params.put("isFamily",family);
        }
    };

    View.OnClickListener carClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            car = (String)v.getTag();
            binding.setHasCar(car.equals("Y")?true:false);
            LoanPersonalActivity.params.put("hasCar",car);
        }
    };

    View.OnClickListener creditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            credit = (String)v.getTag();
            binding.setHasCredit(credit.equals("Y")?true:false);
            LoanPersonalActivity.params.put("hasCreditCard",credit);
        }
    };

    View.OnClickListener privateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            privates = (String)v.getTag();
            LoanPersonalActivity.params.put("isBizEntit",privates);
        }
    };

    //数据回读
    public void setViewHolder(SelfTextInfo.MerSelfTextBean bean){
        if (bean!=null){
            family = bean.isFamily;
            if (!TextUtils.isEmpty(family))
                binding.familyGroup.check(family.equals("Y")?R.id.family_radio1:R.id.family_radio2);
            LoanPersonalActivity.params.put("isFamily",family);

            car = bean.hasCar;
            if (!TextUtils.isEmpty(car)){
                binding.carGroup.check(car.equals("Y")?R.id.car_radio1:R.id.car_radio2);
                binding.setHasCar(car.equals("Y")?true:false);
            }
            LoanPersonalActivity.params.put("hasCar",car);

            credit = bean.hasCreditCard;
            if (!TextUtils.isEmpty(credit)){
                binding.creditGroup.check(credit.equals("Y")?R.id.credit_radio1:R.id.credit_radio2);
                binding.setHasCredit(credit.equals("Y")?true:false);
            }
            LoanPersonalActivity.params.put("hasCreditCard",credit);

            privates = bean.isBizEntit;
            if (!TextUtils.isEmpty(privates)){
                binding.privaterGroup.check(privates.equals("Y")?R.id.privater_radio1:R.id.privater_radio2);
            }
            LoanPersonalActivity.params.put("isBizEntit",privates);

            binding.amt.setText(bean.yearIncAmt);
            binding.plateNo.setText(bean.plateNo);
            binding.credit.setText(bean.creditCardLines);
        }
    }

    @Override
    public void onStop() {
        LoanPersonalActivity.params.put("isFamily",family);
        LoanPersonalActivity.params.put("hasCar",car);
        LoanPersonalActivity.params.put("hasCreditCard",credit);
        LoanPersonalActivity.params.put("isBizEntit",privates);
        LoanPersonalActivity.params.put("plateNo",binding.plateNo.getText().toString());
        LoanPersonalActivity.params.put("yearIncAmt",binding.amt.getText().toString());
        LoanPersonalActivity.params.put("creditCardLines",binding.credit.getText().toString());
        super.onStop();
    }
}
