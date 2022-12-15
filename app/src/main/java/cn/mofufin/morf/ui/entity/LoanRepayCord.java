package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LoanRepayCord {

    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * planList : [{"loansMoney":550000,"surplusMoney":550000,"refundPlanState":0,"refundPlanCode":"JD20190426103921864882","productName":"摩富贷C","refundDateBegin":"2019-04-26 00:00:00","refundDateEnd":"2020-04-20 00:00:00"}]
     */

    public String result_Msg;
    public int result_Code;
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

    public List<PlanListBean> getPlanList() {
        return planList;
    }

    public void setPlanList(List<PlanListBean> planList) {
        this.planList = planList;
    }

    public static class PlanListBean implements Parcelable {
        /**
         * loansMoney : 550000
         * surplusMoney : 550000
         * refundPlanState : 0
         * refundPlanCode : JD20190426103921864882
         * productName : 摩富贷C
         * refundDateBegin : 2019-04-26 00:00:00
         * refundDateEnd : 2020-04-20 00:00:00
         */

        public int loansMoney;
        public int surplusMoney;
        public int refundPlanState;
        public String refundPlanCode;
        public String productName;
        public String refundDateBegin;
        public String refundDateEnd;

        protected PlanListBean(Parcel in) {
            loansMoney = in.readInt();
            surplusMoney = in.readInt();
            refundPlanState = in.readInt();
            refundPlanCode = in.readString();
            productName = in.readString();
            refundDateBegin = in.readString();
            refundDateEnd = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(loansMoney);
            dest.writeInt(surplusMoney);
            dest.writeInt(refundPlanState);
            dest.writeString(refundPlanCode);
            dest.writeString(productName);
            dest.writeString(refundDateBegin);
            dest.writeString(refundDateEnd);
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

        public int getLoansMoney() {
            return loansMoney;
        }

        public void setLoansMoney(int loansMoney) {
            this.loansMoney = loansMoney;
        }

        public int getSurplusMoney() {
            return surplusMoney;
        }

        public void setSurplusMoney(int surplusMoney) {
            this.surplusMoney = surplusMoney;
        }

        public int getRefundPlanState() {
            return refundPlanState;
        }

        public void setRefundPlanState(int refundPlanState) {
            this.refundPlanState = refundPlanState;
        }

        public String getRefundPlanCode() {
            return refundPlanCode;
        }

        public void setRefundPlanCode(String refundPlanCode) {
            this.refundPlanCode = refundPlanCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getRefundDateBegin() {
            return refundDateBegin;
        }

        public void setRefundDateBegin(String refundDateBegin) {
            this.refundDateBegin = refundDateBegin;
        }

        public String getRefundDateEnd() {
            return refundDateEnd;
        }

        public void setRefundDateEnd(String refundDateEnd) {
            this.refundDateEnd = refundDateEnd;
        }
    }
}
