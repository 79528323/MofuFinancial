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
import cn.mofufin.morf.ui.RealNameIdentityActivity;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentTransationPasswordBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransationPasswordFragment extends BaseFragment {
    private FragmentTransationPasswordBinding binding;
    public static TransationPasswordFragment instance=null;
    public TransationPasswordFragment() {
        // Required empty public constructor
    }

    public static TransationPasswordFragment newInstance(){
        if (instance == null)
            instance =  new TransationPasswordFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_transation_password, container, false);
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
        if (isFastClick())
            return;

        String password = binding.editCode.getText().toString();
        String confirm = binding.editConfirm.getText().toString();
        if (TextUtils.isEmpty(password)){
            showTips("请输入交易密码");
            return;
        }else if (TextUtils.isEmpty(confirm)){
            showTips("请再次确认密码");
            return;
        }else if (!TextUtils.equals(password,confirm)){
            showTips("确认密码不一样，请重新输入");
            return;
        }

        RealNameIdentityActivity.params.put("payPassword",confirm);
        RxBus.getInstance().post(RxEvent.SUBMIT_REAL_NAME,5);
    }
}
