package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.CoinDetail;
import cn.mofufin.morf.databinding.LayoutCoinDetailItemBinding;

public class CoinDetailAdapter extends BaseRecyclerAdapter<CoinDetailAdapter.CoinDetailViewHolder,
        CoinDetail.DetailListBean> {

    @Override
    public CoinDetailViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new CoinDetailViewHolder(layoutInflater.inflate(R.layout.layout_coin_detail_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CoinDetailViewHolder coinDetailViewHolder, int position, CoinDetail.DetailListBean detailListBean) {
        coinDetailViewHolder.binding.setDetail(detailListBean);
    }

    class CoinDetailViewHolder extends  BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutCoinDetailItemBinding binding;

        public CoinDetailViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
