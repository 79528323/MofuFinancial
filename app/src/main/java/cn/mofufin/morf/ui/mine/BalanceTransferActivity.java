package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.EditInputFilter;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityBalanceTransferBinding;
import rx.functions.Action1;

/**
 * 余额转存
 */
public class BalanceTransferActivity extends BaseActivity{
    private ActivityBalanceTransferBinding binding;
    private double avAmount = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_transfer);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        String amount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);
        avAmount = TextUtils.isEmpty(amount)?avAmount:Double.valueOf(amount);
        binding.setAmount(getString(R.string.balance_tranf_1,DataUtils.numFormat(avAmount)));

        EditInputFilter filter = new EditInputFilter();
        filter.setMAX_VALUE(avAmount);
        filter.setContext(this);
        InputFilter[] filters = {filter};
        binding.mposSum.setFilters(filters);

        rxManager.onRxEvent(RxEvent.BALANCE_TRANSFER_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                BalanceTransferActivity.this.finish();
            }
        });

    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void trunAll(View view){
        binding.mposSum.setText(avAmount+"");
        binding.mposSum.setSelection(binding.mposSum.getText().length());
    }

    public void clean(View view){
        binding.mposSum.setText("");
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>0){
            binding.clear.setVisibility(View.VISIBLE);
        }else
            binding.clear.setVisibility(View.INVISIBLE);
    }

    public void onNext(View view){
        String str = binding.mposSum.getText().toString();

        if (!TextUtils.isEmpty(str)){
            if (str.substring(str.length()-1 ,str.length()).equals(".")){
                return;
            }else if (Double.valueOf(str)<=0){
                return;
            }

            jumpIntent();

        } else {
            showTips("请输入金额");
        }
    }


    //跳转
    public void jumpIntent(){
        Intent intent = new Intent(BalanceTransferActivity.this,BalanceTransferPasswordActivity.class);
        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,binding.mposSum.getText().toString());
        startActivity(intent);
    }


}
