package cn.mofufin.morf.ui.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.ScanQRSubmitActivity;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentMerchantEntryBinding;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantEntryFragment extends BaseFragment implements TakePhoto.TakeResultListener,InvokeListener {
    private FragmentMerchantEntryBinding binding;
    private InvokeParam invokeParam;
    private File IDCardA//身份证正面
            ,IDCardB//身份证反面
            ,BankA//银行卡正面
            ,BankB//银行卡反面
            ,inside//店内面
            ,License//营业执照
            ,Cashier//收银台
            ,Enterprise;//店面
    private TakePhoto takePhoto;
    private String flag;

    private enum PhotoEnum{
        IDCardA,IDCardB,BankA,BankB,inside,License,Cashier,Enterprise
    }

    public MerchantEntryFragment() {
        // Required empty public constructor
    }

    public static MerchantEntryFragment newInstance() {
        return new MerchantEntryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_merchant_entry, container, false);
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
    }

    public void next(View view){
        if (isFastClick())
            return;

        if (null==IDCardA || !IDCardA.exists()){
            showTips("上传法人身份证正面");
            return;
        }else if (null==IDCardB || !IDCardB.exists()){
            showTips("上传法人身份证反面");
            return;
        }else if (null==BankA || !BankA.exists()){
            showTips("上传银行卡正面");
            return;
        }else if (null==BankB || !BankB.exists()){
            showTips("上传法银行卡反面");
            return;
        }else if (null==License || !License.exists()){
            showTips("上传营业执照");
            return;
        }else if (null==Cashier || !Cashier.exists()){
            showTips("上传收银台照片");
            return;
        }else if (null==Enterprise || !Enterprise.exists()){
            showTips("上传店铺门头照片");
            return;
        }else if (null==inside || !inside.exists()){
            showTips("上传店内照片");
            return;
        }
        ScanQRSubmitActivity.fileMap.put("archFlph05",IDCardA);
        ScanQRSubmitActivity.fileMap.put("archFlph06",IDCardB);
        ScanQRSubmitActivity.fileMap.put("archFlph15",Enterprise);
        ScanQRSubmitActivity.fileMap.put("archFlph16",Cashier);
        ScanQRSubmitActivity.fileMap.put("archFlph01",License);
        ScanQRSubmitActivity.fileMap.put("archFlph17",inside);
        ScanQRSubmitActivity.fileMap.put("archFlph18",BankA);
        ScanQRSubmitActivity.fileMap.put("archFlph19",BankB);
        RxBus.getInstance().post(RxEvent.SCAN_QR_POSITION,5);
    }

    public void takePhoto(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setMessage("选择图片")
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Camera(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
                                        .subscribe(new Action1<Boolean>() {

                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean){
                                                    Album(name);
                                                }else {
                                                    DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
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
        takePhoto(PhotoEnum.inside.name());
    }

    public void takeLicense(View view){
        takePhoto(PhotoEnum.License.name());
    }

    public void takeCashier(View view){
        takePhoto(PhotoEnum.Cashier.name());
    }

    public void takeEnterprise(View view){
        takePhoto(PhotoEnum.Enterprise.name());
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
        Uri imageUri = Uri.fromFile(file);

        configTakePhotoOption(getTakePhoto());
        configCompress(getTakePhoto());
        takePhoto.onPickFromGallery();

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


    @Override
    public void takeSuccess(final TResult result) {
        if (null!=result.getImages()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView photo = null;
                    String path = result.getImages().get(0).getCompressPath();
                    if (flag.equals(PhotoEnum.IDCardA.name())){
                        photo = binding.photoIdcardPostive;
                        IDCardA = new File(path);
                    }else if (flag.equals(PhotoEnum.IDCardB.name())){
                        photo = binding.photoIdcardBack;
                        IDCardB = new File(path);
                    }else if (flag.equals(PhotoEnum.BankA.name())){
                        photo = binding.photoBankcardPostive;
                        BankA = new File(path);
                    }else if (flag.equals(PhotoEnum.BankB.name())){
                        photo = binding.photoBankcardBack;
                        BankB = new File(path);
                    }else if (flag.equals(PhotoEnum.inside.name())){
                        photo = binding.photoInside;
                        inside = new File(path);
                    }else if (flag.equals(PhotoEnum.Cashier.name())){
                        photo = binding.photoCashier;
                        Cashier = new File(path);
                    }else if (flag.equals(PhotoEnum.Enterprise.name())){
                        photo = binding.photoEnterprise;
                        Enterprise = new File(path);
                    }else if (flag.equals(PhotoEnum.License.name())){
                        photo = binding.photoBusinessLicense;
                        License = new File(path);
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
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
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
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }


    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


}
