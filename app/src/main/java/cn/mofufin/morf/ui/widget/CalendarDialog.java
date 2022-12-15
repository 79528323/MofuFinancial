package cn.mofufin.morf.ui.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.RangeMonthView;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.LayoutDialogCalendarBinding;
import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.util.DataUtils;

public class CalendarDialog extends Dialog implements CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarInterceptListener,CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener{
    private LayoutDialogCalendarBinding binding;
    public static int statementDate=0;
    public static int statementMonth=0;
    public static int repaymentDate=0;
    public static int repaymentMonth=0;
    public static int stateYears = 0;
    public static int repayYears = 0;


    private CalendarView calendarView;
    private Context mContext;
    private int mCurrentDay;

    public Calendar mCalendar;

    public CalendarDialog(Context context,int statement,int repayment) {
        super(context, R.style.PopBottomDialogStyle);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_calendar,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);
        setContentView(binding.getRoot());

        this.mContext = context;
        mCalendar = Calendar.getInstance();
        initCalView(mContext);

        mCurrentDay = matchs(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH) + 1,mCalendar.get(Calendar.DAY_OF_MONTH));
        Logger.e("今天："+mCurrentDay);

        if (repayment > statement){
            //TODO 还款日大于账单日，直接为当月份范围
            statementMonth = getCurrentMonth();
            repaymentMonth = statementMonth;
            statementDate = statement;
            repaymentDate = repayment;
            stateYears = getCurrentYear();
            repayYears = stateYears;

            if (repayment > getCurMonthMaxDay()){//TODO 判断还款日是否 > 当前月份最后一天，则标记最后一天为还款日
                repaymentDate = getCurMonthMaxDay();
            }


        }else {
            //TODO 账单日大于还款日，则为次月还款，当前有两种情况，
            //TODO 1、当前日 < 还款日，走当前的还款周期；

            //TODO 2、当前日 > 还款日，走上个月的还款周期；
            //TODO 走情况 2
            //TODO 当前日 >= 还款日 ， 不在上个月还款周期内，走正常当月还款周期
            statementMonth = getCurrentMonth();
            repaymentMonth = statementMonth;

            //TODO 如需要推后一个月，要考虑跨年情况
            stateYears = getCurrentYear();
            repayYears = stateYears;
            if (statementMonth >= 12){//还款推后一个月
                repaymentMonth = 1;
                //跨年情况，年份+1
                repayYears += 1;
            } else
                repaymentMonth = statementMonth + 1;

            statementDate = statement;
            repaymentDate = repayment;

            //TODO 先判断当前日是否还在上个月还款周期内，先判断当前日是否
            int currentDay = getCurrentDay();
            if (currentDay < repayment){
                //TODO 走情况 1
                stateYears = getCurrentYear();
                repayYears = stateYears;
                statementDate = statement;
                repaymentDate = repayment;

                //TODO 判断好月份，比较重要,还款月理所当然是当月，要判断账单月是否也是同一月份
                repaymentMonth = getCurrentMonth();

                //TODO 要判断账单月是否也是同一月份，条件就是信用卡所设的账单日在上个月份内是否存在，大于上月的最后天
                if (repaymentMonth < 2){
                    //1月时要推回上年，年份也要减少
                    statementMonth = 12;
                    stateYears -= 1;
                }else {
                    statementMonth -= 1;
                    int assignMaxday = Integer.parseInt(getAssignMonthMaxDate(stateYears,statementMonth));
                    if (statement > assignMaxday){
                        //TODO 上个月分内没有或 < 该账单日，则顺延到当月1号为账单日
                        statementMonth = repaymentMonth;
                        statementDate = 1;
                    }
                }
            }else {
                //TODO 当前日 > 还款日 ，已经不在上个月还款周期内，按正常流程走次月还款

                int assignMaxday = Integer.parseInt(getAssignMonthMaxDate(stateYears,statementMonth));
                if (statement > assignMaxday){
                    //TODO 上个月分内没有或 < 该账单日，则顺延到当月1号为账单日
                    statementMonth = repaymentMonth;
                    statementDate = 1;
                }
            }

        }

        Logger.e("stateYears=="+stateYears+" statementMonth=="+statementMonth+" statementDate=="+statementDate+"  repayYears=="+repayYears+"repaymentMonth=="+repaymentMonth+" repaymentDate=="+repaymentDate);
        calendarView.setRange(stateYears,statementMonth,statementDate,repayYears,repaymentMonth,repaymentDate);
        binding.tvYear.setText(stateYears+"年");
//        calendarView.update();
        calendarView.updateCurrentDate();
        initWindow(view);
        setCanceledOnTouchOutside(true);
    }

    public void initWindow(View view){
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.measure(0,0);
        int height = view.getMeasuredHeight();
//        Logger.e("height="+height);
        lp.height = height;
        mDialogWindow.setAttributes(lp);
    }


    public void initCalView(Context context){
        calendarView = binding.calendarView;
        calendarView.setOnCalendarMultiSelectListener(this);
        calendarView.setOnCalendarInterceptListener(this);
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnYearChangeListener(this);
        calendarView.setOnlyCurrentMode();

        calendarView.setTextColor(
                ContextCompat.getColor(context,R.color.light_gray),
                ContextCompat.getColor(context,R.color.dark_gray),
                ContextCompat.getColor(context,R.color.light_gray),
                ContextCompat.getColor(context,R.color.brown_ec6257),
                ContextCompat.getColor(context,R.color.green_008100));

    }

    public void cancel(View view){
        dismiss();
    }

    public void confirm(View view){
        dismiss();

        onSelectDayListener.selectDay(sets);
    }

    public void showDialog(){
        show();
        View view = binding.getRoot();
        view.measure(0,0);
        initWindow(view);
    }

    //获取当前月
    public int getCurrentMonth(){
       return mCalendar.get(Calendar.MONTH) + 1;
    }

    //获取当前日
    public int getCurrentDay(){
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    private List<com.haibin.calendarview.Calendar> calendarList = new ArrayList<>();

    @Override
    public void onCalendarMultiSelectOutOfRange(com.haibin.calendarview.Calendar calendar) {
        Logger.e("OutOfRange Calendar day:"+calendar.getMonth()+"月"+calendar.getDay()+"日");
        calendarList.add(calendar);
    }

    @Override
    public void onMultiSelectOutOfSize(com.haibin.calendarview.Calendar calendar, int maxSize) {
//        Logger.e("Calendar day:"+calendar.getDay()+" size="+maxSize);
    }

    @Override
    public void onCalendarMultiSelect(com.haibin.calendarview.Calendar calendar, int curSize, int maxSize) {
        Logger.e("onCalendarMultiSelect:"+calendar.getMonth()+"月"+calendar.getDay());
        if (sets.size() <= 0)
            sets.add(calendar);
        else {
            if (sets.contains(calendar)){
                sets.remove(calendar);
            }else {
                sets.add(calendar);
            }
        }

    }


    private Set<com.haibin.calendarview.Calendar> sets = new HashSet<>();

    @Override
    public boolean onCalendarIntercept(com.haibin.calendarview.Calendar calendar) {
        //true 为拦截点击
        int calendarDay = matchs(calendar.getYear(),calendar.getMonth(),calendar.getDay());
        int stateDay = matchs(stateYears,statementMonth,statementDate);
        int repayDay = matchs(Math.max(stateYears,repayYears),repaymentMonth,repaymentDate);

        if (calendarDay > repayDay || calendarDay < stateDay){
            //大于还款日的拦截
            return true;
        }

        if (stateDay <= mCurrentDay){
            if (calendarDay <= mCurrentDay)
                return true;
        }

        return false;
    }

    @Override
    public void onCalendarInterceptClick(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        Logger.e("InterceptClick "+isClick);
    }


    /**
     * 判断账单日是否大于当前月份的最后一天
     * @param stateDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public int getCurMonthMaxDay(){
        Calendar curentCal = Calendar.getInstance();
        curentCal.add(Calendar.MONTH,1);
        curentCal.set(Calendar.DAY_OF_MONTH,0);
        String curMaxDayOfMonth = new SimpleDateFormat("dd").format(curentCal.getTime());
        Logger.e("curMaxDayOfMonth="+curMaxDayOfMonth);
        return Integer.valueOf(curMaxDayOfMonth);
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


    @Override
    public void onMonthChange(int year, int month) {
        binding.tvMonthDay.setText(month+"月");
    }

    @Override
    public void onYearChange(int year) {

        binding.tvYear.setText(year+"年");
    }

    OnSelectDayListener onSelectDayListener;

    public interface OnSelectDayListener{
        void selectDay(Set<com.haibin.calendarview.Calendar> calendarList);
    }

    public void setOnSelectDayListener(OnSelectDayListener onSelectDayListener) {
        this.onSelectDayListener = onSelectDayListener;
    }


    /**
     * 获取指定年月该月的最后一天 小谷
     * */
    public static String getAssignMonthMaxDate(Integer yer,Integer month){
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,yer);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        DateFormat informat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outformat = new SimpleDateFormat("dd");
        Date date = null;
        try {
            date = informat.parse(lastDayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outformat.format(date);
    }
}
