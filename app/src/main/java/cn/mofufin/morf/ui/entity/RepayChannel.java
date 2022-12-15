package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RepayChannel {


    /**
     * result_Msg : 查询成功
     * channelList : [{"rcSingleDayCardQuota":4500,"rcGoldRate":0.0076,"rcNumber":637982,"rcSinglePenQuota":1000,"rcDaiPayFee":1,"rcType":0,"rcCommonRate":0.0079,"rcName":"小额还款通道","rcDrillRate":0.0073}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<ChannelListBean> channelList;


    public static class ChannelListBean implements Parcelable {
        /**
         * rcSingleDayCardQuota : 4500
         * rcGoldRate : 0.0076
         * rcNumber : 637982
         * rcSinglePenQuota : 1000
         * rcDaiPayFee : 1
         * rcType : 0
         * rcCommonRate : 0.0079
         * rcName : 小额还款通道
         * rcDrillRate : 0.0073
         */

        public int rcSingleDayCardQuota;
        public double rcGoldRate;
        public int rcNumber;
        public int rcSinglePenQuota;
        public double rcDaiPayFee;
        public int rcType;
        public double rcCommonRate;
        public String rcName;
        public double rcDrillRate;

        protected ChannelListBean(Parcel in) {
            rcSingleDayCardQuota = in.readInt();
            rcGoldRate = in.readDouble();
            rcNumber = in.readInt();
            rcSinglePenQuota = in.readInt();
            rcDaiPayFee = in.readDouble();
            rcType = in.readInt();
            rcCommonRate = in.readDouble();
            rcName = in.readString();
            rcDrillRate = in.readDouble();
        }

        public static final Creator<ChannelListBean> CREATOR = new Creator<ChannelListBean>() {
            @Override
            public ChannelListBean createFromParcel(Parcel in) {
                return new ChannelListBean(in);
            }

            @Override
            public ChannelListBean[] newArray(int size) {
                return new ChannelListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeInt(rcSingleDayCardQuota);
            dest.writeDouble(rcGoldRate);
            dest.writeInt(rcNumber);
            dest.writeInt(rcSinglePenQuota);
            dest.writeDouble(rcDaiPayFee);
            dest.writeInt(rcType);
            dest.writeDouble(rcCommonRate);
            dest.writeString(rcName);
            dest.writeDouble(rcDrillRate);
        }
    }
}
