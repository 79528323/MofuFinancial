package cn.mofufin.morf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanRepayRecordDetailItemBinding;
import cn.mofufin.morf.databinding.LayoutLoanRepayRecordItemBinding;
import cn.mofufin.morf.ui.entity.LoanRepayCord;
import cn.mofufin.morf.ui.entity.RecordDetails;

public class LoanRepayRecordDetailAdapter extends BaseRecyclerAdapter<LoanRepayRecordDetailAdapter.RecordViewHolder, RecordDetails.PlanDetailsListBean> {
    private View.OnClickListener onClickListener;
    private Context context;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecordViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecordViewHolder(layoutInflater.inflate(R.layout.layout_loan_repay_record_detail_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecordViewHolder recordViewHolder, int position, RecordDetails.PlanDetailsListBean bean) {
        recordViewHolder.binding.setBean(bean);
        recordViewHolder.binding.setClicklistener(onClickListener);
    }

    class RecordViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutLoanRepayRecordDetailItemBinding binding;

        public RecordViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
