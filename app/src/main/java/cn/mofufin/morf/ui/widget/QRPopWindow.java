package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.MerChantScanQRActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.LayoutPopwindowQrBinding;

public class QRPopWindow extends Dialog {
    private LayoutPopwindowQrBinding binding;
    private Context context;
    private View view;
    private User.DataBean.MerchantInfoBean bean;

    public QRPopWindow(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public QRPopWindow(Context mContext){
        super(mContext, R.style.PopBottomDialogStyle);
        this.context = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindow_qr,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        setContentView(this.view);
        setCanceledOnTouchOutside(true);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialogWindow.setAttributes(lp);

        bean = MerchanInfoDB.queryInfo();
        binding.setClicklistener(clickListener);
        binding.alipayCheck.setChecked(true);
        binding.wechatCheck.setChecked(false);
    }

    public void setCheck(int flag){
        binding.alipayCheck.setChecked(false);
        binding.wechatCheck.setChecked(false);
        if (flag== MerChantScanQRActivity.QR_ALIPAY){
            binding.alipayCheck.setChecked(true);
        }else if (flag == MerChantScanQRActivity.QR_WECHAT){
            binding.wechatCheck.setChecked(true);
        }
    }

    public void showPopwindow(){
        setCheck(MerChantScanQRActivity.QR_ALIPAY);
    }

    public void cancel(View view){
        dismiss();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int flag = Integer.valueOf((String) v.getTag());
            setCheck(flag);
            RxBus.getInstance().post(RxEvent.TRANSFER_PARAMETER,flag);
            dismiss();
        }
    };
}
