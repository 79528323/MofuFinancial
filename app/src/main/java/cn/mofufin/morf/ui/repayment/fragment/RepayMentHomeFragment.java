package cn.mofufin.morf.ui.repayment.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.BankCardAdapter;
import cn.mofufin.morf.databinding.FragmentCardSettleBinding;
import cn.mofufin.morf.databinding.FragmentRepaymentHomeBinding;
import cn.mofufin.morf.ui.Listener.OnRefreshMerchantInfoListener;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MofuResult;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.entity.RepayMentDay;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.mine.CardManagerActivity;
import cn.mofufin.morf.ui.mine.SettlementCardAddActivity;
import cn.mofufin.morf.ui.mine.UnbindCardActivity;
import cn.mofufin.morf.ui.repayment.ChannelOpeningValidateActivity;
import cn.mofufin.morf.ui.repayment.EditorCardDateActivity;
import cn.mofufin.morf.ui.repayment.GenerationProjectActivity;
import cn.mofufin.morf.ui.repayment.RepayMentChannelActivity;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.services.RepayMentImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.SettlementCardDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 延期
 */
public class RepayMentHomeFragment extends BaseFragment {
    public static final int EDITOR_CARD_DATE=0x33;
    public static final int SELECT_CHANNEL=0x66;
    private FragmentRepaymentHomeBinding binding;
    private int type;
    private BankCardAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SettlementCardDialog dialog;
    private int[] dates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_repayment_home,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        RxBus.getInstance().post(RxEvent.REPAYMENT_TITLE,getString(R.string.repayment_title));
        refreshButton();
        binding.setBean(null);
        binding.setCurType(MerchanInfoDB.queryInfo().memberType);

        dialog = new SettlementCardDialog(getActivity(),1);
        dialog.setListener(new SettlementCardDialog.onSettleCardClickListener() {
            @Override
            public void onClickListener(User.DataBean.CardInfoBean bean) {
                matchBankIcon(bean);
                binding.setBean(bean);
                dates[0]=Integer.valueOf(bean.accountDay);
                dates[1]=Integer.valueOf(bean.repaymentDay);
//                getday(String.valueOf(dates[0]));
                refreshButton();
            }
        });

        dates = new int[2];
        getCreditCard();
    }

    public void next(View view){
        initChannel();
//        Intent intent = new Intent(getActivity(), ChannelOpeningValidateActivity.class);
////        Intent intent = new Intent(getActivity(), GenerationProjectActivity.class);
//        intent.putExtra(IntentParam.TYPE,ChannelOpeningValidateActivity.VALIDATE_CHANNEL);
//        intent.putExtra(IntentParam.BEAN,binding.getChannel());
//        intent.putExtra(IntentParam.BANKCARD_NO,binding.getBean().cardCode);
//        intent.putExtra(IntentParam.REPAY_DAYS,dates);
//        startActivity(intent);
    }

    public void selectCard(View view){
        dialog.showDialog();
    }

    public void editCard(View view){
        if (binding.getBean()==null){
            showTips("请先选择信用卡");
            return;
        }

        Intent intent = new Intent(getActivity(), EditorCardDateActivity.class);
        intent.putExtra(IntentParam.BEAN,binding.getBean());
        intent.putExtra(IntentParam.REPAY_DAYS,binding.rpdays.getText().toString());
        startActivityForResult(intent,EDITOR_CARD_DATE);
    }

    public void addChannel(View view){
        Intent intent = new Intent(getActivity(), RepayMentChannelActivity.class);
        startActivityForResult(intent,SELECT_CHANNEL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.e("onActivityResult");
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == EDITOR_CARD_DATE){
            getCreditCard();
        }else if (requestCode == SELECT_CHANNEL){
            RepayChannel.ChannelListBean bean = data.getParcelableExtra(IntentParam.BEAN);
            binding.setChannel(bean);
        }

        refreshButton();
    }


    public void initChannel(){
        Subscription subscription = RepayMentImpAPI.initRefundChannel(HttpParam.OFFICE_CODE,HttpParam.INIT_REPAY_CHANNEL_KEY,
                MerchanInfoDB.queryInfo().merchantCode,Common.LOAN_VERSION,binding.getBean().cardCode,
                String.valueOf(binding.getChannel().rcNumber))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MofuResult>() {
                    @Override
                    public void call(MofuResult mofuResult) {
                        if (mofuResult.result_Code == 1){
                            showTips(mofuResult.result_Msg);
                            return;
                        }

                        Intent intent = null;
                        switch (mofuResult.result_Code){
                            case 0:
                                intent = new Intent(getActivity(),GenerationProjectActivity.class);
                                break;
//                            case 1:
//                                showTips(mofuResult.result_Msg);
//                                break;
                            case 2:
                            case 3:
                                intent = new Intent(getActivity(),ChannelOpeningValidateActivity.class);
                                intent.putExtra(IntentParam.TYPE,mofuResult.result_Code==2?
                                        ChannelOpeningValidateActivity.VALIDATE_CHANNEL:ChannelOpeningValidateActivity.VALIDATE_CREDIT);
                                break;
                        }

                        intent.putExtra(IntentParam.BEAN,binding.getChannel());
                        intent.putExtra(IntentParam.BANKCARD_NO,binding.getBean().cardCode);
                        intent.putExtra(IntentParam.REPAY_DAYS,dates);
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

    //适配银行图标
    public void matchBankIcon(User.DataBean.CardInfoBean cardInfoBean){
        if (cardInfoBean.cardBankName.contains("平安")){
            binding.cardIcon.setImageResource(R.drawable.logo_pingan);
            binding.bankBg.setBackgroundResource(R.drawable.bank_pingan);

        }else if (cardInfoBean.cardBankName.contains("北京")){
            binding.cardIcon.setImageResource(R.drawable.logo_beijing);
            binding.bankBg.setBackgroundResource(R.drawable.bank_beijing);

        }else if (cardInfoBean.cardBankName.contains("工商")){
            binding.cardIcon.setImageResource(R.drawable.logo_gongshang);
            binding.bankBg.setBackgroundResource(R.drawable.bank_gongshang);

        }else if (cardInfoBean.cardBankName.contains("光大")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangda);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangda);
        }else if (cardInfoBean.cardBankName.contains("广发")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangfa);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangfa);

        }else if (cardInfoBean.cardBankName.contains("华夏")){
            binding.cardIcon.setImageResource(R.drawable.logo_huaxia);
            binding.bankBg.setBackgroundResource(R.drawable.bank_huaxia);

        }else if (cardInfoBean.cardBankName.contains("建设")){
            binding.cardIcon.setImageResource(R.drawable.logo_jianshe);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jianshe);

        }else if (cardInfoBean.cardBankName.contains("交通")){
            binding.cardIcon.setImageResource(R.drawable.logo_jiaotong);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jiaotong);

        }else if (cardInfoBean.cardBankName.contains("民生")){
            binding.cardIcon.setImageResource(R.drawable.logo_minsheng);
            binding.bankBg.setBackgroundResource(R.drawable.bank_minsheng);

        }else if (cardInfoBean.cardBankName.contains("农业")){
            binding.cardIcon.setImageResource(R.drawable.logo_nongye);
            binding.bankBg.setBackgroundResource(R.drawable.bank_nongye);

        }else if (cardInfoBean.cardBankName.contains("浦发")){
            binding.cardIcon.setImageResource(R.drawable.logo_pufa);
            binding.bankBg.setBackgroundResource(R.drawable.bank_pufa);

        }else if (cardInfoBean.cardBankName.contains("上海")){
            binding.cardIcon.setImageResource(R.drawable.logo_shanghai);
            binding.bankBg.setBackgroundResource(R.drawable.bank_shanghai);

        }else if (cardInfoBean.cardBankName.contains("兴业")){
            binding.cardIcon.setImageResource(R.drawable.logo_xingye);
            binding.bankBg.setBackgroundResource(R.drawable.bank_xingye);

        }else if (cardInfoBean.cardBankName.contains("邮政")){
            binding.cardIcon.setImageResource(R.drawable.logo_youzheng);
            binding.bankBg.setBackgroundResource(R.drawable.bank_youzheng);

        }else if (cardInfoBean.cardBankName.contains("招商")){
            binding.cardIcon.setImageResource(R.drawable.logo_zhaoshang);
            binding.bankBg.setBackgroundResource(R.drawable.bank_zhaoshang);

        }else if (cardInfoBean.cardBankName.contains("中信")){
            binding.cardIcon.setImageResource(R.drawable.logo_zhongxin);
            binding.bankBg.setBackgroundResource(R.drawable.bank_zhongxin);

        }else if (cardInfoBean.cardBankName.contains("广州")){
            binding.cardIcon.setImageResource(R.drawable.logo_guangzhou);
            binding.bankBg.setBackgroundResource(R.drawable.bank_guangzhou);

        }else if (cardInfoBean.cardBankName.contains("花旗")){
            binding.cardIcon.setImageResource(R.drawable.logo_huaqi);
            binding.bankBg.setBackgroundResource(R.drawable.bank_jiaotong);

        }else {
            if (TextUtils.equals("中国银行",cardInfoBean.cardBankName)){
                binding.cardIcon.setImageResource(R.drawable.logo_china);
                binding.bankBg.setBackgroundResource(R.drawable.bank_china);
            }else {
//                binding.cardIcon.setVisibility(View.INVISIBLE);
                binding.cardIcon.setImageResource(R.drawable.logo_other_bank);
            }
        }
    }


    //刷新信息获取信用卡
    public void getCreditCard(){
        Subscription subscription = BankImpAPI.bankInfo(HttpParam.OFFICE_CODE,HttpParam.BANK_INFO_APPKEY,
                MerchanInfoDB.queryInfo().merchantPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> user) {
                        dialog.setCardInfoBeanList(user.data.getCardInfo());
                        if (binding.getBean()!=null){
                            String no = binding.getBean().cardCode;
                            for (int index=0; index < user.data.getCardInfo().size(); index++){
                                User.DataBean.CardInfoBean bean = user.data.getCardInfo().get(index);
                                if (TextUtils.equals(no,bean.cardCode)){
                                    binding.setBean(bean);

                                    dates[0]=Integer.valueOf(bean.accountDay);
                                    dates[1]=Integer.valueOf(bean.repaymentDay);
//                                    Logger.e("repaymentDay=="+dates[1]);
                                    break;
                                }
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


    public void refreshButton(){
        if (binding.getBean()!=null && binding.getChannel()!=null)
            binding.next.setEnabled(true);
        else
            binding.next.setEnabled(false);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        RxBus.getInstance().post(RxEvent.REPAYMENT_TITLE,getString(R.string.repayment_2));
        super.onHiddenChanged(hidden);
    }


}
