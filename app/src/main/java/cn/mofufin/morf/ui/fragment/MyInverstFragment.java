package cn.mofufin.morf.ui.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions.RxPermissions;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.InverstMentRecordActivity;
import cn.mofufin.morf.ui.mine.CashWithdrawalActivity;
import cn.mofufin.morf.ui.mine.RechargeToBalanceActivity;
import cn.mofufin.morf.ui.ProductAboutUsActivity;
import cn.mofufin.morf.ui.ProductNoticeActivity;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.databinding.FragmentMyinverstBinding;
import rx.functions.Action1;

/**
 * 我的
 */
public class MyInverstFragment extends BaseFragment {
    private FragmentMyinverstBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_myinverst,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        RxBus.getInstance().post(RxEvent.FINANCIAL_REFRESH_STATEBAR_COLOR,2);
    }

    //通知消息
    public void notice(View view){
        startActivity(new Intent(getActivity(), ProductNoticeActivity.class));
    }


    //关于我们
    public void aboutus(View view){
        startActivity(new Intent(getActivity(), ProductAboutUsActivity.class));
    }

    public void back(View view){
        getActivity().finish();
    }

    //投资记录
    public void inverstRecord(View view){
        startActivity(new Intent(getActivity(), InverstMentRecordActivity.class));
    }


    //提现
    public void cashwithdrawal(View view){
        Intent intent = new Intent(getActivity(), CashWithdrawalActivity.class);
        startActivity(intent);
    }

    //充值
    public void charge(View view){
        Intent intent = new Intent(getActivity(), RechargeToBalanceActivity.class);
        startActivity(intent);
    }

    //客服
    public void call(View view){
        new RxPermissions(getActivity())
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
                            DataUtils.TipsDailog(getActivity(), Common.SERVICE_NUMBER, getString(R.string.cancel)
                                    , "呼叫客服", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            Uri data = Uri.parse("tel:" + Common.SERVICE_NUMBER);
                                            intent.setData(data);
                                            startActivity(intent);
                                        }
                                    });
                        }else {
                            DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_call));
                        }
                    }
                });

    }


    //刷新商户信息
//    public void updateMerchantInfo(){
//        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
//        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
//                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
//                bean.merchantPhone,bean.merchantCode)
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
//                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
//                    @Override
//                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
//                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
//                        MerchanInfoDB.updateMerchanInfo(bean);
//
//                        Intent intent = new Intent(getActivity(), CashWithdrawalActivity.class);
//                        intent.putExtra(IntentParam.AMOUNT_AVAILABLE,String.valueOf(bean.getRMB()));
//                        startActivity(intent);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        onError(throwable,true);
//                    }
//                });
//        rxManager.add(subscription);
//    }

}
