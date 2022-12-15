package cn.mofufin.morf.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.adapter.ReceiVablesAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.ReceiVablesDetailActivity;
import cn.mofufin.morf.ui.entity.Order;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;
import cn.mofufin.morf.ui.widget.DoubleTimeSelectDialog;
import cn.mofufin.morf.contract.OrderContract;
import cn.mofufin.morf.databinding.FragmentReceivablesBinding;
import cn.mofufin.morf.presenter.OrderPresenter;
import rx.functions.Action1;

/**
 * 收款
 */
public class ReceiVablesFragment extends BaseFragment implements OrderContract.View{
    private static final String DEFAULT_FINDTYPE="default_findtype";
    private FragmentReceivablesBinding binding;
    private LinearLayoutManager layoutManager;
    private OrderPresenter presenter;
    private ReceiVablesAdapter adapter;
    private boolean isfilterShow =false;
    private int findType =1;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间为2100年，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd="2100-12-31";

    public final String beginDeadTime = "1900-01-01";

    private String orderBegin="" , orderEnd="";

    private TextView button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_receivables,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        binding.setHasData(false);
        binding.setOnPress(false);
        init();

        RxBus.getInstance().postEmpty(RxEvent.ORDER_QUREY_REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.filterWindow.clearAnimation();
    }

    @SuppressLint("WrongConstant")
    public void init(){
        findType = SharedPreferencesUtils.getInt(getActivity(),DEFAULT_FINDTYPE,findType);
        binding.setType(findType);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        presenter = new OrderPresenter(this);
        binding.orderList.setLayoutManager(layoutManager);
        adapter = new ReceiVablesAdapter();
        adapter.setClickListener(clickListener);
        adapter.setFindType(findType);
        binding.orderList.setAdapter(adapter);

        orderEnd = DataUtils.GetDateOrTime("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        defaultWeekEnd = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.MONTH,-1);
        Date beforeMonthDate=calendar.getTime();
        orderBegin = new SimpleDateFormat("yyyy-MM-dd").format(beforeMonthDate);
        defaultWeekBegin = orderBegin;

        binding.timepicker.setText(defaultWeekBegin+"~"+defaultWeekEnd);
        cc.ruis.lib.util.Logger.e("orderbegin="+orderBegin);
        rxManager.onRxEvent(RxEvent.ORDER_QUREY_REFRESH, new Action1<Object>() {
            @Override
            public void call(Object o) {
                binding.timepicker.setText(orderBegin+"~"+orderEnd);
                presenter.refreshData(orderBegin,orderEnd,findType);
            }
        });
//        binding.orderList.setNestedScrollingEnabled(true);

    }

    @Override
    public void onDataSuccess(Order order) {
//        binding.receivableAppbar.invalidate();

        binding.setHasData(order.detailList.size()>0);
        adapter.refresh(order.detailList);
        adapter.setFindType(order.findType);

        binding.setTodayTotalMoney(DataUtils.numFormat(order.todayTotalMoney));
        binding.setTodayTotalPenNumber(String.valueOf(order.todayTotalPenNumber));
        binding.setTotalMoney(DataUtils.numFormat(order.totalMoney));
        binding.setTotalPenNumber(String.valueOf(order.totalPenNumber));
    }

    @Override
    public void onDataFail(String tips) {
        binding.setTodayTotalMoney("0.00");
        binding.setTodayTotalPenNumber("0");
        binding.setTotalMoney("0.00");
        binding.setTotalPenNumber("0");

        binding.setHasData(false);

        showTips(tips);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Order.OrderDetails details = (Order.OrderDetails) v.getTag();
            Intent intent = new Intent(getActivity(),ReceiVablesDetailActivity.class);
            intent.putExtra(IntentParam.ORDER_DETAILS,details);
            intent.putExtra(IntentParam.ORDER_FIND_TYPE,findType);
            if (findType==2){
                intent.putExtra(IntentParam.PAY_TYPE,details.payType);
            }
            if (!binding.getOnPress()){
                startActivity(intent);
            }

        }
    };

    /**
     * 筛选
     * @param view
     */
    public void filterOrderQuery(View view){
        Animation animation;
        setfilterLayout();

        if (!isfilterShow){
            binding.filterWindow.setVisibility(View.VISIBLE);
            animation= AnimationUtils.loadAnimation(getActivity(),R.anim.recei_enter_anims);
            binding.filterWindow.startAnimation(animation);
            isfilterShow = true;

            CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getContext());
            linearLayoutManager.setScrollEnabled(false);
            binding.orderList.setLayoutManager(linearLayoutManager);
        }else {
            animation= AnimationUtils.loadAnimation(getActivity(),R.anim.recei_exit_anims);
            binding.filterWindow.startAnimation(animation);
            binding.filterWindow.setVisibility(View.GONE);
            isfilterShow = false;
            selectorClear();

            binding.orderList.setLayoutManager(layoutManager);
        }
        binding.setOnPress(isfilterShow);
    }

    /**
     * 查询类型
     * @param view
     */
    public void getfindType(View view){
        selectorClear();
        button = (TextView) view;
        button.setTextColor(ContextCompat.getColor(getActivity(),R.color.app_blue));
        button.setSelected(true);
        setDefualtSelect();
    }

    /**
     * 查看
     * @param view
     */
    public void checkOrder(View view){
        binding.orderList.setLayoutManager(layoutManager);
        if (button!=null){
            findType = Integer.valueOf((String) button.getTag());
            binding.setType(findType);
            RxBus.getInstance().postEmpty(RxEvent.ORDER_QUREY_REFRESH);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.filterWindow.startAnimation(
                        AnimationUtils.loadAnimation(getActivity(),R.anim.recei_exit_anims));
                binding.filterWindow.setVisibility(View.GONE);
                isfilterShow = false;
                binding.setOnPress(isfilterShow);
                selectorClear();
            }
        });
    }

    /**
     * 设置默认类型
     * @param view
     */
    public void setDefaultfindtype(View view){
        if (button!=null){
//            findType = Integer.valueOf((String) button.getTag());
            SharedPreferencesUtils.setInt(getActivity(),DEFAULT_FINDTYPE,Integer.valueOf((String) button.getTag()));
            showTips("设置成功");
            setDefaultClear();
        }
    }

    /**
     * 打开日期选择
     */
    public void showCustomTimePicker(View view) {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(getActivity(), beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    orderBegin = startTime;
                    orderEnd = endTime;
                    RxBus.getInstance().postEmpty(RxEvent.ORDER_QUREY_REFRESH);
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }

    public void selectorClear(){
        binding.btn1.setSelected(false);
        binding.btn1.setTextColor(ContextCompat.getColor(getActivity(),R.color.dark_gray));
        binding.btn2.setSelected(false);
        binding.btn2.setTextColor(ContextCompat.getColor(getActivity(),R.color.dark_gray));
        binding.btn3.setSelected(false);
        binding.btn3.setTextColor(ContextCompat.getColor(getActivity(),R.color.dark_gray));
        binding.btn4.setSelected(false);
        binding.btn4.setTextColor(ContextCompat.getColor(getActivity(),R.color.dark_gray));
    }

    public void setDefaultClear(){
        binding.setdefault.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.line));
    }

    public void setDefualtSelect(){
        binding.setdefault.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.app_blue));
    }

    public int getStatusLocation(){
        int y=0;
        int[] location = new int[2];
        binding.selectStatus.getLocationOnScreen(location);
        y = location[1] + binding.selectStatus.getMeasuredHeight() - getStatusBarHeight();
        return y;
    }

    public void setfilterLayout(){
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(binding.filterWindow.getLayoutParams());
        layoutParams.setMargins(0,getStatusLocation(),layoutParams.width,layoutParams.height);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(layoutParams);
        binding.filterWindow.setLayoutParams(params);
    }

    public int getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
