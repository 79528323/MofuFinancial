package cn.mofufin.morf.ui.mine;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.Address;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityRechargeToDepositBinding;
import rx.functions.Action1;

/**
 * 充值到押金
 */
public class RechargeToDepositActivity extends BaseActivity{
    public static final int CHOOSE_RECEI_ADDRESS = 1001;
    private ActivityRechargeToDepositBinding binding;
//    private List<Address.DataBean.AddressInfoBean> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_recharge_to_deposit);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        binding.setBean(null);
        rxManager.onRxEvent(RxEvent.RECHARGE_TO_DEPOSIT_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                RechargeToDepositActivity.this.finish();
            }
        });

        rxManager.onRxEvent(RxEvent.REFRESH_CLEAR_RECEI_ADDRESS).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (binding.getBean() != null){
                    int number = (int) o;
                    if (number == binding.getBean().addressNumber){
                        binding.setBean(null);
                    }
                }else {
                    binding.setBean(null);
                }
            }
        });
    }

    public void addDress(View view){
        Intent intent = new Intent(this,ReceivingAddressActivity.class);
        startActivityForResult(intent,CHOOSE_RECEI_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_RECEI_ADDRESS){
            if (data!=null){
                Address.DataBean.AddressInfoBean address = data.getParcelableExtra(IntentParam.BEAN);
                binding.setBean(address);
                binding.executePendingBindings();
            }
        }
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onNext(View view){
        if (binding.getBean()==null){
            showTips("请添加收货地址");
            return;
        }

        String str = binding.sum.getText().toString();
        if (!TextUtils.isEmpty(str)){
            Intent intent = new Intent(RechargeToDepositActivity.this,BalanceTransferPasswordActivity.class);
            intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.sum.getText().toString());
            intent.putExtra(IntentParam.TYPE,2);
            intent.putExtra(IntentParam.NUMBER,String.valueOf(binding.getBean().addressNumber));
            startActivity(intent);
        }
    }

}
