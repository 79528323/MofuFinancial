package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.databinding.LayoutDialogSettlementCardItemBinding;

public class SetttleMentCardDialogAdapter extends BaseRecyclerAdapter<SetttleMentCardDialogAdapter.RecViewHolder,User.DataBean.CardInfoBean> {
    private Map<Integer,Boolean> checkMap = new HashMap<>();
    private int type;
    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void refresh(List<User.DataBean.CardInfoBean> list) {
        super.refresh(list);
        for (int index=0; index < list.size();index++){
            checkMap.put(index,false);
        }
    }

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_dialog_settlement_card_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder viewHolder, int position, User.DataBean.CardInfoBean cardInfoBean) {
        viewHolder.binding.setBean(cardInfoBean);
        viewHolder.binding.setType(type);
        if (cardInfoBean.cardBankName.contains("平安")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_pingan);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_pingan);

        }else if (cardInfoBean.cardBankName.contains("北京")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_beijing);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_beijing);

        }else if (cardInfoBean.cardBankName.contains("工商")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_gongshang);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_gongshang);

        }else if (cardInfoBean.cardBankName.contains("光大")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_guangda);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_guangda);


        }else if (cardInfoBean.cardBankName.contains("广发")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_guangfa);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_guangfa);

        }else if (cardInfoBean.cardBankName.contains("华夏")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_huaxia);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_huaxia);

        }else if (cardInfoBean.cardBankName.contains("建设")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_jianshe);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_jianshe);

        }else if (cardInfoBean.cardBankName.contains("交通")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_jiaotong);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_jiaotong);

        }else if (cardInfoBean.cardBankName.contains("民生")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_minsheng);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_minsheng);

        }else if (cardInfoBean.cardBankName.contains("农业")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_nongye);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_nongye);

        }else if (cardInfoBean.cardBankName.contains("浦发")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_pufa);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_pufa);

        }else if (cardInfoBean.cardBankName.contains("上海")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_shanghai);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_shanghai);

        }else if (cardInfoBean.cardBankName.contains("兴业")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_xingye);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_xingye);

        }else if (cardInfoBean.cardBankName.contains("邮政")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_youzheng);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_youzheng);

        }else if (cardInfoBean.cardBankName.contains("招商")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_zhaoshang);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_zhaoshang);

        }else if (cardInfoBean.cardBankName.contains("广州")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_guangzhou);

        }else if (cardInfoBean.cardBankName.equals("中信")){
            viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_zhongxin);
//            viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_zhongxin);

        }else {
            if (TextUtils.equals("中国银行",cardInfoBean.cardBankName)){
                viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_china);
//                viewHolder.binding.bankBg.setBackgroundResource(R.drawable.bank_china);
            }else {
//                viewHolder.binding.cardIcon.setVisibility(View.INVISIBLE);
                viewHolder.binding.cardIcon.setImageResource(R.drawable.logo_other_bank);
            }
        }
        viewHolder.binding.setPosition(position);
//        viewHolder.binding.setIsShow(cardInfoBean.cardDef==1);
//        if (cardInfoBean.cardDef > 1)
        viewHolder.binding.setIsCheck(checkMap.get(position));
        viewHolder.binding.setClicklistener(clickListener);

    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutDialogSettlementCardItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    //更新checkbox位置
    public void notifyItemChange(int pos){
        for (int dex = 0; dex < checkMap.size(); dex++){
            checkMap.put(dex,false);
            notifyItemChanged(dex);
        }

        checkMap.put(pos,true);
        notifyItemChanged(pos);
    }

}
