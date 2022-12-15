package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutPopwindowPushBinding;
import cn.mofufin.morf.databinding.LayoutPopwindowShareBinding;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform;

public class MerchanPushPopWindow extends Dialog {
    private LayoutPopwindowPushBinding binding;

    public MerchanPushPopWindow(Context mContext){
        super(mContext, R.style.PopBottomDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindow_push,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        setContentView(view);
        setCanceledOnTouchOutside(true);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialogWindow.setAttributes(lp);

    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        binding.setOnclickListener(onClickListener);
    }

    public void cancel(View view){
        dismiss();
    }

}
