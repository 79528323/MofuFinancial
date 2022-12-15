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

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.FinancialActivity;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.BackpackActivity;
import cn.mofufin.morf.ui.mine.BalanceActivity;
import cn.mofufin.morf.ui.mine.CardManagerActivity;
import cn.mofufin.morf.ui.mine.CommissionActivity;
import cn.mofufin.morf.ui.mine.MemberInfoActivity;
import cn.mofufin.morf.ui.mine.MofuCoinActivity;
import cn.mofufin.morf.ui.mine.MoreActivity;
import cn.mofufin.morf.ui.mine.MyInfoActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentMineBinding;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {
    private FragmentMineBinding binding;
    private static final int REAL_NAME=0x01;
    private static final int CARD_MANAGER=0x02;
    private static final int COMMISSION=0x03;
    private static final int BALANCE=0x04;
    private static final int PUBLIC=0x05;
    private static final int INTEGRAL=0x06;
    private static final int USER_INFO=0x07;
    private static final int HELP_CENTER=0x08;
    private static final int MORE=0x09;
    private static final int MOFU_COIN=0x10;
    private static final int MEMBER=0x11;
    private static final int BACKPACK=0x12;
    private static final int MOFU_FININAL=0x13;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mine,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
//        binding.setReal(1);
//        binding.setType(1);
        rxManager.onRxEvent(RxEvent.REFRESH_USER_MINE_INFO)
                .subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                binding.refreshLayout.autoRefresh();
            }
        });

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                updateMerchantInfo();
            }
        });

        binding.refreshLayout.setEnableLoadMore(false);
//        BezierRadarHeader header = (BezierRadarHeader) binding.refreshLayout.getRefreshHeader();

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_USER_MINE_INFO);

        rxManager.onRxEvent(RxEvent.GOTO_MINE_NOTIFY_ENTRANCE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                int pos = (int) o;
                if (pos == 2){
                    mofuCoin(null);
                }else if (pos == 3){
                    commission(null);
                }else if (pos == 4){
                    balance(null);
                }else if (pos == 5){
                    backpack(null);
                }else if (pos == 6){
                    cardmanager(null);
                }else {
                    userinfo(null);
                }
            }
        });
    }



    public void Public(View view){
        PublicDialogFragment publicDialogFragment = new PublicDialogFragment();
        publicDialogFragment.show(getActivity().getSupportFragmentManager(),"PublicDialogFragment");
    }

    public void member(View view){
        selection(MEMBER);
    }

    public void realName(View view){
        selection(REAL_NAME);
    }

    public void more(View view){
        startActivity(new Intent(getActivity(), MoreActivity.class));
    }

    public void commission(View view){
        selection(COMMISSION);
    }

    public void balance(View view){
        selection(BALANCE);
    }

    public void integral(View view){
        selection(INTEGRAL);
    }

    public void userinfo(View view){
        selection(USER_INFO);
    }

    public void cardmanager(View view){
        selection(CARD_MANAGER);
    }

    public void backpack(View view){
        selection(BACKPACK);
    }

    public void finanal(View view){
        selection(MOFU_FININAL);
    }

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

    public void mofuCoin(View view){
        selection(MOFU_COIN);
    }


    //刷新商户信息
    public void updateMerchantInfo(){
        DataUtils.refreshMerchantInfo(getRxManager(), new OnRefreshMerchantInfoListener() {
            @Override
            public void onSuccess(User.DataBean user) {
                User.DataBean.MerchantInfoBean bean = user.getMerchantInfo();
                MerchanInfoDB.updateMerchanInfo(bean);

                binding.setMerchantInfo(bean);
                binding.setReal(bean.realName);
                binding.setType(bean.memberType);
                binding.refreshLayout.finishRefresh();
            }

            @Override
            public void onErrors(Throwable throwable) {
                binding.refreshLayout.finishRefresh();
            }

            @Override
            public void onToast(String msg) {
                binding.refreshLayout.finishRefresh();
            }
        });

    }

    public void selection(int flag){
        switch (flag){
            case MEMBER:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), MemberInfoActivity.class));
                }
                break;

            case REAL_NAME:
                isRealName();
                break;

            case CARD_MANAGER:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), CardManagerActivity.class));
                }
                break;

            case COMMISSION:
//                DataUtils.comingSoon(getActivity());
                if (isRealName()){
                    startActivity(new Intent(getActivity(), CommissionActivity.class));
                }
                break;

            case BALANCE:
//                DataUtils.comingSoon(getActivity());
                if (isRealName()){
                    startActivity(new Intent(getActivity(), BalanceActivity.class));
                }
                break;

            case USER_INFO:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), MyInfoActivity.class));
                }
                break;

            case INTEGRAL:
                DataUtils.comingSoon(getActivity());
                break;

            case MOFU_COIN:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), MofuCoinActivity.class));
                }
                break;

            case BACKPACK:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), BackpackActivity.class));
                }
                break;

            case MOFU_FININAL:
                if (isRealName()){
                    startActivity(new Intent(getActivity(), FinancialActivity.class));
                }
                break;
        }
    }


    public boolean isRealName(){
        boolean isReal = MerchanInfoDB.queryInfo().realName!=1?false:true;
        String msg;
        if (!isReal){
            int realname = MerchanInfoDB.queryInfo().realName;
            if (realname == 3){
                msg = getString(R.string.not_validate);
            }else if (realname == 2){
                msg = getString(R.string.review_nopass);
            }else {
                msg = "";
            }
            new TipsDialog(getContext(), msg, getString(R.string.realname), getString(R.string.cancel),
                    new TipsDialog.OnButtonClickListener() {
                        @Override
                        public void buttonClicked() {
                            DataUtils.jumpToRealName(getActivity());
                        }
                    }).show();
        }
        return isReal;
    }

}
