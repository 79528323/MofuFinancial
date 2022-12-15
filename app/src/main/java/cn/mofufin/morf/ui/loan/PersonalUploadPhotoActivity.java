package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityPersonalUploadphotoBinding;
import cn.mofufin.morf.databinding.ActivityProductAboutUsBinding;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.entity.SelfImgInfo;
import cn.mofufin.morf.ui.entity.SelfTextInfo;
import cn.mofufin.morf.ui.fragment.MerchantEntryFragment;
import cn.mofufin.morf.ui.services.LoanImAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.IntentParam;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersonalUploadPhotoActivity extends BaseActivity  implements TakePhoto.TakeResultListener, InvokeListener {
    private ActivityPersonalUploadphotoBinding binding;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private String flag;
    private Map<String,File> fileMap = new HashMap<>();
    private Map<String,String> params = new HashMap<>();
    private File IDCardA,IDCardB,IDCard_Handler,BankA,BankB,Credit_report,Income,BankAccount,Property;
    private Context context;
    private enum PhotoEnum{
        IDCardA,IDCardB,IDCard_Handler,BankA,BankB,Credit_report,Income,BankAccount,Property
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_personal_uploadphoto);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        context = PersonalUploadPhotoActivity.this;
        binding.setPosition(1);
        SelfImgInfo.MerSelfImgBean merSelfImgBean = getIntent().getParcelableExtra(IntentParam.BEAN);
        setImgsHolder(merSelfImgBean);
        matchImgLayout();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    @Override
    public void finish() {
        if (binding.getPosition()>1){
            binding.setPosition(binding.getPosition()-1);
        }else
            super.finish();
    }

    public void submitPhoto(View view){
        if (null==Income){
            showTips("请上传收入证明或纳税税单照片");
            return;
        }else if (null==BankAccount){
            showTips("请上传个人银行卡流水照片");
            return;
        }else if (null==Property){
            showTips("请上传财产证明照片");
            return;
        }

        params.put("appKey","9b0d250532fcd28261e5ab263ef29f24");
        params.put("officeCode", HttpParam.OFFICE_CODE);
        params.put("merchantCode", MerchanInfoDB.queryInfo().merchantCode);
        params.put("version", Common.LOAN_VERSION);

        fileMap.put("identityZ",IDCardA);
        fileMap.put("identityF",IDCardB);
        fileMap.put("identityS",IDCard_Handler);
        fileMap.put("cardZ",BankA);
        fileMap.put("cardF",BankB);
        fileMap.put("creditSurvey",Credit_report);
        fileMap.put("incomeProve",Income);
        fileMap.put("propertyProve",Property);
        fileMap.put("bankWater",BankAccount);

        Subscription subscription = LoanImAPI.uploadSelfImg(params,fileMap)
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
                .subscribe(new Action1<BaseResult>() {
                    @Override
                    public void call(BaseResult baseResult) {
                        Intent intent = new Intent(PersonalUploadPhotoActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,4);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        startActivity(intent);
                        if (baseResult.result_Code==0){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.setPosition(1);
                                    finish();
                                }
                            },800);
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

    public void nextA(View view){
        if (null==IDCardA){
            showTips("请上传身份证正面");
            return;
        }else if (null==IDCardB){
            showTips("请上传身份证反面");
            return;
        }else if (null==IDCard_Handler){
            showTips("请上传手持身份证");
            return;
        }
        binding.setPosition(2);
    }

    public void nextB(View view){
        if (null==BankA){
            showTips("请上传银行卡正面");
            return;
        }else if (null==BankB){
            showTips("请上传银行卡反面");
            return;
        }else if (null==Credit_report){
            showTips("请上传征信报告");
            return;
        }
        binding.setPosition(3);
    }

    public void takeIDCardA(View view){
        takePhoto(PhotoEnum.IDCardA.name());
    }

    public void takeIDCardB(View view){
        takePhoto(PhotoEnum.IDCardB.name());
    }

    public void takeBankCardA(View view){
        takePhoto(PhotoEnum.BankA.name());
    }

    public void takeBankCardB(View view){
        takePhoto(PhotoEnum.BankB.name());
    }

    public void takeHandUp(View view){
        takePhoto(PhotoEnum.IDCard_Handler.name());
    }

    public void takeCreditReport(View view){
        takePhoto(PhotoEnum.Credit_report.name());
    }

    public void takeIncome(View view){
        takePhoto(PhotoEnum.Income.name());
    }

    public void takeBankAccount(View view){
        takePhoto(PhotoEnum.BankAccount.name());
    }

    public void takeProperty(View view){
        takePhoto(PhotoEnum.Property.name());
    }


    public void takePhoto(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalUploadPhotoActivity.this)
                .setCancelable(true)
                .setMessage("选择图片")
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(PersonalUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Camera(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(PersonalUploadPhotoActivity.this,getString(R.string.permissions_camer));
                                                }
                                            }
                                        });

                            }
                        });
                    }
                })
                .setPositiveButton("图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(PersonalUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Album(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(PersonalUploadPhotoActivity.this,getString(R.string.permissions_camer));
                                                }
                                            }
                                        });

                            }
                        });
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void Camera(final String name){
        flag = name;
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configTakePhotoOption(getTakePhoto());
        configCompress(getTakePhoto());
        takePhoto.onPickFromCapture(imageUri);
    }


    public void Album(final String name){
        flag = name;
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
//        Uri imageUri = Uri.fromFile(file);

        configTakePhotoOption(getTakePhoto());
        configCompress(getTakePhoto());
        takePhoto.onPickFromGallery();

    }

    @Override
    public void takeSuccess(final TResult result) {
        if (null!=result.getImages()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView photo = null;
                    String path = result.getImages().get(0).getCompressPath();
                    Logger.e("path="+path);
                    if (flag.equals(PhotoEnum.IDCardA.name())){
                        Logger.e("1111path="+path);
                        photo = binding.idCardA;
                        IDCardA = new File(path);
                    }else if (flag.equals(PhotoEnum.IDCardB.name())){
                        photo = binding.idCardB;
                        IDCardB = new File(path);
                    }else if (flag.equals(PhotoEnum.BankA.name())){
                        photo = binding.BankCardA;
                        BankA = new File(path);
                    }else if (flag.equals(PhotoEnum.BankB.name())){
                        photo = binding.BankCardB;
                        BankB = new File(path);
                    }else if (flag.equals(PhotoEnum.IDCard_Handler.name())){
                        photo = binding.idCardHandler;
                        IDCard_Handler = new File(path);
                    }else if (flag.equals(PhotoEnum.Credit_report.name())){
                        photo = binding.CreditReport;
                        Credit_report = new File(path);
                    }else if (flag.equals(PhotoEnum.Property.name())){
                        photo = binding.PropertyCertificate;
                        Property = new File(path);
                    }else if (flag.equals(PhotoEnum.BankAccount.name())){
                        photo = binding.BankAccount;
                        BankAccount = new File(path);
                    }else if (flag.equals(PhotoEnum.Income.name())){
                        photo = binding.IncomeCertificate;
                        Income = new File(path);
                    }
                    Glide.with(photo.getContext()).load(path).apply(new GlideRequestOptions())
                            .into(photo);
                }
            });
        }
    }


    @Override
    public void takeFail(TResult result, String msg) {
        showTips(msg);
    }

    @Override
    public void takeCancel() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }


    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    //回读图片
    public void setImgsHolder(final SelfImgInfo.MerSelfImgBean selfImg){
        String IMG_AUTH = BuildConfig.FLAVOR.equals(Common.TYPE_DEVELOP)?"/JFBank/": "/";
        if (selfImg!=null){
            if (!TextUtils.isEmpty(selfImg.identityZ)){
                String path = Common.HOST +IMG_AUTH + selfImg.imgUploadPath + "/";
                path+=selfImg.identityZ;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                IDCardA = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.idCardA);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.identityF)){
                String path = Common.HOST +IMG_AUTH + selfImg.imgUploadPath + "/";
                path+=selfImg.identityF;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                IDCardB = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.idCardB);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.identityS)){
                String path = Common.HOST +IMG_AUTH + selfImg.imgUploadPath + "/";
                path+=selfImg.identityS;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                IDCard_Handler = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.idCardHandler);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.cardZ)){
                String path = Common.HOST +IMG_AUTH + selfImg.imgUploadPath + "/";
                path+=selfImg.cardZ;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                BankA = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.BankCardA);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.cardF)){
                String path = Common.HOST +IMG_AUTH + selfImg.imgUploadPath + "/";
                path+=selfImg.cardF;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                BankB = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.BankCardB);
                            }
                        });
            }


            if (!TextUtils.isEmpty(selfImg.creditSurvey)){
                String path = Common.HOST +IMG_AUTH+ selfImg.imgUploadPath + "/";
                path+=selfImg.creditSurvey;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                Credit_report = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.CreditReport);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.incomeProve)){
                String path = Common.HOST +IMG_AUTH+ selfImg.imgUploadPath + "/";
                path+=selfImg.incomeProve;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                Income = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.IncomeCertificate);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.propertyProve)){
                String path = Common.HOST +IMG_AUTH+ selfImg.imgUploadPath + "/";
                path+=selfImg.propertyProve;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                Property = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.PropertyCertificate);
                            }
                        });
            }

            if (!TextUtils.isEmpty(selfImg.bankWater)){
                String path = Common.HOST +IMG_AUTH+ selfImg.imgUploadPath + "/";
                path+=selfImg.bankWater;
                Glide.with(context).asFile().load(path)
                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                BankAccount = resource;
                                Glide.with(context).load(resource)
                                        .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                        .into(binding.BankAccount);
                            }
                        });
            }
        }

    }

    /**
     * 设定背景图片宽高
     */
    public void matchImgLayout(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWithd = (metrics.widthPixels/6)*4;
        int screenHeight = screenWithd/2;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.IncomeCertificateLayout.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = screenHeight;
        binding.IncomeCertificateLayout.setLayoutParams(params);
        binding.executePendingBindings();
    }

}
