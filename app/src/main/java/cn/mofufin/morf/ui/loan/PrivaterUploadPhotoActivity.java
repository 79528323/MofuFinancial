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
import cn.mofufin.morf.databinding.ActivityPrivaterUploadphotoBinding;
import cn.mofufin.morf.databinding.ActivitySpouseUploadphotoBinding;
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

public class PrivaterUploadPhotoActivity extends BaseActivity  implements TakePhoto.TakeResultListener, InvokeListener {
    private ActivityPrivaterUploadphotoBinding binding;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private String flag;
    private Map<String,File> fileMap = new HashMap<>();
    private Map<String,String> params = new HashMap<>();
    private File businessLicense,articles,capitlReport,site,bankSingle;
    private Context context;
    private enum PhotoEnum{
        businessLicense,articles,capitlReport,site,bankSingle
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_privater_uploadphoto);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        context = this;
        binding.setPosition(1);
        SelfImgInfo.MerSelfPrivateImg merSelfPrivateImg = getIntent().getParcelableExtra(IntentParam.BEAN);
        setImgsHolder(merSelfPrivateImg);
    }

    public void next(View view){
        if (null == businessLicense){
            showTips("请上传营业执照照片");
            return;
        }else if (null == articles){
            showTips("请上传企业章程及合作经营协议");
            return;
        }else if (null == capitlReport){
            showTips("请上传验资报告及出资协议");
            return;
        }

        binding.setPosition(2);
    }

    public void submit(View view){
        if (null == site){
            showTips("请上传固定经营场所产权证明或租赁协议");
            return;
        }else if (null == bankSingle){
            showTips("请上传近期银行对账单");
            return;
        }

        params.put("appKey","261b90916d263d4d3678a2c4e77d2996");
        params.put("officeCode", HttpParam.OFFICE_CODE);
        params.put("merchantCode", MerchanInfoDB.queryInfo().merchantCode);
        params.put("version", Common.LOAN_VERSION);

        fileMap.put("businessLicense",businessLicense);
        fileMap.put("articles",articles);
        fileMap.put("capitlReport",capitlReport);
        fileMap.put("site",site);
        fileMap.put("bankSingle",bankSingle);

        Subscription subscription = LoanImAPI.uploadSelfPrivateImg(params,fileMap)
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

                        Intent intent = new Intent(PrivaterUploadPhotoActivity.this,LoanUploadSuccessStatusActivity.class);
                        intent.putExtra(IntentParam.STATUS,baseResult.result_Code);
                        intent.putExtra(IntentParam.TYPE,5);
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

    @Override
    public void finish() {
        if (binding.getPosition()>1){
            binding.setPosition(binding.getPosition()-1);
        }else
            super.finish();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();
        super.onDestroy();
    }
    public void takePhoto(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(PrivaterUploadPhotoActivity.this)
                .setCancelable(true)
                .setMessage("选择图片")
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(PrivaterUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Camera(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(PrivaterUploadPhotoActivity.this,getString(R.string.permissions_camer));
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
                                new RxPermissions(PrivaterUploadPhotoActivity.this).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Album(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(PrivaterUploadPhotoActivity.this,getString(R.string.permissions_camer));
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

    public void takeBusin(View view){
        takePhoto(PhotoEnum.businessLicense.name());
    }

    public void takeArt(View view){
        takePhoto(PhotoEnum.articles.name());
    }

    public void takeReport(View view){
        takePhoto(PhotoEnum.capitlReport.name());
    }

    public void takeSite(View view){
        takePhoto(PhotoEnum.site.name());
    }

    public void takeBank(View view){
        takePhoto(PhotoEnum.bankSingle.name());
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
                    if (flag.equals(PhotoEnum.businessLicense.name())){
                        photo = binding.businessLicense;
                        businessLicense = new File(path);
                    }else if (flag.equals(PhotoEnum.articles.name())){
                        photo = binding.articles;
                        articles = new File(path);
                    }else if (flag.equals(PhotoEnum.capitlReport.name())){
                        photo = binding.capitlReport;
                        capitlReport = new File(path);
                    }else if (flag.equals(PhotoEnum.site.name())){
                        photo = binding.site;
                        site = new File(path);
                    }else if (flag.equals(PhotoEnum.bankSingle.name())){
                        photo = binding.bankSingle;
                        bankSingle = new File(path);
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


    public void setImgsHolder(final SelfImgInfo.MerSelfPrivateImg merSelfPrivateImg){
        if (merSelfPrivateImg!=null){
            String IMG_AUTH = BuildConfig.FLAVOR.equals(Common.TYPE_DEVELOP)?"/JFBank/": "/";
            String host = Common.HOST +IMG_AUTH + merSelfPrivateImg.imgUploadPath + "/";

            Glide.with(context).asFile().load(host + merSelfPrivateImg.businessLicense)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            businessLicense = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.businessLicense);
                        }
                    });


            Glide.with(context).asFile().load(host + merSelfPrivateImg.articles)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            articles = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.articles);
                        }
                    });


            Glide.with(context).asFile().load(host + merSelfPrivateImg.capitlReport)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            capitlReport = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.capitlReport);
                        }
                    });


            Glide.with(context).asFile().load(host + merSelfPrivateImg.site)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            site = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.site);
                        }
                    });


            Glide.with(context).asFile().load(host + merSelfPrivateImg.bankSingle)
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            bankSingle = resource;
                            Glide.with(context).load(resource)
                                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                                    .into(binding.bankSingle);
                        }
                    });
        }

    }
}
