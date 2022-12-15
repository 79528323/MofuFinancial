package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RecordDetails {


    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * planDetailsList : [{"detailsOrderCode":"JDM20190426103921446032","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-05-26 00:00:00"},{"detailsOrderCode":"JDM20190426103921481296","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-06-25 00:00:00"},{"detailsOrderCode":"JDM20190426103921695751","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-07-25 00:00:00"},{"detailsOrderCode":"JDM20190426103921981619","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-08-24 00:00:00"},{"detailsOrderCode":"JDM20190426103921551020","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-09-23 00:00:00"},{"detailsOrderCode":"JDM20190426103921766622","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-10-23 00:00:00"},{"detailsOrderCode":"JDM20190426103921231109","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-11-22 00:00:00"},{"detailsOrderCode":"JDM20190426103921521371","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2019-12-22 00:00:00"},{"detailsOrderCode":"JDM20190426103921601146","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2020-01-21 00:00:00"},{"detailsOrderCode":"JDM20190426103921820363","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2020-02-20 00:00:00"},{"detailsOrderCode":"JDM20190426103921498193","planState":0,"refundThisMoney":0,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":8250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2020-03-21 00:00:00"},{"detailsOrderCode":"JDM20190426103921197172","planState":0,"refundThisMoney":550000,"overdueInterest":0,"loansDayRate":5.0E-4,"refundTotalMoney":558250,"refundInterest":8250,"overdueDayRate":8.0E-4,"detailsExplain":"还款明细","affrimRefundDate":null,"refundDate":"2020-04-20 00:00:00"}]
     */

    public String result_Msg;
    public int result_Code;
    public List<PlanDetailsListBean> planDetailsList;


    public static class PlanDetailsListBean implements Parcelable {
        /**
         * detailsOrderCode : JDM20190426103921446032
         * planState : 0
         * refundThisMoney : 0
         * overdueInterest : 0
         * loansDayRate : 5.0E-4
         * refundTotalMoney : 8250
         * refundInterest : 8250
         * overdueDayRate : 8.0E-4
         * detailsExplain : 还款明细
         * affrimRefundDate : null
         * refundDate : 2019-05-26 00:00:00
         */

        public String detailsOrderCode;
        public int planState;
        public double refundThisMoney;
        public double overdueInterest;
        public double loansDayRate;
        public double refundTotalMoney;
        public double refundInterest;
        public double overdueDayRate;
        public String detailsExplain;
        public int overdueDay;
        public String affrimRefundDate;
        public String refundDate;

        protected PlanDetailsListBean(Parcel in) {
            detailsOrderCode = in.readString();
            planState = in.readInt();
            refundThisMoney = in.readDouble();
            overdueInterest = in.readDouble();
            loansDayRate = in.readDouble();
            refundTotalMoney = in.readDouble();
            refundInterest = in.readDouble();
            overdueDayRate = in.readDouble();
            detailsExplain = in.readString();
            overdueDay = in.readInt();
            affrimRefundDate = in.readString();
            refundDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(detailsOrderCode);
            dest.writeInt(planState);
            dest.writeDouble(refundThisMoney);
            dest.writeDouble(overdueInterest);
            dest.writeDouble(loansDayRate);
            dest.writeDouble(refundTotalMoney);
            dest.writeDouble(refundInterest);
            dest.writeDouble(overdueDayRate);
            dest.writeString(detailsExplain);
            dest.writeInt(overdueDay);
            dest.writeString(affrimRefundDate);
            dest.writeString(refundDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlanDetailsListBean> CREATOR = new Creator<PlanDetailsListBean>() {
            @Override
            public PlanDetailsListBean createFromParcel(Parcel in) {
                return new PlanDetailsListBean(in);
            }

            @Override
            public PlanDetailsListBean[] newArray(int size) {
                return new PlanDetailsListBean[size];
            }
        };
    }
}
