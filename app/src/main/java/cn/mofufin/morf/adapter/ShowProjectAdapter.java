package cn.mofufin.morf.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanRepayShowplanItemBinding;
import cn.mofufin.morf.databinding.LayoutRepaymentShowprojectItemBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.entity.RefundPlan;
import cn.mofufin.morf.ui.util.DataUtils;

public class ShowProjectAdapter extends BaseRecyclerAdapter<ShowProjectAdapter.ViewHolder, List<ProjectResult.PlanDataListBean.DetailsBean>> {
    private DateFormat outformat = new SimpleDateFormat("HH:mm");
    private DateFormat informat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_repayment_showproject_item,viewGroup,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<ProjectResult.PlanDataListBean.DetailsBean> detailsBeanList) {
        viewHolder.binding.setDate(detailsBeanList.get(0).deExecuteDay);
        double totalMoney = 0 , totalFee = 0;
        viewHolder.binding.linear.removeAllViews();

        for (int index=0; index < detailsBeanList.size(); index++){
            ProjectResult.PlanDataListBean.DetailsBean detailsBean = detailsBeanList.get(index);

            if (detailsBean.deType == 1){
                totalMoney = detailsBean.dePayMoney;
                totalFee = detailsBean.dePayFee;
                viewHolder.binding.setDetail(detailsBean);
            }else {
                LinearLayout layout = new LinearLayout(BaseApplication.context);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);

                //时间
                Date date = null;
                try {
                    date = informat.parse(detailsBean.deExecuteDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                layout.addView(makeTextCompone(outformat.format(date),1));

                //消费金额
                String consumeMoney = (detailsBean.deType==0?"消费":"延期")+"/元："+DataUtils.numFormat(detailsBean.dePayMoney);
                layout.addView(makeTextCompone(consumeMoney,2));

                //手续费
                String fee = "手续费/元："+DataUtils.numFormat(detailsBean.dePayFee);
                layout.addView(makeTextCompone(fee,2));

                viewHolder.binding.linear.addView(layout);
            }

        }
        viewHolder.binding.setTotalMoney(DataUtils.numFormat(totalMoney));
        viewHolder.binding.setTotalFee(DataUtils.numFormat(totalFee));

        viewHolder.binding.nullLinear.setVisibility((position == getDataList().size() -1)?View.VISIBLE:View.GONE);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRepaymentShowprojectItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    public TextView makeTextCompone(String str,int weight){
        TextView textView = new TextView(BaseApplication.context);
        textView.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.light_gray));
        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        textparams.topMargin = textparams.bottomMargin = DataUtils.dipToPx(BaseApplication.context,2);
        textparams.weight = weight;
        textView.setTextSize(12);//sp
        textView.setLayoutParams(textparams);
        textView.setText(str);
        if (str.contains("手")){
            textView.setGravity(Gravity.END);
        }else if (str.contains("消") || str.contains("延")){
            textView.setGravity(Gravity.CENTER);
        }
        return textView;
    }


    public void notifyDataItem(){

        notifyItemRangeChanged(0,getItemCount());
    }
}
