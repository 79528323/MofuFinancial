package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutAddressItemBinding;
import cn.mofufin.morf.ui.entity.Address;
import cn.mofufin.morf.ui.mine.ReceivingAddressActivity;

public class AddressAdapter extends BaseRecyclerAdapter<AddressAdapter.ViewHolder, Address.DataBean.AddressInfoBean> {
    public View.OnClickListener onClickListener;
    public ReceivingAddressActivity activity;

    public void setActivity(ReceivingAddressActivity activity) {
        this.activity = activity;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_address_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, Address.DataBean.AddressInfoBean address) {
        viewHolder.binding.setBean(address);
        viewHolder.binding.setHandlers(activity);
        viewHolder.binding.setClicklistener(onClickListener);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutAddressItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
