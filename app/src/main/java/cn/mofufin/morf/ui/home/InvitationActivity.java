package cn.mofufin.morf.ui.home;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.util.RxEvent;
import cn.mofufin.morf.ui.widget.SharePopWindow;
import cn.mofufin.morf.databinding.ActivityInvitationBinding;
import rx.functions.Action1;

public class InvitationActivity extends BaseActivity {
    private ActivityInvitationBinding binding;
    private SharePopWindow sharePopWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_invitation);
        binding.setHandlers(this);

        rxManager.onRxEvent(RxEvent.WECHAT_REGISTER_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object o) {
                showTips("分享成功");
            }
        });
        sharePopWindow = new SharePopWindow(this);
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }

    public void shares(View view){
        if (!sharePopWindow.isShowing()){
            sharePopWindow.show();
        }
    }
}
