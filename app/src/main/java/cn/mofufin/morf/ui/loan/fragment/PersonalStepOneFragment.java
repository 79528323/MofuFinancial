package cn.mofufin.morf.ui.loan.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.FragmentPersonalStepOneBinding;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.loan.LoanPersonalActivity;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.CityPicker;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 个人资料1页
 */
public class PersonalStepOneFragment extends BaseFragment {
    private FragmentPersonalStepOneBinding binding;
    private CityPicker cityPicker;
    private String regProvId,regCityId,regAreaId;
    private TimePickerView timePickerView;
    private String selectTag="";
    private String proCodes="",cityCodes="";

    public static PersonalStepOneFragment newInstance(){
        return new PersonalStepOneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_step_one,container,false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(){
        binding.setOnClickListener(onClickListener);
        binding.setIsLongrange(true);

        Bundle bundle = getArguments();
        final SelfTextInfo.MerSelfTextBean bean = bundle.getParcelable(IntentParam.BEAN);
        setViewHolder(bean);
        if (bean != null) {
            if (TextUtils.isEmpty(bean.debitCardCode))
                updateMerchantInfo();
        }else
            updateMerchantInfo();
    }

    public void SelectProCity(final View view){
        initCityPick();
        cityPicker.show();
    }

    public void selectDate(View view){
        initTimePick();
        if (timePickerView.isShowing()){
            timePickerView.dismiss();
        }else{
            selectTag = (String) view.getTag();
            timePickerView.setTitle(selectTag.equals("birthDay")?
                    "出生日期":selectTag.equals("issue")?"签发日期":"到期日期");
            timePickerView.show();
        }
    }

    public void next(View view){
        if (TextUtils.isEmpty(binding.cardNum.getText().toString())){
            showTips("请填写借记卡");
            return;
        }else if (TextUtils.isEmpty(binding.providerCity.getText().toString())){
            showTips("请选择常住省市");
            return;
        }else if (TextUtils.isEmpty(binding.issue.getText().toString())){
            showTips("请填写身份证签发日期");
            return;
        }else if (!binding.getIsLongrange()){
            if (TextUtils.isEmpty(binding.expiry.getText().toString())){
                showTips("请填写身份证到期日期");
                return;
            }
        }else if (TextUtils.isEmpty(binding.birthDay.getText().toString())){
            showTips("请填写出生日期");
            return;
        }else if (TextUtils.isEmpty(binding.email.getText().toString())){
            showTips("请填写邮箱");
            return;
        }else if (TextUtils.isEmpty(binding.QQ.getText().toString())){
            showTips("请填写QQ号");
            return;
        }else if (TextUtils.isEmpty(binding.wechat.getText().toString())){
            showTips("请填写微信号");
            return;
        }

        LoanPersonalActivity.params.put("debitCardCode",binding.cardNum.getText().toString());
        LoanPersonalActivity.params.put("mtCityCdName",regCityId);
        LoanPersonalActivity.params.put("mtCityCd",cityCodes);
        LoanPersonalActivity.params.put("mtStateCd",proCodes);
        LoanPersonalActivity.params.put("mtStateCdName",regProvId);
        LoanPersonalActivity.params.put("dtIssue",binding.issue.getText().toString());
        LoanPersonalActivity.params.put("isLongEffec",binding.getIsLongrange()?"Y":"N");
        if (!binding.getIsLongrange())
            LoanPersonalActivity.params.put("dtExpiry",binding.expiry.getText().toString());
        LoanPersonalActivity.params.put("dtRegistered",binding.birthDay.getText().toString());
        LoanPersonalActivity.params.put("email",binding.email.getText().toString());
        LoanPersonalActivity.params.put("qq",binding.QQ.getText().toString());
        LoanPersonalActivity.params.put("weChat",binding.wechat.getText().toString());
        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,2);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag=Integer.parseInt((String)v.getTag());
            binding.setIsLongrange(tag>0?true:false);
        }
    };

    //数据回读
    public void setViewHolder(final SelfTextInfo.MerSelfTextBean bean){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bean!=null){
                    binding.wechat.setText(bean.weChat);
                    binding.QQ.setText(bean.qq);
                    binding.email.setText(bean.email);
                    String dtRegistered = bean.dtRegistered;
                    binding.birthDay.setText(TextUtils.isEmpty(dtRegistered)?"":dtRegistered.substring(0,10));

                    regProvId=bean.mtStateCdName;
                    regCityId=bean.mtCityCdName;
                    if (!TextUtils.isEmpty(regProvId)&&!TextUtils.isEmpty(regCityId))
                        binding.providerCity.setText(regProvId+" "+regCityId);

                    proCodes = bean.mtStateCd;
                    cityCodes = bean.mtCityCd;
                    binding.cardNum.setText(bean.debitCardCode);

                    String issue = bean.dtIssue;
                    binding.issue.setText(TextUtils.isEmpty(issue)?"":issue.substring(0,10));

                    if (!TextUtils.isEmpty(bean.isLongEffec))
                        binding.cardGroup.check(bean.isLongEffec.equals("Y")?R.id.radio1:R.id.radio2);

                    if (TextUtils.isEmpty(bean.isLongEffec))
                        binding.setIsLongrange(false);
                    else
                        binding.setIsLongrange(bean.isLongEffec.equals("Y")?true:false);

                    if (!binding.getIsLongrange()){
                        String dtExpiry = bean.dtExpiry;
                        binding.expiry.setText(TextUtils.isEmpty(dtExpiry)?"":dtExpiry.substring(0,10));
                    }
                }
            }
        });

    }

    //刷新商户信息
    public void updateMerchantInfo(){
        DataUtils.refreshMerchantInfo(getRxManager(), new OnRefreshMerchantInfoListener() {
            @Override
            public void onSuccess(User.DataBean user) {
                List<User.DataBean.CardInfoBean> list = user.getCardInfo();
                if (list.size()>0)
                    binding.cardNum.setText(list.get(0).cardCode);
            }

            @Override
            public void onErrors(Throwable throwable) {
                onError(throwable,true);
            }

            @Override
            public void onToast(String msg) {

            }
        });
    }


    @Override
    public void onStop() {
        LoanPersonalActivity.params.put("debitCardCode",binding.cardNum.getText().toString());
        LoanPersonalActivity.params.put("mtCityCdName",regCityId);
        LoanPersonalActivity.params.put("mtCityCd",cityCodes);
        LoanPersonalActivity.params.put("mtStateCd",proCodes);
        LoanPersonalActivity.params.put("mtStateCdName",regProvId);
        LoanPersonalActivity.params.put("dtIssue",binding.issue.getText().toString());
        LoanPersonalActivity.params.put("isLongEffec",binding.getIsLongrange()?"Y":"N");
        if (!binding.getIsLongrange())
            LoanPersonalActivity.params.put("dtExpiry",binding.expiry.getText().toString());
        LoanPersonalActivity.params.put("dtRegistered",binding.birthDay.getText().toString());
        LoanPersonalActivity.params.put("email",binding.email.getText().toString());
        String qq = binding.QQ.getText().toString();
        LoanPersonalActivity.params.put("qq",qq);
        LoanPersonalActivity.params.put("weChat",binding.wechat.getText().toString());
        super.onStop();
    }

    public void initCityPick(){
        if (cityPicker==null){
            cityPicker = new CityPicker(getActivity(),true);
            cityPicker.setTitle("选择省市");
            cityPicker.listener = new CityPicker.OnSelectListener() {
                @Override
                public void Select(String s1, String s2, String s3) {
                    regProvId=s1;
                    regCityId=s2;
//                regAreaId=s3;
                    binding.providerCity.setText(regProvId +" "+regCityId);
                }
            };
            cityPicker.setCoderListener(new CityPicker.onCoderListener() {
                @Override
                public void getCode(String Procode, String cityCode) {
                    proCodes = Procode;
                    cityCodes = cityCode;
                }
            });
        }
    }

    public void initTimePick(){
        if (timePickerView==null){
            timePickerView = new TimePickerView(getActivity(),TimePickerView.Type.YEAR_MONTH_DAY);
            timePickerView.setRange(1900,2100);
            timePickerView.setTime(new Date());
            timePickerView.setCyclic(false);
            timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    String dates = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    if (TextUtils.equals(selectTag,"birthDay")){
                        binding.birthDay.setText(dates);
                    }else if (TextUtils.equals(selectTag,"issue")){
                        binding.issue.setText(dates);
                    }else if (TextUtils.equals(selectTag,"limit")){
                        binding.expiry.setText(dates);
                    }
                }
            });
        }
    }
}
