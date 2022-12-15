package cn.mofufin.morf.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.base.BaseResponse;
import cn.mofufin.morf.ui.mine.MofuPlayActivity;
import cn.mofufin.morf.ui.db.MerchanInfoDB;
import cn.mofufin.morf.ui.entity.Member;
import cn.mofufin.morf.ui.entity.Tree;
import cn.mofufin.morf.ui.entity.User;
import cn.mofufin.morf.ui.services.SubMissionImpAPI;
import cn.mofufin.morf.ui.services.UtilsImpAPI;
import cn.mofufin.morf.ui.util.HttpParam;
import cn.mofufin.morf.ui.util.RequestTransformer;
import cn.mofufin.morf.databinding.ActivityAnimationTreeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AnimationTreeActivity extends BaseActivity {
    private ActivityAnimationTreeBinding binding;
    private int transOffset = 20;
    private static final int DURATION = 30;
    private int totalRound = 14;
    private int curRound = 0;
    private int time = 2000;

    private int mHeight = 0;
    private boolean isAnimStart = false;

    private AnimationDrawable frameAnim;
    private Animation animation = null;

    private int[] framesArry ={R.drawable.frame_1,R.drawable.frame_2,R.drawable.frame_3,R.drawable.frame_4,R.drawable.frame_5
            ,R.drawable.frame_6,
            R.drawable.frame_7,R.drawable.frame_8,R.drawable.frame_9,R.drawable.frame_10,R.drawable.frame_11};

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (frameAnim!=null)
                frameAnim.stop();

            hitTree();
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_animation_tree);
        binding.setHandlers(this);
        setStatusBar();
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initView(){
        updateMerchantInfo();//刷新次数
        Glide.with(this).load(R.drawable.tree_anim_bg).into(binding.treeBgd);//设置底图
        getMember();
        frameAnim = new AnimationDrawable();
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            for (int index=0; index < 11; index++){
                drawable = getResources().getDrawable(framesArry[index]);
                frameAnim.addFrame(drawable,DURATION);
            }
        }else {
            for (int index=0; index<11; index++){
                drawable = getDrawable(framesArry[index]);
                frameAnim.addFrame(drawable,DURATION);
            }
        }

        frameAnim.setOneShot(false);
        binding.tree.setBackground(frameAnim);
        binding.tree.setOnClickListener(treeClickListener);
    }


    //刷新商户信息
    public void updateMerchantInfo(){
        final User.DataBean.MerchantInfoBean bean = MerchanInfoDB.queryInfo();
        Subscription subscription = SubMissionImpAPI.refreshMerchantInfo(
                HttpParam.OFFICE_CODE,HttpParam.REFRESH_MERCHANT_APPKEY,
                bean.merchantPhone,bean.merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new RequestTransformer(this))
                .subscribe(new Action1<BaseResponse<User.DataBean>>() {
                    @Override
                    public void call(BaseResponse<User.DataBean> dataBeanBaseResponse) {
                        User.DataBean.MerchantInfoBean bean = dataBeanBaseResponse.data.getMerchantInfo();
                        MerchanInfoDB.updateMerchanInfo(bean);

                        final User.DataBean.MerchantInfoBean beans = MerchanInfoDB.queryInfo();
                        binding.setCount(bean.treeNumber);
//                        binding.setCount(binding.getCount() + 10);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    //摇树
    public void hitTree(){
        Subscription subscription = UtilsImpAPI.hit(HttpParam.HIT_TREE_APPKEY,MerchanInfoDB.queryInfo().merchantCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Tree>() {
                    @Override
                    public void call(Tree tree) {
                        if (tree.bool){
                            updateMerchantInfo();
                            binding.ball.setText("+"+tree.data.getCoin());
                            animTree();
                        }else
                            isAnimStart =false;

                        showTips(tree.data.getReason());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        isAnimStart =false;
                        showTips(throwable.getMessage());
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
    }

    //查询会员日
    public void getMember(){
        Subscription subscription = UtilsImpAPI.treeMemberQuery(HttpParam.QUERY_MEMBER_APPKEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Member>() {
                    @Override
                    public void call(Member member) {
                        binding.setDay(member.data.getTsMember());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable,true);
                    }
                });
        rxManager.add(subscription);
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

    public View.OnClickListener treeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!frameAnim.isRunning() && !isAnimStart){
                isAnimStart = true;
                frameAnim.start();
                handler.sendEmptyMessageDelayed(0,
                        (frameAnim.getNumberOfFrames() * DURATION )* 2);
            }
        }
    };

    public void animTree(){
        animation = AnimationUtils.loadAnimation(AnimationTreeActivity.this,R.anim.tree_coin_anims);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.ball.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.ball.setVisibility(View.GONE);
                isAnimStart = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.ball.startAnimation(animation);
    }

    @Override
    public void submit(View view) {
        super.submit(view);
        startActivity(new Intent(this,MofuPlayActivity.class));
    }
}
