package cn.mofufin.morf.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutCardItemBinding;
import cn.mofufin.morf.databinding.LayoutRepaymentProjectDetailItemBinding;
import cn.mofufin.morf.ui.entity.RepayMentProject;
import cn.mofufin.morf.ui.entity.User;

public class RepayMentProjectListAdapter extends BaseRecyclerAdapter<RepayMentProjectListAdapter.ViewHolder, RepayMentProject.PlanListBean> {

    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_repayment_project_detail_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, RepayMentProject.PlanListBean planListBean) {
        viewHolder.binding.setBean(planListBean);
        viewHolder.binding.setClicklistener(clickListener);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRepaymentProjectDetailItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
