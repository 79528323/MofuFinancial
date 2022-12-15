package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.fragment.BankCardFragment;
import cn.mofufin.morf.ui.fragment.PhotoIDCardFragment;
import cn.mofufin.morf.ui.fragment.ScanIDCardFragment;
import cn.mofufin.morf.ui.fragment.TransationPasswordFragment;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.GeneralResponse;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.util.FileUtil;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.databinding.ActivityScanningBinding;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;
import cn.mofufin.morf.ui.widget.TipsDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RealNameIdentityActivity extends BaseActivity {

    public static final int REQUEST_CODE_BANKCARD = 111;

    ActivityScanningBinding binding;
    public static boolean hasGotToken = false;
    private ScanIDCardFragment scanIDCardFragment;
    private BankCardFragment bankCardFragment;
    private PhotoIDCardFragment photoIDCardFragment;
    private TransationPasswordFragment transationPasswordFragment;

    private FragmentManager fragmentManager;

    public static Map<String,String> params;
    public static Map<String,File> fileMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isGone = SharedPreferencesUtils.getBoolean(
                this,RealNameCourseActivity.PARAM_REALNAME_COURSE,false);
        if (!isGone){
            startActivity(new Intent(this,RealNameCourseActivity.class));
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(this,R.layout.activity_scanning);
        binding.setHandlers(this);

        initView();
    }

    public void initView(){
        params = new HashMap<>();
        fileMap = new HashMap<>();

        params.put("appKey", HttpParam.SUBMISSION_APPKEY);
        params.put("officeCode",HttpParam.OFFICE_CODE);
        User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        params.put("merchantPhone",bean.merchantPhone);

        rxManager.onRxEvent(RxEvent.SUBMIT_REAL_NAME).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                int pos = (int) o;
                if (pos <= 4){
                    binding.setPosition(pos);
                    selectFragment(pos);
                }else {
                    submit();
                }
            }
        });

        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        Logger.e(msg);
                    }
                });

        initAccessToken();

        scanIDCardFragment = ScanIDCardFragment.newInstance();
        bankCardFragment = BankCardFragment.newInstance();
        photoIDCardFragment = PhotoIDCardFragment.newInstance();
        transationPasswordFragment = TransationPasswordFragment.newInstance();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.scanning_container,scanIDCardFragment);
        transaction.add(R.id.scanning_container,bankCardFragment);
        transaction.add(R.id.scanning_container,photoIDCardFragment);
        transaction.add(R.id.scanning_container,transationPasswordFragment);
        transaction.commitAllowingStateLoss();
        RxBus.getInstance().post(RxEvent.SUBMIT_REAL_NAME,1);
    }


    public void hideFragmentAll(FragmentTransaction fragmentTransaction){
        if (scanIDCardFragment!=null)
            fragmentTransaction.hide(scanIDCardFragment);

        if (bankCardFragment!=null)
            fragmentTransaction.hide(bankCardFragment);

        if (photoIDCardFragment!=null)
            fragmentTransaction.hide(photoIDCardFragment);

        if (transationPasswordFragment!=null)
            fragmentTransaction.hide(transationPasswordFragment);
    }

    public void selectFragment(int position){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragmentAll(transaction);

        switch (position){
            case 1:
                if (scanIDCardFragment!=null){
                    transaction.show(scanIDCardFragment);
                }else {
                    scanIDCardFragment = ScanIDCardFragment.newInstance();
                    transaction.add(R.id.scanning_container,scanIDCardFragment);
                }
                break;

            case 2:
                if (bankCardFragment!=null){
                    transaction.show(bankCardFragment);
                }else {
                    bankCardFragment = BankCardFragment.newInstance();
                    transaction.add(R.id.scanning_container,bankCardFragment);
                }
                break;

            case 3:
                if (photoIDCardFragment!=null){
                    transaction.show(photoIDCardFragment);
                }else {
                    photoIDCardFragment = PhotoIDCardFragment.newInstance();
                    transaction.add(R.id.scanning_container,photoIDCardFragment);
                }
                break;

            case 4:
                if (transationPasswordFragment!=null){
                    transaction.show(transationPasswordFragment);
                }else {
                    transationPasswordFragment = TransationPasswordFragment.newInstance();
                    transaction.add(R.id.scanning_container,transationPasswordFragment);
                }
                break;
        }

        transaction.commitAllowingStateLoss();
    }

//    public void selectFragment(View view){
//        int position = Integer.valueOf((String) view.getTag());
//        binding.setPosition(position);
//        selectFragment(position);
//    }

    @Override
    protected boolean enableSliding() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 存储 Fragment 的状态。
//        getSupportFragmentManager().putFragment(outState, "ScanIDCardFragment", scanIDCardFragment);
    }

    @Override
    public void submit(View view) {
        startActivity(new Intent(this,RealNameCourseActivity.class));
        super.submit(view);
    }

    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
//        params.clear();
        params = null;

        FileUtil.deleteAllFile(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (binding.getPosition()>1){
            binding.setPosition(binding.getPosition()-1);
            selectFragment(binding.getPosition());
        }else {
//            if (params.size()>3)
                exitDialog();
//            else
//                super.onBackPressed();
        }
    }

    @Override
    public void exit(View view) {
//        if (params.size() >3)
            exitDialog();
//        else
//            super.exit(view);
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Logger.e("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    public void submit(){
        Subscription subscription= SubMissionImpAPI.identityAuthen(params,fileMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        hiddenLoading();
                    }
                })
                .subscribe(new Action1<BaseResponse<GeneralResponse.DataBean>>() {
                    @Override
                    public void call(final BaseResponse<GeneralResponse.DataBean> baseResponse) {
//                        showTips();
                        new TipsDialog(RealNameIdentityActivity.this, baseResponse.data.reason, "确定", null,
                                new TipsDialog.OnButtonClickListener() {
                                    @Override
                                    public void buttonClicked() {
                                        if (baseResponse.bool)
                                            finish();
                                    }
                                }).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }


    public void exitDialog(){
        new TipsDialog(this,
                getString(R.string.exit_dailog_tips),
                getString(R.string.confirm),
                getString(R.string.cancel),
                new TipsDialog.OnButtonClickListener() {
            @Override
            public void buttonClicked() {
                finish();
            }
        }).show();
    }
}
