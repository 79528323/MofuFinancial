package cn.mofufin.morf.ui;

import android.content.res.AssetManager;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hope.paysdk.NetInterface;
import com.hope.paysdk.PaySdkEnvionment;
import com.hope.paysdk.framework.EnumClass;
import com.hope.paysdk.framework.IndustryInfo;
import com.hope.paysdk.framework.beans.Industry;
import com.hope.paysdk.framework.beans.User;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.adapter.MposIndustryAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BusinessModel;
import cn.mofufin.morf.ui.entity.MposRatio;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.contract.MposIndustryContract;
import cn.mofufin.morf.databinding.ActivityMposIndustryBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 //TYPE_GETFUND_CUSTOM_MERCHANT_D0
 */
public class MPosIndustryActivity extends BaseActivity{
    private static final String PAY_JSON_UNION="industrybizcode_unionPay.json";
    private static final String PAY_JSON_NORMAL="industrybizcode.json";
    private ActivityMposIndustryBinding binding;
    private MposIndustryAdapter adapter;
    private float sum = 0;
    private MposIndustryContract.Presenter presenter;
    private LinearLayoutManager layoutManager;
    private int quota = 0;
    private Industry industry;
    private String token;
    private final static String TYPE_ARRY_CODE = EnumClass.TYPE_ARRIVE.TYPE_ARRIVE_D0.name();
    private String ratioQueryType;
    private String memberId;
    private String merPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mpos_industry);
        binding.setHandlers(this);

        merPhone = MerchanInfoDB.queryInfo().merchantPhone;
        double sum = getIntent().getDoubleExtra(IntentParam.AMOUNT,0);
        String s = String.valueOf(sum);
        this.sum = Float.valueOf(s);

        String mode = getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE);
        if (TextUtils.equals(mode,"nfc")){
            ratioQueryType = "1";
        }else
            ratioQueryType = "0";

        token = getIntent().getStringExtra(IntentParam.PAY_TOKEN);
        binding.setAmount(DataUtils.numFormat(sum));

        adapter = new MposIndustryAdapter();
        adapter.setClickListener(clickListener);

        layoutManager = new LinearLayoutManager(this);
        binding.industryList.setLayoutManager(layoutManager);
        binding.industryList.setAdapter(adapter);

        login();
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }


    public void getIndustryList(final String fileName){
        Subscription subscription = UserImpAPI.queryMerMposRatio(HttpParam.OFFICE_CODE,
                HttpParam.QUERY_MPOS_INDUSTRY_LIST_KEY, MerchanInfoDB.queryInfo().merchantCode,
                Common.LOAN_VERSION, ratioQueryType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MposRatio>() {
                    @Override
                    public void call(MposRatio mposRatio) {
                        List<BusinessModel> datas = new ArrayList<>();

                        AssetManager manager = getAssets();
                        String result = null;
                        try {
                            InputStream stream = manager.open(fileName,AssetManager.ACCESS_BUFFER);
                            byte[] b = new byte[stream.available()];
                            stream.read(b);
                            result = new String(b);

                            Gson gson = new Gson();
                            List<BusinessModel> businessModelList =
                                    gson.fromJson(result,new TypeToken<List<BusinessModel>>(){}.getType());
                            Iterator<BusinessModel> iterator = businessModelList.iterator();
                            while (iterator.hasNext()){
                                BusinessModel model = iterator.next();
                                if (MerchanInfoDB.queryInfo().memberType==1){
                                    model.setFee1_d0(String.valueOf(mposRatio.ratio));
                                }else if (MerchanInfoDB.queryInfo().memberType==2){
                                    model.setFee2_d0(String.valueOf(mposRatio.ratio));
                                }else {
                                    model.setFee3_d0(String.valueOf(mposRatio.ratio));
                                }
                                model.setExtraCharge(String.valueOf(mposRatio.fee));

                                datas.add(model);
                            }

                            adapter.refresh(datas);
                        } catch (Exception e) {
                            e.printStackTrace();
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float rate = (float) v.getTag();//获取费率
            rate = rate/100;
            float charge = (sum * rate);
            charge += Common.MPOS_CHARGE;
            binding.setCharge(String.valueOf(decimal1(2,charge)));

            float actualCount = sum - charge;
            Logger.e("actualCount="+actualCount);
            binding.setActualCount(String.valueOf(decimal1(2,actualCount,BigDecimal.ROUND_DOWN)));

            ViewGroup group = (ViewGroup) v;
            View layout1 = group.getChildAt(0);
            if (layout1 instanceof ImageView){
                int position = (int) layout1.getTag();
                adapter.notifyItem(position);
                BusinessModel model = adapter.getItemData(position);
                industry = new Industry();
                industry.setPriority(String.valueOf(model.getPriority()));
                industry.setIndustryName(model.getInd_name());
                industry.setIndustryCode(model.getInd_code());
                industry.setImgDesc(model.getImg_desc());
                industry.setBizCode(model.getBiz_code());
            }

            View layout2 = group.getChildAt(1);
            if (layout2 instanceof LinearLayout){
                quota = (int) layout2.getTag();
            }
        }
    };

    public void next(View view){
        if (industry==null){
            showTips("请选择行业");
            return;
        }


        if (sum > quota){
            showTips("请选择相应的金额区间");
        }else {
            if (!TextUtils.isEmpty(token) && industry!=null){
                IndustryInfo industryInfo = new IndustryInfo();
                industryInfo.setIndustryCode(industry.getIndustryCode());
                industryInfo.setIndustryName(industry.getIndustryName());
                industryInfo.setBizCode(industry.getBizCode());

//                PaySdkEnvionment.startPay(MPosIndustryActivity.this
//                        ,DataUtils.createSerialNo()
//                        ,binding.getAmount()
//                        ,EnumClass.TYPE_OPEMODE.TYPE_NFC
//                        ,EnumClass.TYPE_GETFUND.TYPE_GETFUND_CUSTOM_MERCHANT_D0
//                        ,industryInfo
//                        ,null
//                        ,judgeAttachParams()
//                        ,token);
                PaySdkEnvionment.startPay(this,
                        DataUtils.createSerialNo(),
                        binding.getAmount(),
                        judgeOpeMode(getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE)),
                        EnumClass.TYPE_GETFUND.TYPE_GETFUND_CUSTOM_MERCHANT_D0,
                        industryInfo,
                        null,
                        null,
                        judgeAttachParams(),
                        token);
            }
        }
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
                    if (!TextUtils.isEmpty(token)){
//                        presenter.refresh(new String[]{merPhone,TYPE_ARRY_CODE});
                        String fileName;
                        String mode=MPosIndustryActivity.this.getIntent().getStringExtra(IntentParam.PAY_ENNUM_MODE);
                        if (TextUtils.equals(mode,"nfc")){
                            fileName = PAY_JSON_UNION;
                        }else
                            fileName = PAY_JSON_NORMAL;

                        getIndustryList(fileName);
                    }
                    memberId = String.valueOf(ret.getMemberId());
                    Logger.e("token="+token);
                    Logger.e("memberId="+memberId);


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



    private String judgeAttachParams() {
        JSONObject attachParams = new JSONObject();
        attachParams.put("fee_d0", String.valueOf(decimal1(2,Float.valueOf(binding.getCharge()))));
        attachParams.put("fee_t1", String.valueOf(decimal1(2,Float.valueOf(binding.getCharge()))));
//        attachParams.put("fee_d0", "30.57");
//        attachParams.put("fee_t1", "30.57");
        if (attachParams.size() == 0) {
            return null;
        } else {
            return attachParams.toString();
        }
    }

    /**
     * 卡友登录
     */
    public void login(){
        new LoginUserTask().execute(merPhone,Common.PAYSDK_PASSWORD);
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

    private EnumClass.TYPE_OPEMODE judgeOpeMode(String opeMode) {
        if ("nfc".equals(opeMode)) {
            return EnumClass.TYPE_OPEMODE.TYPE_NFC;
        } else {
            return EnumClass.TYPE_OPEMODE.TYPE_NORMAL;
        }
    }
}
