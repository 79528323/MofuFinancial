package cn.mofufin.morf.ui.repayment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.mofufin.morf.R;
import cn.mofufin.morf.adapter.CoinAdapter;
import cn.mofufin.morf.databinding.ActivityAboutRepaymentBinding;
import cn.mofufin.morf.databinding.ActivityMofuCoinBinding;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Coin;
import cn.mofufin.morf.ui.mine.CoinDetailActivity;
import cn.mofufin.morf.ui.mine.MofuPlayActivity;
import cn.mofufin.morf.ui.services.UserImpAPI;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.widget.MyDatePickerDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 摩富币
 */
public class AboutRepayMentActivity extends BaseActivity {
    private ActivityAboutRepaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_repayment);
        setStatusBar();
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        SpannableString spannableDetails = new SpannableString(binding.details.getText().toString());
        spannableDetails.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.app_blue)) ,16,
                binding.details.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableDetails.setSpan(new StyleSpan(Typeface.BOLD),16,
                binding.details.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        binding.details.setText(spannableDetails);


        SpannableString spannableDetails1 = new SpannableString(binding.details1.getText().toString());
        spannableDetails1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.app_blue)) ,23,
                binding.details1.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableDetails1.setSpan(new StyleSpan(Typeface.BOLD),23,
                binding.details1.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        binding.details1.setText(spannableDetails1);

        SpannableString spannablefail = new SpannableString(binding.detailsFail.getText().toString());
        spannablefail.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this,R.color.app_blue)) ,27,
                binding.detailsFail.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        spannablefail.setSpan(new StyleSpan(Typeface.BOLD),27,
                binding.detailsFail.getText().toString().length(),SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        binding.detailsFail.setText(spannablefail);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void setStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    /*| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.WHITE);
        }else {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
