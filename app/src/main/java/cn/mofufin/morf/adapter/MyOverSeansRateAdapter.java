package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Overseans;
import cn.mofufin.morf.databinding.LayoutOverRateItemBinding;

public class MyOverSeansRateAdapter extends BaseRecyclerAdapter<MyOverSeansRateAdapter.RecViewHolder,Overseans.OverListBean> {

    public int memberType;

    public MyOverSeansRateAdapter() {
        memberType = MerchanInfoDB.queryInfo().memberType;
    }

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_over_rate_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder recViewHolder, int i, Overseans.OverListBean overListBean) {
        recViewHolder.binding.setMemberType(memberType);
        recViewHolder.binding.setOver(overListBean);
    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutOverRateItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
