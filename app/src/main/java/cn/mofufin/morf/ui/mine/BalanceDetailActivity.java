package cn.mofufin.morf.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.BalanceDetail;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityBalanceDetailBinding;
import rx.functions.Action1;

public class BalanceDetailActivity extends BaseActivity {
    private ActivityBalanceDetailBinding binding;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_detail);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        BalanceDetail.DetailLists details = getIntent().getParcelableExtra(IntentParam.BALANCE_DETAIL_BEAN);
        binding.setType(details.cdWay);
        binding.setDetails(details);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                            DataUtils.TipsDailog(BalanceDetailActivity.this, Common.SERVICE_NUMBER, getString(R.string.cancel)
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
                            DataUtils.setPermissionDailog(BalanceDetailActivity.this,getString(R.string.permissions_call));
                        }
                    }
                });
    }
}
