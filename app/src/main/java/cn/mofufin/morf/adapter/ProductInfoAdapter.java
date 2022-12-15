package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.BR;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.ProductInfo;
import cn.mofufin.morf.databinding.LayoutInverstmentItemBinding;
import cn.mofufin.morf.databinding.LayoutInverstmentRookieItemBinding;

public class ProductInfoAdapter extends BaseRecyclerAdapter<ProductInfoAdapter.ViewHolder,ProductInfo.ProductListBean> {

    private static final int TYPE_ROOKIE =0;
    private static final int TYPE_NO_ROOKIE =1;

    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        int adver = getDataList().get(position).fdIsAdverProduct;
        if (adver == 0){
            return TYPE_ROOKIE;
        }

        return TYPE_NO_ROOKIE;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        if (type == TYPE_ROOKIE){
            return new InverstmentRookieViewHolder(layoutInflater.inflate(R.layout.layout_inverstment_rookie_item,viewGroup,false));
        }
        return new InverstmentViewHolder(layoutInflater.inflate(R.layout.layout_inverstment_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, ProductInfo.ProductListBean productListBean) {
        viewHolder.binding.setVariable(BR.bean,productListBean);
        viewHolder.binding.setVariable(BR.clicklistener,clickListener);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        ViewDataBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public void bind(int position ,ProductInfo.ProductListBean productListBean){

        }
    }

    class InverstmentViewHolder extends ViewHolder{
        LayoutInverstmentItemBinding binding;

        public InverstmentViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    class InverstmentRookieViewHolder extends ViewHolder{
        LayoutInverstmentRookieItemBinding binding;

        public InverstmentRookieViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }


}
