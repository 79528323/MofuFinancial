package cn.mofufin.morf.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.RealNameIdentityActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.FileUtil;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.util.Validator;
import cn.mofufin.morf.ui.widget.DoubleTimeSelectDialog;
import cn.mofufin.morf.databinding.FragmentScanIdcardBinding;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanIDCardFragment extends BaseFragment {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;

    public static ScanIDCardFragment instance= null;
    private FragmentScanIdcardBinding binding;
    private Activity context;
    private int whichImg=0;// 0为正面 1为反面
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间为2100年，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd="2100-12-31";
    public String paramsStrTime = "";
    final String beginDeadTime = "1900-01-01";

    public Bundle savedState;

    private String path1 , path2;

    private RequestOptions options  = new RequestOptions();

    public ScanIDCardFragment() {
        // Required empty public constructor
    }

    public static ScanIDCardFragment newInstance() {
        if (instance == null)
            instance = new ScanIDCardFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_scan_idcard, container, false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        binding.setHandlers(this);
        binding.setIsInfoVisiable(false);

        options.signature(new ObjectKey(DataUtils.GetNowTime()));
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.encodeQuality(100);

        binding.positiveCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()){
                    return;
                }else {
                    new RxPermissions(getActivity()).request(Manifest.permission.CAMERA
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean permission) {
                                    if (permission) {//同意权限
                                        whichImg = 0;
                                        Intent intent = new Intent(context, CameraActivity.class);
                                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                                FileUtil.getSaveFile(context,0).getAbsolutePath());
                                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                                        // 使用本地质量控制能力需要授权，需要在OCR调用initAccessToken或者
                                        // initAccessTokenWithAkSk成功返回后才能获取License授权本地质量控制能力
                                        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,OCR.getInstance(getActivity()).getLicense());
                                        // 使用本地质量控制能力需要设置开启
                                        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);

                                        startActivityForResult(intent, REQUEST_CODE_CAMERA);


                                    }else {
                                        DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
                                    }
                                }
                            });

                }

            }
        });

        binding.backCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()){
                    return;
                }else {

                    new RxPermissions(getActivity()).request(Manifest.permission.CAMERA
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean permission) {
                                    if (permission){//同意权限
                                        whichImg = 1;
                                        Intent intent = new Intent(context, CameraActivity.class);
                                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                                FileUtil.getSaveFile(context,1).getAbsolutePath());
                                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                                        // 使用本地质量控制能力需要授权，需要在OCR调用initAccessToken或者
                                        // initAccessTokenWithAkSk成功返回后才能获取License授权本地质量控制能力
                                        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,OCR.getInstance(getActivity()).getLicense());
                                        // 使用本地质量控制能力需要设置开启
                                        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);

                                        startActivityForResult(intent, REQUEST_CODE_CAMERA);

                                    }else {
                                        // 用户拒绝了该权限，并且选中『不再询问』
                                        DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
                                    }
                                }
                            });



                }

            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.with(this).onDestroy();
    }

    public void next(){
        File cardA = new File(FileUtil.getSaveFile(context,0).getAbsolutePath());
        File cardB = new File(FileUtil.getSaveFile(context,1).getAbsolutePath());
        String IDNo = binding.scanIdIDNo.getText().toString();
        String IDName = binding.scanIdIDName.getText().toString();
        String date = binding.scanIdEdit.getText().toString();
        date = DataUtils.fullToHalf(date);
        if (!cardA.exists()){
            showTips("请上传身份证正面");
            return;
        }else if (!cardB.exists()){
            showTips("请上传身份证反面");
            return;
        }else if (TextUtils.isEmpty(IDNo)){
            showTips("请填写身份证号");
            return;
        }else if (TextUtils.isEmpty(IDName)){
            showTips("请填写真实姓名");
            return;
        }else if (TextUtils.isEmpty(date)){
            showTips("请选择有效期");
            return;
        }else if (!Validator.isValidIdCardDate(date)){
            showTips("有效期格式有误!");
            return;
        }


        RealNameIdentityActivity.fileMap.put("IDCardA",cardA);
        RealNameIdentityActivity.fileMap.put("IDCardB",cardB);
        RealNameIdentityActivity.params.put("IDNo",IDNo);
        RealNameIdentityActivity.params.put("IDName",IDName);

        try {
            int index = date.indexOf("-");
            String startDate = date.substring(0,index);
            String endDate = date.substring(index+1,date.length());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = format1.parse(startDate);
            RealNameIdentityActivity.params.put("IDStartDate",format2.format(date1));


            if (!TextUtils.equals("长期",endDate)){
                Date date2 = format1.parse(endDate);
                endDate = format2.format(date2);
            }

            RealNameIdentityActivity.params.put("IDEndDate",endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            showTips("有效期不正确");
        }

        RxBus.getInstance().post(RxEvent.SUBMIT_REAL_NAME,2);
    }

    private boolean checkTokenStatus() {
        if (!RealNameIdentityActivity.hasGotToken) {
            showTips("启动中，请稍候!");
        }
        return RealNameIdentityActivity.hasGotToken;
    }

    private boolean checkGalleryPermission() {
        int ret = ActivityCompat.checkSelfPermission(context, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
            return false;
        }
        return true;
    }

    private void recIDCard(String idCardSide, final String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        binding.setIsInfoVisiable(true);
        if (whichImg==0){
            Glide.with(this).load(filePath)
                    .apply(new GlideRequestOptions()
                            .bitmapTransform(new RoundedCorners(20))
                            .signature(new ObjectKey(DataUtils.GetNowTime()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(binding.positiveCardImg);

        }else {
            Glide.with(this).load(filePath)
                    .apply(new GlideRequestOptions()
                            .bitmapTransform(new RoundedCorners(20))
                            .signature(new ObjectKey(DataUtils.GetNowTime()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(binding.backCardImg);
        }


        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(30);

        OCR.getInstance(context).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if (whichImg==0){
                        binding.scanIdIDName.setText(result.getName().toString());
                        binding.scanIdIDNo.setText(result.getIdNumber().toString());
                    }else {
                        Word wordSign = result.getSignDate();
                        Word wordExpory = result.getExpiryDate();
                        if (wordExpory!=null && wordSign!=null){
                            binding.scanIdEdit.setText(result.getSignDate().toString() + "-"+result.getExpiryDate().toString());
                        }

                    }

                }
            }

            @Override
            public void onError(OCRError error) {
                Logger.e( error.getMessage());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
//                String filePath = FileUtil.getSaveFile(context,whichImg).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
//                        File A= new File(FileUtil.getSaveFile(context,0).getAbsolutePath());
//                        if (A.exists()){
//                            A.delete();
//                        }
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, FileUtil.getSaveFile(context,0).getAbsolutePath());
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, FileUtil.getSaveFile(context,1).getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 打开日期选择
     */
    public void showCustomTimePicker() {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(getActivity(), beginDeadTime, defaultWeekBegin, defaultWeekEnd);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    startTime = startTime.replace("-", "");
                    endTime = endTime.replace("-", "");

                    binding.scanIdEdit.setText(startTime + "-" + endTime);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        saveStateToArguments();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        saveStateToArguments();
//        outState = new Bundle();
//                if (null!=binding.positiveCardImg.getTag())
//            outState.putString("positiveCardImg", (String) binding.positiveCardImg.getTag());
//        if (null!=binding.backCardImg.getTag())
//            outState.putString("backCardImg", (String) binding.backCardImg.getTag());
//
//        outState.putString("name",binding.scanIdIDName.getText().toString());
//        outState.putString("idno",binding.scanIdIDNo.getText().toString());
//        outState.putString("date",binding.scanIdEdit.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState!=null){
//                    String path1= (String) savedState.getString("positiveCardImg","");
//        String path2= (String) savedState.getString("backCardImg","");
//
//        Glide.with(this).load(path1).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .signature(new StringSignature(DataUtils.GetNowTime()))
//                .into(binding.positiveCardImg);
//
//        Glide.with(this).load(path2).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .signature(new StringSignature(DataUtils.GetNowTime()))
//                .into(binding.backCardImg);
//
//        String name = savedState.getString("name","");
//        binding.scanIdIDName.setText(name);
//
//        String idNo = savedState.getString("idno","");
//        binding.scanIdIDNo.setText(idNo);
//
//        String date = savedState.getString("date","");
//        binding.scanIdEdit.setText(date);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //    private void saveStateToArguments() {
//        savedState = saveState();
//        if (savedState != null) {
//            Bundle b = getArguments();
//            b.putBundle("internalSavedViewState8954201239547", savedState);
//        }
//    }
//
//    private boolean restoreStateFromArguments() {
//        Bundle b = getArguments();
//        savedState = b.getBundle("internalSavedViewState8954201239547");
//        if (savedState != null) {
//            restoreState();
//            return true;
//        }
//        return false;
//    }

//    @Override
//    protected void onSaveState(Bundle outState) {
//        if (null!=binding.positiveCardImg.getTag())
//            outState.putString("positiveCardImg", (String) binding.positiveCardImg.getTag());
//        if (null!=binding.backCardImg.getTag())
//            outState.putString("backCardImg", (String) binding.backCardImg.getTag());
//
//        outState.putString("name",binding.scanIdIDName.getText().toString());
//        outState.putString("idno",binding.scanIdIDNo.getText().toString());
//        outState.putString("date",binding.scanIdEdit.getText().toString());
//    }
//
//    @Override
//    protected void onRestoreState(Bundle savedInstanceState) {
//        String path1= (String) savedState.getString("positiveCardImg","");
//        String path2= (String) savedState.getString("backCardImg","");
//
//        Glide.with(this).load(path1).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .signature(new StringSignature(DataUtils.GetNowTime()))
//                .into(binding.positiveCardImg);
//
//        Glide.with(this).load(path2).diskCacheStrategy(DiskCacheStrategy.ALL)
//                .signature(new StringSignature(DataUtils.GetNowTime()))
//                .into(binding.backCardImg);
//
//        String name = savedState.getString("name","");
//        binding.scanIdIDName.setText(name);
//
//        String idNo = savedState.getString("idno","");
//        binding.scanIdIDNo.setText(idNo);
//
//        String date = savedState.getString("date","");
//        binding.scanIdEdit.setText(date);
//    }

}
