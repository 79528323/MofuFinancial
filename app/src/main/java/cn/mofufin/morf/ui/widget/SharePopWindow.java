package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform;
import cn.mofufin.morf.databinding.LayoutPopwindowShareBinding;

public class SharePopWindow extends Dialog {
    private LayoutPopwindowShareBinding binding;
    private Context context;
    private final static String SHARE_URL ="http://mofufin.cn" + Common.HOSTTYPE+"/Share/shareRegist?code=";
    private View view;
    private User.DataBean.MerchantInfoBean bean;
    private BitmapDrawable drawable = null;

    public SharePopWindow(Context mContext){
        super(mContext, R.style.PopBottomDialogStyle);
        this.context = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindow_share,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        drawable = (BitmapDrawable) ContextCompat.getDrawable(this.context, R.drawable.icon_share_mask);

        setContentView(this.view);
        setCanceledOnTouchOutside(true);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialogWindow.setAttributes(lp);

        bean = MerchanInfoDB.queryInfo();
    }

    public void cancel(View view){
        dismiss();
    }

    public void weixin(View view){

        WechatPlatform.shareSessionURL(this.context,SHARE_URL + bean.merchantCode
                ,this.context.getString(R.string.wechat_share_title)
                ,this.context.getString(R.string.invitation_text8)
                ,drawable.getBitmap());
    }

    public void dynamic(View view){

        WechatPlatform.shareMomentsURL(this.context,SHARE_URL + bean.merchantCode
                ,this.context.getString(R.string.wechat_share_title)
                ,this.context.getString(R.string.invitation_text8)
                ,drawable.getBitmap());
    }
}
