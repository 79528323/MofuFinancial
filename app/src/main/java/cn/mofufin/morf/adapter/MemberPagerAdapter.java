package cn.mofufin.morf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cc.ruis.lib.adapter.BannerAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.MemberBanner;
import cn.mofufin.morf.databinding.LayoutMemberPagerItemBinding;

public class MemberPagerAdapter extends BannerAdapter<MemberBanner, MemberPagerAdapter.ViewHolder> {
    private int membertype;

    public MemberPagerAdapter(Context context, List<MemberBanner> list) {
        super(context, list);
        membertype = MerchanInfoDB.queryInfo().memberType;
    }

    @Override
    protected MemberPagerAdapter.ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View view=layoutInflater.inflate(R.layout.layout_member_pager_item, null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(int position, MemberBanner banner, MemberPagerAdapter.ViewHolder viewHolder) {
        viewHolder.binding.bannerView.setImageResource(banner.resId);
        viewHolder.binding.setName(banner.memberName);
        viewHolder.binding.setMembertype(membertype);
        viewHolder.binding.setLevel(position+1);
        viewHolder.binding.executePendingBindings();
    }

    class ViewHolder extends BannerAdapter.ViewHolder{
        LayoutMemberPagerItemBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding=LayoutMemberPagerItemBinding.bind(view);
        }
    }
}
