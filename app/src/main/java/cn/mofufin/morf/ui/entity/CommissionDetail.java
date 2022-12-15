package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CommissionDetail implements Parcelable{

    /**
     * result_Msg : 查询成功
     * detailList : [{"incomeMoney":0.75,"rebateOrderCode":"20181031152328846756","incomeType":1,"rebateOrderMoney":1864,"rebatePersonPhone":"15734566234","rebateOrderFee":11.75,"rebatePersonName":"杨莹","rebateCreateDate":"2018-10-31 15:23:46"},{"incomeMoney":2.39,"rebateOrderCode":"20181031080638720736","incomeType":1,"rebateOrderMoney":5968,"rebatePersonPhone":"15734566234","rebateOrderFee":37.6,"rebatePersonName":"杨莹","rebateCreateDate":"2018-10-31 08:07:00"},{"incomeMoney":0.04,"rebateOrderCode":"20181030150058857518","incomeType":1,"rebateOrderMoney":103,"rebatePersonPhone":"15970906220","rebateOrderFee":0.65,"rebatePersonName":"胡英英","rebateCreateDate":"2018-10-30 15:01:13"},{"incomeMoney":0.08,"rebateOrderCode":"20181030140939653671","incomeType":1,"rebateOrderMoney":187,"rebatePersonPhone":"15970906220","rebateOrderFee":1.18,"rebatePersonName":"胡英英","rebateCreateDate":"2018-10-30 14:09:52"},{"incomeMoney":0.14,"rebateOrderCode":"20181030121643341873","incomeType":1,"rebateOrderMoney":326,"rebatePersonPhone":"15970906220","rebateOrderFee":2.06,"rebatePersonName":"胡英英","rebateCreateDate":"2018-10-30 12:17:01"},{"incomeMoney":6,"rebateOrderCode":"20181030065231506314","incomeType":1,"rebateOrderMoney":15001,"rebatePersonPhone":"13552774041","rebateOrderFee":94.51,"rebatePersonName":"刘亭","rebateCreateDate":"2018-10-30 06:53:01"}]
     * result_Code : 0
     * monthRebate : 9.4
     */

    public String result_Msg;
    public int result_Code;
    public double monthRebate;
    public List<DetailListBean> detailList;



    protected CommissionDetail(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
        monthRebate = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
        dest.writeDouble(monthRebate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommissionDetail> CREATOR = new Creator<CommissionDetail>() {
        @Override
        public CommissionDetail createFromParcel(Parcel in) {
            return new CommissionDetail(in);
        }

        @Override
        public CommissionDetail[] newArray(int size) {
            return new CommissionDetail[size];
        }
    };

    public static class DetailListBean implements Parcelable{
        /**
         * incomeMoney : 0.75
         * rebateOrderCode : 20181031152328846756
         * incomeType : 1
         * rebateOrderMoney : 1864
         * rebatePersonPhone : 15734566234
         * rebateOrderFee : 11.75
         * rebatePersonName : 杨莹
         * rebateCreateDate : 2018-10-31 15:23:46
         */

        public double incomeMoney;
        public String rebateOrderCode;
        public int incomeType;
        public double rebateOrderMoney;
        public String rebatePersonPhone;
        public double rebateOrderFee;
        public String rebatePersonName;
        public String rebateCreateDate;

        protected DetailListBean(Parcel in) {
            incomeMoney = in.readDouble();
            rebateOrderCode = in.readString();
            incomeType = in.readInt();
            rebateOrderMoney = in.readDouble();
            rebatePersonPhone = in.readString();
            rebateOrderFee = in.readDouble();
            rebatePersonName = in.readString();
            rebateCreateDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(incomeMoney);
            dest.writeString(rebateOrderCode);
            dest.writeInt(incomeType);
            dest.writeDouble(rebateOrderMoney);
            dest.writeString(rebatePersonPhone);
            dest.writeDouble(rebateOrderFee);
            dest.writeString(rebatePersonName);
            dest.writeString(rebateCreateDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DetailListBean> CREATOR = new Creator<DetailListBean>() {
            @Override
            public DetailListBean createFromParcel(Parcel in) {
                return new DetailListBean(in);
            }

            @Override
            public DetailListBean[] newArray(int size) {
                return new DetailListBean[size];
            }
        };
    }
}
