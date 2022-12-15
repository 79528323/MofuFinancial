package cn.mofufin.morf.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanRepayShowplanItemBinding;
import cn.mofufin.morf.databinding.LayoutRankingItemBinding;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.entity.RefundPlan;
import cn.mofufin.morf.ui.util.DataUtils;

public class RefundPlanAdapter extends BaseRecyclerAdapter<RefundPlanAdapter.ViewHolder, RefundPlan.RefundPlanDetailBean> {

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_loan_repay_showplan_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, RefundPlan.RefundPlanDetailBean bean) {
        viewHolder.binding.setDetail(bean);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutLoanRepayShowplanItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }


    public String coverNamePhone(String name){
        String str="";
        if (TextUtils.isEmpty(name))
            return "";

        if (name.length()>= 2){
            str = name.substring(0,1) + "**";
        }

        return str;
    }
}
