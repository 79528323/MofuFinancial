package cn.mofufin.morf.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityReceivablesDetailBinding;
import rx.functions.Action1;

public class ReceiVablesDetailActivity extends BaseActivity {
    private ActivityReceivablesDetailBinding binding;
    private int findType=1 ,payType=0;
    private ClipboardManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_receivables_detail);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        Order.OrderDetails details = getIntent().getParcelableExtra(IntentParam.ORDER_DETAILS);
        binding.setDetails(details);
        findType = getIntent().getIntExtra(IntentParam.ORDER_FIND_TYPE,findType);
        binding.setType(findType);
        if (findType == 2){
            String payTypes = getIntent().getStringExtra(IntentParam.PAY_TYPE);
            this.payType = Integer.valueOf(payTypes);
            binding.setPaytype(payType);
        }

        cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        binding.setLongclick(onLongClickListener);
    }

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            LinearLayout layout = (LinearLayout) v;
            TextView order = (TextView) layout.getChildAt(1);
            String content = order.getText().toString();
            if (!TextUtils.isEmpty(content)){
                final ClipData clipData = ClipData.newPlainText("Label",content);
                cm.setPrimaryClip(clipData);
                showTips("已复制");
                return true;
            }
            return false;
        }
    };

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
                            DataUtils.TipsDailog(ReceiVablesDetailActivity.this, Common.SERVICE_NUMBER, getString(R.string.cancel)
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
                            DataUtils.setPermissionDailog(ReceiVablesDetailActivity.this,getString(R.string.permissions_call));
                        }
                    }
                });
    }
}
