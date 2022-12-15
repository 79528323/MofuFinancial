package cn.mofufin.morf.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.BalanceDetail;
import cn.mofufin.morf.databinding.LayoutBalanceDetailItemBinding;

public class BalanceDetailAdapter extends BaseRecyclerAdapter<BalanceDetailAdapter.BalanceDetailViewHolder,
        BalanceDetail.DetailLists> {

    private View.OnClickListener onClickListener;
    private Context context;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        context = BaseApplication.context;
    }

    @Override
    public BalanceDetailViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new BalanceDetailViewHolder(layoutInflater.inflate(R.layout.layout_balance_detail_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(BalanceDetailViewHolder commissionViewHolder, int position, BalanceDetail.DetailLists detailLists) {
        commissionViewHolder.binding.setDetail(detailLists);
        commissionViewHolder.binding.setClicklistener(onClickListener);

        int state = detailLists.cdState;
        commissionViewHolder.binding.status.setText(
                state==0?context.getString(R.string.success):
                        state==1?context.getString(R.string.fail):
                                state==2?context.getString(R.string.mine_text14):
                                        state==3?context.getString(R.string.balance_details_unpaid):context.getString(R.string.balance_details_handling));
    }

    class BalanceDetailViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutBalanceDetailItemBinding binding;

        public BalanceDetailViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
