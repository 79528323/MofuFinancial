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
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.LoanApplyRecord;

public class LoanApplyRecordAdapter extends BaseRecyclerAdapter<LoanApplyRecordAdapter.RecordViewHolder, LoanApplyRecord.ApplyLoansListBean> {
    private View.OnClickListener onClickListener;
    private Context context;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public LoanApplyRecordAdapter() {
        context = BaseApplication.context;
    }

    @Override
    public RecordViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecordViewHolder(layoutInflater.inflate(R.layout.layout_loan_record_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecordViewHolder recordViewHolder, int position, LoanApplyRecord.ApplyLoansListBean bean) {
        recordViewHolder.binding.setBean(bean);
        recordViewHolder.binding.setClicklistener(onClickListener);
        switch (bean.applyState){
            case 0:
                recordViewHolder.binding.status.setStrokeColor(ContextCompat.getColor(context,R.color.app_blue));
                break;

            case 1:
                recordViewHolder.binding.status.setStrokeColor(ContextCompat.getColor(context,R.color.loan_red));
                break;

            default:
                recordViewHolder.binding.status.setStrokeColor(ContextCompat.getColor(context,R.color.ok));
                break;
        }
    }

    class RecordViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutLoanRecordItemBinding binding;

        public RecordViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
