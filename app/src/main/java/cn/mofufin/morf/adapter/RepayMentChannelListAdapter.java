package cn.mofufin.morf.adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cc.ruis.lib.event.RxManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutChannelItemBinding;
import cn.mofufin.morf.databinding.LayoutRepaymentChannelItemBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.base.BaseUI;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.entity.RepayChannel;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 卡延期通道
 */
public class RepayMentChannelListAdapter extends BaseRecyclerAdapter<RepayMentChannelListAdapter.ViewHolder, RepayChannel.ChannelListBean>{
    private View.OnClickListener clickListener,supportListener;
    private int curType=0;

    public RepayMentChannelListAdapter() {
        curType = MerchanInfoDB.queryInfo().memberType;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setSupportListener(View.OnClickListener supportListener) {
        this.supportListener = supportListener;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup,final int position) {
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_repayment_channel_item,viewGroup,false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position, final RepayChannel.ChannelListBean bean) {
        viewHolder.binding.setBean(bean);
        viewHolder.binding.setPosition(position);
        viewHolder.binding.setCurType(curType);
        viewHolder.binding.setClicklistener(clickListener);
        viewHolder.binding.setGetSupportBank(supportListener);
    }


    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutRepaymentChannelItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

}
