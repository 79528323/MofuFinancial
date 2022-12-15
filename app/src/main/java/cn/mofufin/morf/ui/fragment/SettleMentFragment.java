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
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.Validator;
import cn.mofufin.morf.databinding.FragmentSettleMentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettleMentFragment extends BaseFragment {
    private FragmentSettleMentBinding binding;

    public SettleMentFragment() {
        // Required empty public constructor
    }

    public static SettleMentFragment newInstance(){
        return new SettleMentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settle_ment, container, false);
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
       String bankCard = binding.editBankcard.getText().toString();
       if (TextUtils.isEmpty(bankCard)){
           showTips("请填写法人借记卡卡号");
           return;
       }else if (!Validator.checkBankCard(bankCard)){
           showTips("请正确填写法人借记卡卡号");
           return;
       }

       ScanQRSubmitActivity.params.put("bankActId",bankCard);

//       ManagementInfoFragment fragment = ManagementInfoFragment.newInstance();
//       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fragment_slide_right_in,
//                R.anim.fragment_slide_left_out,R.anim.fragment_slide_left_in,R.anim.fragment_slide_right_out);
//       transaction.add(R.id.scan_qr_container,fragment,"ManagementInfoFragment");
//       transaction.addToBackStack(null).commitAllowingStateLoss();
       RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,3);
    }
}
