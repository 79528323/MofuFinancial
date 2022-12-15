package cn.mofufin.morf.ui.fragment;


import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.ScanQRReceiVablesActivity;
import cn.mofufin.morf.databinding.FragmentCreatImageBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatImageFragment extends DialogFragment {
    FragmentCreatImageBinding binding;
    private View layoutView;
    public CreatImageFragment() {

    }

    @SuppressLint("ValidFragment")
    public CreatImageFragment(String money, String name, int type, byte[] bytes,int QR_TYPE) {
        Bundle bundle = new Bundle();
        bundle.putString("money",money);
        bundle.putString("name",name);
        bundle.putInt("type",type);
        bundle.putByteArray("byte",bytes);
        bundle.putInt("qr_type",QR_TYPE);
        setArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_creat_image,container,false);
        layoutView = binding.getRoot();
        return layoutView;
    }

    public View getLayoutViews(){
        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );

        WindowManager.LayoutParams params = window.getAttributes();

        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height=  ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        Bundle bundle = getArguments();
        int QR_TYPE= bundle.getInt("qr_type");
        if (QR_TYPE == ScanQRReceiVablesActivity.QR_CODE_DYNAMIC){
            binding.money.setVisibility(View.VISIBLE);
            binding.money.setText("￥"+bundle.getString("money"));
        }else if (QR_TYPE == ScanQRReceiVablesActivity.QR_CODE_STATIC){
            binding.money.setVisibility(View.GONE);
        }else {
            binding.money.setVisibility(View.VISIBLE);
            binding.money.setText("￥"+bundle.getString("money"));
            binding.titles.setText("可在任意三方应用扫一扫，向我付款");
            binding.merchantName.setText(bundle.getString("name"));

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int size = (metrics.densityDpi/10)/3;
            binding.titles.setTextSize(size);
        }

//        int type = bundle.getInt("type");
//        binding.desc.setText(type==1?getString(R.string.type_w):getString(R.string.type_a));

        byte[] bytes = bundle.getByteArray("byte");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.createQr.setImageBitmap(bitmap);
    }
}
