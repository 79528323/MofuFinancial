package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * MPOS行业信息类
 */
public class IndustryInfos {

    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * mercInfoList : [{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"54548","mcc_code":"4722","merc_name":"广州恒浩通信技术有限公司"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"192276","mcc_code":"7333","merc_name":"广州市荔湾区蒙娜之丽莎公主馆婚纱摄影楼"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"55596","mcc_code":"7011","merc_name":"红帆酒店有限责任公司"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"187739","mcc_code":"5812","merc_name":"广州市荔湾区章章小食店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"55732","mcc_code":"7011","merc_name":"绿洲大酒店有限责任公司"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"222045","mcc_code":"5812","merc_name":"广州市番禺区小谷围瞳熠吉吉吉甜品店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"55598","mcc_code":"7011","merc_name":"新港明珠大酒店有限公司"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"222053","mcc_code":"5812","merc_name":"广州市南沙区张亮麻辣烫餐饮店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"193384","mcc_code":"5812","merc_name":"广州市增城正威农家乐食店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"222058","mcc_code":"5812","merc_name":"广州市增城展梦饮品店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"192365","mcc_code":"5812","merc_name":"广州市天河区长兴三世冰香饮品店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"201584","mcc_code":"5812","merc_name":"（广州）日之船章鱼小丸子"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"207984","mcc_code":"5812","merc_name":"广州市天河区员村鸿运川粤菜馆1"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"192881","mcc_code":"5812","merc_name":"广州市黄埔区源慧餐厅"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"207986","mcc_code":"5812","merc_name":"广州市番禺区南村致盛餐厅（蛇口店）"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"209010","mcc_code":"5812","merc_name":"广州市荔湾区大月月鸟寿司店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"210803","mcc_code":"5812","merc_name":"广州市天河区长兴稻原快餐店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"187763","mcc_code":"5812","merc_name":"广州市南沙区南沙申记快餐店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"187764","mcc_code":"5812","merc_name":"广州市增城得记美食店"},{"merc_city_id":"5810","merc_city_name":"广州市","merc_id":"192628","mcc_code":"5812","merc_name":"广州市天河区棠下弘凯餐饮店"}]
     */

    public String result_Msg;
    public int result_Code;

    public double ratio;
    public double fee;

    public List<MercInfoListBean> mercInfoList;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public List<MercInfoListBean> getMercInfoList() {
        return mercInfoList;
    }

    public void setMercInfoList(List<MercInfoListBean> mercInfoList) {
        this.mercInfoList = mercInfoList;
    }

    public static class MercInfoListBean implements Parcelable {
        /**
         * merc_city_id : 5810
         * merc_city_name : 广州市
         * merc_id : 54548
         * mcc_code : 4722
         * merc_name : 广州恒浩通信技术有限公司
         */

        public String merc_city_id;
        public String merc_city_name;
        public String merc_id;
        public String mcc_code;
        public String merc_name;

        protected MercInfoListBean(Parcel in) {
            merc_city_id = in.readString();
            merc_city_name = in.readString();
            merc_id = in.readString();
            mcc_code = in.readString();
            merc_name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(merc_city_id);
            dest.writeString(merc_city_name);
            dest.writeString(merc_id);
            dest.writeString(mcc_code);
            dest.writeString(merc_name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MercInfoListBean> CREATOR = new Creator<MercInfoListBean>() {
            @Override
            public MercInfoListBean createFromParcel(Parcel in) {
                return new MercInfoListBean(in);
            }

            @Override
            public MercInfoListBean[] newArray(int size) {
                return new MercInfoListBean[size];
            }
        };
    }
}
