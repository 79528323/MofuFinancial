package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.PushMerchant;
import cn.mofufin.morf.databinding.LayoutPushItemBinding;

public class PushMerchantAdapter extends BaseRecyclerAdapter<PushMerchantAdapter.RecViewHolder,PushMerchant.DirectListBean> {

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_push_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder recViewHolder, int i, PushMerchant.DirectListBean listBean) {
        recViewHolder.binding.setPhone(listBean.merchantPhone);
        recViewHolder.binding.setName(listBean.fdMerIdentityCardName);
        recViewHolder.binding.setMemberType(listBean.memberType);
    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutPushItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
