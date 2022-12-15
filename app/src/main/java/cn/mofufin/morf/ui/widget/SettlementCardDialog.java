package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.Iterator;
import java.util.List;

import cn.mofufin.morf.adapter.SetttleMentCardDialogAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.mine.CreditCardAddActivity;
import cn.mofufin.morf.ui.mine.SettlementCardAddActivity;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.databinding.LayoutDialogSettlementCardBinding;

public class SettlementCardDialog extends Dialog {
    private LayoutDialogSettlementCardBinding binding;
    private Context mContext;
    private SetttleMentCardDialogAdapter adapter;
    private List<User.DataBean.CardInfoBean> cardInfoBeanList;
    private int type=0;

    public SettlementCardDialog(Context context) {
        super(context, R.style.PopBottomDialogStyle);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_settlement_card,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        setContentView(binding.getRoot());
        initView();
    }

    public SettlementCardDialog(Context context,int type) {
        super(context, R.style.PopBottomDialogStyle);
        mContext = context;
        this.type = type;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_settlement_card,null);
        binding = DataBindingUtil.bind(view);
        setContentView(binding.getRoot());
        binding.setHandlers(this);

        initView();
    }

    public void initView(){
        binding.setType(type);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        adapter = new SetttleMentCardDialogAdapter();
        adapter.setType(type);
        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                adapter.notifyItemChange(pos);

                User.DataBean.CardInfoBean bean = (User.DataBean.CardInfoBean) ((ViewGroup)v).getChildAt(0).getTag();
                listener.onClickListener(bean);

                dismiss();
            }
        });
        binding.settelCardList.setLayoutManager(new LinearLayoutManager(mContext));
        binding.settelCardList.setAdapter(adapter);

        setCanceledOnTouchOutside(true);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;

        int height = metrics.heightPixels;
        lp.height = height/2+(height/6);
        mDialogWindow.setAttributes(lp);
    }

    //添加结算卡
    public void settleAdd(View view){
        Class<?> cls;
        if (type == 0){
            cls = SettlementCardAddActivity.class;
        }else
            cls = CreditCardAddActivity.class;
        Intent intent = new Intent(mContext,cls);
        mContext.startActivity(intent);
        dismiss();
    }

    public void cancel(View view){
        dismiss();
    }

    public void showDialog(){
//        List<User.DataBean.CardInfoBean> list = CardInfoDB.queryCardALL();
        if (cardInfoBeanList!=null && !cardInfoBeanList.isEmpty()){
            Iterator<User.DataBean.CardInfoBean> iterator = cardInfoBeanList.iterator();
            int cardType = type==0?2:1;
            while (iterator.hasNext()){
                if (iterator.next().cardType==cardType)
                    iterator.remove();
            }
            adapter.refresh(cardInfoBeanList);
        }

        show();
    }



    public onSettleCardClickListener listener;

    public void setListener(onSettleCardClickListener listener) {
        this.listener = listener;
    }

    public interface onSettleCardClickListener{
        void onClickListener(User.DataBean.CardInfoBean bean);
    }

    public void setCardInfoBeanList(List<User.DataBean.CardInfoBean> cardInfoBeanList) {
        this.cardInfoBeanList = cardInfoBeanList;
    }
}
