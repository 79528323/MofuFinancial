package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RepayMentProject {

    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * totalRefundMoney : 6851.86
     * totalConsumeMoney : 6900
     * planList : [{"rpOrderCode":"HK20190723170523484465","rpCardBankName":"招商银行","rpCardCode":"6225768725723819","rpConsumeTotalFee":42.14,"rpTotalFee":6,"rpBeginDate":"2019-07-24 00:00:00","rpTotalMoney":6851.86,"rpEndDate":"2019-07-25 00:00:00","rpConsumeTotalMoney":6900,"rpState":0}]
     */

    public String result_Msg;
    public int result_Code;
    public double totalRefundMoney;
    public int totalConsumeMoney;
    public List<PlanListBean> planList;

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

    public double getTotalRefundMoney() {
        return totalRefundMoney;
    }

    public void setTotalRefundMoney(double totalRefundMoney) {
        this.totalRefundMoney = totalRefundMoney;
    }

    public int getTotalConsumeMoney() {
        return totalConsumeMoney;
    }

    public void setTotalConsumeMoney(int totalConsumeMoney) {
        this.totalConsumeMoney = totalConsumeMoney;
    }

    public List<PlanListBean> getPlanList() {
        return planList;
    }

    public void setPlanList(List<PlanListBean> planList) {
        this.planList = planList;
    }

    public static class PlanListBean implements Parcelable {
        /**
         * rpOrderCode : HK20190723170523484465
         * rpCardBankName : 招商银行
         * rpCardCode : 6225768725723819
         * rpConsumeTotalFee : 42.14
         * rpTotalFee : 6
         * rpBeginDate : 2019-07-24 00:00:00
         * rpTotalMoney : 6851.86
         * rpEndDate : 2019-07-25 00:00:00
         * rpConsumeTotalMoney : 6900
         * rpState : 0
         */

        public String rpOrderCode;
        public String rpCardBankName;
        public String rpCardCode;
        public double rpConsumeTotalFee;
        public int rpTotalFee;
        public String rpBeginDate;
        public double rpTotalMoney;
        public String rpEndDate;
        public int rpConsumeTotalMoney;
        public int rpState;

        protected PlanListBean(Parcel in) {
            rpOrderCode = in.readString();
            rpCardBankName = in.readString();
            rpCardCode = in.readString();
            rpConsumeTotalFee = in.readDouble();
            rpTotalFee = in.readInt();
            rpBeginDate = in.readString();
            rpTotalMoney = in.readDouble();
            rpEndDate = in.readString();
            rpConsumeTotalMoney = in.readInt();
            rpState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(rpOrderCode);
            dest.writeString(rpCardBankName);
            dest.writeString(rpCardCode);
            dest.writeDouble(rpConsumeTotalFee);
            dest.writeInt(rpTotalFee);
            dest.writeString(rpBeginDate);
            dest.writeDouble(rpTotalMoney);
            dest.writeString(rpEndDate);
            dest.writeInt(rpConsumeTotalMoney);
            dest.writeInt(rpState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlanListBean> CREATOR = new Creator<PlanListBean>() {
            @Override
            public PlanListBean createFromParcel(Parcel in) {
                return new PlanListBean(in);
            }

            @Override
            public PlanListBean[] newArray(int size) {
                return new PlanListBean[size];
            }
        };

        public String getRpOrderCode() {
            return rpOrderCode;
        }

        public void setRpOrderCode(String rpOrderCode) {
            this.rpOrderCode = rpOrderCode;
        }

        public String getRpCardBankName() {
            return rpCardBankName;
        }

        public void setRpCardBankName(String rpCardBankName) {
            this.rpCardBankName = rpCardBankName;
        }

        public String getRpCardCode() {
            return rpCardCode;
        }

        public void setRpCardCode(String rpCardCode) {
            this.rpCardCode = rpCardCode;
        }

        public double getRpConsumeTotalFee() {
            return rpConsumeTotalFee;
        }

        public void setRpConsumeTotalFee(double rpConsumeTotalFee) {
            this.rpConsumeTotalFee = rpConsumeTotalFee;
        }

        public int getRpTotalFee() {
            return rpTotalFee;
        }

        public void setRpTotalFee(int rpTotalFee) {
            this.rpTotalFee = rpTotalFee;
        }

        public String getRpBeginDate() {
            return rpBeginDate;
        }

        public void setRpBeginDate(String rpBeginDate) {
            this.rpBeginDate = rpBeginDate;
        }

        public double getRpTotalMoney() {
            return rpTotalMoney;
        }

        public void setRpTotalMoney(double rpTotalMoney) {
            this.rpTotalMoney = rpTotalMoney;
        }

        public String getRpEndDate() {
            return rpEndDate;
        }

        public void setRpEndDate(String rpEndDate) {
            this.rpEndDate = rpEndDate;
        }

        public int getRpConsumeTotalMoney() {
            return rpConsumeTotalMoney;
        }

        public void setRpConsumeTotalMoney(int rpConsumeTotalMoney) {
            this.rpConsumeTotalMoney = rpConsumeTotalMoney;
        }

        public int getRpState() {
            return rpState;
        }

        public void setRpState(int rpState) {
            this.rpState = rpState;
        }
    }
}
