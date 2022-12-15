package cn.mofufin.morf.ui.loan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.BuildConfig;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityBusinessUploadphotoBinding;
import cn.mofufin.morf.databinding.ActivityPrivaterUploadphotoBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.BaseResult;
import cn.mofufin.morf.ui.entity.SelfImgInfo;
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

public class BusinessUploadPhotoActivity extends BaseActivity  implements TakePhoto.TakeResultListener, InvokeListener {
    private ActivityBusinessUploadphotoBinding binding;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private String flag;
    private Map<String,File> fileMap = new HashMap<>();
    private Map<String,String> params = new HashMap<>();
    private File loansUseProve,refundSource,pact;
    private Context context;
    private enum PhotoEnum{
        loansUseProve,refundSource,pact
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_business_uploadphoto);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        context = this;
        SelfImgInfo.MerBusinessImg merBusinessImg = getIntent().getParcelableExtra(IntentParam.BEAN);
        setImgsHolder(merBusinessImg);
    }

    @Override
    public void submit(View view) {
        super.submit(view);
    }

    public void submits(View view){
        if (null == loansUseProve){
            showTips("请上传贷款用途证明照片");
            return;
        }else if (null == refundSource){
            showTips("请上传还款来源决议或承诺书照片");
            return;
        }else if (null == pact){
            showTips("请上传电子合同照片");
            return;
        }

        params.put("appKey","fcbb6dcbbdc3a0a5efcc1e8773bb4757");
        params.put("officeCode", HttpParam.OFFICE_CODE);
        params.put("merchantCode", MerchanInfoDB.queryInfo().merchantCode);
        params.put("version", Common.LOAN_VERSION);

        fileMap.put("loansUseProve",loansUseProve);
        fileMap.put("refundSource",refundSource);
        fileMap.put("pact",pact);

        Subscription subscription = LoanImAPI.uploadBusinessImg(params,fileMap)
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

                        Intent intent = new Intent(BusinessUploadPhotoActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,6);
                        intent.putExtra(IntentParam.TIPS,baseResult.result_Msg);
                        startActivity(intent);
                        if (baseResult.result_Code==0){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
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

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }


    public void takePhoto(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessUploadPhotoActivity.this)
                .setCancelable(true)
                .setMessage("选择图片")
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(BusinessUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Camera(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(BusinessUploadPhotoActivity.this,getString(R.string.permissions_camer));
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
                                new RxPermissions(BusinessUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Album(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(BusinessUploadPhotoActivity.this,getString(R.string.permissions_camer));
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

    public void takeProve(View view){
        takePhoto(PhotoEnum.loansUseProve.name());
    }

    public void takeRefund(View view){
        takePhoto(PhotoEnum.refundSource.name());
    }

    public void takePact(View view){
        takePhoto(PhotoEnum.pact.name());
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
                    if (flag.equals(PhotoEnum.loansUseProve.name())){
                        photo = binding.takeProve;
                        loansUseProve = new File(path);
                    }else if (flag.equals(PhotoEnum.refundSource.name())){
                        photo = binding.takeRefund;
                        refundSource = new File(path);
                    }else if (flag.equals(PhotoEnum.pact.name())){
                        photo = binding.takePact;
                        pact = new File(path);
                    }

                    Glide.with(photo.getContext()).load(path)
                            .apply(new RequestOptions()
                                    .signature(new ObjectKey(System.currentTimeMillis())))
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


    public void setImgsHolder(final SelfImgInfo.MerBusinessImg merBusinessImg){
        if (merBusinessImg!=null){
            String IMG_AUTH = BuildConfig.FLAVOR.equals(Common.TYPE_DEVELOP)?"/JFBank/": "/";
            String host = Common.HOST +IMG_AUTH + merBusinessImg.imgUploadPath + "/";

            Glide.with(context).asFile().load(host + merBusinessImg.loansUseProve)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            loansUseProve = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.takeProve);
                        }
                    });


            Glide.with(context).asFile().load(host + merBusinessImg.refundSource)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            refundSource = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.takeRefund);
                        }
                    });


            Glide.with(context).asFile().load(host + merBusinessImg.pact)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            pact = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.takePact);
                        }
                    });

        }

    }
}
