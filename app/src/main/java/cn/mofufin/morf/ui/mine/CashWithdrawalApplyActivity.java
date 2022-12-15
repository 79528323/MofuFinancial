package cn.mofufin.morf.ui.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityCashwithdrawalApplyBinding;

public class CashWithdrawalApplyActivity extends BaseActivity {
    private ActivityCashwithdrawalApplyBinding binding;
    private boolean isBackCashWithdrawal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cashwithdrawal_apply);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        String avmount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);
        binding.setAvmount(avmount);

        double fee = getIntent().getDoubleExtra(IntentParam.FEE,0.00);
//        MerchantBag.DataBean.ListBean bag = getIntent().getParcelableExtra(IntentParam.MERCHANT_BAG);
//        if (bag!=null){
//            double sub = bag.gdGoodsDenomination;
//            if (sub >= fee)
//                fee = 0;
//            else {
//                fee = fee - sub;
//            }
//        }
        binding.setFee(getString(R.string.withdraw_unit,DataUtils.numFormat(fee)));

        //应到账金额
        double mount = Double.valueOf(avmount) - fee;
        binding.setShouldMount(getString(R.string.withdraw_unit,DataUtils.numFormat(mount)));

        User.DataBean.CardInfoBean bean = getIntent().getParcelableExtra(IntentParam.CARD_INFO_BEAN);
        String lastNum = bean.cardCode.substring(bean.cardCode.length()-4,bean.cardCode.length());
        binding.avaliableBank.setText(bean.cardBankName+"("+lastNum+")");

        binding.setDate(getString(R.string.withdraw_13,new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
    }


    public void complete(View view){
        RxBus.getInstance().postEmpty(RxEvent.MERCHANT_BALANCE_QUREY_REFRESH);
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_WITHDRAW_INFO);
        if (!isBackCashWithdrawal){
            RxBus.getInstance().postEmpty(RxEvent.CASH_WITHDRAWAL_FINISH);
        }
        finish();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
}
