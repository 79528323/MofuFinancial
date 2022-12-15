package cn.mofufin.morf.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.databinding.FragmentHomeBinding;
import cn.mofufin.morf.databinding.FragmentMaskHomeBinding;
import cn.mofufin.morf.ui.AnimationTreeActivity;
import cn.mofufin.morf.ui.CreditApplyActivity;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.MofuMallActivity;
import cn.mofufin.morf.ui.MposChanceActivity;
import cn.mofufin.morf.ui.OverseasChancesActivity;
import cn.mofufin.morf.ui.ScanQRReceiVablesActivity;
import cn.mofufin.morf.ui.ScanQRSubmitActivity;
import cn.mofufin.morf.ui.SelectionChannelActivity;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.dailog.LoadingDialog;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Banner;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.entity.User.DataBean.MerchantInfoBean;
import cn.mofufin.morf.ui.home.InvitationActivity;
import cn.mofufin.morf.ui.loan.MofuLoanHomeActivity;
import cn.mofufin.morf.ui.mine.DepositActivity;
import cn.mofufin.morf.ui.repayment.RepayMentManagerActivity;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.FinancialUpgradeDialog;
import cn.mofufin.morf.ui.widget.TipsDialog;
import cn.mofufin.morf.ui.widget.TipsHtmlDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 首页
 */
public class MaskHomeFragment extends BaseFragment {
    private FragmentMaskHomeBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mask_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void back(View view){

    }


}
