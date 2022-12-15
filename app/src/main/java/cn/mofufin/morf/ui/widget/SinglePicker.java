package cn.mofufin.morf.ui.widget;

import android.content.Context;
import android.content.res.AssetManager;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.entity.FallProvinceCity;
import cn.mofufin.morf.ui.entity.MposArea;
import cn.mofufin.morf.ui.entity.RepayMentDay;

/**
 * 单联动选择
 */
public class SinglePicker {
    public static final int TYPE_MERCHANT=1;
    public static final int TYPE_CREDITDAY=2;
    public static final int TYPE_JOBS=3;
    public static final int TYPE_POST=4;
    public static final int TYPE_RELATION=5;
    public static final int TYPE_INCOME=6;
    public static final int TYPE_MPOSAREA=7;
    public static final int TYPE_CARD_REPAYMENT=8;
    public static final int TYPE_REPAYMENT_DAY=9;


    private String[] tempData;
    private final String[] typeArry={"政府机构","国营企业","私营企业","外交企业","个体工商户","事业单位"};
    private final String[] dayArry={"每月1号","每月2号","每月3号","每月4号","每月5号","每月6号","每月7号","每月8号","每月9号","每月10号","每月11号",
            "每月12号","每月13号","每月14号","每月15号","每月16号","每月17号","每月18号","每月19号","每月20号","每月21号","每月22号","每月23号",
            "每月24号","每月25号","每月26号","每月27号","每月28号","每月29号","每月30号","每月31号"};
//    private final String[] typeArry={"政府机构","国营企业","私营企业","外交企业","个体工商户","事业单位"};

    private Context context;
    private static OptionsPickerView pvOptions;//地址选择器

    private ArrayList<String> ManchantList = new ArrayList<>();//省

    private static SinglePicker singlePicker = null;

    private ArrayList<String> JobKeyList= new ArrayList<>();//职业
    private ArrayList<String> JobValueList= new ArrayList<>();//职业
    Map<String,String> Jsonmap;

    private ArrayList<Map<String,String>> postList=new ArrayList<>();//职位

    private int type;

    public SinglePicker(Context context, final int type) {
        this.type = type;
        this.context = context;
        ManchantList = getManchantType(type);

        pvOptions = new OptionsPickerView(context);
        //设置三级联动效果
        pvOptions.setPicker(ManchantList);

        Logger.e("pvOptions="+pvOptions.hashCode()+"   ManchantList="+ManchantList.size());
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
                switch (type){
                    case TYPE_MERCHANT:
                    case TYPE_CREDITDAY:
                    case TYPE_MPOSAREA:
                    case TYPE_REPAYMENT_DAY:
                        listener.singleSelect(ManchantList.get(options1),options1);
                        break;

                    case TYPE_JOBS:
                    case TYPE_POST:
                    case TYPE_RELATION:
                    case TYPE_INCOME:
                        codeSelectListener.singleSelect(ManchantList.get(options1),JobValueList.get(options1));
                        break;

                    case TYPE_CARD_REPAYMENT:
//                        listener.singleSelect(ManchantList.get(options1),options1);
                        break;

//                    case TYPE_REPAYMENT_DAY:
//                        listener.singleSelect(ManchantList.get(options1),options1);
//                        break;
                }
            }
        });
    }

    public void getJobs(Context context){
        Gson gson = new Gson();
        AssetManager manager = context.getAssets();
        String result = null;
        try {
            String fileName = "";
            switch (type){
                case TYPE_JOBS:
                    fileName = "job_json.txt";
                    break;

                case TYPE_POST:
                    fileName = "post_json.txt";
                    break;

                case TYPE_RELATION:
                    fileName = "relationship_json.txt";
                    break;

                case TYPE_INCOME:
                    fileName = "income_json.txt";
                    break;
            }
            InputStream stream = manager.open(fileName,AssetManager.ACCESS_BUFFER);
            byte[] b = new byte[stream.available()];
            stream.read(b);
            result = new String(b);
            Jsonmap =  gson.fromJson(result,new TypeToken<Map<String,String>>(){}.getType());
            if (!Jsonmap.isEmpty()){
                for (Map.Entry<String,String> m:Jsonmap.entrySet()){
                    Logger.e("key="+m.getKey()+"  value="+m.getValue());
                    ManchantList.add(m.getKey());
                    JobValueList.add(m.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private ArrayList<String> getManchantType(int type){
        if (type == TYPE_MERCHANT){
            tempData = typeArry;
            for (int index=0; index<tempData.length; index++){
                ManchantList.add(tempData[index]);
            }
        }else if (type == TYPE_CREDITDAY){
            tempData = dayArry;
            for (int index=0; index<tempData.length; index++){
                ManchantList.add(tempData[index]);
            }
        }else if (type == TYPE_JOBS){
            getJobs(context);
        }else if (type == TYPE_POST){
            getJobs(context);
        }else if (type == TYPE_RELATION){
            getJobs(context);
        }else if (type == TYPE_INCOME){
            getJobs(context);
        }else if (type == TYPE_MPOSAREA){
//            getJobs(context);
        }else if (type == TYPE_CARD_REPAYMENT){
//            getJobs(context);
//            ManchantList.add("");
        }


        return ManchantList;
    }


    /**
     * MPOS消费城市
     * @param list
     */
    public void setAreaList(List<MposArea.AreaListBean> list){
        if (list!=null && !list.isEmpty()){
            Iterator<MposArea.AreaListBean> iterator = list.iterator();
            while (iterator.hasNext()){
                ManchantList.add(iterator.next().areaName);
            }
            pvOptions.setPicker(ManchantList, null, null, false);
        }
    }


    /**
     * 根据账单日筛选回来的还款日范围
     * @param list
     */
    public void setRepayMentDay(List<RepayMentDay.RefundDayBean> list){
        if (list!=null && !list.isEmpty()){
            Iterator<RepayMentDay.RefundDayBean> iterator = list.iterator();
            String months="";
            while (iterator.hasNext()){
                if (iterator.next().getDayType().equals("d")){
                    months = "当月";
                }else
                    months = "次月";

                ManchantList.add(months+iterator.next().getDayParams()+"号");
            }
            pvOptions.setPicker(ManchantList, null, null, false);
        }
    }


    public OnSingleSelectListener listener;
    public onSingleCodeSelectListener codeSelectListener;

    public interface OnSingleSelectListener{
        void singleSelect(String type,int num);
    }

    public interface onSingleCodeSelectListener{
        void singleSelect(String value,String code);
    }



    OnMutipleSelectListener onMutipleSelectListener;
    public void setOnMutipleSelectListener(OnMutipleSelectListener onMutipleSelectListener) {
        this.onMutipleSelectListener = onMutipleSelectListener;
    }

    public interface OnMutipleSelectListener{
        void multiple(String province,String city);
    }




    OnRepayMentDaySelectListener onRepayMentDaySelectListener;
    public interface OnRepayMentDaySelectListener{
        void onSelectDay(RepayMentDay.RefundDayBean bean);
    }

    public void setOnRepayMentDaySelectListener(OnRepayMentDaySelectListener onRepayMentDaySelectListener) {
        this.onRepayMentDaySelectListener = onRepayMentDaySelectListener;
    }
}
