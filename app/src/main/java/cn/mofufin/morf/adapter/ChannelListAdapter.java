package cn.mofufin.morf.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.adapter.BaseRecyclerAdapter;
import cc.ruis.lib.event.RxManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.base.BaseUI;
import cn.mofufin.morf.ui.entity.Channel;
import cn.mofufin.morf.ui.services.QueryChannelImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.databinding.LayoutChannelItemBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 快捷通道
 */
public class ChannelListAdapter extends BaseRecyclerAdapter<ChannelListAdapter.ViewHolder,Channel.ChannelListBean>{
    private BaseUI view;
    private View.OnClickListener clickListener;
    private List<Boolean> btnCheck = new ArrayList<Boolean>();
    private LayoutInflater layoutInflater;
    private RxManager rxManager;
    private int horizontalCount=0;
    private int item_position= -1;

    public ChannelListAdapter(BaseUI view) {
        this.view = view;
        rxManager = view.getRxManager();
    }

    public void setHorizontalCount(int horizontalCount) {
        this.horizontalCount = horizontalCount;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void refresh(List<Channel.ChannelListBean> list) {
        for (int index=0; index<list.size(); index++)
            btnCheck.add(false);
        super.refresh(list);
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup,final int position) {
        this.layoutInflater = layoutInflater;
        return new ViewHolder(layoutInflater.inflate(R.layout.layout_channel_item,viewGroup,false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int position, final Channel.ChannelListBean channelListBean) {
        viewHolder.binding.setChannel(channelListBean);
        viewHolder.binding.setPosition(position);
        viewHolder.binding.setIsCheck(btnCheck.get(position));
        viewHolder.binding.setClicklistener(clickListener);
        if (viewHolder.binding.supportBankList.getLayoutManager()==null)
            viewHolder.binding.supportBankList.setLayoutManager(new GridLayoutManager(BaseApplication.context,horizontalCount));

        if (!channelListBean.getBankList().isEmpty()){
            SupportBankListAdapter supportBankListAdapter = new SupportBankListAdapter();
            supportBankListAdapter.setBankListBeans(channelListBean.getBankList());
            viewHolder.binding.supportBankList.setAdapter(supportBankListAdapter);
        }

        viewHolder.binding.bankCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnCheck.get(position)){//todo 选中展开ITEM时
                    btnCheck.set(position,false);
                    viewHolder.binding.setIsCheck(btnCheck.get(position));
                    notifyItemChanged(position);
                    item_position = -1;//复原点击记录
                    return;
                }

                resetItem(item_position,viewHolder);//todo 复原列表ITEM状态

                item_position = position;//记住当前点击记录
                btnCheck.set(item_position,true);
                //TODO 展开被点击的ITEM
                if (!channelListBean.bankList.isEmpty()){//直接判断listBean的银行列表 为空去网络获取，否则直接展示
                    handler.sendEmptyMessage(1);
                    viewHolder.binding.setIsCheck(btnCheck.get(item_position));
                    notifyItemChanged(item_position);
                    handler.sendEmptyMessage(0);
                }else {
                    //网络获取列表
                    Subscription subscription = QueryChannelImpAPI.querychannelSupportBankList(
                            HttpParam.OFFICE_CODE,HttpParam.QUERY_CHANNEL_SUPPORT_BANK_LIST_APPKEY,
                            channelListBean.tcType)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    view.showLoading();
                                }
                            }).doOnCompleted(new Action0() {
                                @Override
                                public void call() {
                                    view.hiddenLoading();
                                }
                            })
                            .subscribe(new Action1<Channel>() {
                                @Override
                                public void call(Channel channel) {
                                    List<Channel.BankListBean> listBeans = channel.getBankList();
                                    SupportBankListAdapter supportBankListAdapter = new SupportBankListAdapter();
                                    supportBankListAdapter.setBankListBeans(listBeans);
                                    viewHolder.binding.supportBankList.setAdapter(supportBankListAdapter);
                                    viewHolder.binding.setIsCheck(btnCheck.get(item_position));
                                    notifyItemChanged(item_position);
                                    channelListBean.setBankList(listBeans);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    view.onError(throwable,true);
                                }
                            });
                    rxManager.add(subscription);
                }
            }
        });
    }


    class ViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{
        LayoutChannelItemBinding binding;

        public ViewHolder(View view){
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                view.showLoading();
            }else
                view.hiddenLoading();
        }
    };

    /**
     * 复原列表ITEM状态
     */
    public void resetItem(final int position,ViewHolder holder){
        if (position >= 0){
            btnCheck.set(position,false);
            holder.binding.setIsCheck(btnCheck.get(position));
            notifyItemChanged(position);
        }
    }


//
//    private void openAnim(View v,int position,ViewHolder viewHolder) {
////        v.setVisibility(View.VISIBLE);
//        int mHeight = unDisplayViewSize(viewHolder.binding.supportBankList)[1];
//        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
//        animator.start();
//    }
//
//
//    private void closeAnimate(final View view,final int position,final ViewHolder viewHolder) {
//        int origHeight = view.getHeight();
//        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                viewHolder.binding.setIsCheck(false);
//            }
//        });
//        animator.start();
//    }
//
//    private ValueAnimator createDropAnimator(final View v, int start, int end) {
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator arg0) {
//                int value = (int) arg0.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
//                layoutParams.height = value;
//                v.setLayoutParams(layoutParams);
//            }
//        });
//        return animator;
//    }


//    public int[] unDisplayViewSize(View view) {
//        int size[] = new int[2];
//        int width = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int height = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        view.measure(width, height);
//        size[0] = view.getMeasuredWidth();
//        size[1] = view.getMeasuredHeight();
//        return size;
//    }

    /**
     * TODO 支持银行列表适配器
     */
    private class SupportBankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<Channel.BankListBean> bankListBeans =new ArrayList<>();

        public SupportBankListAdapter() {
        }

        public void setBankListBeans(List<Channel.BankListBean> bankListBeans) {
            this.bankListBeans.clear();
            this.bankListBeans = bankListBeans;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemViewHolder(layoutInflater.inflate(R.layout.layout_spport_bank_item,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            holder.bankname.setText(DataUtils.subString(bankListBeans.get(i).cbName,4)+"("+bankListBeans.get(i).cbNumber+")");
        }

        @Override
        public int getItemCount() {
            return this.bankListBeans.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView bankname;

            public ItemViewHolder(View itemView) {
                super(itemView);
                bankname = itemView.findViewById(R.id.bankname);
            }
        }
    }
}
