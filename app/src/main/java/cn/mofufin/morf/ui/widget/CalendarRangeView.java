package cn.mofufin.morf.ui.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MultiMonthView;

import androidx.core.content.ContextCompat;
import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseApplication;

public class CalendarRangeView extends MultiMonthView {
    private RxManager rxManager = new RxManager();
    private Context context;
    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();

    private Paint strokeCircle = new Paint();

    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;
//
    private Calendar calendarState ,calendarRepay;

    private int mCurrentDays;
    private int stateDay ,repayDay;

    public CalendarRangeView(Context context) {
        super(context);
        this.context = context;

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        //4.0以上硬件加速会导致无效
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));

        strokeCircle.setStyle(Paint.Style.STROKE);
        strokeCircle.setStrokeWidth(dipToPx(context,1.5f));
        strokeCircle.setAntiAlias(true);
        strokeCircle.setColor(ContextCompat.getColor(context, R.color.app_blue));

        mCurrentDays = matchs(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
                java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
                java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH));

        stateDay = matchs(CalendarDialog.stateYears,CalendarDialog.statementMonth,CalendarDialog.statementDate);
        repayDay = matchs(Math.max(CalendarDialog.stateYears,CalendarDialog.repayYears),CalendarDialog.repaymentMonth,CalendarDialog.repaymentDate);

        mCurMonthTextPaint.setFakeBoldText(true);
        mOtherMonthTextPaint.setFakeBoldText(false);
        mSelectTextPaint.setFakeBoldText(true);
        mCurMonthTextPaint.setTextSize(dipToPx(context,20));
        mSelectTextPaint.setTextSize(dipToPx(context,20));
        mOtherMonthTextPaint.setTextSize(dipToPx(context,20));
    }

    @Override
    protected boolean isCalendarSelected(Calendar calendar) {
        return super.isCalendarSelected(calendar);
    }

    @Override
    protected void onLoopStart(int x, int y) {
        super.onLoopStart(x, y);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(ContextCompat.getColor(context, R.color.app_blue));
        float radius = (mItemWidth/2);
        float cx = x + radius;
        float cy = y + (mItemHeight/2);
        canvas.drawCircle(cx,cy,(Math.min(mItemHeight,mItemWidth)/2) - mPadding,mSelectedPaint);
//        canvas.drawRect(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
//        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
//
//        canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, y + mPadding + mRadio, mRadio, mSchemeBasicPaint);
//
//        canvas.drawText(calendar.getScheme(),
//                x + mItemWidth - mPadding - mRadio / 2 - getTextWidth(calendar.getScheme()) / 2,
//                y + mPadding + mSchemeBaseLine, mTextPaint);
    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }


    /**
     * 绘制日历文本日期字体状态
     * @param canvas
     * @param calendar
     * @param x
     * @param y
     * @param hasScheme
     * @param isSelected
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        int offset = 5 + (mPadding * 2);
        float cy = mTextBaseLine + top + offset;
        String dates = String.valueOf(calendar.getDay());
        int calendarDay = matchs(calendar.getYear(),calendar.getMonth(),calendar.getDay());

        mSelectTextPaint.setColor(ContextCompat.getColor(context,R.color.white));
        mSelectTextPaint.setTextSize(dipToPx(context,20));

        if (calendarDay > repayDay){
            //大于还款日的拦截
            canvas.drawText(dates, cx, cy,mOtherMonthTextPaint);
            return;
        }

        if (calendarDay == repayDay){
            dates = "还款日";
            if (isSelected){
                mSelectTextPaint.setTextSize(dipToPx(context,12));
                canvas.drawText(dates, cx, cy,mSelectTextPaint);
            }else {

                float radius = (mItemWidth/2);
                float sx = x + radius;
                float sy = y + (mItemHeight/2);

                if (mCurrentDays >= repayDay){
                    canvas.drawText(dates, cx, cy,mOtherMonthTextPaint);
                }else {
                    canvas.drawCircle(sx,sy,(Math.min(mItemHeight,mItemWidth)/2)-mPadding,strokeCircle);
                    mCurMonthTextPaint.setTextSize(dipToPx(context,12));
                    mCurMonthTextPaint.setColor(ContextCompat.getColor(context,R.color.app_blue));
                    canvas.drawText(dates, cx, cy,mCurMonthTextPaint);
                }
            }

            return;
        }

        if (mCurrentDays < stateDay){
            //未到账单日
            if (calendarDay < stateDay){
                canvas.drawText(dates, cx, cy,mOtherMonthTextPaint);
            }else if (calendarDay == stateDay){//未到账单日，但这边应该是可选并配上圆形着色
                dates = "账单日";
                if (isSelected){
                    mSelectTextPaint.setTextSize(dipToPx(context,12));
                    canvas.drawText(dates, cx, cy,mSelectTextPaint);
                }else {
                    float radius = (mItemWidth/2);
                    float sx = x + radius;
                    float sy = y + (mItemHeight/2);
                    mCurMonthTextPaint.setTextSize(dipToPx(context,12));
                    mCurMonthTextPaint.setColor(ContextCompat.getColor(context,R.color.app_blue));
                    canvas.drawText(dates, cx, cy,mCurMonthTextPaint);
                    canvas.drawCircle(sx,sy,(Math.min(mItemHeight,mItemWidth)/2)-mPadding,strokeCircle);
                }
            }else
                canvasPayDate(isSelected,canvas,dates,cx,cy);
        }else if (mCurrentDays > stateDay){
            if (calendarDay <= mCurrentDays){
                if (calendarDay == stateDay) {
                    dates = "账单日";
                }
                canvas.drawText(dates, cx, cy,mOtherMonthTextPaint);

            }else
                canvasPayDate(isSelected,canvas,dates,cx,cy);

        }else {
            if (calendarDay <= stateDay && calendarDay <= mCurrentDays){
                if (calendarDay == stateDay){
                    dates = "账单日";
                }
                canvas.drawText(dates, cx, cy,mOtherMonthTextPaint);
            }else
                canvasPayDate(isSelected,canvas,dates,cx,cy);
        }

    }


    /**
     * //绘制区间日期
     * @param isSelected
     * @param canvas
     * @param day
     * @param cx
     * @param cy
     */
    public void canvasPayDate(boolean isSelected,Canvas canvas,String day,float cx,float cy){

        if (isSelected){
            canvas.drawText(day, cx, cy,mSelectTextPaint);
        }else {
            mCurMonthTextPaint.setTextSize(dipToPx(context,20));
            mCurMonthTextPaint.setFakeBoldText(true);
            mCurMonthTextPaint.setColor(ContextCompat.getColor(context,R.color.dark_gray));
            canvas.drawText(day, cx, cy,mCurMonthTextPaint);
        }

    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 拼接日期对比大小
     * @param year
     * @param month
     * @param day
     * @return
     */
    public int matchs(int year ,int month ,int day){
        int temp=0;
        if (month > 0 && day > 0){
            String years = String.valueOf(year);
            String months = String.valueOf((month < 10) ? "0"+month : month);
            String days = String.valueOf((day < 10)?"0"+day:day);

            String str = years + months + days;
            temp = Integer.valueOf(str);
        }
        return temp;
    }
}
