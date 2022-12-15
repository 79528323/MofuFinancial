package cn.mofufin.morf.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cn.mofufin.morf.BR;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.entity.MerchantBag;
import cn.mofufin.morf.databinding.LayoutCurrencyCouponItemBinding;

public class BackpackUsedExpiredAdapter extends BaseRecyclerAdapter<BackpackUsedExpiredAdapter.MerchantBagViewHolder,
        MerchantBag.DataBean.ListBean> {
    private static final int TYPE_MEMBER_COUPON=0x01;
    private static final int TYPE_INTEREST_COUPON=0x02;
    private static final int TYPE_TELEPHONE_COUPON=0x03;
    private static final int TYPE_CURRENCY_COUPON=0x04;
    private static final int TYPE_COUPON_COUPON=0x05;

    private View.OnClickListener onClickListener;
    private Context context;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        context = BaseApplication.context;
    }

    @Override
    public int getItemViewType(int position) {
        MerchantBag.DataBean.ListBean listBean = getItemData(position);
        if (listBean.gdGoodsType == 0){
            if (listBean.getGdGoodsBranchType() == 1 || listBean.getGdGoodsBranchType() == 2
                    ||listBean.getGdGoodsBranchType() == 3 || listBean.getGdGoodsBranchType() == 5
                    || listBean.getGdGoodsBranchType() == 6){
                return TYPE_COUPON_COUPON;
            }else if (listBean.getGdGoodsBranchType() == 8 || listBean.getGdGoodsBranchType() == 9
                    ||listBean.getGdGoodsBranchType() == 10 || listBean.getGdGoodsBranchType() == 11
                    || listBean.getGdGoodsBranchType() == 12|| listBean.getGdGoodsBranchType() == 13
                    || listBean.getGdGoodsBranchType() == 0){
                return TYPE_INTEREST_COUPON;
            }else if (listBean.getGdGoodsBranchType() == 7){
                return TYPE_CURRENCY_COUPON;
            }else if (listBean.getGdGoodsBranchType() == 4){
                return TYPE_TELEPHONE_COUPON;
            }else if (listBean.getGdGoodsBranchType() == 14 || listBean.getGdGoodsBranchType() == 15){
                return TYPE_MEMBER_COUPON;
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public MerchantBagViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        switch (type){
            case TYPE_COUPON_COUPON:
                return new CouponViewHolder(
                        layoutInflater.inflate(R.layout.layout_coupon_item_usexpd,viewGroup,false));

            case TYPE_INTEREST_COUPON:
                return new InterestViewHolder(
                        layoutInflater.inflate(R.layout.layout_interest_coupon_item_usexpd,viewGroup,false));

            case TYPE_CURRENCY_COUPON:
                return new CurrencyViewHolder(
                        layoutInflater.inflate(R.layout.layout_currency_coupon_item,viewGroup,false));
//
//            case TYPE_TELEPHONE_COUPON:
//                return new CouponViewHolder(
//                        layoutInflater.inflate(R.layout.layout_telephone_coupon_item,viewGroup,false));
//
            case TYPE_MEMBER_COUPON:
                return new MemberViewHolder(
                        layoutInflater.inflate(R.layout.layout_member_coupon_item_usexpd,viewGroup,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(MerchantBagViewHolder merchantBagViewHolder, int position, MerchantBag.DataBean.ListBean listBean) {
        merchantBagViewHolder.binding.setVariable(BR.listbean,listBean);
        merchantBagViewHolder.binding.executePendingBindings();
    }

    class MerchantBagViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        ViewDataBinding binding;

        public MerchantBagViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public void bind(int position ,MerchantBag.DataBean.ListBean listBean){
        }
    }


    class CouponViewHolder extends MerchantBagViewHolder{

        public CouponViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(int position, MerchantBag.DataBean.ListBean listBean) {
            super.bind(position, listBean);
            binding.setVariable(BR.listbean,listBean);
        }
    }

    class InterestViewHolder extends MerchantBagViewHolder{

        public InterestViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(int position, MerchantBag.DataBean.ListBean listBean) {
            super.bind(position, listBean);
            binding.setVariable(BR.listbean,listBean);
        }
    }


    class MemberViewHolder extends MerchantBagViewHolder{

        public MemberViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(int position, MerchantBag.DataBean.ListBean listBean) {
            super.bind(position, listBean);
            binding.setVariable(BR.listbean,listBean);
        }
    }

    class CurrencyViewHolder extends MerchantBagViewHolder {
        LayoutCurrencyCouponItemBinding binding;

        public CurrencyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        @Override
        public void bind(int position, MerchantBag.DataBean.ListBean listBean) {
            super.bind(position, listBean);
            binding.setVariable(BR.listbean,listBean);
        }
    }

}
