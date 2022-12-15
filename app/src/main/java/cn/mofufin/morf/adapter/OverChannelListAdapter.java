package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.databinding.ViewDataBinding;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.BR;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutOverchannelItemTitleBinding;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.databinding.LayoutOverChannelItemBinding;

/**
 * 跨境通道
 */
public class OverChannelListAdapter extends BaseRecyclerAdapter<OverChannelListAdapter.ViewHolder,Overseans.OverListBean>{
    private View.OnClickListener clickListener;
    private User.DataBean.MerchantInfoBean user;

    public OverChannelListAdapter() {
        user = MerchanInfoDB.queryInfo();
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getDataList().get(position).getType();
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup,final int type) {
        if (type==0)
            return new TitleViewHolder(layoutInflater.inflate(
                    R.layout.layout_overchannel_item_title,viewGroup,false));
        else
            return new OverChannelViewHolder(layoutInflater.inflate(
                    R.layout.layout_over_channel_item,viewGroup,false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position, final Overseans.OverListBean overListBean) {
        viewHolder.binding.setVariable(BR.clicklistener,clickListener);
        viewHolder.binding.setVariable(BR.overUser,user);
        viewHolder.binding.setVariable(BR.over,overListBean);
        viewHolder.binding.setVariable(BR.position,position);

        int member = user.memberType==1?overListBean.merCommonSettlePeriod :
                user.memberType == 2?overListBean.merGoldSettlePeriod:overListBean.merDrillSettlePeriod;
        viewHolder.binding.setVariable(BR.type,member);
        viewHolder.binding.executePendingBindings();
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        ViewDataBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public void bind(int position , Overseans.OverListBean listBean){
//            super.binding(position, listBean);
        }
    }

    class OverChannelViewHolder extends ViewHolder{
        LayoutOverChannelItemBinding binding;

        public OverChannelViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        @Override
        public void bind(int position, Overseans.OverListBean listBean) {
//            switch (member){
//                case 0:
//                    binding.ivChannel.setImageResource(R.drawable.d0);
//                    break;
//                case 1:
//                    binding.ivChannel.setImageResource(R.drawable.d1);
//                    break;
//                case 2:
//                    binding.ivChannel.setImageResource(R.drawable.d2);
//                    break;
//                case 3:
//                    binding.ivChannel.setImageResource(R.drawable.d3);
//                    break;
//                case 4:
//                    binding.ivChannel.setImageResource(R.drawable.d4);
//                    break;
//                case 5:
//                    binding.ivChannel.setImageResource(R.drawable.d5);
//                    break;
//                case 6:
//                    binding.ivChannel.setImageResource(R.drawable.d6);
//                    break;
//                    default:
//                        binding.ivChannel.setImageResource(R.drawable.d7);
//                        break;
//
//            }
        }
    }


    class TitleViewHolder extends ViewHolder{
        LayoutOverchannelItemTitleBinding binding;

        public TitleViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        @Override
        public void bind(int position, Overseans.OverListBean listBean) {
            super.bind(position, listBean);
        }
    }

}
