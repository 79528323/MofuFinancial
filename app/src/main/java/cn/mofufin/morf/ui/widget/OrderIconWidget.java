package cn.mofufin.morf.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Build;
import android.util.AttributeSet;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;

public class OrderIconWidget extends AppCompatTextView {
    //填充色
    private int solidColor = 0;
    //边框色
    private int strokeColor = 0;
    //按下填充色
    private int solidTouchColor = 0;
    //按下边框色
    private int strokeTouchColor = 0;
    //边框宽度
    private int strokeWith = 0;
    private int shapeTpe = 0;
    //按下字体色
    private int textTouchColor=0;
    //字体色
    private int textColor=0;

    private int orderText=0;
    private int orderPaytype=0;

    //圆角的半径
    float radius =0;
    float topLeftRadius=0;
    float topRightRadius=0;
    float bottomLeftRadius=0;
    float bottomRightRadius=0;

    public OrderIconWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @SuppressLint("ResourceType")
    public void init(Context context, AttributeSet attrs){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.order_icon, 0, 0);

        solidColor = ta.getInteger(R.styleable.order_icon_solidColor, 0x00000000);
        strokeColor = ta.getInteger(R.styleable.order_icon_strokeColor, 0x00000000);

        solidTouchColor = ta.getInteger(R.styleable.order_icon_solidTouchColor, 0x00000000);
        strokeTouchColor = ta.getInteger(R.styleable.order_icon_strokeTouchColor, 0x00000000);
        textTouchColor= ta.getInteger(R.styleable.order_icon_textTouchColor, 0x00000000);
        textColor=getCurrentTextColor();
        strokeWith = ta.getInteger(R.styleable.order_icon_strokeWith, 0);

        radius = ta.getDimension(R.styleable.order_icon_radius, 0);
        topLeftRadius = ta.getDimension(R.styleable.order_icon_topLeftRadius, 0);
        topRightRadius = ta.getDimension(R.styleable.order_icon_topRightRadius, 0);
        bottomLeftRadius = ta.getDimension(R.styleable.order_icon_bottomLeftRadius, 0);
        bottomRightRadius = ta.getDimension(R.styleable.order_icon_bottomRightRadius, 0);
        shapeTpe= ta.getInt(R.styleable.order_icon_shapeTpe, GradientDrawable.OVAL);
        ta.recycle();


        if(strokeColor!=0&&strokeWith>0) {
            paintStroke = new Paint();
            paintStroke.setColor(strokeColor);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setStrokeWidth(strokeWith);
            paintStroke.setAntiAlias(true);
        }

        paint=new Paint();
        paint.setColor(solidColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if(radius==0 && shapeTpe==GradientDrawable.RECTANGLE){
            path=new Path();
            //顺时针绘制 左上 右上 右下 左下
            radii=new float[]{topLeftRadius,topLeftRadius,topRightRadius,topRightRadius,bottomRightRadius,bottomRightRadius,bottomLeftRadius,bottomLeftRadius};
        }

    }



    private final RectF mRect = new RectF();
    //创建一个画笔
    private Paint paintStroke ;
    private Paint paint ;
    private Path path;
    float[] radii;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mRect.set(strokeWith, strokeWith, width - strokeWith, height - strokeWith);
        if(path!=null)
            path.addRoundRect(mRect, radii, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(shapeTpe== GradientDrawable.RECTANGLE){
            if(radius==0){
                canvas.drawPath(path, paint);
                if(paintStroke!=null)
                    canvas.drawPath(path, paintStroke);
            }else{
                canvas.drawRoundRect(mRect, radius, radius, paint);
                if(paintStroke!=null)
                    canvas.drawRoundRect(mRect, radius, radius, paintStroke);
            }
        }else{
            canvas.drawOval(mRect,paint);
            if(paintStroke!=null)
                canvas.drawOval(mRect,paintStroke);
        }
        super.onDraw(canvas);
    }


////    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (solidTouchColor != 0||paintStroke!=null|| textTouchColor!=0){
//            if (event.getAction() == MotionEvent.ACTION_DOWN||event.getAction() == MotionEvent.ACTION_MOVE) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN)
//                    drowBackgroud(true);
//            } else {
//                drowBackgroud(false);
//            }
//            return true;
//
//        }else{
//            return super.onTouchEvent(event);
//        }
//    }
    /**
     * 设置按下颜色值
     */
    private void drowBackgroud(boolean isTouch) {
        if (isTouch) {
            if(solidTouchColor!=0)
                paint.setColor(solidTouchColor);
            if(paintStroke!=null)
                paintStroke.setColor(strokeTouchColor);
            if(textTouchColor!=0)
                setTextColor(textTouchColor);
        } else {
            if(solidColor!=0)
                paint.setColor(solidColor);

            if(paintStroke!=null)
                paintStroke.setColor(strokeColor);

            if(textTouchColor!=0)
                setTextColor(textColor);
        }


        postInvalidate();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        if(paintStroke!=null){
            paintStroke.setColor(strokeColor);
        }
        invalidate();
    }
}
