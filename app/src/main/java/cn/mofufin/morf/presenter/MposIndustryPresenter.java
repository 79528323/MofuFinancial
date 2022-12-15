package cn.mofufin.morf.presenter;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.hope.paysdk.NetInterface;
import com.hope.paysdk.PaySdkEnvionment;
import com.hope.paysdk.framework.EnumClass;
import com.hope.paysdk.framework.beans.Industry;
import com.hope.paysdk.framework.beans.IndustrySet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.event.RxManager;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.contract.MposIndustryContract;

public class MposIndustryPresenter implements MposIndustryContract.Presenter {

    private MposIndustryContract.View view;
    private RxManager rxManager;
    private String mode;

    public MposIndustryPresenter(MposIndustryContract.View view,String mode) {
        this.view = view;
        this.rxManager = view.getRxManager();
        this.mode = mode;
    }

    @Override
    public void refresh(String... params) {

        Logger.d("params=="+params[0]+"   "+params[1]);
        new GetIndustryListTask().execute(params[0],params[1]);
    }

    /**
     * 获取行业
     */
    private class GetIndustryListTask extends AsyncTask<String, Void, IndustrySet> {

        private EnumClass.TYPE_ARRIVE type_arrive;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showLoading();
        }

        @Override
        public IndustrySet doInBackground(String... arg0) {
//            type_arrive = EnumClass.TYPE_ARRIVE.TYPE_ARRIVE_D0;
//            if ("0".equals(arg0[1])) {
//                type_arrive = EnumClass.TYPE_ARRIVE.TYPE_ARRIVE_D0;
//            } else if ("1".equals(arg0[1])) {
//                type_arrive = EnumClass.TYPE_ARRIVE.TYPE_ARRIVE_T1;
//            }
            NetInterface netInterface = PaySdkEnvionment.getNetInterfaceController();
//            if (netInterface == null) {
//                IndustrySet industrySet = new IndustrySet();
//                industrySet.setSuccess(false);
//                industrySet.setCode(-3);
//                industrySet.setMsg("PaySdk未初始化.");
//                return industrySet;
//            } else {
            IndustrySet industrySet = netInterface.getIndustryList(arg0[0], EnumClass.TYPE_ARRIVE.TYPE_ARRIVE_D0,judgeOpeMode(mode));

//            List<Industry> list = new ArrayList<Industry>();

//            try {
//                InputStream stream = manager.open("about_mine.txt", AssetManager.ACCESS_BUFFER);
//                byte[] b = new byte[0];
//                b = new byte[stream.available()];
//                stream.read(b);
////                String result = new String(b);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



            return industrySet;
//            }
        }

        @Override
        public void onPostExecute(IndustrySet ret) {
            if (ret != null && ret.code != -1) {
                if (ret.code == 0) {
                    List<Industry> list = ret.getIndustryList();
                    Logger.d("IndustryList=="+list.size());
                    view.refresh(list);

                } else if (ret.code == 918) {
                    view.showTips(ret.getMsg());
                }
            } else {
                view.showTips("获取行业异常");
            }

            view.hiddenLoading();
        }
    }

    private EnumClass.TYPE_OPEMODE judgeOpeMode(String opeMode) {
        if ("nfc".equals(opeMode)) {
            return EnumClass.TYPE_OPEMODE.TYPE_NFC;
        } else {
            return EnumClass.TYPE_OPEMODE.TYPE_NORMAL;
        }
    }

}
