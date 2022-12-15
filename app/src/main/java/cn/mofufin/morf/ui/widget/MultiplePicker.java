package cn.mofufin.morf.ui.widget;

import android.content.Context;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import cn.mofufin.morf.ui.entity.FallProvinceCity;

/**
 * 还款计划  选择省市控件
 */
public class MultiplePicker {

    private OptionsPickerView pvOptions;
    private ArrayList<String> proCityListBeanList = new ArrayList<>();
    private ArrayList<List<String>>  cityListBeanList = new ArrayList<>();
    private FallProvinceCity fallProvinceCity;

    public MultiplePicker(Context context) {

        //预加载空数据解决实例创建问题
        proCityListBeanList.add("");
        cityListBeanList.add(new ArrayList<String>());

        pvOptions = new OptionsPickerView(context);
        //设置三级联动效果
        pvOptions.setPicker(proCityListBeanList,cityListBeanList,null,false);

        //设置选择的三级单位
//        pvOptions.setLabels("省", "市");
//        pvOptions.setTitle("选择城市");
        //设置是否循环滚动
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = proCityListBeanList.get(options1) +"  "
                        + cityListBeanList.get(options1).get(option2);
//                pvOptions.setTitle(tx);
                FallProvinceCity.ProCityListBean proCityListBean = fallProvinceCity.getProCityList().get(options1);
                FallProvinceCity.ProCityListBean.CityListBean cityListBean = fallProvinceCity.getProCityList()
                        .get(options1).getCityList().get(option2);

                onMultipleSelectProCityListener.multipleSelect(tx,options1,option2,proCityListBean,cityListBean);
//                listener.Select(options1Items.get(options1).getPro_name()
//                        ,options2Items.get(options1).get(option2).getName()
//                        ,is2Item?null:options3Items.get(options1).get(option2).get(options3).getName());
//                if (is2Item){
//                    coderListener.getCode(options1Items.get(options1).getPro_code(),options2Items.get(options1).get(option2).getCode());
//                }
            }
        });
    }



    public void show(){
        pvOptions.show();
    }


    public void dimiss(){
        pvOptions.dismiss();
    }

    /**
     * 还款消费省市
     * @param provinceCity
     */
    public void setRepayMentChannelFallProvinceCity(FallProvinceCity fallProvinceCity){
        this.fallProvinceCity =fallProvinceCity;
        if (!fallProvinceCity.getProCityList().isEmpty()){
            proCityListBeanList.clear();
            cityListBeanList.clear();

            for (int index=0; index < fallProvinceCity.getProCityList().size(); index++){
                FallProvinceCity.ProCityListBean bean = fallProvinceCity.getProCityList().get(index);
                proCityListBeanList.add(bean.getProvinceName());

                List<FallProvinceCity.ProCityListBean.CityListBean> cityListBeans = bean.getCityList();

                List<String> cityStringList = new ArrayList<>();
                for (int i=0; i < cityListBeans.size(); i++){
                    cityStringList.add(cityListBeans.get(i).getCityName());
                }
                cityListBeanList.add(cityStringList);
            }

        }
        pvOptions.setPicker(proCityListBeanList,cityListBeanList,null,true);
    }


    public interface OnMultipleSelectProCityListener{
        /**
         *
         * @param proCityName 省市String
         * @param options1 省的Position
         * @param options2 市的Position
         * @param proCityListBean 省结构
         * @param cityListBean 市结构
         */
        void multipleSelect(String proCityName,int options1,int options2,FallProvinceCity.ProCityListBean proCityListBean,
                            FallProvinceCity.ProCityListBean.CityListBean cityListBean);
    }

    public OnMultipleSelectProCityListener onMultipleSelectProCityListener;

    public void setOnMultipleSelectProCityListener(OnMultipleSelectProCityListener onMultipleSelectProCityListener) {
        this.onMultipleSelectProCityListener = onMultipleSelectProCityListener;
    }
}
