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
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.ScanQRSubmitActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.Validator;
import cn.mofufin.morf.databinding.FragmentBaseInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseInfoFragment extends BaseFragment {
    private FragmentBaseInfoBinding binding;

    public BaseInfoFragment() {
        // Required empty public constructor
    }

    public static BaseInfoFragment newInstance(){
        return new BaseInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_base_info, container, false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
    }

    public void OnSubmit(View view){
        String name = binding.editName.getText().toString();
        String idcard = binding.editIdcard.getText().toString();
        String idNum = binding.editIdnum.getText().toString();
        String email = binding.editEmail.getText().toString();

        binding.editIdcard.setText(DataUtils.fullToHalf(idcard));
        idcard = binding.editIdcard.getText().toString();

        if (TextUtils.isEmpty(name)){
            showTips("???????????????????????????");
            return;
        }else if (TextUtils.isEmpty(idNum)){
            showTips("?????????????????????");
            return;
        }else if (TextUtils.isEmpty(idcard)){
            showTips("?????????????????????????????????");
            return;
        }else if (TextUtils.isEmpty(email)){
            showTips("?????????????????????");
            return;
        }else if (!Validator.isChinese(name)){
            showTips("?????????????????????");
            return;
        }else /*if (!Validator.isIDCard(idNum)){
            showTips("????????????????????????");
            return;
        }*/if (!Validator.isValidIdCardDate(idcard)){
            showTips("?????????????????????!");
            return;
        }

        //TODO ??????????????????????????????:1????????? 2??????
        String idValidType ="" ,idSdate="" ,idEdate="";
        CharSequence sequence = "-";
        int index = idcard.indexOf(sequence.toString());

        String temp = idcard.substring(index+1);
        if (TextUtils.equals("??????",temp)){
            idValidType ="2";
        }else{
            idValidType ="1";
            idEdate = temp;
        }

        idSdate = idcard.substring(0,idcard.indexOf(sequence.toString()));

        ScanQRSubmitActivity.params.put("legalName",name);
        ScanQRSubmitActivity.params.put("idNo",idNum);
        ScanQRSubmitActivity.params.put("idValidType",idValidType);
        ScanQRSubmitActivity.params.put("idSdate",idSdate);
        ScanQRSubmitActivity.params.put("idEdate",idEdate);
        ScanQRSubmitActivity.params.put("contactEmail",email);


//        SettleMentFragment fragment = SettleMentFragment.newInstance();
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fragment_slide_right_in,
//                R.anim.fragment_slide_left_out,R.anim.fragment_slide_left_in,R.anim.fragment_slide_right_out);
//        transaction.add(R.id.scan_qr_container,fragment,"SettleMentFragment");
//        transaction.addToBackStack(null);
//        transaction.commitAllowingStateLoss();

        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,2);
    }

}
