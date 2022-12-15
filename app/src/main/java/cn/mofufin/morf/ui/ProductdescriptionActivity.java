package cn.mofufin.morf.ui;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import cn.mofufin.morf.R;
import cn.mofufin.morf.ui.base.BaseActivity;
import cn.mofufin.morf.ui.entity.ProductDetails;
import cn.mofufin.morf.ui.util.IntentParam;
import cn.mofufin.morf.databinding.ActivityProductDescriptionBinding;

public class ProductdescriptionActivity extends BaseActivity {
    private ActivityProductDescriptionBinding binding;
    private ProductDetails.ProductDetailsBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_description);
        setStatusBar(Color.TRANSPARENT);
        binding.setHandlers(this);
        initView();
    }

    public void initView(){
        bean = getIntent().getParcelableExtra(IntentParam.BEAN);
        binding.setDetails(bean);
    }

    @Override
    protected boolean enableSliding() {
        return true;
    }

}
