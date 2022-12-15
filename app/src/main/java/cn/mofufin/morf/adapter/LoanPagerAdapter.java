package cn.mofufin.morf.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cc.ruis.lib.adapter.BannerAdapter;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutLoanPagerItemBinding;
import cn.mofufin.morf.databinding.LayoutPagerItemBinding;
import cn.mofufin.morf.ui.entity.Banner;
import cn.mofufin.morf.ui.util.Common;

public class LoanPagerAdapter extends BannerAdapter<Banner, LoanPagerAdapter.ViewHolder> {

    public Context context;
    public View.OnClickListener clickListener;

    public LoanPagerAdapter(Context context, List<Banner> list) {
        super(context, list);
        this.context = context;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected LoanPagerAdapter.ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View view=layoutInflater.inflate(R.layout.layout_loan_pager_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(int position, Banner banner, LoanPagerAdapter.ViewHolder viewHolder) {
        String url = Common.BANNER_HOST+banner.imgUrl;
        Glide.with(context).load(url).apply(new RequestOptions().centerInside()).into(viewHolder.binding.bannerView);
        if (!TextUtils.isEmpty(banner.href)){
            viewHolder.binding.setPos(position);
            viewHolder.binding.setOnClick(clickListener);
        }
    }

    class ViewHolder extends BannerAdapter.ViewHolder{
        LayoutLoanPagerItemBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding=LayoutLoanPagerItemBinding.bind(view);
        }
    }
}
