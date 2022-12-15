package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.databinding.LayoutOrderItemBinding;

public class ReceiVablesAdapter extends BaseRecyclerAdapter<ReceiVablesAdapter.RecViewHolder,Order.OrderDetails> {
    private int findType;
    private View.OnClickListener clickListener;

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setFindType(int findType) {
        this.findType = findType;
    }

    @Override
    public RecViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new RecViewHolder(layoutInflater.inflate(R.layout.layout_order_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecViewHolder viewHolder, int position, Order.OrderDetails orderDetails) {
        viewHolder.binding.setDetail(orderDetails);
        viewHolder.binding.setType(findType);
        if (findType==2){
            int paytype = Integer.valueOf(orderDetails.payType);
            viewHolder.binding.setPaytype(paytype);
            switch (paytype){
                case 10:
                    viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_we));
                    break;

                case 20:
                    viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_ali));
                    break;

                    default:
                        viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.white));
            }
            if (TextUtils.equals("S",orderDetails.transStat)){
                viewHolder.binding.status.setText(BaseApplication.context.getString(R.string.success));
                viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
            }else if (TextUtils.equals("I",orderDetails.transStat)){
                viewHolder.binding.status.setText("未支付");
                viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
            }else{
                viewHolder.binding.status.setText(BaseApplication.context.getString(R.string.fail));
                viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
            }

        }else if (findType ==1){
            viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_M));
            if (TextUtils.equals("00",orderDetails.resultCode)){
                viewHolder.binding.status.setText(BaseApplication.context.getString(R.string.success));
                viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
            }else{
                viewHolder.binding.status.setText(BaseApplication.context.getString(R.string.fail));
                viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
            }

        }else if (findType == 3){
            Logger.e("genPayMoney "+orderDetails.genPayMoney);
            viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
            String status="";
            switch (orderDetails.status){
                case "1":
                    status = "未支付";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
                    break;

                case "2":
                    status = "待支付";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
                    break;

                case "3":
                    status = "支付中";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_ali));
                    break;

                case "4":
                    status = "支付成功";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
                    break;

                case "5":
                    status = "支付失败";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
                    break;

                case "6":
                    status = "未知";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
                    break;

                case "7":
                    status = "退款成功";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
                    break;

                case "8":
                    status = "退款进行中";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_ali));
                    break;

                case "9":
                    status = "退款失败";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_short));
                    break;

            }
            viewHolder.binding.status.setText(status);
        }else if (findType == 4){
            viewHolder.binding.icon.setStrokeColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
            String status="";
            switch (orderDetails.ovPayState){
                case 0:
                    status = "未支付";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
                    break;

                case 1:
                    status = "支付失败";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_over));
                    break;

                case 2:
                    status = "支付成功等待回款";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.stroke_ali));
                    break;

                case 3:
                    status = "支付成功已回款";
                    viewHolder.binding.status.setTextColor(ContextCompat.getColor(BaseApplication.context,R.color.dark_gray));
                    break;
            }

            viewHolder.binding.status.setText(status);
        }
        viewHolder.binding.setClicklistener(this.clickListener);
//        viewHolder.binding.executePendingBindings();
    }

    class RecViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutOrderItemBinding binding;

        public RecViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
