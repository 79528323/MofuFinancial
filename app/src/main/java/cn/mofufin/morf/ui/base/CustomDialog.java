package cn.mofufin.morf.ui.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;


import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.R;

/**
 * Created by yhz on 2016/10/26.
 */
public abstract class CustomDialog extends DialogFragment implements BaseUI {
    protected final String TAG = getClass().getSimpleName();

    public static final float DEFAULT_DIM = 0.6f;

    private static final String KEY_LAYOUT_RES = "layout_res";
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_DIM = "dim";
    private static final String KEY_GRAVITY = "gravity";
    private static final String KEY_CANCEL_OUTSIDE = "cancel_outside";

    private FragmentManager fragmentManager;

    private boolean mIsCancelOutside = true;
    private float mDimAmount = DEFAULT_DIM;
    private int mHeight = -1, mWidth;
    private int gravity = Gravity.CENTER;

    @LayoutRes
    private int mLayoutRes;

    public void setFragmentManager(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoadingDialogStyle);
        if (savedInstanceState != null) {
            mLayoutRes = savedInstanceState.getInt(KEY_LAYOUT_RES);
            mWidth = savedInstanceState.getInt(KEY_WIDTH);
            mHeight = savedInstanceState.getInt(KEY_HEIGHT);
            mDimAmount = savedInstanceState.getFloat(KEY_DIM);
            gravity = savedInstanceState.getInt(KEY_GRAVITY);
            mIsCancelOutside = savedInstanceState.getBoolean(KEY_CANCEL_OUTSIDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        mLayoutRes = getLayoutRes();
        View v = inflater.inflate(mLayoutRes, container, false);
        bindView(v);
        return v;
    }

    protected void bindView(View v) {
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        mDimAmount = params.dimAmount = getDimAmount();
        mWidth = params.width = getWidth();
        mHeight = params.height = getHeight();
        gravity = params.gravity = getGravity();

        window.setAttributes(params);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LAYOUT_RES, mLayoutRes);
        outState.putInt(KEY_WIDTH, mWidth);
        outState.putInt(KEY_HEIGHT, mHeight);
        outState.putFloat(KEY_DIM, mDimAmount);
        outState.getInt(KEY_GRAVITY, gravity);
        outState.putBoolean(KEY_CANCEL_OUTSIDE, mIsCancelOutside);

        super.onSaveInstanceState(outState);
    }

    public void show() {
        if (!isAdded())
            show(fragmentManager, getFragmentTag());
    }

    protected int getGravity() {
        return gravity;
    }

    protected void setGravity(int gravity) {
        this.gravity = gravity;
    }

    protected float getDimAmount() {
        return mDimAmount;
    }

    protected int getHeight() {
        return LayoutParams.WRAP_CONTENT;
    }

    protected int getWidth() {
        return LayoutParams.MATCH_PARENT;
    }

    protected boolean getCancelOutside() {
        return mIsCancelOutside;
    }

    protected String getFragmentTag() {
        return "tag";
    }

    @Override
    public void onError(Throwable throwable, boolean show) {
//        if (throwable instanceof SocketTimeoutException || throwable instanceof HttpException) {
//            showTips(R.string.default_http_time_out);
//        } else if (throwable instanceof SSLHandshakeException || throwable instanceof UnknownHostException) {
//            showTips(R.string.default_http_error);
//        } else {
//            if (show) {
//                try {
//                    showTips(throwable.getMessage());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                throwable.printStackTrace();
//            }
//        }
    }

    @Override
    public void showTips(@StringRes int tipRes) {
        Toast.makeText(getActivity(), tipRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTips(String tip) {
        if (TextUtils.isEmpty(tip)) {
            return;
        }
        Logger.i(TAG, tip);
        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hiddenLoading() {

    }

    @Override
    public RxManager getRxManager() {
        return null;
    }
}