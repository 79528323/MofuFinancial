package cn.mofufin.morf.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.mofufin.morf.R;

public class MyDatePickerDialog extends AlertDialog implements
        DatePicker.OnDateChangedListener{

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private final DatePicker mDatePicker;
    private final OnDateSetListener mDateSetListener;
    private final Calendar mCalendar;

    private boolean mTitleNeedsUpdate = true;

    private View view;

    private String minDate;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {
        void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    public MyDatePickerDialog(boolean isQueryAll,Context context, OnDateSetListener callBack,
                              int year, int monthOfYear, int dayOfMonth) {
        this(isQueryAll,context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    public MyDatePickerDialog(boolean isQueryAll,Context context, int theme,
                              OnDateSetListener listener, int year, int monthOfYear,
                              int dayOfMonth) {
        super(context, theme);

        mDateSetListener = listener;
        mCalendar = Calendar.getInstance();

        Context themeContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        view = inflater.inflate(R.layout.date_picker_dialog, null);
        view.setBackgroundColor(Color.WHITE);
        // setView(view);

        // setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok),
        // this);
        // setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel),
        // this);
        // setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);
        view.findViewById(R.id.date_picker_queryall).setVisibility(isQueryAll?View.VISIBLE:View.GONE);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        // TODO ??????????????????????????????
        mDatePicker.setMaxDate(System.currentTimeMillis() - 1000L);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);

        // mDatePicker.setValidationCallback(mValidationCallback);
        // ????????????????????????ok??????
        // setTitle("????????????:");
        setButton();
    }

    // private void setTitle(String title) {
    // //?????????????????????title??????????????????
    // ((TextView) view.findViewById(R.id.date_picker_title)).setText(title);
    // }
    private void setButton() {
        // ????????????????????????????????????????????????????????????????????????????????????CallBack????????????????????????????????????????????????????????????????????????????????????????????????????????????
        view.findViewById(R.id.date_picker_ok).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDateSetListener != null) {
                            // Clearing focus forces the dialog to commit any
                            // pending
                            // changes, e.g. typed text in a NumberPicker.
                            mDatePicker.clearFocus();
                            mDateSetListener.onDateSet(mDatePicker,
                                    mDatePicker.getYear(),
                                    mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth());
                            dismiss();
                        }
                    }
                });
        view.findViewById(R.id.date_picker_cancle).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                });
    }

    public void setQueryALLClickListener(View.OnClickListener queryALLClickListener) {
        view.findViewById(R.id.date_picker_queryall).setOnClickListener(queryALLClickListener);
    }

    public void myShow() {
        // ????????????show???????????????????????????setContentView????????????show?????????????????????????????????
        show();
        setContentView(view);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        mDatePicker.init(year, month, day, this);

    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
    }

    public void setMinDate(String minDate) {
        if (mDatePicker!=null){
            String format = "yyyy-MM";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                Date date = null;
                date = sdf.parse(minDate);
                mDatePicker.setMinDate(date.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int year = savedInstanceState.getInt(YEAR);
        final int month = savedInstanceState.getInt(MONTH);
        final int day = savedInstanceState.getInt(DAY);
        mDatePicker.init(year, month, day, this);
    }

    View.OnClickListener queryALLClickListener;
}
