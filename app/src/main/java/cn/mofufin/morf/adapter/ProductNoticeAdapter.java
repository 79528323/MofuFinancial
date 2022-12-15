package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.ProductNotice;
import cn.mofufin.morf.databinding.LayoutProductNoticeItemBinding;

public class ProductNoticeAdapter extends BaseRecyclerAdapter<ProductNoticeAdapter.NoticeViewHolder,ProductNotice.InformListBean> {


    @Override
    public NoticeViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new NoticeViewHolder(layoutInflater.inflate(R.layout.layout_product_notice_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder viewHolder, int position, ProductNotice.InformListBean bean) {
        viewHolder.binding.setNotice(bean);
    }

    class NoticeViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutProductNoticeItemBinding binding;

        public NoticeViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
