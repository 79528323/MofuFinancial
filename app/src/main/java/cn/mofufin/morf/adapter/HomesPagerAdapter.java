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
import cn.mofufin.morf.ui.entity.Banner;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.databinding.LayoutPagerItemBinding;

public class HomesPagerAdapter extends BannerAdapter<Banner, HomesPagerAdapter.ViewHolder> {

    public Context context;
    public View.OnClickListener clickListener;

    public HomesPagerAdapter(Context context, List<Banner> list) {
        super(context, list);
        this.context = context;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected HomesPagerAdapter.ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View view=layoutInflater.inflate(R.layout.layout_pager_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(int position, Banner banner, HomesPagerAdapter.ViewHolder viewHolder) {
        String url = Common.BANNER_HOST + banner.imgUrl;
        Logger.e("url=="+url);
        Glide.with(context).load(url).apply(new RequestOptions().centerInside()).into(viewHolder.binding.bannerView);
        if (!TextUtils.isEmpty(banner.href)){
            viewHolder.binding.setPos(position);
            viewHolder.binding.setOnClick(clickListener);
        }
    }

    class ViewHolder extends BannerAdapter.ViewHolder{
        LayoutPagerItemBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding=LayoutPagerItemBinding.bind(view);
        }
    }
}
