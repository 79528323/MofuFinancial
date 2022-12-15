package cn.mofufin.morf.adapter;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutRepaymentDetailsitemsItemBinding;
import cn.mofufin.morf.databinding.LayoutRepaymentShowprojectItemBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.ProjectDetails;
import cn.mofufin.morf.ui.entity.ProjectResult;
import cn.mofufin.morf.ui.util.DataUtils;

public class RepayMentProjectDetailsAdapter extends BaseRecyclerAdapter<RepayMentProjectDetailsAdapter.ViewHolder, List<ProjectDetails.PlanDetailsListBean>> {
    private LayoutInflater inflater;

    public RepayMentProjectDetailsAdapter() {
        inflater = LayoutInflater.from(BaseApplication.context);
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_repayment_detailsitems_item,viewGroup,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<ProjectDetails.PlanDetailsListBean> planDetailsListBeanList) {
        viewHolder.binding.setBean(planDetailsListBeanList.get(0));
        viewHolder.binding.itemLayout.removeAllViews();
        viewHolder.binding.nullLinear.setVisibility(position+1 == getItemCount()?View.VISIBLE:View.GONE);

        long starttime = System.currentTimeMillis();
        if (viewHolder.binding.itemLayout.getChildCount() <= 0){
            for (int index=0; index < planDetailsListBeanList.size(); index++){
                ProjectDetails.PlanDetailsListBean detailsBean = planDetailsListBeanList.get(index);

                View layout = inflater.inflate(R.layout.layout_repayment_details_items_layout,null);
                TextView day = layout.findViewById(R.id.time);
                String times=detailsBean.deExecuteDate;
                day.setText(times.substring(0,times.lastIndexOf(":")));
                if (detailsBean.deType == 1){//延期颜色加深
                    day.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
                }

                TextView consume = layout.findViewById(R.id.total);
                consume.setText((detailsBean.deType==0?"消费":"延期")+"/元："+DataUtils.numFormat(detailsBean.dePayMoney));

                TextView fee = layout.findViewById(R.id.fee);
                fee.setText((detailsBean.deType==0?"消费":"延期")+"手续费/元："+DataUtils.numFormat(detailsBean.dePayFee));

                TextView status = layout.findViewById(R.id.status);
                String text="";
                int color = 0;
                switch (detailsBean.deState){
                    case 0:
                        text=BaseApplication.context.getString(R.string.project_stay);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.game_hot);
                        break;

                    case 1:
                        text=BaseApplication.context.getString(R.string.project_fail);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.fail);
                        break;

                    case 2:
                        text=BaseApplication.context.getString(R.string.project_finish);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.dark_gray);
                        break;

                    case 3:
                        text=BaseApplication.context.getString(R.string.project_handle_cancel);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.fail);
                        break;

                    case 4:
                        text=BaseApplication.context.getString(R.string.project_re_processing);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.app_blue);
                        break;

                    case 5:
                        text=BaseApplication.context.getString(R.string.project_re_ok);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.dark_gray);
                        break;

                    case 6:
                        text=BaseApplication.context.getString(R.string.project_re_fail);
                        color = ContextCompat.getColor(BaseApplication.context,R.color.fail);
                        break;
                }
                status.setText(text);
                status.setTextColor(color);

                TextView commission = layout.findViewById(R.id.commission);
                commission.setText(BaseApplication.context.getString(R.string.project_detials_1,detailsBean.deIsRebate==0?"是":"否"));

                viewHolder.binding.itemLayout.addView(layout);
                day.measure(0,0);
                int width = day.getMeasuredWidth();
                if (index +1 < planDetailsListBeanList.size()){
                    ImageView line = new ImageView(BaseApplication.context);
                    line.setBackground(BaseApplication.context.getDrawable(R.drawable.repay_item_dottedline));
                    line.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,6);
                    params.leftMargin = (int) (width + (BaseApplication.context.getResources().getDimension(R.dimen.default_margin))*5);
                    line.setLayoutParams(params);

                    viewHolder.binding.itemLayout.addView(line);
                }
            }
        }


        long endtime = (System.currentTimeMillis() - starttime);
        if (endtime > 1000){
            endtime = endtime/1000;
        }
        Logger.e("第"+position+" 用时："+endtime);
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRepaymentDetailsitemsItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
