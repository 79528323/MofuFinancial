package cn.mofufin.morf.ui.widget;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cc.ruis.lib.event.RxBus;
import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.util.RxEvent;

public class TransPassWordDialog extends DialogFragment {
    private EditText pw_edit;
    private TextView ok , cancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return LayoutInflater.from(getContext()).inflate(R.layout.unbind_card_dialog,container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pw_edit = view.findViewById(R.id.password);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pw_edit.getText().toString();
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(),"请输入6位密码",Toast.LENGTH_SHORT).show();
                }else {
                    RxBus.getInstance().post(RxEvent.UNBIND_CARD_TRAN_PASSWORD,password);
                    dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
