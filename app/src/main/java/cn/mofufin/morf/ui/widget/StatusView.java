package cn.mofufin.morf.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;

public class StatusView extends AppCompatTextView {
    private static final int TYPE_SUBMITTED = 0;
    private static final int TYPE_NOTSUBMITTED = 1;
    private static final int TYPE_NOTPASS = 2;
    public int width = 150;
    public int height = 150;
    private Paint circlePaint;
//    private Paint textPaint;
    public int types;
    float textWidth;
    float textHeight;
//    public StatusView(Context context) {
//        super(context);
//        init(context,null);
//        Logger.e("StatusView(Context context)");
//    }


    public void setTypes(int types) {
        this.types = types;
        invalidate();
    }

    public StatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
        Logger.e("StatusView(Context context, @Nullable AttributeSet attrs)");
    }

//    public StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context,attrs);
//        Logger.e("StatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)");
//    }

    public void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatusView,0,0);
        types = typedArray.getInteger(R.styleable.StatusView_type,0);
//        Logger.e("StatusView(Context context) type="+type);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);

//        textPaint = new Paint();
//        textPaint.setAntiAlias(true);
//        textPaint.setTextSize(15);
//        textPaint.setFakeBoldText(true);

        String text="";
        switch (types) {
            case TYPE_SUBMITTED:
                circlePaint.setColor(ContextCompat.getColor(context, R.color.app_blue));
//                textPaint.setColor(ContextCompat.getColor(context, R.color.app_blue));
                setTextColor(ContextCompat.getColor(context, R.color.app_blue));
                text = "已提交";
                break;

            case TYPE_NOTSUBMITTED:
                circlePaint.setColor(ContextCompat.getColor(context, R.color.ok));
//                textPaint.setColor(ContextCompat.getColor(context, R.color.ok));
                setTextColor(ContextCompat.getColor(context, R.color.ok));
                text = "未提交";
                break;

            case TYPE_NOTPASS:
                circlePaint.setColor(ContextCompat.getColor(context, R.color.loan_red));
//                textPaint.setColor(ContextCompat.getColor(context, R.color.loan_red));
                setTextColor(ContextCompat.getColor(context, R.color.loan_red));
                text = "未通过";
                break;

            default:
                break;
        }

        setText(text);

         textWidth = 100;
         textHeight = 70;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(0,0,textWidth,textHeight,20,20,circlePaint);
        super.onDraw(canvas);
    }


}
