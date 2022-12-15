package cn.mofufin.morf.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutIntergalRankingItemBinding;
import cn.mofufin.morf.databinding.LayoutRankingDayItemBinding;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.IntergalRank;

public class IntergalRankDayAdapter extends BaseRecyclerAdapter<IntergalRankDayAdapter.ViewHolder, IntergalRank.ActivityBean.FdDayListBean> {

    private String code;

    public IntergalRankDayAdapter() {
        this.code = MerchanInfoDB.queryInfo().merchantCode;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_ranking_day_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, IntergalRank.ActivityBean.FdDayListBean fdDayListBean) {
        viewHolder.binding.setBean(fdDayListBean);
        viewHolder.binding.setPosition(position+1);
        viewHolder.binding.setIslist(TextUtils.equals(fdDayListBean.merchantCode,code));
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRankingDayItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
