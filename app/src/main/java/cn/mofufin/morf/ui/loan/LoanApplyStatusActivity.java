package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityBalanceTransferStatusBinding;
import cn.mofufin.morf.databinding.ActivityLoanApplyStatusBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.mine.BalanceActivity;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import rx.functions.Action1;

/**
 * 申请贷款成功页面
 */
public class LoanApplyStatusActivity extends BaseActivity{
    private ActivityLoanApplyStatusBinding binding;
    private int status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_apply_status);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);

        status = getIntent().getIntExtra(IntentParam.STATUS,status);
        binding.setStatus(status);
        if (status>0){
            String amount = getIntent().getStringExtra(IntentParam.AMOUNT);
            int period = getIntent().getIntExtra(IntentParam.PERIOD,0);
            double ratio = getIntent().getDoubleExtra(IntentParam.RATIO,0);
            String banknum = getIntent().getStringExtra(IntentParam.BANKCARD_NO);

            binding.setAmount(amount+"元");
            binding.setMonths(period);
            binding.setRatio(ratio);
            if (!TextUtils.isEmpty(banknum)){
                banknum = banknum.substring(banknum.length()-4,banknum.length());
                binding.setBankinfo(banknum);
            }
        }else {
            String tips = getIntent().getStringExtra(IntentParam.TIPS);
            binding.setTips(tips);
        }
    }


    @Override
    protected boolean enableSliding() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPress(View view){
        finish();
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        new RxPermissions(this)
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
                            DataUtils.TipsDailog(LoanApplyStatusActivity.this, Common.SERVICE_NUMBER, getString(R.string.cancel)
                                    , "呼叫客服", new DialogInterface.OnClickListener() {
                                        @SuppressLint("MissingPermission")
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            Uri data = Uri.parse("tel:" + Common.SERVICE_NUMBER);
                                            intent.setData(data);
                                            startActivity(intent);
                                        }

                                    });
                        }else {
                            DataUtils.setPermissionDailog(LoanApplyStatusActivity.this,getString(R.string.permissions_call));
                        }
                    }
                });
    }
}
