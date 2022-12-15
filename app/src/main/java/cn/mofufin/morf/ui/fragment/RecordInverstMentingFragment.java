package cn.mofufin.morf.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.mofufin.morf.adapter.InverstMentRecordAdapter;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.InverstMentRecordActivity;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.InverstMentRecordDetailActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.InverstMemtRecord;
import cn.mofufin.morf.ui.services.ProductImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.ui.widget.DoubleTimeSelectDialog;
import cn.mofufin.morf.databinding.FragmentRecordInverstmentingBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordInverstMentingFragment extends BaseFragment {
    private FragmentRecordInverstmentingBinding binding;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    private LinearLayoutManager layoutManager;
    private InverstMentRecordAdapter adapter;
    private String orderBegin,orderEnd;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间为2100年，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd="2100-12-31";

//    public String paramsStrTime = "";

    public final String beginDeadTime = "1900-01-01";


    public RecordInverstMentingFragment() {
        // Required empty public constructor
    }

    public static RecordInverstMentingFragment newInstance(){
        return new RecordInverstMentingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_record_inverstmenting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        binding.setOnPress(false);
        orderEnd = DataUtils.GetDateOrTime("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        defaultWeekEnd = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.MONTH,- InverstMentRecordActivity.DIFFER_MONTH);
        Date beforeMonthDate=calendar.getTime();
        orderBegin = new SimpleDateFormat("yyyy-MM-dd").format(beforeMonthDate);
        defaultWeekBegin = orderBegin;

        binding.timepicker.setText(defaultWeekBegin+"~"+defaultWeekEnd);

        adapter = new InverstMentRecordAdapter();
        adapter.setClickListener(clickListener);

        layoutManager = new LinearLayoutManager(getActivity());
        binding.list.setLayoutManager(layoutManager);
        binding.list.setAdapter(adapter);
        refreshInverstMentRecord();
    }

    public void refreshInverstMentRecord(){
        Subscription subscription = ProductImpAPI.queryMerInvestRecode(HttpParam.OFFICE_CODE
                ,HttpParam.QUERY_INVERST_ORDER_KEY, MerchanInfoDB.queryInfo().merchantCode,orderBegin,orderEnd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<InverstMemtRecord>() {
                    @Override
                    public void call(InverstMemtRecord record) {
                        if (record.result_Code==0){
                            binding.setAmount(record.thenTotalMoney);
                            binding.setCount(record.thenTotalPenNumber);
                            List<InverstMemtRecord.RecordListBean> recordList = record.recordList;
                            Iterator<InverstMemtRecord.RecordListBean> iterator = recordList.iterator();
                            InverstMemtRecord.RecordListBean recordListBean = null;
                            while (iterator.hasNext()){
                                recordListBean = iterator.next();
                                if (recordListBean.foOrderState != 0)
                                    iterator.remove();
                            }
                            adapter.refresh(recordList);
                            binding.setHasData(recordList.size()>0);
                        }else {
                            showTips(record.result_Msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InverstMemtRecord.RecordListBean bean = (InverstMemtRecord.RecordListBean) v.getTag();
            Intent intent = new Intent(getActivity(), InverstMentRecordDetailActivity.class);
            intent.putExtra(IntentParam.BEAN,bean);
            getActivity().startActivity(intent);
        }
    };

    public void showCustomTimePicker(View view) {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(getActivity(), beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {

                    orderBegin = startTime;
                    orderEnd = endTime;
                    binding.timepicker.setText(orderBegin+"~"+orderEnd);
                    refreshInverstMentRecord();
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    binding.setOnPress(!binding.getOnPress());
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
            binding.setOnPress(!binding.getOnPress());
        }
    }
}
