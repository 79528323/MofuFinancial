package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.Ranking;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.databinding.LayoutRankingItemBinding;

public class CommissionAdapter extends BaseRecyclerAdapter<CommissionAdapter.CommissionViewHolder,Ranking.RankingListBean> {

    private int mypos=-1;

    public void setMypos(int mypos) {
        this.mypos = mypos;
    }

    @Override
    public CommissionViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new CommissionViewHolder(layoutInflater.inflate(R.layout.layout_ranking_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(CommissionViewHolder commissionViewHolder, int position, Ranking.RankingListBean rankingListBean) {
        commissionViewHolder.binding.setPosition(String.valueOf(position +1));
        commissionViewHolder.binding.setIsMine(false);
        if (mypos == position)
            commissionViewHolder.binding.setIsMine(true);

        commissionViewHolder.binding.setIncomeMoney(DataUtils.numFormat(rankingListBean.totalMoney));
        commissionViewHolder.binding.setHistory(DataUtils.numFormat(rankingListBean.historyTotal));
        commissionViewHolder.binding.setName(coverNamePhone(rankingListBean.incomePersonName));


        commissionViewHolder.binding.executePendingBindings();
    }

    class CommissionViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRankingItemBinding binding;

        public CommissionViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }


    public String coverNamePhone(String name){
        String str="";
        if (TextUtils.isEmpty(name))
            return "";

        if (name.length()>= 2){
            str = name.substring(0,1) + "**";
        }

        return str;
    }
}
