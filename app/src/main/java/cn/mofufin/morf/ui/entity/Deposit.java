package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Deposit implements Parcelable{


    /**
     * bool : true
     * data : {"reason":"Mpos plan query success!","plan":{"csHistoryTotalMoney":0,"csCurrentRetrunMoney":0,"csClearingDate":"01","csLastCondition":20000,"csFirsetCondition":10000,"csFirstMonth":28,"csClearingTime":"03:00","csCentreCondition":20000,"csHistoryPastMoeny":0,"csName":"保证金计划","csPlanMoney":128,"csCurrentTotalMoney":0,"csCentreMonth":40,"csLastMonth":60}}
     * stateCode : 10000
     * message : Request Success!
     */

    public boolean bool;
    public DataBean data;
    public int stateCode;
    public String message;

    protected Deposit(Parcel in) {
        bool = in.readByte() != 0;
        stateCode = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (bool ? 1 : 0));
        dest.writeInt(stateCode);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Deposit> CREATOR = new Creator<Deposit>() {
        @Override
        public Deposit createFromParcel(Parcel in) {
            return new Deposit(in);
        }

        @Override
        public Deposit[] newArray(int size) {
            return new Deposit[size];
        }
    };

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Parcelable{
        /**
         * reason : Mpos plan query success!
         * plan : {"csHistoryTotalMoney":0,"csCurrentRetrunMoney":0,"csClearingDate":"01","csLastCondition":20000,"csFirsetCondition":10000,"csFirstMonth":28,"csClearingTime":"03:00","csCentreCondition":20000,"csHistoryPastMoeny":0,"csName":"保证金计划","csPlanMoney":128,"csCurrentTotalMoney":0,"csCentreMonth":40,"csLastMonth":60}
         */

        public String reason;
        public PlanBean plan;

        protected DataBean(Parcel in) {
            reason = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(reason);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public PlanBean getPlan() {
            return plan;
        }

        public void setPlan(PlanBean plan) {
            this.plan = plan;
        }

        public static class PlanBean implements Parcelable{
            /**
             * csHistoryTotalMoney : 0
             * csCurrentRetrunMoney : 0
             * csClearingDate : 01
             * csLastCondition : 20000
             * csFirsetCondition : 10000
             * csFirstMonth : 28
             * csClearingTime : 03:00
             * csCentreCondition : 20000
             * csHistoryPastMoeny : 0
             * csName : 保证金计划
             * csPlanMoney : 128
             * csCurrentTotalMoney : 0
             * csCentreMonth : 40
             * csLastMonth : 60
             */

            public int csHistoryTotalMoney;
            public int csCurrentRetrunMoney;
            public String csClearingDate;
            public int csLastCondition;
            public int csFirsetCondition;
            public int csFirstMonth;
            public String csClearingTime;
            public int csCentreCondition;
            public int csHistoryPastMoeny;
            public String csName;
            public int csPlanMoney;
            public int csCurrentTotalMoney;
            public int csCentreMonth;
            public int csLastMonth;

            protected PlanBean(Parcel in) {
                csHistoryTotalMoney = in.readInt();
                csCurrentRetrunMoney = in.readInt();
                csClearingDate = in.readString();
                csLastCondition = in.readInt();
                csFirsetCondition = in.readInt();
                csFirstMonth = in.readInt();
                csClearingTime = in.readString();
                csCentreCondition = in.readInt();
                csHistoryPastMoeny = in.readInt();
                csName = in.readString();
                csPlanMoney = in.readInt();
                csCurrentTotalMoney = in.readInt();
                csCentreMonth = in.readInt();
                csLastMonth = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(csHistoryTotalMoney);
                dest.writeInt(csCurrentRetrunMoney);
                dest.writeString(csClearingDate);
                dest.writeInt(csLastCondition);
                dest.writeInt(csFirsetCondition);
                dest.writeInt(csFirstMonth);
                dest.writeString(csClearingTime);
                dest.writeInt(csCentreCondition);
                dest.writeInt(csHistoryPastMoeny);
                dest.writeString(csName);
                dest.writeInt(csPlanMoney);
                dest.writeInt(csCurrentTotalMoney);
                dest.writeInt(csCentreMonth);
                dest.writeInt(csLastMonth);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<PlanBean> CREATOR = new Creator<PlanBean>() {
                @Override
                public PlanBean createFromParcel(Parcel in) {
                    return new PlanBean(in);
                }

                @Override
                public PlanBean[] newArray(int size) {
                    return new PlanBean[size];
                }
            };

            public int getCsHistoryTotalMoney() {
                return csHistoryTotalMoney;
            }

            public void setCsHistoryTotalMoney(int csHistoryTotalMoney) {
                this.csHistoryTotalMoney = csHistoryTotalMoney;
            }

            public int getCsCurrentRetrunMoney() {
                return csCurrentRetrunMoney;
            }

            public void setCsCurrentRetrunMoney(int csCurrentRetrunMoney) {
                this.csCurrentRetrunMoney = csCurrentRetrunMoney;
            }

            public String getCsClearingDate() {
                return csClearingDate;
            }

            public void setCsClearingDate(String csClearingDate) {
                this.csClearingDate = csClearingDate;
            }

            public int getCsLastCondition() {
                return csLastCondition;
            }

            public void setCsLastCondition(int csLastCondition) {
                this.csLastCondition = csLastCondition;
            }

            public int getCsFirsetCondition() {
                return csFirsetCondition;
            }

            public void setCsFirsetCondition(int csFirsetCondition) {
                this.csFirsetCondition = csFirsetCondition;
            }

            public int getCsFirstMonth() {
                return csFirstMonth;
            }

            public void setCsFirstMonth(int csFirstMonth) {
                this.csFirstMonth = csFirstMonth;
            }

            public String getCsClearingTime() {
                return csClearingTime;
            }

            public void setCsClearingTime(String csClearingTime) {
                this.csClearingTime = csClearingTime;
            }

            public int getCsCentreCondition() {
                return csCentreCondition;
            }

            public void setCsCentreCondition(int csCentreCondition) {
                this.csCentreCondition = csCentreCondition;
            }

            public int getCsHistoryPastMoeny() {
                return csHistoryPastMoeny;
            }

            public void setCsHistoryPastMoeny(int csHistoryPastMoeny) {
                this.csHistoryPastMoeny = csHistoryPastMoeny;
            }

            public String getCsName() {
                return csName;
            }

            public void setCsName(String csName) {
                this.csName = csName;
            }

            public int getCsPlanMoney() {
                return csPlanMoney;
            }

            public void setCsPlanMoney(int csPlanMoney) {
                this.csPlanMoney = csPlanMoney;
            }

            public int getCsCurrentTotalMoney() {
                return csCurrentTotalMoney;
            }

            public void setCsCurrentTotalMoney(int csCurrentTotalMoney) {
                this.csCurrentTotalMoney = csCurrentTotalMoney;
            }

            public int getCsCentreMonth() {
                return csCentreMonth;
            }

            public void setCsCentreMonth(int csCentreMonth) {
                this.csCentreMonth = csCentreMonth;
            }

            public int getCsLastMonth() {
                return csLastMonth;
            }

            public void setCsLastMonth(int csLastMonth) {
                this.csLastMonth = csLastMonth;
            }
        }
    }
}
