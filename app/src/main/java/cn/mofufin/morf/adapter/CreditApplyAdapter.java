package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.CreditApply;
import cn.mofufin.morf.databinding.LayoutCreditApplyItemBinding;

public class CreditApplyAdapter extends BaseRecyclerAdapter<CreditApplyAdapter.ViewHolder,CreditApply.DataBean.ListBean> {
//    中国(BOC),中信(CITIC),光大(CEB),浦发(SPDB),广发(CGB),兴业(CIB),华夏(HXB),交通(BCOM),民生(CMBC),上海(Shanghai)
    private static final String BOC="BOC";
    private static final String CITIC="CITIC";
    private static final String CEB="CEB";
    private static final String SPDB="SPDB";
    private static final String CGB="CGB";
    private static final String CIB="CIB";
    private static final String HXB="HXB";
    private static final String BCOM="BCOM";
    private static final String CMBC="CMBC";
    private static final String Shanghai="Shanghai";

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_credit_apply_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i, CreditApply.DataBean.ListBean listBean) {
        viewHolder.binding.setBean(listBean);
        String alias = listBean.cardAlias;
        if (TextUtils.equals(alias,BOC)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_zhonghang);
        }else if (TextUtils.equals(alias,CITIC)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_zhongxin);
        }else if (TextUtils.equals(alias,CEB)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_guangda);
        }else if (TextUtils.equals(alias,SPDB)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_pufa);
        }else if (TextUtils.equals(alias,CGB)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_gangfa);
        }else if (TextUtils.equals(alias,CIB)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_xingye);
        }else if (TextUtils.equals(alias,HXB)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_huaxia);
        }else if (TextUtils.equals(alias,BCOM)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_jiaotong);
        }else if (TextUtils.equals(alias,CMBC)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_minsheng);
        }else if (TextUtils.equals(alias,Shanghai)){
            viewHolder.binding.img.setBackgroundResource(R.drawable.cdtaly_shanghai);
        }
        viewHolder.binding.setClicklistener(onClickListener);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutCreditApplyItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
