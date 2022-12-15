package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.databinding.LayoutRateItemBinding;
import cn.mofufin.morf.ui.util.DataUtils;

public class MyRateAdapter extends BaseRecyclerAdapter<MyRateAdapter.RecViewHolder,Channel.ChannelListBean> {

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_rate_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder recViewHolder, int i, Channel.ChannelListBean channelListBean) {
        recViewHolder.binding.setName(channelListBean.tcName+" D+0");
        recViewHolder.binding.setRate(DataUtils.converOverPercent(channelListBean.tcrUserRatioD0) + " + 2");
    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRateItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
