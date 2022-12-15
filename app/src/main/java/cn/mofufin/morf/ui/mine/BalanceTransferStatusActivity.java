package cn.mofufin.morf.ui.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityBalanceTransferStatusBinding;

/**
 * 余额转存 完成状态页面
 */
public class BalanceTransferStatusActivity extends BaseActivity{
    public static final int TYPE_CHARGE_DEPOSIT = 2;
    public static final int TYPE_MERCHANT_EXCHANGE = 3;
    public static final int TYPE_MEMBER_UPGRADE=4;
    public static final int TYPE_INVERSTMENT_PAYMENT=5;
    public static final int TYPE_MPOS_UPDATE_CARD=6;
    public static final int TYPE_CHARGE_CREDIT_PAYMENT=7;
    private ActivityBalanceTransferStatusBinding binding;
    private String avAmount = "";
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_balance_transfer_status);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        type = getIntent().getIntExtra(IntentParam.TYPE,type);
        binding.setType(type);
        if (type == TYPE_CHARGE_CREDIT_PAYMENT){//TODO 刷卡金充值
            boolean status = getIntent().getBooleanExtra(IntentParam.STATUS,false);
            binding.setIsTransferOK(status);
            binding.setTips(getIntent().getStringExtra(IntentParam.TIPS));
            binding.setTitle(getString(R.string.charge_credit_payment_title));
            binding.toast.setVisibility(View.VISIBLE);
            binding.toast.setText(status?"充值成功":"充值失败");
            binding.toastTime.setVisibility(View.INVISIBLE);
            binding.setBtntx(status?"跳转至余额":"重新充值");


        }else if (type == TYPE_INVERSTMENT_PAYMENT){//TODO 购买理财
            BaseResult result = getIntent().getParcelableExtra(IntentParam.BEAN);
            binding.setIsTransferOK(result.result_Code==0?true:false);
            binding.setTitle(getString(R.string.inverstment_title));
            binding.toastTime.setVisibility(View.GONE);
            binding.toast.setText(result.result_Code==0?"投资成功":"投资失败");
            if (result.result_Code==0){
                binding.expincomeYear.setText(DataUtils.converOverPercent(result.fdAnnualized));
                binding.inverstAmount.setText(DataUtils.numFormat(result.foBuyMoney));
                binding.productName.setText(result.fdName);
                binding.productDate.setText(result.foProductBuyDate);
                binding.inverstDate.setText(result.fdProductDate+"");
                binding.setBtntx(getString(R.string.confirm));
            }else {
                binding.inverstLayout.setVisibility(View.GONE);
                binding.toast.setText(result.result_Msg);
                binding.setBtntx("返回");
            }

        }else if (type == TYPE_MEMBER_UPGRADE){//TODO 会员升级
            boolean status = getIntent().getBooleanExtra(IntentParam.STATUS,false);
            binding.setIsTransferOK(status);
            binding.setTips(getIntent().getStringExtra(IntentParam.TIPS));
            binding.setTitle(getIntent().getStringExtra(IntentParam.TITLE));
            binding.toast.setVisibility(View.VISIBLE);
            binding.toast.setText(status?"升级成功":"升级失败");
            binding.toastTime.setVisibility(View.INVISIBLE);
            binding.setBtntx(status?getString(R.string.finish):"重新升级");

        }else if (type == TYPE_MERCHANT_EXCHANGE){//TODO 商品兑换
            boolean status = getIntent().getBooleanExtra(IntentParam.STATUS,false);
            binding.setIsTransferOK(status);
            binding.setTips(getIntent().getStringExtra(IntentParam.TIPS));
            binding.setTitle(getString(R.string.merchant_ex_title));
            binding.toast.setVisibility(View.VISIBLE);
            binding.toast.setText(getIntent().getStringExtra(IntentParam.TITLE));
            binding.toastTime.setVisibility(View.INVISIBLE);
            binding.setBtntx(status?getString(R.string.confirm):"返回");

        }else if (type==TYPE_CHARGE_DEPOSIT){//TODO 充值押金
            boolean status=getIntent().getBooleanExtra(IntentParam.CHARGE_DEPOSIT_STATUS,false);
            String respone = getIntent().getStringExtra(IntentParam.BALANCE_RESPONE);
            binding.setIsTransferOK(status);
            binding.setTips(status?getString(R.string.balance_tranf_pwd_6):respone);
            binding.setTitle(getString(R.string.recharge_deposit_title));
            binding.toast.setVisibility(View.VISIBLE);
            binding.toast.setText(status?getString(R.string.balance_tranf_pwd_7):getString(R.string.balance_tranf_pwd_8));
            binding.setBtntx(status?getString(R.string.confirm):"返回");
            binding.toastTime.setVisibility(View.INVISIBLE);

        }else if (type==TYPE_MPOS_UPDATE_CARD){//TODO 变更结算卡
            boolean status=getIntent().getBooleanExtra(IntentParam.STATUS,false);
            String respone = getIntent().getStringExtra(IntentParam.BALANCE_RESPONE);
            binding.setIsTransferOK(status);
            binding.setTips(respone);
            binding.setTitle(getString(R.string.change_card_14));
            binding.toast.setVisibility(View.VISIBLE);
            binding.toast.setText(status?getString(R.string.change_card_15):getString(R.string.change_card_16));
            binding.toastTime.setVisibility(View.INVISIBLE);
            binding.setBtntx(status?getString(R.string.confirm):"返回");

        }else {//TODO 转至余额
            binding.setTitle(getString(R.string.balance_tranf_title));
            avAmount = getIntent().getStringExtra(IntentParam.AMOUNT_AVAILABLE);

            if (TextUtils.isEmpty(avAmount)){
                String respone = getIntent().getStringExtra(IntentParam.BALANCE_RESPONE);
                binding.setIsTransferOK(false);
                binding.setTips(respone);
                binding.toast.setText("转至余额失败");
                binding.toast.setVisibility(View.VISIBLE);
                binding.toastTime.setVisibility(View.INVISIBLE);
            }else {
                binding.setIsTransferOK(true);
                double amount = Double.valueOf(avAmount);
                avAmount = DataUtils.numFormat(amount);
                String unit = "元";
                if (type > -1){
                    unit = (type == 0 ? "港元":"美元");
                }
                binding.setTips(getString(R.string.balance_tranf_pwd_4,avAmount+unit));
                binding.toast.setText("转至余额成功");
                binding.toast.setVisibility(View.VISIBLE);
                binding.toastTime.setVisibility(View.INVISIBLE);
            }

            binding.toast.setVisibility(View.VISIBLE);
            binding.toastTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean enableSliding() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onPress(View view){
        if (binding.getIsTransferOK()){
            RxBus.getInstance().postEmpty(RxEvent.BALANCE_TRANSFER_FINISH);
            RxBus.getInstance().postEmpty(RxEvent.REFRESH_QUERY_MERREBATE);
            RxBus.getInstance().postEmpty(RxEvent.RECHARGE_TO_DEPOSIT_FINISH);
            RxBus.getInstance().postEmpty(RxEvent.MERCHANT_DETAIL_FINISH);
            RxBus.getInstance().postEmpty(RxEvent.MERCHANT_EXCHANGE_FINISH);
            RxBus.getInstance().postEmpty(RxEvent.REFRESH_PRODUCT_INFO);
            RxBus.getInstance().postEmpty(RxEvent.REFRESH_BANKCARD_INFO);
            if (type == TYPE_MERCHANT_EXCHANGE){
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_MALL);
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_MALL_VIRTUAL_INFO);
            }

            if (type == TYPE_INVERSTMENT_PAYMENT){
//                RxBus.getInstance().postEmpty(RxEvent.REFRESH_PAYMENT_DETAILS_INFO);
                RxBus.getInstance().postEmpty(RxEvent.PRODUCT_PAYMENT_FINISH);
            }

            if (type == TYPE_CHARGE_CREDIT_PAYMENT){
                RxBus.getInstance().postEmpty(RxEvent.CHARGE_CREDIT_PAYMENT_FINISH);
                RxBus.getInstance().postEmpty(RxEvent.RECHARGE_TO_BALANCE_FINISH);
                startActivity(new Intent(this,BalanceActivity.class));
            }
        }

        RxBus.getInstance().postEmpty(RxEvent.REFRESH_PAYMENT_DETAILS_INFO);
        finish();
    }

}
