package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Random;

public class Overseans implements Parcelable{

    /**
     * result_Msg : 查询成功
     * overList : [{"channelMegMoney":5,"channelKind":0,"channelQuotaBegin":"100","merDrillCostringD0":0.06,"channelNumber":"125041","merGoldCostringT1":0.052,"channelPayEnd":"23:00:00","merCommonCostringT1":0.054,"isSupportT1":1,"merGoldCostringD0":0.062,"merDrillCostringT1":0.05,"channelPayBegin":"01:00:00","channelName":"国际银联HKD","channelQuotaEnd":"50000","merCommonCostringD0":0.064},{"channelMegMoney":1,"channelKind":0,"channelQuotaBegin":"100","merDrillCostringD0":0.06,"channelNumber":"325825","merGoldCostringT1":0.052,"channelPayEnd":"23:00:00","merCommonCostringT1":0.054,"isSupportT1":0,"merGoldCostringD0":0.062,"merDrillCostringT1":0.05,"channelPayBegin":"07:00:00","channelName":"国际银联USD","channelQuotaEnd":"50000","merCommonCostringD0":0.064}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<OverListBean> overList;

    protected Overseans(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
    }

    public static final Creator<Overseans> CREATOR = new Creator<Overseans>() {
        @Override
        public Overseans createFromParcel(Parcel in) {
            return new Overseans(in);
        }

        @Override
        public Overseans[] newArray(int size) {
            return new Overseans[size];
        }
    };

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

    public List<OverListBean> getOverList() {
        return overList;
    }

    public void setOverList(List<OverListBean> overList) {
        this.overList = overList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
    }

    public static class OverListBean implements Parcelable{
        /**
         * channelMegMoney : 5
         * channelKind : 0
         * channelQuotaBegin : 100
         * merDrillCostringD0 : 0.06
         * channelNumber : 125041
         * merGoldCostringT1 : 0.052
         * channelPayEnd : 23:00:00
         * merCommonCostringT1 : 0.054
         * isSupportT1 : 1
         * merGoldCostringD0 : 0.062
         * merDrillCostringT1 : 0.05
         * channelPayBegin : 01:00:00
         * channelName : 国际银联HKD
         * channelQuotaEnd : 50000
         * merCommonCostringD0 : 0.064
         */

        public int channelMegMoney;
        public int channelKind;
        public String channelQuotaBegin;
        public double merDrillCostringD0;
        public String channelNumber;
        public double merGoldCostringT1;
        public String channelPayEnd;
        public double merCommonCostringT1;
        public int isSupportT1;
        public double merGoldCostringD0;
        public double merDrillCostringT1;
        public String channelPayBegin;
        public String channelName;
        public String channelQuotaEnd;
        public double merCommonCostringD0;
        public int merDrillSettlePeriod;
        public int merGoldSettlePeriod;
        public int merCommonSettlePeriod;
        public int type;

        public OverListBean() {
        }

        protected OverListBean(Parcel in) {
            channelMegMoney = in.readInt();
            channelKind = in.readInt();
            channelQuotaBegin = in.readString();
            merDrillCostringD0 = in.readDouble();
            channelNumber = in.readString();
            merGoldCostringT1 = in.readDouble();
            channelPayEnd = in.readString();
            merCommonCostringT1 = in.readDouble();
            isSupportT1 = in.readInt();
            merGoldCostringD0 = in.readDouble();
            merDrillCostringT1 = in.readDouble();
            channelPayBegin = in.readString();
            channelName = in.readString();
            channelQuotaEnd = in.readString();
            merCommonCostringD0 = in.readDouble();
            merDrillSettlePeriod = in.readInt();
            merGoldSettlePeriod = in.readInt();
            merCommonSettlePeriod = in.readInt();
            type = in.readInt();
        }

        public static final Creator<OverListBean> CREATOR = new Creator<OverListBean>() {
            @Override
            public OverListBean createFromParcel(Parcel in) {
                return new OverListBean(in);
            }

            @Override
            public OverListBean[] newArray(int size) {
                return new OverListBean[size];
            }
        };

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof OverListBean){
                OverListBean bean = (OverListBean)obj;
                return (type == bean.type);
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode() + new Random().nextInt();
        }

        public int getChannelMegMoney() {
            return channelMegMoney;
        }

        public void setChannelMegMoney(int channelMegMoney) {
            this.channelMegMoney = channelMegMoney;
        }

        public int getChannelKind() {
            return channelKind;
        }

        public void setChannelKind(int channelKind) {
            this.channelKind = channelKind;
        }

        public String getChannelQuotaBegin() {
            return channelQuotaBegin;
        }

        public void setChannelQuotaBegin(String channelQuotaBegin) {
            this.channelQuotaBegin = channelQuotaBegin;
        }

        public double getMerDrillCostringD0() {
            return merDrillCostringD0;
        }

        public void setMerDrillCostringD0(double merDrillCostringD0) {
            this.merDrillCostringD0 = merDrillCostringD0;
        }

        public String getChannelNumber() {
            return channelNumber;
        }

        public void setChannelNumber(String channelNumber) {
            this.channelNumber = channelNumber;
        }

        public double getMerGoldCostringT1() {
            return merGoldCostringT1;
        }

        public void setMerGoldCostringT1(double merGoldCostringT1) {
            this.merGoldCostringT1 = merGoldCostringT1;
        }

        public String getChannelPayEnd() {
            return channelPayEnd;
        }

        public void setChannelPayEnd(String channelPayEnd) {
            this.channelPayEnd = channelPayEnd;
        }

        public double getMerCommonCostringT1() {
            return merCommonCostringT1;
        }

        public void setMerCommonCostringT1(double merCommonCostringT1) {
            this.merCommonCostringT1 = merCommonCostringT1;
        }

        public int getIsSupportT1() {
            return isSupportT1;
        }

        public void setIsSupportT1(int isSupportT1) {
            this.isSupportT1 = isSupportT1;
        }

        public double getMerGoldCostringD0() {
            return merGoldCostringD0;
        }

        public void setMerGoldCostringD0(double merGoldCostringD0) {
            this.merGoldCostringD0 = merGoldCostringD0;
        }

        public double getMerDrillCostringT1() {
            return merDrillCostringT1;
        }

        public void setMerDrillCostringT1(double merDrillCostringT1) {
            this.merDrillCostringT1 = merDrillCostringT1;
        }

        public String getChannelPayBegin() {
            return channelPayBegin;
        }

        public void setChannelPayBegin(String channelPayBegin) {
            this.channelPayBegin = channelPayBegin;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getChannelQuotaEnd() {
            return channelQuotaEnd;
        }

        public void setChannelQuotaEnd(String channelQuotaEnd) {
            this.channelQuotaEnd = channelQuotaEnd;
        }

        public double getMerCommonCostringD0() {
            return merCommonCostringD0;
        }

        public void setMerCommonCostringD0(double merCommonCostringD0) {
            this.merCommonCostringD0 = merCommonCostringD0;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(channelMegMoney);
            dest.writeInt(channelKind);
            dest.writeString(channelQuotaBegin);
            dest.writeDouble(merDrillCostringD0);
            dest.writeString(channelNumber);
            dest.writeDouble(merGoldCostringT1);
            dest.writeString(channelPayEnd);
            dest.writeDouble(merCommonCostringT1);
            dest.writeInt(isSupportT1);
            dest.writeDouble(merGoldCostringD0);
            dest.writeDouble(merDrillCostringT1);
            dest.writeString(channelPayBegin);
            dest.writeString(channelName);
            dest.writeString(channelQuotaEnd);
            dest.writeDouble(merCommonCostringD0);
            dest.writeInt(merDrillSettlePeriod);
            dest.writeInt(merGoldSettlePeriod);
            dest.writeInt(merCommonSettlePeriod);
            dest.writeInt(type);

        }


    }
}
