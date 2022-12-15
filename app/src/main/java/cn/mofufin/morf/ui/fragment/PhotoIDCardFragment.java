package cn.mofufin.morf.ui.fragment;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

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
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.RealNameIdentityActivity;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.FragmentPhotoIdcardBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoIDCardFragment extends BaseFragment implements TakePhoto.TakeResultListener,InvokeListener{
    public static PhotoIDCardFragment instance=null;
    private FragmentPhotoIdcardBinding binding;
//    private TakePictureManager takePictureManager;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private File uploadfile;
    public PhotoIDCardFragment() {
        // Required empty public constructor
    }

    public static PhotoIDCardFragment newInstance() {
        if (instance==null)
            instance = new PhotoIDCardFragment();
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_photo_idcard, container, false);
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
        if (null == uploadfile || !uploadfile.exists()){
            showTips("请上传图片");
            return;
        }

        RealNameIdentityActivity.fileMap.put("handPhotoA",uploadfile);
//        TransationPasswordFragment passwordFragment = TransationPasswordFragment.newInstance();
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fragment_slide_right_in,
//                R.anim.fragment_slide_left_out,R.anim.fragment_slide_left_in,R.anim.fragment_slide_right_out);
//        transaction.add(R.id.scanning_container,passwordFragment);
//        transaction.addToBackStack(null);
//        transaction.commitAllowingStateLoss();

        RxBus.getInstance().post(RxEvent.SUBMIT_REAL_NAME,4);
    }

    public void takePhoto(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setMessage("选择图片")
                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Camera();
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

                                Album();
                            }
                        });
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void Camera(){
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configTakePhotoOption(getTakePhoto());
        configCompress(getTakePhoto());
        takePhoto.onPickFromCapture(imageUri);


//        takePictureManager = new TakePictureManager(this);
////        takePictureManager.setTailor(10, 10, 550, 550);
//        takePictureManager.startTakeWayByCarema();
//        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
//            @Override
//            public void successful(boolean isTailor, File outFile, Uri filePath) {
//                Logger.e(UpdateVersionUtil.getAppCurrVersionName()+" takePhoto=="+filePath.getPath());
//                Logger.e(UpdateVersionUtil.getAppCurrVersionName()+" outFile=="+outFile.getAbsolutePath());
//                binding.photo.setTag(null);
//                Glide.with(binding.photo.getContext()).load(outFile).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(binding.photo);
//                binding.photo.setTag(outFile);
//            }
//
//            @Override
//            public void failed(int errorCode, List<String> deniedPermissions) {
//
//            }
//        });
    }

    public void Album(){
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configTakePhotoOption(getTakePhoto());
        configCompress(getTakePhoto());
        takePhoto.onPickFromGallery();
//        takePictureManager = new TakePictureManager(this);
////        takePictureManager.setTailor(10, 10, 550, 550);
//        takePictureManager.startTakeWayByAlbum();
//        takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
//            @Override
//            public void successful(boolean isTailor, File outFile, Uri filePath) {
//                binding.photo.setTag(null);
//                Glide.with(binding.photo.getContext()).load(outFile).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(binding.photo);
//                binding.photo.setTag(outFile);
//            }
//
//            @Override
//            public void failed(int errorCode, List<String> deniedPermissions) {
//
//            }
//
//        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Logger.e("data=="+data);
////        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

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
    public void takeSuccess(final TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        if (null!=result.getImages()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String path = result.getImages().get(0).getCompressPath();
                    Glide.with(PhotoIDCardFragment.this).load(path).apply(new GlideRequestOptions()).into(binding.photo);
                    uploadfile = new File(path);
                    Logger.e("file size ="+ uploadfile.length());
                }
            });
        }

    }

    @Override
    public void takeFail(TResult result, String msg) {
//        Log.i(TAG, "takeFail:" + msg);
        showTips(msg);
    }

    @Override
    public void takeCancel() {
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


}
