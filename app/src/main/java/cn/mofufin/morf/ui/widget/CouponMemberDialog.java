package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.LayoutDialogCouponMemberBinding;

public class CouponMemberDialog extends Dialog {
    private LayoutDialogCouponMemberBinding binding;
    private Context mContext;
    private List<MerchantBag.DataBean.ListBean> listBeans;

    private MerchantBag.DataBean.ListBean tag=null;

    public CouponMemberDialog(Context context) {
        super(context, R.style.PopBottomDialogStyle);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_coupon_member,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        setContentView(binding.getRoot());

        setCanceledOnTouchOutside(true);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mDialogWindow.setAttributes(lp);

        binding.setClicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag()==null)
                    return;

                tag = (MerchantBag.DataBean.ListBean) v.getTag();
                binding.goldCheck.setChecked(false);
                binding.daimondCheck.setChecked(false);
                if (tag.gdGoodsBranchType == 14){
                    binding.goldCheck.setChecked(true);
                }else
                    binding.daimondCheck.setChecked(true);
            }
        });
    }

    public void upgrade(View view){
        if (tag==null){
            Toast.makeText(mContext,"请选择优惠卷",Toast.LENGTH_SHORT).show();
            return;
        }

        dismiss();
        RxBus.getInstance().post(RxEvent.MEMBER_COUPON_UPGRADE,tag);
    }

    public void cancel(View view){
        dismiss();
    }

    public void refreshData(List<MerchantBag.DataBean.ListBean> listBeans){
        this.listBeans = listBeans;
        for (int i=0; i<this.listBeans.size(); i++){
            MerchantBag.DataBean.ListBean bean = listBeans.get(i);
            if (bean.getGdGoodsBranchType() == 14){
                binding.goldCoupon.setVisibility(View.VISIBLE);
                binding.goldCoupon.setTag(bean);
            }

            if (bean.getGdGoodsBranchType() == 15){
                binding.diamondCoupon.setVisibility(View.VISIBLE);
                binding.diamondCoupon.setTag(bean);
            }
        }

        binding.executePendingBindings();
    }

    public void showDialog(){
        show();
    }


}
