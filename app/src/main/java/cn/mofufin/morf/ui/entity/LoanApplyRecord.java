package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LoanApplyRecord  {

    /**
     * result_Msg : 查询成功
     * applyLoansList : [{"realityMoney":null,"loansPeriod":24,"applyMoney":100000,"applyMsg":"申请中","applyState":2,"loansInterestWay":0,"applyRecordCode":"20190425170831662549","applyDate":"2019-04-25 17:08:31","loansRate":5.0E-4,"productName":"摩富贷A"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<ApplyLoansListBean> applyLoansList;

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

    public List<ApplyLoansListBean> getApplyLoansList() {
        return applyLoansList;
    }

    public void setApplyLoansList(List<ApplyLoansListBean> applyLoansList) {
        this.applyLoansList = applyLoansList;
    }

    public static class ApplyLoansListBean implements Parcelable{
        /**
         * realityMoney : null
         * loansPeriod : 24
         * applyMoney : 100000
         * applyMsg : 申请中
         * applyState : 2
         * loansInterestWay : 0
         * applyRecordCode : 20190425170831662549
         * applyDate : 2019-04-25 17:08:31
         * loansRate : 5.0E-4
         * productName : 摩富贷A
         */

        public double realityMoney;
        public int loansPeriod;
        public int applyMoney;
        public String applyMsg;
        public int applyState;
        public int loansInterestWay;
        public String applyRecordCode;
        public String applyDate;
        public double loansRate;
        public String productName;

        protected ApplyLoansListBean(Parcel in) {
            realityMoney = in.readDouble();
            loansPeriod = in.readInt();
            applyMoney = in.readInt();
            applyMsg = in.readString();
            applyState = in.readInt();
            loansInterestWay = in.readInt();
            applyRecordCode = in.readString();
            applyDate = in.readString();
            loansRate = in.readDouble();
            productName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(realityMoney);
            dest.writeInt(loansPeriod);
            dest.writeInt(applyMoney);
            dest.writeString(applyMsg);
            dest.writeInt(applyState);
            dest.writeInt(loansInterestWay);
            dest.writeString(applyRecordCode);
            dest.writeString(applyDate);
            dest.writeDouble(loansRate);
            dest.writeString(productName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ApplyLoansListBean> CREATOR = new Creator<ApplyLoansListBean>() {
            @Override
            public ApplyLoansListBean createFromParcel(Parcel in) {
                return new ApplyLoansListBean(in);
            }

            @Override
            public ApplyLoansListBean[] newArray(int size) {
                return new ApplyLoansListBean[size];
            }
        };

        public Object getRealityMoney() {
            return realityMoney;
        }

//        public void setRealityMoney(Object realityMoney) {
//            this.realityMoney = realityMoney;
//        }

        public int getLoansPeriod() {
            return loansPeriod;
        }

        public void setLoansPeriod(int loansPeriod) {
            this.loansPeriod = loansPeriod;
        }

        public int getApplyMoney() {
            return applyMoney;
        }

        public void setApplyMoney(int applyMoney) {
            this.applyMoney = applyMoney;
        }

        public String getApplyMsg() {
            return applyMsg;
        }

        public void setApplyMsg(String applyMsg) {
            this.applyMsg = applyMsg;
        }

        public int getApplyState() {
            return applyState;
        }

        public void setApplyState(int applyState) {
            this.applyState = applyState;
        }

        public int getLoansInterestWay() {
            return loansInterestWay;
        }

        public void setLoansInterestWay(int loansInterestWay) {
            this.loansInterestWay = loansInterestWay;
        }

        public String getApplyRecordCode() {
            return applyRecordCode;
        }

        public void setApplyRecordCode(String applyRecordCode) {
            this.applyRecordCode = applyRecordCode;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public double getLoansRate() {
            return loansRate;
        }

        public void setLoansRate(double loansRate) {
            this.loansRate = loansRate;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
