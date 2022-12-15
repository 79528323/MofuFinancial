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
import cn.mofufin.morf.databinding.FragmentSpouseStepThreeBinding;
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
public class SpouseStepThreeFragment extends BaseFragment {
    private FragmentSpouseStepThreeBinding binding;
    private String car="" , family="" , credit="";

    public static SpouseStepThreeFragment newInstance(){
        return new SpouseStepThreeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_spouse_step_three,container,false);
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

        binding.setCarClickListener(carClickListener);
        binding.setCreditClickListener(creditClickListener);
        binding.setFamilyClickListener(familyClickListener);
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

        LoanSpouseActivity.params.put("creditCardLines",binding.credit.getText().toString());
        LoanSpouseActivity.params.put("plateNo",binding.plateNo.getText().toString());
        LoanSpouseActivity.params.put("yearIncAmt",binding.amt.getText().toString());
        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,4);
    }


    View.OnClickListener familyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            family = (String)v.getTag();
            LoanSpouseActivity.params.put("isFamily",family);
        }
    };

    View.OnClickListener carClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            car = (String)v.getTag();
            binding.setHasCar(car.equals("Y")?true:false);
            LoanSpouseActivity.params.put("hasCar",car);
        }
    };

    View.OnClickListener creditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            credit = (String)v.getTag();
            binding.setHasCredit(credit.equals("Y")?true:false);
            LoanSpouseActivity.params.put("hasCreditCard",credit);
        }
    };

    //数据回读
    public void setViewHolder(SelfTextInfo.MerSpouseTextBean bean){
        if (bean!=null){
            family = bean.isFamily;
            if (!TextUtils.isEmpty(family)){
                binding.familyGroup.check(family.equals("Y")?R.id.family_radio1:R.id.family_radio2);
            }
            LoanSpouseActivity.params.put("isFamily",family);

            car = bean.hasCar;
            if (!TextUtils.isEmpty(car)){
                binding.carGroup.check(car.equals("Y")?R.id.car_radio1:R.id.car_radio2);
                binding.setHasCar(car.equals("Y")?true:false);
            }
            LoanSpouseActivity.params.put("hasCar",car);

            credit = bean.hasCreditCard;
            if (!TextUtils.isEmpty(credit)){
                binding.creditGroup.check(credit.equals("Y")?R.id.credit_radio1:R.id.credit_radio2);
                binding.setHasCredit(credit.equals("Y")?true:false);
            }
            LoanSpouseActivity.params.put("hasCreditCard",credit);

            binding.amt.setText(bean.yearIncAmt);
            binding.plateNo.setText(bean.plateNo);
            binding.credit.setText(bean.creditCardLines);
        }
    }

    @Override
    public void onStop() {
        LoanSpouseActivity.params.put("isFamily",family);
        LoanSpouseActivity.params.put("hasCar",car);
        LoanSpouseActivity.params.put("hasCreditCard",credit);

        LoanSpouseActivity.params.put("creditCardLines",binding.credit.getText().toString());
        LoanSpouseActivity.params.put("plateNo",binding.plateNo.getText().toString());
        LoanSpouseActivity.params.put("yearIncAmt",binding.amt.getText().toString());
        super.onStop();
    }
}
