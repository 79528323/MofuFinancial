package cn.mofufin.morf.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.ByteArrayOutputStream;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.fragment.CreatImageFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.services.ReceiVablesImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.ZXingUtils;
import cn.mofufin.morf.databinding.ActivityScanQrReceivablesBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ScanQRReceiVablesActivity extends BaseActivity{
    public final static int QR_CODE_STATIC = 0x101;
    public final static int QR_CODE_DYNAMIC = 0x102;
    public final static int QR_CODE_OVER = 0x103;

    public final static int QR_WECHAT = 1;
    public final static int QR_ALIPAY = 2;
    private ActivityScanQrReceivablesBinding binding;
    private String payChannelType;
    private float totalMoney;
    private CreatImageFragment dailog;
    private int type ,qr_type;
    private Bitmap bmp = null;
    private String mcbGoodsNumber;
    private Bitmap logo = null;
    private int withd , height;
    private final static String staticUrl = "http://mofufin.cn/mobile/remitPay/initH5PayPage?appKey="+HttpParam.STATIC_QR_KEY
            +"&officeCode="+HttpParam.OFFICE_CODE+"&merchantCode="+MerchanInfoDB.queryInfo().merchantCode;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                if (qr_type == QR_CODE_DYNAMIC){
                    refreshUrl();
                }else if (qr_type == QR_CODE_OVER){
                    overQR(getIntent().getStringExtra(IntentParam.QR_URL));
                }else
                    staticQR();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_scan_qr_receivables);
        binding.setHandlers(this);

        withd = getResources().getDisplayMetrics().widthPixels /4;
        height = withd;

        binding.setTitle(getString(R.string.home_head3));

        payChannelType = getIntent().getStringExtra(IntentParam.PAYCHANNELTYPE);
        totalMoney = getIntent().getFloatExtra(IntentParam.QR_TOTAL,0.00f);
        type = getIntent().getIntExtra(IntentParam.TYPE,2);
        mcbGoodsNumber = getIntent().getStringExtra(IntentParam.MCBGOODSNUMBER);

        qr_type = getIntent().getIntExtra(IntentParam.QR_TYPE,QR_CODE_STATIC);//当前状态 101静态码 102动态码
        if (qr_type == QR_CODE_OVER){
            binding.setTitle(getString(R.string.home_head4));
            binding.titles.setText("可在任意三方应用扫一扫，向我付款！");
            binding.titles.setTextColor(Color.WHITE);
            binding.titles.setBackgroundColor(Color.parseColor("#97A6B4"));
            binding.setAMount.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
            binding.moneyText.setVisibility(View.VISIBLE);
            binding.moneyText.setText(DataUtils.numFormat(totalMoney));
            binding.titlebar.setBackgroundColor(Color.parseColor("#E75A30"));
            binding.layout.setBackgroundColor(Color.parseColor("#F6F6F6"));
            binding.tips.setText("平台交易时间为上午九点至晚上23点之间！");
        }else {
            if (qr_type == QR_CODE_STATIC){
                binding.moneyText.setVisibility(View.GONE);
            }else {
                binding.moneyText.setVisibility(View.VISIBLE);
                binding.moneyText.setText("￥"+DataUtils.numFormat(totalMoney));

                withd = withd * 4;
                height = withd;
            }
        }


        Logger.e("withd="+withd+"  height="+height);

        logo = BitmapFactory.decodeResource(getResources(),R.drawable.mofu_logo);//摩富LOGO
        binding.ImgQr.post(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what =0;
                handler.sendMessage(message);
            }
        });
    }

    //保存二维码到本地
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
                                    ,type
                                    ,covertBitmap(bmp),qr_type);
                            dailog.showNow(getSupportFragmentManager(),"CreatImageFragment");

                            bmp = DataUtils.getViewBitmap(dailog.getLayoutViews());

                            DataUtils.saveBitmap(ScanQRReceiVablesActivity.this,bmp,"QR_"+System.currentTimeMillis());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dailog.dismiss();
                                    bmp.recycle();
                                }
                            },2000);
                        }else {
                            DataUtils.setPermissionDailog(ScanQRReceiVablesActivity.this,getString(R.string.permissions_savephoto));
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //重设金额
    public void setTotal(View v){
        startActivity(new Intent(this,MerChantScanQRActivity.class));
        RxBus.getInstance().postEmpty(RxEvent.EDITEXT_GET_FOCUS);
    }

    public void refreshUrl(){
        String merCode = MerchanInfoDB.queryInfo().merchantCode;
        Subscription subscription = ReceiVablesImpAPI.QR(HttpParam.OFFICE_CODE,HttpParam.QR_APPKEY
                ,payChannelType, DataUtils.numFormat(totalMoney),merCode,mcbGoodsNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        if (baseResult.result_Code==0){
                            Bitmap bitmap = ZXingUtils.createQRImage(baseResult.qrcodeUrl,withd,height,null);
                            binding.ImgQr.setImageBitmap(bitmap);
                        }else {
                            showTips(baseResult.result_Msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    public void staticQR(){
        Bitmap bitmap = ZXingUtils.createQRImage(staticUrl,withd,height,null);
        binding.ImgQr.setImageBitmap(bitmap);
    }

    public void overQR(String url){
        Bitmap bitmap = ZXingUtils.createQRImage(url,withd,height,null);
        binding.ImgQr.setImageBitmap(bitmap);
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
}
