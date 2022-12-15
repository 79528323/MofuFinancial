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
            showTips("请填写法人真实姓名");
            return;
        }else if (TextUtils.isEmpty(idNum)){
            showTips("请填写法人身份");
            return;
        }else if (TextUtils.isEmpty(idcard)){
            showTips("请填写法人身份证有效期");
            return;
        }else if (TextUtils.isEmpty(email)){
            showTips("请填写法人邮箱");
            return;
        }else if (!Validator.isChinese(name)){
            showTips("请填写正确姓名");
            return;
        }else /*if (!Validator.isIDCard(idNum)){
            showTips("请填写正确身份证");
            return;
        }*/if (!Validator.isValidIdCardDate(idcard)){
            showTips("有效期格式有误!");
            return;
        }

        //TODO 法人身份证有效期类型:1非长期 2长期
        String idValidType ="" ,idSdate="" ,idEdate="";
        CharSequence sequence = "-";
        int index = idcard.indexOf(sequence.toString());

        String temp = idcard.substring(index+1);
        if (TextUtils.equals("长期",temp)){
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
