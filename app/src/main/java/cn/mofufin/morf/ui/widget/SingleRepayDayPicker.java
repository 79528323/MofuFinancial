package cn.mofufin.morf.ui.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.entity.MposArea;
import cn.mofufin.morf.ui.entity.RepayMentDay;

/**
 * 单联动选择 还款日单独专用
 */
public class SingleRepayDayPicker {

    private Context context;
    private static OptionsPickerView pvOptions;//地址选择器
    private ArrayList<String> ManchantList = new ArrayList<>();

    public SingleRepayDayPicker(Context context,ArrayList<RepayMentDay.RefundDayBean> list) {
        this.context = context;
        pvOptions = new OptionsPickerView(context);
        ManchantList.clear();
        Iterator<RepayMentDay.RefundDayBean> iterator = list.iterator();
        String months="";
        while (iterator.hasNext()){
            RepayMentDay.RefundDayBean bean = iterator.next();
            if (TextUtils.equals(bean.getDayType(),"d")){
                months = "当月";
            }else
                months = "次月";

            ManchantList.add(months + bean.getDayParams()+"号");
        }

        //设置三级联动效果
        pvOptions.setPicker(ManchantList);

        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("请选择商户种类");
        //设置是否循环滚动
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                listener.singleSelect(ManchantList.get(options1),options1);
            }
        });
    }




    public void setTitle(String title){
        pvOptions.setTitle(title);
    }

    public void dimiss(){
        pvOptions.dismiss();
    }

    public void show(){
        pvOptions.show();
    }

    /**
     * 根据账单日筛选回来的还款日范围
     * @param list
     */
    public void setRepayMentDay(List<RepayMentDay.RefundDayBean> list){
        if (list!=null && !list.isEmpty()){

            ManchantList.clear();
            pvOptions.setPicker(ManchantList);
        }
    }


    public OnSingleSelectListener listener;

    public interface OnSingleSelectListener{
        void singleSelect(String type, int num);
    }

}
