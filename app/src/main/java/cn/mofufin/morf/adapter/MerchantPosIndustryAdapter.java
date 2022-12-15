package cn.mofufin.morf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutIndustryItemBinding;
import cn.mofufin.morf.databinding.LayoutMerchantPosIndustryItemBinding;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BusinessModel;
import cn.mofufin.morf.ui.entity.IndustryInfos;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;

public class MerchantPosIndustryAdapter extends BaseRecyclerAdapter<MerchantPosIndustryAdapter.ViewHolder, IndustryInfos.MercInfoListBean> {
    private View.OnClickListener clickListener;
    private List<Boolean> btnCheck = new ArrayList<Boolean>();
    private double ratio , fee;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void refresh(List<IndustryInfos.MercInfoListBean> list) {
        for (int index=0; index<list.size(); index++)
            btnCheck.add(false);
        super.refresh(list);
    }

    public void setRatio(double ratio) {
        this.ratio = Double.valueOf(new DecimalFormat("######0.00").format(ratio * 100));
    }

    public double getRatio() {
        return ratio;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_merchant_pos_industry_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, IndustryInfos.MercInfoListBean bean) {
        viewHolder.binding.setMName(bean.merc_city_name+"  "+bean.merc_name);
        viewHolder.binding.setRate(String.valueOf(ratio));
        viewHolder.binding.setFee(String.valueOf(fee));
        viewHolder.binding.setPosition(position);
        viewHolder.binding.setIsCheck(!btnCheck.isEmpty()?btnCheck.get(position):false);
        viewHolder.binding.setClicklistener(clickListener);
        viewHolder.binding.executePendingBindings();
    }

    public void notifyItem(int pos){
        if (!btnCheck.isEmpty()){
            for (int dex = 0; dex <btnCheck.size(); dex++)
                btnCheck.set(dex,false);

            btnCheck.set(pos,true);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutMerchantPosIndustryItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
