package cn.mofufin.morf.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hope.paysdk.NetInterface;
import com.hope.paysdk.PaySdkEnvionment;
import com.hope.paysdk.framework.EnumClass;
import com.hope.paysdk.framework.IndustryInfo;
import com.hope.paysdk.framework.MerchantInfo;
import com.hope.paysdk.framework.beans.Industry;
import com.hope.paysdk.framework.beans.User;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.MerchantPosIndustryAdapter;
import cn.mofufin.morf.adapter.MposIndustryAdapter;
import cn.mofufin.morf.contract.MposIndustryContract;
import cn.mofufin.morf.databinding.ActivityMerchantPosIndustryBinding;
import cn.mofufin.morf.databinding.ActivityMposIndustryBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BusinessModel;
import cn.mofufin.morf.ui.entity.IndustryInfos;
import cn.mofufin.morf.ui.entity.MposArea;
import cn.mofufin.morf.ui.entity.MposRatio;
import cn.mofufin.morf.ui.manager.LocationService;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 *TODO 新版MPOS
 */
public class MerchantPosIndustryActivity extends BaseActivity{

    private ActivityMerchantPosIndustryBinding binding;
    private MerchantPosIndustryAdapter adapter;
    private double sum = 0;
    private String token;

    private String merPhone;

    private String queryType , ratioQueryType;

    private String longitude;//经度
    private String latitude;//纬度

    private MerchantInfo merchantInfo;

    private MposArea.AreaListBean area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_merchant_pos_industry);
        binding.setHandlers(this);

        String queryType = getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE);
        if (queryType.equals("nfc")){
            this.queryType = "2";
            ratioQueryType = "1";
        }else {
            this.queryType = "1";
            ratioQueryType = "0";
        }

        area = getIntent().getParcelableExtra(IntentParam.BEAN);
        sum = getIntent().getDoubleExtra(IntentParam.AMOUNT,0);
        token = getIntent().getStringExtra(IntentParam.PAY_TOKEN);
        binding.setAmount(DataUtils.numFormat(sum));
        merPhone = MerchanInfoDB.queryInfo().merchantPhone;

        adapter = new MerchantPosIndustryAdapter();
        adapter.setClickListener(clickListener);
        binding.industryList.setLayoutManager(new LinearLayoutManager(this));
        binding.industryList.setAdapter(adapter);

        mPosLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        service.stop();
//        service.unregisterListener(bdAbstractLocationListener);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double ratio = adapter.getRatio() / 100;//获取费率
            double charge = (sum * ratio) + adapter.getFee();
            binding.setCharge(String.valueOf(decimal1(2,charge)));

            double actualCount = sum - charge;
            Logger.e("actualCount="+actualCount);
            binding.setActualCount(String.valueOf(decimal1(2,actualCount,BigDecimal.ROUND_DOWN)));

            int position = (int) v.getTag();
            adapter.notifyItem(position);
            IndustryInfos.MercInfoListBean bean = adapter.getItemData(position);
            if (merchantInfo==null)
                merchantInfo = new MerchantInfo();
            merchantInfo.setMerchantId(bean.merc_id);
            merchantInfo.setMerchantName(bean.merc_name);
        }
    };

    public void next(View view){
        if (merchantInfo==null){
            showTips("请选择商户");
            return;
        }

        PaySdkEnvionment.startPay(this,
                DataUtils.createSerialNo(), binding.getAmount(),
                judgeOpeMode(getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE)),
                EnumClass.TYPE_GETFUND.TYPE_GETFUND_CUSTOM_MERCHANT_OUTSIDE_D0,
                null, merchantInfo, "", judgeAttachParams(), token);
    }

    /**
     * 登录
     */
    private class LoginUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        public User doInBackground(String... arg0) {
            NetInterface netInterface = PaySdkEnvionment.getNetInterfaceController();
            if (netInterface == null) {
                User user = new User();
                user.setSuccess(false);
                user.setCode(-3);
                user.setMsg("PaySdk未初始化.");
                return user;
            } else {
                return netInterface.login(arg0[0], arg0[1]);
            }
        }

        @Override
        public void onPostExecute(User ret) {
            if (ret != null && ret.code != -1) {
                if (ret.code == 0) {
                    token = ret.getToken();
                    getMerchantList();

                } else if (ret.code == 918) {
                    showTips(ret.getMsg());
                } else {
                    showTips(ret.getMsg());
                }
            } else {
//                showTips("支付登录异常，请稍候重试");
            }

            hiddenLoading();
        }
    }


    public void getMerchantList(){
        Subscription subscription = Observable.zip(UserImpAPI.mPosMercQuery(HttpParam.OFFICE_CODE,
                HttpParam.MERCHANT_MPOS_INDUSTRY_LIST_KEY,
                MerchanInfoDB.queryInfo().merchantCode, Common.LOAN_VERSION, area.longitude,area.latitude,
                this.queryType, DataUtils.numFormat(sum))
                , UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY, MerchanInfoDB.queryInfo().merchantCode,
                Common.LOAN_VERSION, ratioQueryType), new Func2<IndustryInfos, MposRatio, IndustryInfos>() {
            @Override
            public IndustryInfos call(IndustryInfos industryInfos, MposRatio mposRatio) {
                if (mposRatio.result_Code == 0){
                    if (industryInfos.result_Code == 0){
                        industryInfos.ratio = mposRatio.ratio;
                        industryInfos.fee = mposRatio.fee;
                        return industryInfos;
                    }
                }else{
                    DataUtils.TipsDailog(MerchantPosIndustryActivity.this, mposRatio.result_Msg,
                            "", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                }

                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<IndustryInfos>() {
                    @Override
                    public void call(IndustryInfos industryInfos) {
                        if (industryInfos!=null){
                            adapter.refresh(industryInfos.mercInfoList);
                            adapter.setFee(industryInfos.fee);
                            adapter.setRatio(industryInfos.ratio);
                            adapter.notifyDataSetChanged();
                        }else {
                            showTips("获取商户失败");
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


    private String judgeAttachParams() {
        JSONObject attachParams = new JSONObject();
        attachParams.put("fee_d0", binding.getCharge());
        attachParams.put("fee_t1", binding.getCharge());
        if (attachParams.size() == 0) {
            return null;
        } else {
            return attachParams.toString();
        }
    }

    /**
     * 卡友登录
     */
    public void mPosLogin(){
        new LoginUserTask().execute(merPhone,Common.PAYSDK_PASSWORD);
//        service.registerListener(bdAbstractLocationListener);
//        service.start();
    }

//    BDAbstractLocationListener bdAbstractLocationListener = new BDAbstractLocationListener() {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            if (bdLocation!=null){
//                latitude = String.valueOf(bdLocation.getLatitude());
//                longitude = String.valueOf(bdLocation.getLongitude());
//                service.stop();
////                showTips("纬度："+latitude+"  经度："+longitude+" \n地址："+bdLocation.getAddrStr());
//                new LoginUserTask().execute(merPhone,Common.PAYSDK_PASSWORD);
//            }
//        }
//    };

    public static double decimal1(Integer place, double result) {
        BigDecimal b = new BigDecimal(result);
        // 舍入远离零的舍入模式。
        double df = b.setScale(place, BigDecimal.ROUND_UP).doubleValue();
        return df;
    }

    public static float decimal1(Integer place, float result) {
        BigDecimal b = new BigDecimal(result);
        // 舍入远离零的舍入模式。
        float df = b.setScale(place, BigDecimal.ROUND_UP).floatValue();
        return df;
    }

    public static float decimal1(Integer place, float result,int roundMode) {
        BigDecimal b = new BigDecimal(result);
        // 舍入远离零的舍入模式。
        float df = b.setScale(place, roundMode).floatValue();
        return df;
    }

    public static double decimal1(Integer place, double result,int roundMode) {
        BigDecimal b = new BigDecimal(result);
        // 舍入远离零的舍入模式。
        double df = b.setScale(place, roundMode).doubleValue();
        return df;
    }

    private EnumClass.TYPE_OPEMODE judgeOpeMode(String opeMode) {
        if ("nfc".equals(opeMode)) {
            return EnumClass.TYPE_OPEMODE.TYPE_NFC;
        } else {
            return EnumClass.TYPE_OPEMODE.TYPE_NORMAL;
        }
    }
}
