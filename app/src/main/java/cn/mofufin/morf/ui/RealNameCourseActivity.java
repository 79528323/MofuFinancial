package cn.mofufin.morf.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.InputStream;
import java.math.BigDecimal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import cn.mofufin.morf.R;
import cn.mofufin.morf.databinding.ActivityAboutMineBinding;
import cn.mofufin.morf.databinding.ActivityRealnameCourseBinding;
import cn.mofufin.morf.ui.Listener.GlideRequestOptions;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.util.SharedPreferencesUtils;

public class RealNameCourseActivity extends BaseActivity {
    public static final String PARAM_REALNAME_COURSE = "param_realname_course";
    private ActivityRealnameCourseBinding binding;
    private DisplayMetrics metrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_realname_course);
        binding.setHandlers(this);

        initView();
    }

    public void initView() {
        boolean isGone = SharedPreferencesUtils.getBoolean(
                this,PARAM_REALNAME_COURSE,false);
        binding.setIsGone(isGone);

        metrics = getResources().getDisplayMetrics();

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.real_course)
                .into(new SimpleTarget<Bitmap>(/*Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL*/) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.course.getLayoutParams();
                        int reW = resource.getWidth();
                        params.width = metrics.widthPixels;
                        if (reW > metrics.widthPixels){

                            double sample = (double)params.width/(double)reW;

                            BigDecimal bigDecimal = new BigDecimal(sample);
                            sample = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            if (sample > 1){
                                sample -=1;
                            }

                            sample = 1-sample;

                            int reH = resource.getHeight();
                            int proportion = (int) (((double)reH)*sample);
                            int realHeight = Math.abs(resource.getHeight() - proportion);
                            params.height = realHeight;
                        }else {

                            double sample = (double)reW/(double)params.width;
                            BigDecimal bigDecimal = new BigDecimal(sample);
                            sample = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            if (sample > 1){
                                sample -=1;
                            }

                            sample = 1-sample;
                            int reH = resource.getHeight();
                            int proportion = (int) (((double)reH)*sample);
                            int realHeight = Math.abs(resource.getHeight() + proportion);
                            params.height = realHeight;
                        }

                        binding.course.setLayoutParams(params);

                        Glide.with(RealNameCourseActivity.this)
                                .load(R.drawable.real_course)
//                                .apply(new GlideRequestOptions())
                                .into(binding.course);
                    }
                });
    }

    public void agree(View view){
        binding.setIsAgree(!binding.getIsAgree());
    }

    public void confirm(View view){
        if (!binding.getIsAgree()){
            showTips("请同意相关要求");
        }else {
            SharedPreferencesUtils.setBoolean(this,PARAM_REALNAME_COURSE,true);
            startActivity(new Intent(this,RealNameIdentityActivity.class));
            finish();
        }
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }
}
