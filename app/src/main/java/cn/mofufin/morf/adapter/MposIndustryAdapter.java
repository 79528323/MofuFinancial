package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BusinessModel;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.databinding.LayoutIndustryItemBinding;

public class MposIndustryAdapter extends BaseRecyclerAdapter<MposIndustryAdapter.ViewHolder, BusinessModel> {
    private View.OnClickListener clickListener;
    private List<Boolean> btnCheck = new ArrayList<Boolean>();
    private int merberType=0;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        merberType = MerchanInfoDB.queryInfo().memberType;
    }

    @Override
    public void refresh(List<BusinessModel> list) {
        for (int index=0; index<list.size(); index++)
            btnCheck.add(false);
        super.refresh(list);
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_industry_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, BusinessModel model) {
        int drawableId=0;
        int bizCode = Integer.valueOf(model.getBiz_code());
        if (bizCode == Common.STANDARD_BIZCODE || bizCode == Common.UN_STANDARD_BIZCODE){
            drawableId = R.drawable.industry_standardmerchant;
        }else if (bizCode == Common.BUSINESS_BIZCODE || bizCode == Common.UN_BUSINESS_BIZCODE){
            drawableId = R.drawable.industry_business;
        }else if (bizCode == Common.COMMODITY_BIZCODE || bizCode == Common.UN_COMMODITY_BIZCODE){
            drawableId = R.drawable.industry_commodity;
        }else if (bizCode == Common.RESTAURANT_BIZCODE || bizCode == Common.UN_RESTAURANT_BIZCODE){
            drawableId = R.drawable.industry_restaurant;
        }else if (bizCode == Common.JEWELLERY_BIZCODE || bizCode == Common.UN_JEWELLERY_BIZCODE){
            drawableId = R.drawable.industry_jewellery;
        }else if (bizCode == Common.HOUSE_BIZCODE || bizCode == Common.UN_HOUSE_BIZCODE){
            drawableId = R.drawable.industry_house;
        }

        float rate = 0f ,fee=0;
        if (merberType == User.ORDINARY_MEMBER){
            fee = Float.valueOf(model.getFee1_d0());
        }else if (merberType == User.GOLDEN_MEMBER){
            fee = Float.valueOf(model.getFee2_d0());
        }else {
            fee = Float.valueOf(model.getFee3_d0());
        }

        rate = Float.valueOf(new DecimalFormat("######0.00").format(fee * 100));

        int quota = Integer.valueOf(model.getMaxSinglelimit());
        viewHolder.binding.setCharge(model.getExtraCharge());
        viewHolder.binding.ivIndustry.setImageResource(drawableId);
        viewHolder.binding.setIndustryName(model.getInd_name());
        viewHolder.binding.setRate(rate);
        viewHolder.binding.setQuota(quota);
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
        LayoutIndustryItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
