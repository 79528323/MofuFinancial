package cn.mofufin.morf.ui.widget;

import android.app.AlertDialog;
import android.content.Context;

import cn.mofufin.morf.R;

public class ConfirmDialog extends AlertDialog {

    public ConfirmDialog(Context context){
        super(context, R.style.confirm_dailog_style);
        setContentView(R.layout.confirm_dialog);

    }

//    protected ConfirmDialog(Context context) {
//        super(context, R.style.confirm_dailog_style);
//    }

}
