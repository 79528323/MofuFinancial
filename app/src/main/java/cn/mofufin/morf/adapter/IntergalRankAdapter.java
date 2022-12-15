package cn.mofufin.morf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutIntergalRankingItemBinding;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.IntergalRank;

public class IntergalRankAdapter extends BaseRecyclerAdapter<IntergalRankAdapter.ViewHolder, IntergalRank.ActivityBean.FdMonthListBean> {
    private String merChanCode;
    public static final int TYPE_DAY= 0x131;
    public static final int TYPE_MONTH= 0x132;
    public List<IntergalRank.ActivityBean.FdMonthListBean> fdCurrentMonthList = new ArrayList<>();
    public List<IntergalRank.ActivityBean.FdMonthListBean> faUpMonthList = new ArrayList<>();

    public IntergalRankAdapter(){
        merChanCode = MerchanInfoDB.queryInfo().merchantCode;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_intergal_ranking_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, IntergalRank.ActivityBean.FdMonthListBean monthListBean) {
        viewHolder.binding.setBean(monthListBean);
        viewHolder.binding.setMerCode(merChanCode);
        viewHolder.binding.setPosition(position);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutIntergalRankingItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
