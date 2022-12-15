package cn.mofufin.morf.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.RealNameIdentityActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.SingleBankType;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.BankImpAPI;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.FileUtil;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RecognizeService;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentBankCardBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankCardFragment extends BaseFragment {
    public static BankCardFragment instance=null;
    private FragmentBankCardBinding binding;
    private int which=0;
    private String bankpathA,bankpathB;

    public BankCardFragment() {
        // Required empty public constructor
    }

    public static BankCardFragment newInstance() {
        if (instance==null)
            instance = new BankCardFragment();
        return instance;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BankCardResult result = (BankCardResult) msg.obj;
            String bankPath = FileUtil.getSaveFile(
                    getContext().getApplicationContext(),which).getAbsolutePath();

            if (which == 2){
                binding.cardNo.setText(result.getBankCardNumber());
                binding.openline.setText(result.getBankName());
                bankpathA = bankPath;
                binding.setIsShow(true);
            }else{
                bankpathB = bankPath;
            }

            setBankImageView(bankPath,which==2?binding.bankImg:binding.bankBackImg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.e("lbd","onCreateView");
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bank_card, container, false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e("lbd","onCreateView");
        binding.setHandlers(this);
        binding.setCardNo("");
        binding.setIsShow(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("onResume");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("lbd","onDestroy");
    }

    public void takeCardPhoto(View view){

        which=2;
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getContext(),which).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_BANK_CARD);

        startActivityForResult(intent, RealNameIdentityActivity.REQUEST_CODE_BANKCARD);
    }

    public void takeCardBackPhoto(View view){
        which=3;
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getContext(),which).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_BANK_CARD);

        startActivityForResult(intent, RealNameIdentityActivity.REQUEST_CODE_BANKCARD);
    }

    public void bankNext(View view){
        File bankImgA = new File(bankpathA);
        File backImgB = new File(bankpathB);
        String cardNo = binding.cardNo.getText().toString();
        String phone =  binding.phone.getText().toString();
        String openline = binding.openline.getText().toString();
        if (!bankImgA.exists()){
            showTips("请上传银行卡正面");
            return;
        }else if (!backImgB.exists()){
            showTips("请上传银行卡背面");
            return;
        }else if (TextUtils.isEmpty(openline)){
            showTips("请填写银行开户行");
            return;
        }else if (TextUtils.isEmpty(cardNo)){
            showTips("请填写银行卡号");
            return;
        }else if (TextUtils.isEmpty(phone)){
            showTips("请输入银行预留手机号");
            return;
        }

        RealNameIdentityActivity.fileMap.put("bankCardA",bankImgA);
        RealNameIdentityActivity.fileMap.put("bankCardB",backImgB);
        cardNo = cardNo.replace(" ","");
        RealNameIdentityActivity.params.put("acco_no",cardNo);
        RealNameIdentityActivity.params.put("mobile",phone);

        RxBus.getInstance().post(RxEvent.SUBMIT_REAL_NAME,3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，银行卡识别
        if (requestCode == RealNameIdentityActivity.REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
            String path = FileUtil.getSaveFile(getContext().getApplicationContext(),2).getAbsolutePath();
            recBankCard(getActivity(),path);
        }
    }

    /**
     * 接收银行卡返回信息
     * @param ctx
     * @param filePath
     */
    public void recBankCard(final Context ctx, final String filePath) {
        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));
        OCR.getInstance(ctx).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                handler.sendMessage(handler.obtainMessage(which,result));
            }

            @Override
            public void onError(OCRError error) {
                BankCardFragment.this.onError(error,true);
            }
        });
    }

    public void setBankImageView(String filepath,ImageView imageView){
        Glide.with(this)
                .load(filepath)
                .apply(new GlideRequestOptions().bitmapTransform(new RoundedCorners(20)).signature(new ObjectKey(DataUtils.GetNowTime()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(imageView);
    }

}
