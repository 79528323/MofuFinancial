package cn.mofufin.morf.ui.dailog;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import cn.mofufin.morf.R;

/**
 * Created by yhz on 2016/10/28.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.layout_loading);
        setCanceledOnTouchOutside(false);
//        setCancelable(false);
        getWindow().setDimAmount(0f);//背影全透明关键代码
//        Window window=getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.dimAmount = CustomDialog.DEFAULT_DIM;
    }

    /*@Override
    protected boolean getCancelOutside() {
        return false;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_loading;
    }*/
}
