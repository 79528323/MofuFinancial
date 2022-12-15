package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.databinding.LayoutCoinRankingItemBinding;

public class CoinAdapter extends BaseRecyclerAdapter<CoinAdapter.CoinViewHolder,Coin.MoBiListBean> {


    @Override
    public CoinViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new CoinViewHolder(layoutInflater.inflate(R.layout.layout_coin_ranking_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CoinViewHolder coinViewHolder, int position, Coin.MoBiListBean moBiListBean) {
        coinViewHolder.binding.setPosition(String.valueOf(position +1));
        coinViewHolder.binding.setIncomeMoney(String.valueOf(moBiListBean.totalMoney));
        coinViewHolder.binding.setHistory(String.valueOf(moBiListBean.historyTotal));
        coinViewHolder.binding.setName(coverNamePhone(moBiListBean.merchantName));
        coinViewHolder.binding.executePendingBindings();
    }

    class CoinViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutCoinRankingItemBinding binding;

        public CoinViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }


    public static String coverNamePhone(String name){
        String str="";
        if (TextUtils.isEmpty(name))
            return "";

        if (name.length()>= 2){
            str = name.substring(0,1) + "**";
        }

        return str;
    }
}
