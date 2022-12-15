package cn.mofufin.morf.ui;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.adapter.OverChannelListAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.contract.MposContract;
import cn.mofufin.morf.databinding.ActivityOverseasBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OverseasActivity extends BaseActivity{
    private ActivityOverseasBinding binding;
    private int ovPayWay = 0;
    private MerchantBag.DataBean.ListBean bean=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_overseas);
        binding.setHandlers(this);
        binding.setTotalMoney("");
        initView();
    }

    public void initView(){
        Overseans.OverListBean bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setOver(bean);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void clean(View view){
        binding.mposSum.setText("");
    }

    public void onNext(View view){

        final String str = binding.mposSum.getText().toString();
        if (!TextUtils.isEmpty(str)){
//            if (TextUtils.isEmpty(number)){
//                showTips("请选择通道");
//                return;
//            }

            double sum = Double.valueOf(str);
            double minSum = Double.valueOf(binding.getOver().channelQuotaBegin);
            double maxSum = Double.valueOf(binding.getOver().channelQuotaEnd);
            if (sum < minSum || sum > maxSum){
                DataUtils.realNameTipsDailog(this,"请输入区间内金额",getString(R.string.ok),"");
                return;
            }else if (calculatSum(sum)){
                DataUtils.TipsDailog(this, getString(R.string.over_max_warning),
                        getString(R.string.cancel), "继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sumbitAmount(str);
                            }
                        });
            }else
                sumbitAmount(str);

        } else {
            showTips("请输入金额");
        }
    }

    //监听edittext输入输出操作
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.setTotalMoney(s.toString());
    }


    public void sumbitAmount(String money){
//        Subscription subscription = QueryChannelImpAPI.proceedPay(HttpParam.OFFICE_CODE,HttpParam.PROCEEDPAY_APPKEY,money,
//                MerchanInfoDB.queryInfo().merchantCode,binding.getOver().channelNumber,String.valueOf(ovPayWay),
//                binding.getBean()!=null?binding.getBean().mcbGoodsNumber:"")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .doOnCompleted(new Action0() {
//                    @Override
//                    public void call() {
//                        hiddenLoading();
//                    }
//                })
//                .subscribe(new Action1<BaseResult>() {
//                    @Override
//                    public void call(BaseResult baseResult) {
//                        if (baseResult.result_Code == 0){
//                            if (binding.getOver().channelKind==0){
//
//                                startActivity(new Intent(
//                                        OverseasActivity.this,OverseansWebActivity.class)
//                                        .putExtra("HTML",baseResult.url)
//                                        .putExtra(IntentParam.ACTIVITY_FLAG,OverseasActivity.this.TAG));
//
//                            }else {
//                                startActivity(new Intent(
//                                        OverseasActivity.this,ScanQRReceiVablesActivity.class)
//                                        .putExtra(IntentParam.QR_URL,baseResult.url)
//                                        .putExtra(IntentParam.QR_TYPE,ScanQRReceiVablesActivity.QR_CODE_OVER));
//
//                            }
//                        }else {
//                            showTips(baseResult.result_Msg);
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });

        Intent intent = new Intent(OverseasActivity.this,OverRiskManagementActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getOver());
        intent.putExtra(IntentParam.MERCHANT_BAG,binding.getBean());
        intent.putExtra(IntentParam.AMOUNT,money);
        intent.putExtra(IntentParam.QR_TOTAL,binding.getTotalMoney());
        startActivity(intent);


//        Subscription subscription = QueryChannelImpAPI.proceedPayNew(HttpParam.OFFICE_CODE,
//                HttpParam.PROCEEDPAY_NEW_APPKEY,money,
//                MerchanInfoDB.queryInfo().merchantCode,binding.getOver().channelNumber,
//                binding.getBean()!=null?binding.getBean().mcbGoodsNumber:"", Common.LOAN_VERSION)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .doOnCompleted(new Action0() {
//                    @Override
//                    public void call() {
//                        hiddenLoading();
//                    }
//                })
//                .subscribe(new Action1<BaseResult>() {
//                    @Override
//                    public void call(BaseResult baseResult) {
//                        if (baseResult.result_Code == 0){
//
//
//                            if (binding.getOver().channelKind==0){//TODO 0:h5 , 1:扫码
//
//                                startActivity(new Intent(
//                                        OverseasActivity.this,OverseansWebActivity.class)
//                                        .putExtra("HTML",baseResult.url)
//                                        .putExtra(IntentParam.ACTIVITY_FLAG,OverseasActivity.this.TAG));
//
//                            }else {
//                                startActivity(new Intent(
//                                        OverseasActivity.this,ScanQRReceiVablesActivity.class)
//                                        .putExtra(IntentParam.QR_URL,baseResult.url)
//                                        .putExtra(IntentParam.QR_TOTAL,Float.valueOf(binding.getTotalMoney()))
//                                        .putExtra(IntentParam.QR_TYPE,ScanQRReceiVablesActivity.QR_CODE_OVER));
//
//                            }
//                        }else {
//                            showTips(baseResult.result_Msg);
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
    }

    public void selectionCoupon(View view){
        Intent intent = new Intent(this, BackpackActivity.class);
        intent.putExtra(IntentParam.BACK_PACK_SELECTION,BackpackActivity.SELECTION_TYPE_OVER_SEAN);
        startActivityForResult(intent,BackpackActivity.SELECTION_TYPE_OVER_SEAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case BackpackActivity.SELECTION_TYPE_OVER_SEAN:
                if (data!=null){
                    MerchantBag.DataBean.ListBean bean=data.getParcelableExtra(IntentParam.MERCHANT_BAG);
                    binding.setBean(bean);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean calculatSum(double sum){
        boolean isShow =false;
        double limitPrecent=90.0d;
        double maxSum = Double.valueOf(binding.getOver().channelQuotaEnd);
        double percent = (sum/maxSum)*100;
        if (percent >= limitPrecent){
            isShow = true;
        }
//        Logger.e("percent="+percent);
        return isShow;
    }
}
