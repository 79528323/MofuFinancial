package cn.mofufin.morf.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.HomesPagerAdapter;
import cn.mofufin.morf.contract.OrderContract;
import cn.mofufin.morf.presenter.OrderPresenter;
import cn.mofufin.morf.ui.AnimationTreeActivity;
import cn.mofufin.morf.ui.H5PayActivity;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.MaskActivity;
import cn.mofufin.morf.ui.MofuLoanChanceActivity;
import cn.mofufin.morf.ui.MposChanceActivity;
import cn.mofufin.morf.ui.OverseasChancesActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.CreditApplyActivity;
import cn.mofufin.morf.ui.dailog.LoadingDialog;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.home.InvitationActivity;
import cn.mofufin.morf.ui.loan.MofuLoanHomeActivity;
import cn.mofufin.morf.ui.mine.DepositActivity;
import cn.mofufin.morf.ui.MofuMallActivity;
import cn.mofufin.morf.ui.OverseasActivity;
import cn.mofufin.morf.ui.ScanQRReceiVablesActivity;
import cn.mofufin.morf.ui.ScanQRSubmitActivity;
import cn.mofufin.morf.ui.SelectionChannelActivity;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Banner;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.repayment.AboutRepayMentActivity;
import cn.mofufin.morf.ui.repayment.RepayMentManagerActivity;
import cn.mofufin.morf.ui.repayment.RepayMentProjectDetailsActivity;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.CalendarDialog;
import cn.mofufin.morf.ui.widget.FinancialUpgradeDialog;
import cn.mofufin.morf.ui.widget.TipsDialog;
import cn.mofufin.morf.databinding.FragmentHomeBinding;
import cn.mofufin.morf.ui.widget.TipsHtmlDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import cn.mofufin.morf.ui.entity.User.DataBean.*;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements OrderContract.View{
    private static final int TYPE_MPOS=1;
    private static final int TYPE_SCAN=2;
    private static final int TYPE_SHORT=3;
    private static final int TYPE_OVER=4;
    private static final int TYPE_REPAY=5;
    private FragmentHomeBinding binding;
    private int[] res= {R.drawable.mofu_banner,R.drawable.happynewyear_banner,R.drawable.over_banner};
    private HomesPagerAdapter adapter =null;
    private OrderPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        binding.scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
        rxManager.onRxEvent(RxEvent.SUBMISSION_MPOS, new Action1<Object>() {
            @Override
            public void call(Object o) {
                String code = (String) o;
                subMissionMPos(code);
            }
        });

        rxManager.onRxEvent(RxEvent.SUBMISSION_SCAN_QR, new Action1<Object>() {
            @Override
            public void call(Object o) {
               Intent intent = new Intent(getActivity(),ScanQRSubmitActivity.class);
               startActivity(intent);
            }
        });

        rxManager.onRxEvent(RxEvent.HOME_HORSE_LANTERN, new Action1<Object>() {
            @Override
            public void call(Object o) {
                String content = (String) o;
                binding.marquee.setText(content);
                toastTipDailog(content);
            }
        });

        rxManager.onRxEvent(RxEvent.GOTO_NOTIFY_ENTRANCE, new Action1<Object>() {
            @Override
            public void call(Object o) {
                int pos = (int) o;
                if (pos == 14){
                    MPOS(null);
                }else if (pos == 15){
                    shortReceiVables(null);
                }else if (pos == 16){
                    businessReceiVables(null);
                }else if (pos == 17){
                    OverSeansReiceiVables(null);
                }else if (pos == 9){
                    integral(null);
                }
            }
        });
    }

    public void initView(){
        binding.maskLinear.setVisibility(
                inTimeFrame("2020-03-25 00:00:00","2020-04-30 23:59:59")?View.GONE:View.VISIBLE);
        presenter = new OrderPresenter(this);
        Subscription subscription = UtilsImpAPI.getRotationChart(HttpParam.ROTATION_CHART_KEY,HttpParam.OFFICE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(BaseResponse<GeneralResponse.DataBean> response) {
                        if (response.bool){
                            List<Banner> viewList = new ArrayList<>();
                            List<GeneralResponse.DataBean.RcBean> rcBeanList = response.data.rc;
                            if (rcBeanList.size() >0){
                                for (int i=0; i<rcBeanList.size(); i++){
                                    viewList.add(new Banner(rcBeanList.get(i).imgAddress,rcBeanList.get(i).hrefAddress));
                                }

                                adapter = new HomesPagerAdapter(getContext(),viewList);
                                adapter.setClickListener(clickListener);
                                binding.homeViewpager.setAdapter(adapter);

                                //获取指定MPOS浮标位置
                                binding.homeTabLinear.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        int height = binding.overBtn.getMeasuredHeight();
                                        int tabheight = (int) getResources().getDimension(R.dimen.title_height);
//                                        Logger.e("height="+height);
                                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.maskLinear.getLayoutParams();
                                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                        params.topMargin = height + tabheight;
                                        binding.maskLinear.setLayoutParams(params);
                                    }
                                });
                            }else {
                                binding.homeViewpager.setVisibility(View.GONE);
                            }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 快捷收款
     * @param view
     */
    public void shortReceiVables(View view){
        updateMerchantInfo(TYPE_SHORT);
    }

    /**
     * MPOS
     * @param view
     */
    public void MPOS(View view){
//        updateMerchantInfo(TYPE_MPOS);

        String str = "摩富MPOS因合作方卡友支付业务模式调整，摩富金服APP的MPOS收款功能迁移至卡友支付新版信付通APP。\n" +
                "\n" +
                "信付通安卓版APP下载地址：\n" +
                "http://xft.enjoyfin.com/xft/download.html\n" +
                "\n" +
                "操作流程请看此链接：https://w.url.cn/s/A0VNbew\n" +
                "\n" +
                "【重要提醒】\n" +
                "下载新的app后，客户原账号不变，继续使用摩富金服APP账号（即登陆手机号）登陆，首次登陆自行重置密码即可。";
        new TipsHtmlDialog(getActivity(), str, getString(R.string.confirm),
                null,
                null).show();
    }

    /**
     * 扫码收款
     * @param view
     */
    public void businessReceiVables(View view){
        updateMerchantInfo(TYPE_SCAN);
    }


    /**
     * 境外收款
     * @param view
     */
    public void OverSeansReiceiVables(View view){
//        DataUtils.comingSoon(getActivity());
        updateMerchantInfo(TYPE_OVER);
    }

    /**
     * 信用卡还款
     * @param view
     */
    public void creditRepay(View view){
        DataUtils.comingSoon(getActivity());
    }

    /**
     * 摩富理财
     * @param view
     */
    public void mofuFinancing(View view){
        new FinancialUpgradeDialog(getActivity()).showDialog();
    }


    /**
     *
     * @param view
     */
    public void vehicleCheck(View view){
        DataUtils.comingSoon(getActivity());
    }


    LoadingDialog loadingDialog;
    /**
     * 商城
     * @param view
     */
    public void mall(View view){
        startActivity(new Intent(getActivity(), MofuMallActivity.class));
    }


    /**
     * 转账
     * @param view
     */
    public void transfer(View view){
        DataUtils.comingSoon(getActivity());
    }


    /**
     * 自己借 (摩富树)
     * @param view
     */
    public void ownBorrow(View view){
        startActivity(new Intent(getActivity(), MofuLoanHomeActivity.class));
//        startActivity(new Intent(getActivity(), MofuLoanChanceActivity.class));
    }


    public void tree(View view){
        startActivity(new Intent(getActivity(), AnimationTreeActivity.class));
    }

    /**
     * 延期还款
     * @param view
     */
    public void integral(View view){
        updateMerchantInfo(TYPE_REPAY);
    }


    /**
     * 邀请好友
     * @param view
     */
    public void invitationFriend(View view){
        startActivity(new Intent(getActivity(), InvitationActivity.class));
    }

    /**
     * 申请信用卡
     * @param view
     */
    public void applyCreditCard(View view){
        startActivity(new Intent(getActivity(), CreditApplyActivity.class));
    }

    /**
     * 关闭浮标
     * @param view
     */
    public void closeMpos(View view){
        binding.floatLinear.setVisibility(View.GONE);
    }

    /**
     * 押金
     * @param view
     */
    public void deposit(View view){
        startActivity(new Intent(getActivity(), DepositActivity.class));
    }


    /**
     *
     * @param view
     */
    public void getMask(View view){
        presenter.refreshData("2020-03-15","2020-04-15",3);
    }


    public void closeMask(View view){
        binding.maskLinear.setVisibility(View.GONE);
    }


    public void marquees(View view){
        TextView textView = (TextView) view;
        toastTipDailog(textView.getText().toString());
    }

    /**
     * 通知弹窗
     * @param str
     */
    public void toastTipDailog(String str){
        if (!TextUtils.isEmpty(str)){
            new TipsDialog(getActivity(), str, getString(R.string.confirm),
                    null,
                    null).show();
        }
    }

    //更新商户信息
    public void updateMerchantInfo(final int flag){
//        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        DataUtils.refreshMerchantInfo(getRxManager(), new OnRefreshMerchantInfoListener() {
            @Override
            public void onSuccess(User.DataBean user) {
                User.DataBean.MerchantInfoBean merchantInfo = user.getMerchantInfo();
                MerchanInfoDB.updateMerchanInfo(merchantInfo);
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_USER_MINE_INFO);
                int real = merchantInfo.realName;
                switch (real){//TODO  0:待审核 1:审核通过 2:审核未通过 3:未上传认证信息
                    case 0:
                        new TipsDialog(getContext(),"正在审核中"
                                ,getString(R.string.ok),null,null).show();
                        break;


                    case 1:
                        switch (flag){
                            case TYPE_MPOS:
                                userfuncReview(flag,merchantInfo);
                                break;

                            case TYPE_SCAN:
                                userfuncReview(flag,merchantInfo);
                                break;

                            case TYPE_SHORT:
                                startActivity(new Intent(getActivity(),SelectionChannelActivity.class));
                                break;

                            case TYPE_OVER:
                                startActivity(new Intent(getActivity(), OverseasChancesActivity.class));
                                break;

                            case TYPE_REPAY:
                                startActivity(new Intent(getActivity(),RepayMentManagerActivity.class));
                                break;

                        }
                        break;


                    case 2:
                        goRealName(real);
                        break;

                    case 3:
                        goRealName(real);
                        break;
                }
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

    public void subMissionMPos(String code){
        Subscription subscription = SubMissionImpAPI.connectionMPos(
                HttpParam.OFFICE_CODE,HttpParam.SUBMISSION_MPOS_APPKEY,code)
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
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        DataUtils.realNameTipsDailog(getContext(),mofuResult.result_Msg,getString(R.string.ok),"");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    /**
     * 进件返回信息判断
     * @param type
     * @param bean
     */
    public void userfuncReview(final int type ,final MerchantInfoBean bean){
        String content ="",confirm=getString(R.string.ok);
        final int feedPart = (type == TYPE_SCAN?bean.remitPayFeedPart:bean.mposFeedPart);

        switch (feedPart){//0:进件失败 1:进件成功 2:申请进件 3:正在审核 4:未进件
            case 0:
                content =getString(R.string.review_nopass);
                confirm=getString(R.string.review_apply);
                break;
            case 1:
                Intent intent = null;
                if (type == TYPE_SCAN){
                    intent = new Intent(getActivity(), ScanQRReceiVablesActivity.class);
                    intent.putExtra(IntentParam.QR_TYPE, ScanQRReceiVablesActivity.QR_CODE_STATIC);
                }else {
                    intent = new Intent(getActivity(), MposChanceActivity.class);
                }
                startActivity(intent);
                return;
            case 2:
                content=getString(R.string.review_remit);
                break;
            case 3:
                content=getString(R.string.review_submited);
                break;
            case 4:
                if (type == TYPE_SCAN){
                    content="你还没开通扫码功能";
                }else
                    content="你还没开通MPOS功能";

                confirm=getString(R.string.review_apply);
                break;
        }

        new TipsDialog(getContext(), content, confirm, getString(R.string.cancel),
                new TipsDialog.OnButtonClickListener() {
                    @Override
                    public void buttonClicked() {
                        if (feedPart == 4 || feedPart == 0){
                            if (type == TYPE_SCAN)
                                RxBus.getInstance().post(RxEvent.SUBMISSION_SCAN_QR,null);
                            else
                                RxBus.getInstance().post(RxEvent.SUBMISSION_MPOS,bean.merchantCode);
                        }
                    }
                }).show();

    }

    //未审核过关，弹窗显示
    public void goRealName(final int type){
        String msg = type==2?getString(R.string.review_nopass):
                type==3?getString(R.string.not_validate):"";
        new TipsDialog(getContext(), msg
                ,getString(R.string.realname), getString(R.string.cancel),
                new TipsDialog.OnButtonClickListener() {
                    @Override
                    public void buttonClicked() {
                        DataUtils.jumpToRealName(getContext());
                    }
                }).show();
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Banner banner = adapter.getItemData(position);
//            String href = (String) v.getTag();
            Intent intent = new Intent(getActivity(),WebActivity.class);
            intent.putExtra("HTML",banner.href);
            intent.putExtra(IntentParam.TITLE,"");
            startActivity(intent);
        }
    };




    //刷新商户信息
//    public void getMerchantInfo(){
//        DataUtils.refreshMerchantInfo(getRxManager(), new OnRefreshMerchantInfoListener() {
//            @Override
//            public void onSuccess(User.DataBean user) {
//                User.DataBean.MerchantInfoBean bean = user.getMerchantInfo();
//                MerchanInfoDB.updateMerchanInfo(bean);
//
////                String creatime = User.DataBean.MerchantInfoBean.
//            }
//
//            @Override
//            public void onErrors(Throwable throwable) {
//                showTips("查找商户信息失败");
//            }
//
//            @Override
//            public void onToast(String msg) {
//                showTips(msg);
//            }
//        });
//
//    }

    @Override
    public void onDataSuccess(Order order) {

        User.DataBean.MerchantInfoBean merchantInfoBean = MerchanInfoDB.queryInfo();
        StringBuilder builder = new StringBuilder("http://appweb.mofufin.com/maskApp/pages/receiveIndex.html?");
        builder.append("realnameAuth="+merchantInfoBean.realName)
                .append("&").append("amount="+order.totalMoney)
                .append("&").append("realname="+merchantInfoBean.fdMerIdentityCardName)
                .append("&").append("phone="+merchantInfoBean.merchantPhone);

        Intent intent = new Intent(getActivity(),WebActivity.class);
        intent.putExtra("HTML",builder.toString());
        intent.putExtra(IntentParam.TITLE,"口罩领取");
        startActivity(intent);
    }

    @Override
    public void onDataFail(String tips) {

    }


    /**
     * 是否在免费领取口罩日期内 (2020-03-15至2020-04-15)
     * @return
     */
    public boolean inTimeFrame(String minDate,String maxDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String registerDate = MerchanInfoDB.queryInfo().merchantDateTime;
        Date min =null ,max =null ,cur=null;
        try {
            min = sdf.parse(minDate);
            max = sdf.parse(maxDate);
            cur = sdf.parse(registerDate);
            if (cur.getTime() > min.getTime() && cur.getTime() < max.getTime()){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
