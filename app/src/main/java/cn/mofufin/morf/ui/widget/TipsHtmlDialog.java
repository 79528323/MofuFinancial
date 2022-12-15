package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.mofufin.morf.R;

/**
 * 提示弹窗
 */

public class TipsHtmlDialog extends Dialog implements View.OnClickListener {
    private View contentview;
    private TextView msgTv ,btn ,cancel;
    private OnButtonClickListener listener;

    public TipsHtmlDialog(Context context, String tipsMsg, String btStr, String neivation, OnButtonClickListener listener) {
        super(context);
        this.listener = listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        contentview = LayoutInflater.from(context).inflate(R.layout.tips_dialog, null);
        ((LinearLayout)contentview.findViewById(R.id.bg)).setBackgroundResource(R.drawable.shape_white_radius);
        msgTv = (TextView) contentview.findViewById(R.id.msg_tv);
        msgTv.setTextColor(Color.DKGRAY);
        msgTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        msgTv.setText(TextUtils.isEmpty(tipsMsg) ? "请输入提示内容" : tipsMsg);

        btn = (TextView) contentview.findViewById(R.id.button_tv);
        btn.setText(TextUtils.isEmpty(btStr) ? "确定" : btStr);
        btn.setOnClickListener(this);

        if (!TextUtils.isEmpty(neivation)){
            cancel = (TextView) contentview.findViewById(R.id.button_nv);
            cancel.setText(neivation);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }


        setContentView(contentview);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        lp.width = (metrics.widthPixels/8) * 6;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.buttonClicked();
        }
        dismiss();
    }

    public interface OnButtonClickListener {
        void buttonClicked();
    }
}
