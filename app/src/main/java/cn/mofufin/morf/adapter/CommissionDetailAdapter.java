package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.CommissionDetail;
import cn.mofufin.morf.databinding.LayoutCommissionItemBinding;

public class CommissionDetailAdapter extends BaseRecyclerAdapter<CommissionDetailAdapter.CommissionDetailViewHolder,
        CommissionDetail.DetailListBean> {

    @Override
    public CommissionDetailViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new CommissionDetailViewHolder(layoutInflater.inflate(R.layout.layout_commission_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CommissionDetailViewHolder commissionViewHolder, int position, CommissionDetail.DetailListBean detailListBean) {
        commissionViewHolder.binding.setDetail(detailListBean);
    }

    class CommissionDetailViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutCommissionItemBinding binding;

        public CommissionDetailViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
