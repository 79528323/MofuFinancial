package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityLoanApplyStatusBinding;
import cn.mofufin.morf.databinding.ActivityLoanUploadSuccessStatusBinding;
import cn.mofufin.morf.ui.ReceiVablesDetailActivity;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.dao.MerSelfTextBeanDao;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import retrofit2.http.PUT;
import rx.functions.Action1;

/**
 * TODO 贷款资料提交成功页面
 */
public class LoanUploadSuccessStatusActivity extends BaseActivity{
    private ActivityLoanUploadSuccessStatusBinding binding;
    public int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loan_upload_success_status);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);

        int status = getIntent().getIntExtra(IntentParam.STATUS,0);
        binding.setStatus(status);
        binding.setTips(getIntent().getStringExtra(IntentParam.TIPS));
        type = getIntent().getIntExtra(IntentParam.TYPE,type);
        binding.setType(type);
//        switch (type){
//            case 1:
//                MerSelfTextBeanDao dao = new MerSelfTextBeanDao(this);
//                dao.deleteBean();
//                break;
//        }

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_LOAN_IDENTITY_STATUS);
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
                            DataUtils.TipsDailog(LoanUploadSuccessStatusActivity.this, Common.SERVICE_NUMBER, getString(R.string.cancel)
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
                            DataUtils.setPermissionDailog(LoanUploadSuccessStatusActivity.this,getString(R.string.permissions_call));
                        }
                    }
                });
    }
}
