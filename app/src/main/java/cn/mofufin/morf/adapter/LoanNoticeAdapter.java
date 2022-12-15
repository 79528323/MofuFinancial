package cn.mofufin.morf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanNoticeItemBinding;
import cn.mofufin.morf.databinding.LayoutProductNoticeItemBinding;
import cn.mofufin.morf.ui.entity.LoanNotify;
import cn.mofufin.morf.ui.entity.ProductNotice;

public class LoanNoticeAdapter extends BaseRecyclerAdapter<LoanNoticeAdapter.NoticeViewHolder, LoanNotify.InformListBean> {


    @Override
    public NoticeViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new NoticeViewHolder(layoutInflater.inflate(R.layout.layout_loan_notice_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder viewHolder, int position, LoanNotify.InformListBean bean) {
        viewHolder.binding.setDetail(bean);
    }

    class NoticeViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutLoanNoticeItemBinding binding;

        public NoticeViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
