package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.ExchangeHistory;
import cn.mofufin.morf.databinding.LayoutExchangeHistoryItemBinding;

public class ExChangeHistoryAdapter extends BaseRecyclerAdapter<ExChangeHistoryAdapter.ViewHolder,ExchangeHistory.DataBean.ListBean> {

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_exchange_history_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i, ExchangeHistory.DataBean.ListBean listBean) {
        viewHolder.binding.setDetail(listBean);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutExchangeHistoryItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
