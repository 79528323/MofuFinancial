package cn.mofufin.morf.ui;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.MposArea;
import cn.mofufin.morf.ui.manager.LocationService;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityMposBinding;
import cn.mofufin.morf.ui.widget.SinglePicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MposActivity extends BaseActivity{
    private ActivityMposBinding binding;
    private String mode;
    private SinglePicker picker;
    private List<MposArea.AreaListBean> areaList;
    private MposArea.AreaListBean area;
    private LocationService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mpos);
        binding.setHandlers(this);
        binding.setTotalMoney("");

        mode = getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE);
        if (TextUtils.equals(mode,"nfc")){
            binding.setTitle("云闪付收款");
            binding.setQuota(1000);
        }else {
            binding.setTitle("刷卡收款");
            binding.setQuota(50000);
        }


        service = new LocationService(this);
        service.setLocationOption(service.getDefaultLocationClientOption());
        service.registerListener(bdAbstractLocationListener);
        service.start();


        picker = new SinglePicker(this,SinglePicker.TYPE_MPOSAREA);
        picker.setTitle("");
        picker.listener = new SinglePicker.OnSingleSelectListener(){

            @Override
            public void singleSelect(String type, int num) {
                if (areaList!=null && !areaList.isEmpty()){
                    for (MposArea.AreaListBean bean:areaList){
                        if (TextUtils.equals(bean.areaName,type)){
                            area = new MposArea.AreaListBean(bean.areaName,bean.latitude,bean.longitude);
                            binding.setArea(area);
                        }
                    }
                }
            }
        };
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.stop();
        service.unregisterListener(bdAbstractLocationListener);
    }

    public void clean(View view){
        binding.sum.setText("");
    }

    public void selectArea(View view){
        picker.show();
    }

    public void onNext(View view){
        String str = binding.sum.getText().toString();

        if (!TextUtils.isEmpty(str)){
            if (binding.getArea()==null){
                showTips("请选择消费的省市");
                return;
            }

            double sum = Double.valueOf(str);
            if (sum < 10 || sum > binding.getQuota()){
                DataUtils.realNameTipsDailog(this,"请输入区间内金额",getString(R.string.ok),"");
                return;
            }

            jumpIntent(getIntent().getIntExtra(IntentParam.TYPE,2));

        } else {
            showTips("请输入金额");
        }
    }

    //跳转
    public void jumpIntent(int use){
        Class<?> classes = null;
        switch (use){
            case 0:
                classes = MPosIndustryActivity.class;
                break;

            case 1:
                classes = MerchantPosIndustryActivity.class;
                break;

                default:
                    classes = MerchantPosIndustryActivity.class;
                    break;
        }
        Intent intent = new Intent(MposActivity.this,classes);
        double sums = Float.valueOf(binding.sum.getText().toString());
        intent.putExtra(IntentParam.PAY_ENNUM_MODE,mode);
        intent.putExtra(IntentParam.AMOUNT,sums);
        intent.putExtra(IntentParam.BEAN,area);
        startActivity(intent);
    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.setTotalMoney(s.toString());
    }

    public void queryArea(){
        Subscription subscription = UserImpAPI.queryMposArea(HttpParam.QUERY_MPOS_AREA_KEY,Common.LOAN_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MposArea>() {
                    @Override
                    public void call(MposArea mposArea) {
                        if (mposArea.result_Code==0){
                            Logger.e("call");
                            areaList = mposArea.areaList;
                            areaList.add(0,binding.getArea());
                            picker.setAreaList(areaList);
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


    BDAbstractLocationListener bdAbstractLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation!=null){
                String latitude = String.valueOf(bdLocation.getLatitude());
                String longitude = String.valueOf(bdLocation.getLongitude());

                area = new MposArea.AreaListBean("当前位置（"+bdLocation.getCity()+"）",latitude,longitude);
                binding.setArea(area);
                service.stop();

                queryArea();
            }
        }
    };

}
