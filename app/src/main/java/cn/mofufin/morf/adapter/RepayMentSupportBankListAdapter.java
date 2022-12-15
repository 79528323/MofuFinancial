package cn.mofufin.morf.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutRepaymentShowprojectItemBinding;
import cn.mofufin.morf.databinding.LayoutSupportBankListItemBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.SupportBank;
import cn.mofufin.morf.ui.util.DataUtils;

public class RepayMentSupportBankListAdapter extends BaseRecyclerAdapter<RepayMentSupportBankListAdapter.ViewHolder, SupportBank.SupportBankBean> {

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_support_bank_list_item,viewGroup,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, SupportBank.SupportBankBean bankBean) {
        viewHolder.binding.setBean(bankBean);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutSupportBankListItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
