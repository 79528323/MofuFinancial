package cn.mofufin.morf.ui.fragment;


import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
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
import org.json.JSONException;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.FragmentFoundBinding;
import cn.mofufin.morf.ui.LaucherActivity;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.base.BaseFragment;
import cn.mofufin.morf.ui.entity.Contacts;
import cn.mofufin.morf.ui.util.DataUtils;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.functions.Action1;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;

/**
 * 发现
 */
public class FoundFragment extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener {
    public FragmentFoundBinding binding;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private DisplayMetrics metrics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_found,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setHandlers(this);
        metrics = getActivity().getResources().getDisplayMetrics();

        ImageView add = new ImageView(getActivity());
        add.setBackgroundResource(R.drawable.add_image);
        add.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels/3,metrics.widthPixels/3));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImg(v);
            }
        });
        binding.photoList.addView(add);



    }

    public void addImg(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                .setCancelable(true)
//                .setMessage("选择图片")
//                .setNegativeButton("相机", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
//                                        .subscribe(new Action1<Boolean>() {
//
//                                            @Override
//                                            public void call(Boolean aBoolean) {
//                                                if (aBoolean){
//                                                    Camera();
//                                                }else {
//                                                    DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
//                                                }
//                                            }
//                                        });
//
//                            }
//                        });
//                    }
//                })
//                .setPositiveButton("图库", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new RxPermissions(getActivity()).request(Manifest.permission.CAMERA)
//                                        .subscribe(new Action1<Boolean>() {
//
//                                            @Override
//                                            public void call(Boolean aBoolean) {
//                                                if (aBoolean){
//                                                    Album();
//                                                }else {
//                                                    DataUtils.setPermissionDailog(getActivity(),getString(R.string.permissions_camer));
//                                                }
//                                            }
//                                        });
//
//                            }
//                        });
//                    }
//                });
//        Dialog dialog = builder.create();
//        dialog.show();

        new RxPermissions(getActivity()).request(Manifest.permission.READ_CONTACTS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean){
//                            getContactsInfo();
                            new ContactsInfoQuery(getActivity().getContentResolver())
                                    .startQuery(0,null,ContactsContract.Data.CONTENT_URI,null,null,null,ContactsContract.Data.RAW_CONTACT_ID);
                        }else {
//                            new TipsDialog(getActivity(), "权限管理", "确定"
//                                    , new TipsDialog.OnButtonClickListener() {
//                                @Override
//                                public void buttonClicked() {}
//                            });
                        }
                    }
                });
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
    public void takeSuccess(TResult result) {
        if (null!=result.getImages()){
            String path = result.getImages().get(0).getCompressPath();
            ImageView photo = new ImageView(getActivity());
            photo.setScaleType(ImageView.ScaleType.CENTER);

//            ViewGroup.LayoutParams params = binding.photoList.getLayoutParams();
//            params.width = metrics.widthPixels/3;
//            params.height = metrics.widthPixels/3;
            photo.setLayoutParams(new ViewGroup.LayoutParams(metrics.widthPixels/3,metrics.widthPixels/3));
            Glide.with(photo.getContext()).load(path).apply(new GlideRequestOptions())
                    .into(photo);
            binding.photoList.addView(photo,0);
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


    private List<Contacts> contactsList = new ArrayList<>();

    public void getContactsInfo(Cursor cursor) throws JSONException {
        String mimetype = "";
        int oldrid = -1;
        int contactId = -1;
//        Uri uri = ContactsContract.Data.CONTENT_URI;
//        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null, ContactsContract.Data.RAW_CONTACT_ID);
        int num =0;
        while (cursor.moveToNext()){
            contactId = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            if (oldrid != contactId){

            }

            mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
            if (StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)){
                String displayName = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));
                Logger.e("displayName=="+displayName);

            }

            if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)){
                int phoneType = cursor
                        .getInt(cursor.getColumnIndex(Phone.TYPE));
                if (phoneType == Phone.TYPE_MOBILE){ // 手机
                    String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    Logger.e("手机=="+phone);
                }
                if (phoneType == Phone.TYPE_WORK){ // 单位
                    String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    Logger.e("单位=="+phone);
                }
                if (phoneType == Phone.TYPE_MMS){ // 彩信
                    String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    Logger.e("彩信=="+phone);
                }
                if (phoneType == Phone.TYPE_TELEX){ // TELEX
                    String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
                    Logger.e("TELEX=="+phone);
                }
            }

            if (Email.CONTENT_ITEM_TYPE.equals(mimetype)){
                Logger.e("Email.CONTENT_ITEM_TYPE");
                int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));
                if (emailType == Email.TYPE_CUSTOM){
                    String email = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    Logger.e("自定义邮件地址=="+email);
                }
                if (emailType == Email.TYPE_HOME){
                    String email = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    Logger.e("住宅邮件地址=="+email);
                }
                if (emailType == Email.TYPE_WORK){
                    String email = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    Logger.e("单位邮件地址=="+email);
                }
                if (emailType == Email.TYPE_MOBILE){
                    String email = cursor.getString(cursor.getColumnIndex(Email.DATA));
                    Logger.e("手机邮件地址=="+email);
                }
            }

            if (SipAddress.CONTENT_ITEM_TYPE.equals(mimetype)){
                int addressType = cursor.getInt(cursor.getColumnIndex(SipAddress.TYPE));
                if (addressType == SipAddress.TYPE_HOME){
                    String address = cursor.getString(cursor.getColumnIndex(SipAddress.DATA));
                }
            }
        }
    }

    class ContactsInfoQuery extends AsyncQueryHandler{

        public ContactsInfoQuery(ContentResolver cr) {
            super(cr);
        }

        @Override
        public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
            Logger.e("startQuery");
            super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Logger.e("onQueryComplete");
            try {
                getContactsInfo(cursor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onQueryComplete(token, cookie, cursor);
        }
    }
}
