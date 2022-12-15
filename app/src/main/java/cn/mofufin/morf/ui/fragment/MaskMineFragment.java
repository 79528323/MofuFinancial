package cn.mofufin.morf.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.FragmentMaskHomeBinding;
import cn.mofufin.morf.databinding.FragmentMaskMineBinding;
import cn.mofufin.morf.ui.base.BaseFragment;

/**
 * 首页
 */
public class MaskMineFragment extends BaseFragment {
    private FragmentMaskMineBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mask_mine, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void back(View view){

    }


}
