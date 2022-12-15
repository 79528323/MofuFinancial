package cn.mofufin.morf.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import cn.mofufin.morf.R;

public class TextButtonWidget extends AppCompatTextView {
    private Context mContext;

    public TextButtonWidget(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("ResourceType")
    public TextButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.TextButtonWidget,0,0);
        boolean isEnable=array.getBoolean(R.styleable.TextButtonWidget_enables,false);
        setEnabled(isEnable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        setTextEnableView(enabled);
        super.setEnabled(enabled);
    }

    public void setTextEnableView(boolean enable){
        if (enable){
            setTextColor(ContextCompat.getColor(mContext, R.color.white));
            setBackground(mContext.getDrawable(R.drawable.login_button));
        }else {
            setTextColor(ContextCompat.getColor(mContext, R.color.light_gray));
            setBackground(mContext.getDrawable(R.drawable.login_disable_button));
        }
    }
}
