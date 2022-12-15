package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.BR;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.InverstMemtRecord;
import cn.mofufin.morf.databinding.LayoutInverstmentRecordItemBinding;
import cn.mofufin.morf.databinding.LayoutInverstmentedRecordItemBinding;

public class InverstMentRecordAdapter extends BaseRecyclerAdapter<InverstMentRecordAdapter.ViewHolder,InverstMemtRecord.RecordListBean> {
    public static final int INVESTING_IN=0;
    public static final int REDEEMED=1;
    public static final int REVOCATION_OF_INVESTMENT=2;

    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (getDataList().get(position).foOrderState){
            case INVESTING_IN:
                return INVESTING_IN;

                default:
                    return REDEEMED;
        }
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        if (type == INVESTING_IN){
            return new ViewHolder(layoutInflater.inflate(R.layout.layout_inverstment_record_item,viewGroup,false));
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_inverstmented_record_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, InverstMemtRecord.RecordListBean recordListBean) {
        viewHolder.binding.setVariable(BR.bean,recordListBean);
        viewHolder.binding.setVariable(BR.onclicklistener,clickListener);
    }



    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        ViewDataBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    class ViewHolderInverMented extends ViewHolder{
        LayoutInverstmentedRecordItemBinding binding;

        public ViewHolderInverMented(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    class ViewHolderInverMent extends ViewHolder{
        LayoutInverstmentRecordItemBinding binding;

        public ViewHolderInverMent(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }



}
