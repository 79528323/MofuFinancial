package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Channel {


    /**
     * result_Msg : 查询成功
     * channelList : [{"tcName":"银联免密秒到A","tcEndDate":"22:00:00","tcType":452335,"tcHighestMoney":20000,"tcrUserRatioT1":0.0063,"tcStartDate":"09:00:00","tcLowestMoney":100,"tcrUserRatioD0":0.0063},{"tcName":"银联免密秒到B","tcEndDate":"22:00:00","tcType":129781,"tcHighestMoney":20000,"tcrUserRatioT1":0.0063,"tcStartDate":"09:00:00","tcLowestMoney":100,"tcrUserRatioD0":0.0063},{"tcName":"银联免密秒到C","tcEndDate":"22:00:00","tcType":326805,"tcHighestMoney":20000,"tcrUserRatioT1":0.0063,"tcStartDate":"09:00:00","tcLowestMoney":100,"tcrUserRatioD0":0.0063}]
     * result_Code : 0
     */

    private String result_Msg;
    private int result_Code;
    private List<ChannelListBean> channelList;
    private List<BankListBean> bankList;

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

    public List<ChannelListBean> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelListBean> channelList) {
        this.channelList = channelList;
    }

    public List<BankListBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankListBean> bankList) {
        this.bankList = bankList;
    }

    public static class BankListBean implements Parcelable{
        public String cbName;
        public String cbNumber;

        protected BankListBean(Parcel in) {
            cbName = in.readString();
            cbNumber=in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(cbName);
            dest.writeString(cbNumber);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<BankListBean> CREATOR = new Creator<BankListBean>() {
            @Override
            public BankListBean createFromParcel(Parcel in) {
                return new BankListBean(in);
            }

            @Override
            public BankListBean[] newArray(int size) {
                return new BankListBean[size];
            }
        };
    }

    public static class ChannelListBean implements Parcelable{
        /**
         * tcName : 银联免密秒到A
         * tcEndDate : 22:00:00
         * tcType : 452335
         * tcHighestMoney : 20000
         * tcrUserRatioT1 : 0.0063
         * tcStartDate : 09:00:00
         * tcLowestMoney : 100
         * tcrUserRatioD0 : 0.0063
         */

        public String tcName;
        public String tcEndDate;
        public int tcType;
        public int tcHighestMoney;
        public double tcrUserRatioT1;
        public String tcStartDate;
        public int tcLowestMoney;
        public double tcrUserRatioD0;
        public List<BankListBean> bankList = new ArrayList<BankListBean>();

        public ChannelListBean() {
            bankList = new ArrayList<BankListBean>();
        }

        protected ChannelListBean(Parcel in) {
            tcName = in.readString();
            tcEndDate = in.readString();
            tcType = in.readInt();
            tcHighestMoney = in.readInt();
            tcrUserRatioT1 = in.readDouble();
            tcStartDate = in.readString();
            tcLowestMoney = in.readInt();
            tcrUserRatioD0 = in.readDouble();
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

        public List<BankListBean> getBankList() {
            return bankList;
        }

        public void setBankList(List<BankListBean> bankList) {
            this.bankList = bankList;
        }

        public String getTcName() {
            return tcName;
        }

        public void setTcName(String tcName) {
            this.tcName = tcName;
        }

        public String getTcEndDate() {
            return tcEndDate;
        }

        public void setTcEndDate(String tcEndDate) {
            this.tcEndDate = tcEndDate;
        }

        public int getTcType() {
            return tcType;
        }

        public void setTcType(int tcType) {
            this.tcType = tcType;
        }

        public int getTcHighestMoney() {
            return tcHighestMoney;
        }

        public void setTcHighestMoney(int tcHighestMoney) {
            this.tcHighestMoney = tcHighestMoney;
        }

        public double getTcrUserRatioT1() {
            return tcrUserRatioT1;
        }

        public void setTcrUserRatioT1(double tcrUserRatioT1) {
            this.tcrUserRatioT1 = tcrUserRatioT1;
        }

        public String getTcStartDate() {
            return tcStartDate;
        }

        public void setTcStartDate(String tcStartDate) {
            this.tcStartDate = tcStartDate;
        }

        public int getTcLowestMoney() {
            return tcLowestMoney;
        }

        public void setTcLowestMoney(int tcLowestMoney) {
            this.tcLowestMoney = tcLowestMoney;
        }

        public double getTcrUserRatioD0() {
            return tcrUserRatioD0;
        }

        public void setTcrUserRatioD0(double tcrUserRatioD0) {
            this.tcrUserRatioD0 = tcrUserRatioD0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(tcName);
            dest.writeString(tcEndDate);
            dest.writeInt(tcType);
            dest.writeInt(tcHighestMoney);
            dest.writeDouble(tcrUserRatioT1);
            dest.writeString(tcStartDate);
            dest.writeInt(tcLowestMoney);
            dest.writeDouble(tcrUserRatioD0);
        }
    }
}
