package cn.mofufin.morf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanRecordItemBinding;
import cn.mofufin.morf.databinding.LayoutLoanRepayRecordItemBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;
import cn.mofufin.morf.ui.entity.LoanRepayCord;

public class LoanRepayRecordAdapter extends BaseRecyclerAdapter<LoanRepayRecordAdapter.RecordViewHolder, LoanRepayCord.PlanListBean> {
    private View.OnClickListener onClickListener;
    private Context context;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public RecordViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecordViewHolder(layoutInflater.inflate(R.layout.layout_loan_repay_record_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecordViewHolder recordViewHolder, int position, LoanRepayCord.PlanListBean bean) {
        recordViewHolder.binding.setBean(bean);
        recordViewHolder.binding.setClicklistener(onClickListener);
        switch (bean.refundPlanState){
            case 1:
            case 3:
                recordViewHolder.binding.repayStatus.setStrokeColor(ContextCompat.getColor(context,R.color.app_blue));
                break;

                default:
                    recordViewHolder.binding.repayStatus.setStrokeColor(ContextCompat.getColor(context,R.color.loan_red));
                    break;


        }
        recordViewHolder.binding.executePendingBindings();
    }

    class RecordViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutLoanRepayRecordItemBinding binding;

        public RecordViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
