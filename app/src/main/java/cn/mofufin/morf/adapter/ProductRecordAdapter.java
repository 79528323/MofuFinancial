package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.databinding.LayoutProductRecordItemBinding;

public class ProductRecordAdapter extends BaseRecyclerAdapter<ProductRecordAdapter.RecViewHolder,ProductDetails.RecordListBean> {

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_product_record_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder recViewHolder, int i, ProductDetails.RecordListBean recordListBean) {
        recViewHolder.binding.setOver(recordListBean);
    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutProductRecordItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
