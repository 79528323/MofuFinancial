package cn.mofufin.morf.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;

import cn.mofufin.morf.ui.base.BaseApplication;
import cn.mofufin.morf.ui.manager.DBManager;
import cn.mofufin.morf.ui.entity.AreaBean;
import cn.mofufin.morf.ui.entity.CityBean;
import cn.mofufin.morf.ui.entity.ProvinceBean;

/**
 * 多联动城市
 */
public class CityPicker {
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();//省
    private ArrayList<ArrayList<CityBean>> options2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items = new ArrayList<>();//区
    private ArrayList<String> Provincestr = new ArrayList<>();//省
    private ArrayList<ArrayList<String>> Citystr = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> Areastr = new ArrayList<>();//区

//    private static Context context;

    private static OptionsPickerView pvOptions;//地址选择器
//    private static CityPicker singlePicker = null;

    public CityPicker(Context context,boolean is2Item) {
        initData(context, is2Item);
    }

//    public CityPicker(Context context,boolean is2Item){
//        //选项选择器
//        pvOptions = new OptionsPickerView(context);
//        // 获取数据库
//        SQLiteDatabase db = DBManager.getdb(BaseApplication.instance);
//        //省
//        Cursor cursor = db.query("province", null, null, null, null, null,
//                null);
//        while (cursor.moveToNext()) {
//            int pro_id = cursor.getInt(0);
//            String pro_code = cursor.getString(1);
//            String pro_name = cursor.getString(2);
//            String pro_name2 = cursor.getString(3);
//            ProvinceBean provinceBean = new ProvinceBean(pro_id, pro_code, pro_name, pro_name2);
//            options1Items.add(provinceBean);//添加一级目录
//            Provincestr.add(cursor.getString(2));
//            //查询二级目录，市区
//            Cursor cursor1 = db.query("city", null, "province_id=?", new String[]{pro_id + ""}, null, null,
//                    null);
//            ArrayList<CityBean> cityBeanList = new ArrayList<>();
//            ArrayList<String> cityStr = new ArrayList<>();
//            //地区集合的集合（注意这里要的是当前省份下面，当前所有城市的地区集合我去）
//            ArrayList<ArrayList<AreaBean>> options3Items_03 = new ArrayList<>();
//            ArrayList<ArrayList<String>> options3Items_str = new ArrayList<>();
//            while (cursor1.moveToNext()) {
//                int cityid = cursor1.getInt(0);
//                int province_id = cursor1.getInt(1);
//                String code = cursor1.getString(2);
//                String name = cursor1.getString(3);
//                String provincecode = cursor1.getString(4);
//                CityBean cityBean = new CityBean(cityid, province_id, code, name, provincecode);
//                //添加二级目录
//                cityBeanList.add(cityBean);
//                cityStr.add(cursor1.getString(3));
//                //查询三级目录
//                Cursor cursor2 = db.query("area", null, "city_id=?", new String[]{cityid + ""}, null, null,
//                        null);
//                ArrayList<AreaBean> areaBeanList = new ArrayList<>();
//                ArrayList<String> areaBeanstr = new ArrayList<>();
//                while (cursor2.moveToNext()) {
//                    int areaid = cursor2.getInt(0);
//                    int city_id = cursor2.getInt(1);
////                    String code0=cursor2.getString(2);
//                    String areaname = cursor2.getString(3);
//                    String citycode = cursor2.getString(4);
//                    AreaBean areaBean = new AreaBean(areaid, city_id, areaname, citycode);
//                    areaBeanList.add(areaBean);
//                    areaBeanstr.add(cursor2.getString(3));
//                }
//                options3Items_str.add(areaBeanstr);//本次查询的存储内容
//                options3Items_03.add(areaBeanList);
//            }
//            options2Items.add(cityBeanList);//增加二级目录数据
//            Citystr.add(cityStr);
////            options3Items.add(options3Items_03);//添加三级目录
////            Areastr.add(options3Items_str);
//        }
//        //设置三级联动效果
//        pvOptions.setPicker(Provincestr, Citystr, null, true);
//        //设置选择的三级单位
////        pvOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("选择城市");
//        //设置是否循环滚动
//        pvOptions.setCyclic(false, false, false);
//        //设置默认选中的三级项目
//        //监听确定选择按钮
//        pvOptions.setSelectOptions(0, 0, 0);
//        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                //返回的分别是三个级别的选中位置
////                String tx = options1Items.get(options1).getPro_name()
////                        + options2Items.get(options1).get(option2).getName()
////                        + options3Items.get(options1).get(option2).get(options3).getName();
////                tvTitle.setText(tx);
//                listener.Select(options1Items.get(options1).getPro_name()
//                        ,options2Items.get(options1).get(option2).getName()
//                        ,null);
//                coderListener.getCode(options1Items.get(options1).getPro_code(),
//                        options2Items.get(options1).get(option2).getCode());
//            }
//        });
//    }

    public void show(){
        pvOptions.show();
    }

    public void setTitle(String title) {
        pvOptions.setTitle(title);
    }

    //    public void onCallback(final OnSelectListener listener){
//        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                listener.Select(options1Items.get(options1).getPro_name()
//                        ,options2Items.get(options1).get(option2).getName()
//                        ,options3Items.get(options1).get(option2).get(options3).getName());
//            }
//        });
//    }

//    public static CityPicker getInstance(Context mContext) {
//        context = mContext;
//
//        if (singlePicker==null){
//            singlePicker = new CityPicker();
//
//        }
//        return singlePicker;
//    }

    public OnSelectListener listener;

    public interface OnSelectListener{
        void Select(String s1,String s2 ,String s3);
    }

    private void initData(Context context, final boolean is2Item) {
        //选项选择器
        pvOptions = new OptionsPickerView(context);
        // 获取数据库
        SQLiteDatabase db = DBManager.getdb(BaseApplication.instance);
        //省
        Cursor cursor = db.query("province", null, null, null, null, null,
                null);
        Cursor cursor1 , cursor2;
        try {
            while (cursor.moveToNext()) {
                int pro_id = cursor.getInt(0);
                String pro_code = cursor.getString(1);
                String pro_name = cursor.getString(2);
                String pro_name2 = cursor.getString(3);
                ProvinceBean provinceBean = new ProvinceBean(pro_id, pro_code, pro_name, pro_name2);
                options1Items.add(provinceBean);//添加一级目录
                Provincestr.add(cursor.getString(2));
                //查询二级目录，市区
                cursor1 = db.query("city", null, "province_id=?", new String[]{pro_id + ""}, null, null,
                        null);
                ArrayList<CityBean> cityBeanList = new ArrayList<>();
                ArrayList<String> cityStr = new ArrayList<>();
                //地区集合的集合（注意这里要的是当前省份下面，当前所有城市的地区集合我去）
                ArrayList<ArrayList<AreaBean>> options3Items_03 = new ArrayList<>();
                ArrayList<ArrayList<String>> options3Items_str = new ArrayList<>();
                while (cursor1.moveToNext()) {
                    int cityid = cursor1.getInt(0);
                    int province_id = cursor1.getInt(1);
                    String code = cursor1.getString(2);
                    String name = cursor1.getString(3);
                    String provincecode = cursor1.getString(4);
                    CityBean cityBean = new CityBean(cityid, province_id, code, name, provincecode);
                    //添加二级目录
                    cityBeanList.add(cityBean);
                    cityStr.add(cursor1.getString(3));
                    //查询三级目录
                    cursor2 = db.query("area", null, "city_id=?", new String[]{cityid + ""}, null, null,
                            null);
                    ArrayList<AreaBean> areaBeanList = new ArrayList<>();
                    ArrayList<String> areaBeanstr = new ArrayList<>();
                    while (cursor2.moveToNext()) {
                        int areaid = cursor2.getInt(0);
                        int city_id = cursor2.getInt(1);
//                    String code0=cursor2.getString(2);
                        String areaname = cursor2.getString(3);
                        String citycode = cursor2.getString(4);
                        AreaBean areaBean = new AreaBean(areaid, city_id, areaname, citycode);
                        areaBeanList.add(areaBean);
                        areaBeanstr.add(cursor2.getString(3));
                    }
                    cursor2.close();

                    options3Items_str.add(areaBeanstr);//本次查询的存储内容
                    options3Items_03.add(areaBeanList);
                }
                cursor1.close();

                options2Items.add(cityBeanList);//增加二级目录数据
                Citystr.add(cityStr);

                if (!is2Item){
                    options3Items.add(options3Items_03);//添加三级目录
                    Areastr.add(options3Items_str);
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        //设置三级联动效果
        pvOptions.setPicker(Provincestr, Citystr, is2Item?null:Areastr, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        //设置是否循环滚动
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPro_name()
//                        + options2Items.get(options1).get(option2).getName()
//                        + options3Items.get(options1).get(option2).get(options3).getName();
//                tvTitle.setText(tx);
                listener.Select(options1Items.get(options1).getPro_name()
                        ,options2Items.get(options1).get(option2).getName()
                        ,is2Item?null:options3Items.get(options1).get(option2).get(options3).getName());
                if (is2Item){
                    coderListener.getCode(options1Items.get(options1).getPro_code(),options2Items.get(options1).get(option2).getCode());
                }
            }
        });
    }

    onCoderListener coderListener;

    public interface onCoderListener{
        void getCode(String Procode,String cityCode);
    }

    public void setCoderListener(onCoderListener coderListener) {
        this.coderListener = coderListener;
    }
}
