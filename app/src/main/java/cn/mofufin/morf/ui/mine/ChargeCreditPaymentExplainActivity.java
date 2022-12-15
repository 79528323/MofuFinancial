package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.databinding.ActivityChargeCreditPaymentExplainBinding;

public class ChargeCreditPaymentExplainActivity extends BaseActivity {
    private ActivityChargeCreditPaymentExplainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_charge_credit_payment_explain);
//        setStatusBar();
        binding.setHandlers(this);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }



}
