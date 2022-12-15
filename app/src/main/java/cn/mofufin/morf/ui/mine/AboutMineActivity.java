package cn.mofufin.morf.ui.mine;

import android.content.res.AssetManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import java.io.InputStream;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.databinding.ActivityAboutMineBinding;

public class AboutMineActivity extends BaseActivity {
    private AssetManager manager;
    private ActivityAboutMineBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about_mine);
        binding.setHandlers(this);

        manager = getAssets();
        binding.setTextput(readFile());
    }

    public String readFile() {
        String result = null;
        try {
            InputStream stream = manager.open("mabout_mine.txt",AssetManager.ACCESS_BUFFER);
            byte[] b = new byte[stream.available()];
            stream.read(b);
            result = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected boolean enableSliding() {
        return true;
    }
}
