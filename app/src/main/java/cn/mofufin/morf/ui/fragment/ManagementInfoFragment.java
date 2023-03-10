package cn.mofufin.morf.ui.fragment;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.ScanQRSubmitActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.Validator;
import cn.mofufin.morf.ui.widget.CityPicker;
import cn.mofufin.morf.ui.widget.SinglePicker;
import cn.mofufin.morf.databinding.FragmentManagementInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagementInfoFragment extends BaseFragment {
    private FragmentManagementInfoBinding binding;
    private SinglePicker singlePicker;
    private CityPicker cityPicker;
    private String regProvId,regCityId,regAreaId;
    private int machanType=0;
    public ManagementInfoFragment() {
        // Required empty public constructor
    }

    public static ManagementInfoFragment newInstance(){
        return new ManagementInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_management_info, container, false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);

        singlePicker = new SinglePicker(getActivity(),1);
        singlePicker.listener = new SinglePicker.OnSingleSelectListener() {
            @Override
            public void singleSelect(String type, int num) {
                singlePicker.dimiss();
                binding.editType.setText(type);
                if (num == 5){
                    machanType = num + 2;
                }else {
                    machanType = num + 1;
                }
            }
        };
        cityPicker = new CityPicker(getActivity(),false);
        cityPicker.listener = new CityPicker.OnSelectListener() {
            @Override
            public void Select(String s1, String s2, String s3) {
                regProvId=s1;
                regCityId=s2;
                regAreaId=s3;
                binding.editLocal.setText(regProvId +" "+regCityId+" "+regAreaId);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void selectManchant(final View view){
        singlePicker.show();
    }

    public void SelectProCity(final View view){
        cityPicker.show();
    }

    public void OnSubmit(View view){
        String editName = binding.editName.getText().toString();
        String editLocal = binding.editLocal.getText().toString();
        String editAddress = binding.editAddress.getText().toString();
        String editCode = binding.editCode.getText().toString();
        String editType = binding.editType.getText().toString();
        String editValidity = binding.editValidity.getText().toString();
        binding.editValidity.setText(DataUtils.fullToHalf(editValidity));
        editValidity = binding.editValidity.getText().toString();

        if (TextUtils.isEmpty(editName)){
            showTips("?????????????????????");
            return;
        }else if (TextUtils.isEmpty(editLocal)){
            showTips("??????????????????");
            return;
        }else if (TextUtils.isEmpty(editAddress)){
            showTips("?????????????????????");
            return;
        }else if (TextUtils.isEmpty(editCode)){
            showTips("?????????????????????");
            return;
        }else if (TextUtils.isEmpty(editValidity)){
            showTips("??????????????????????????????");
            return;
        }else if (TextUtils.isEmpty(editType)){
            showTips("?????????????????????");
            return;
        }else if (!Validator.isChinese(editName)){
            showTips("?????????????????????");
            return;
        }else if (!Validator.isValidIdCardDate(editValidity)){
            showTips("?????????????????????!");
            return;
        }


        String licType ="";//TODO ??????????????????????????????1????????? 2??????
        String licSdate="";//TODO ????????????????????????????????????:?????????20180902
        String LicEdate="";//TODO ????????????????????????????????????:??????20180902,????????????????????????????????????????????????,?????????
        CharSequence sequence = "-";
        int index = editValidity.indexOf(sequence.toString());
        String temp = editValidity.substring(index+1);
        if (TextUtils.equals("??????",temp)){
            licType ="2";
        }else{
            licType ="1";
            LicEdate = temp;
        }


        licSdate = editValidity.substring(0,index);
        ScanQRSubmitActivity.params.put("licSdate",licSdate);
        ScanQRSubmitActivity.params.put("licEdate",LicEdate);
        ScanQRSubmitActivity.params.put("merchName",editName);
        ScanQRSubmitActivity.params.put("regProvId",regProvId);
        ScanQRSubmitActivity.params.put("regCityId",regCityId);
        ScanQRSubmitActivity.params.put("regAreaId",regAreaId);
        ScanQRSubmitActivity.params.put("regAddr",editAddress);
        ScanQRSubmitActivity.params.put("creditCode",editCode);
        ScanQRSubmitActivity.params.put("licType",licType);
        ScanQRSubmitActivity.params.put("pnrpayMerType",machanType+"");

        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,4);
    }
}
