package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.Coupons;
import cn.mofufin.morf.databinding.LayoutMallItemBinding;

public class MallVirtualAdapter extends BaseRecyclerAdapter<MallVirtualAdapter.CouponsViewHolder,Coupons.DataBean.ListBean> {

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public CouponsViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        return new CouponsViewHolder(layoutInflater.inflate(R.layout.layout_mall_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CouponsViewHolder couponsViewHolder, int position, Coupons.DataBean.ListBean dataBean) {
        couponsViewHolder.binding.setBean(dataBean);
        if (dataBean.isTitle){
            couponsViewHolder.binding.titleColor.setBackgroundColor(
                        ContextCompat.getColor(BaseApplication.context,position%2==0?R.color.red:R.color.button_bg_purple));
        }

        if (!dataBean.AbNormalGoods){
            couponsViewHolder.binding.setClicklistener(onClickListener);
        }

        couponsViewHolder.binding.executePendingBindings();
    }

    class CouponsViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutMallItemBinding binding;

        public CouponsViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
