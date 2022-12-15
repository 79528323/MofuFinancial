package cn.mofufin.morf.ui.mine;

import android.Manifest;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.ByteArrayOutputStream;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.fragment.CreatImageFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.ChargeImpAPI;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityChargeCreditPaymentBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 刷卡金充值
 */
public class ChargeCreditPaymentActivity extends BaseActivity{
    private ActivityChargeCreditPaymentBinding binding;
    private double totalMoney=0.00;
    private CreatImageFragment dailog;
    private Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_charge_credit_payment);
        binding.setHandlers(this);

//        String amount = getIntent().getStringExtra(IntentParam.AMOUNT);
//        totalMoney = TextUtils.isEmpty(amount)?0:Double.valueOf(amount);
        binding.setTotalMoney(totalMoney);
        updateMerchantInfo();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mofu_qr_code);
        binding.ImgQr.setImageBitmap(bitmap);

        rxManager.onRxEvent(RxEvent.CHARGE_CREDIT_PAYMENT_FINISH).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                ChargeCreditPaymentActivity.this.finish();
            }
        });
    }
//
//    //保存二维码到本地
    public void saveQR(View view){
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                        if (aBoolean){
                            bmp = ((BitmapDrawable)binding.ImgQr.getDrawable()).getBitmap();
                            dailog = new CreatImageFragment(DataUtils.numFormat(totalMoney)
                                    ,MerchanInfoDB.queryInfo().fdMerIdentityCardName
                                    ,0
                                    ,covertBitmap(bmp),0);
                            dailog.showNow(getSupportFragmentManager(),"CreatImageFragment");

                            bmp = DataUtils.getViewBitmap(dailog.getLayoutViews());

                            DataUtils.saveBitmap(ChargeCreditPaymentActivity.this,bmp,"QR_"+System.currentTimeMillis());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dailog.dismiss();
                                    bmp.recycle();
                                }
                            },2000);
                        }else {
                            DataUtils.setPermissionDailog(ChargeCreditPaymentActivity.this,getString(R.string.permissions_savephoto));
                        }
                    }
                });


    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void submit(View view) {
        startActivity(new Intent(this,ChargeCreditPaymentExplainActivity.class));
        super.submit(view);
    }

    public void confirm(View view){
        Subscription subscription = ChargeImpAPI.merBalanceAliPayEnter(HttpParam.OFFICE_CODE,HttpParam.CHARGE_CREDIT_PAYMENT_KEY,
                MerchanInfoDB.queryInfo().merchantCode,binding.orders.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                }).subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        Intent intent = new Intent(ChargeCreditPaymentActivity.this,BalanceTransferStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code==0);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        intent.putExtra(IntentParam.TYPE,BalanceTransferStatusActivity.TYPE_CHARGE_CREDIT_PAYMENT);
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    //生成本地图片
    public byte[] covertBitmap(Bitmap bitmap){
        if (bitmap==null)
            return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
//        bitmap.recycle();
        return baos.toByteArray();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
                        binding.setTotalMoney(bean.cardMoney);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }
}
