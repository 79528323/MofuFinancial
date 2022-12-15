package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanRecordDetailsBinding;
import cn.mofufin.morf.databinding.ActivityProductAboutUsBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.functions.Action1;

public class LoanRecordDetailsActivity extends BaseActivity {
    private ActivityLoanRecordDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_record_details);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        LoanApplyRecord.ApplyLoansListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setDetails(bean);
    }

    @Override
    protected boolean enableSliding() {
        return true;
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
                            DataUtils.TipsDailog(LoanRecordDetailsActivity.this, Common.SERVICE_NUMBER, getString(R.string.cancel)
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
                            DataUtils.setPermissionDailog(LoanRecordDetailsActivity.this,getString(R.string.permissions_call));
                        }
                    }
                });
    }

}
