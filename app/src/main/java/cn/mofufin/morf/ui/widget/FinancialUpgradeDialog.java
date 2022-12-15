package cn.mofufin.morf.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.FinancialActivity;
import cn.mofufin.morf.ui.WebActivity;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.LayoutDialogFinancialUpgradeBinding;

public class FinancialUpgradeDialog extends Dialog {
    private LayoutDialogFinancialUpgradeBinding binding;

    private Context mContext;

    public FinancialUpgradeDialog(Context context) {
        super(context, R.style.FinancialDialogStyle);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_financial_upgrade,null);
        binding = DataBindingUtil.bind(view);
        binding.setHandlers(this);

        setContentView(binding.getRoot());

        setCanceledOnTouchOutside(false);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
//        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mDialogWindow.setAttributes(lp);

    }
    public void cancel(View view){
        dismiss();
    }

    //新版理财
    public void comeIntoNewFinancial(View view){
        mContext.startActivity(new Intent(mContext, FinancialActivity.class));
        dismiss();
    }

    //旧版理财
    public void comeIntoOldFinancial(View view){
        Intent intent = new Intent(mContext,WebActivity.class);
        intent.putExtra("HTML", Common.MOFU_LICAI);
        intent.putExtra(IntentParam.TITLE,mContext.getString(R.string.home_shortservice2));
        mContext.startActivity(intent);
        dismiss();
    }

    public void showDialog(){
        show();
    }


}
