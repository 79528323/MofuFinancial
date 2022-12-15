package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.githang.statusbar.StatusBarCompat;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.QRPopWindow;
import cn.mofufin.morf.databinding.ActivityMerchantScanqrBinding;
import rx.functions.Action1;

/**
 * 扫码收款
 */
public class MerChantScanQRActivity extends BaseActivity{
    public final static int QR_WECHAT = 1;
    public final static int QR_ALIPAY = 2;
    public final static String VALUE_WECHAT = "W1";
    public final static String VALUE_ALIPAY = "A1";
    private QRPopWindow window;
    private ActivityMerchantScanqrBinding binding;
    private WindowManager.LayoutParams params;
    private String payChannelType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_merchant_scanqr);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_blue));
        binding.setHandlers(this);
        binding.setType(QR_ALIPAY);

        rxManager.onRxEvent(RxEvent.EDITEXT_GET_FOCUS).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showKeyboard();
                    }
                },200);

            }
        });

        rxManager.onRxEvent(RxEvent.TRANSFER_PARAMETER, new Action1<Object>() {
            @Override
            public void call(Object o) {
                int type = (int) o;
                binding.setType(type);
            }
        });

        window = new QRPopWindow(this);
    }

    public void changePay(View view){
        window.setCheck(binding.getType());
        if (!window.isShowing()){
            window.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void clean(View view){
        binding.qrSum.setText("");
    }

    public void onNext(View view){
        String money = binding.qrSum.getText().toString();
        if (TextUtils.isEmpty(money)){
            showTips("请输入金额");
            return;
        }

        final float totalMoney = Float.valueOf(money);
        if (totalMoney < 0 || totalMoney >50000){
            showTips("超出金额限制!");
            return;
        }

        payChannelType = binding.getType()==QR_ALIPAY?VALUE_ALIPAY:VALUE_WECHAT;
        Intent intent = new Intent(MerChantScanQRActivity.this,ScanQRReceiVablesActivity.class);
        intent.putExtra(IntentParam.PAYCHANNELTYPE,payChannelType);
        intent.putExtra(IntentParam.QR_TOTAL,totalMoney);
        intent.putExtra(IntentParam.TYPE,binding.getType());
        intent.putExtra(IntentParam.QR_TYPE,ScanQRReceiVablesActivity.QR_CODE_DYNAMIC);
        intent.putExtra(IntentParam.MCBGOODSNUMBER,binding.getBean()!=null?binding.getBean().mcbGoodsNumber:"");
        startActivity(intent);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.setTotalMoney(s.toString());
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }


    public void selectionCoupon(View view){
        Intent intent = new Intent(this, BackpackActivity.class);
        intent.putExtra(IntentParam.BACK_PACK_SELECTION,BackpackActivity.SELECTION_TYPE_SCAN_QR);
        startActivityForResult(intent,BackpackActivity.SELECTION_TYPE_SCAN_QR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case BackpackActivity.SELECTION_TYPE_SCAN_QR:
                if (data!=null){
                    MerchantBag.DataBean.ListBean bean=data.getParcelableExtra(IntentParam.MERCHANT_BAG);
                    binding.setBean(bean);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
