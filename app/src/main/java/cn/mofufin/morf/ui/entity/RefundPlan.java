package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RefundPlan {

    /**
     * result_Msg : 生成计划成功
     * refundPlanDetail : [{"interest":150,"countRefundMoney":566.59,"thisMoney":416.59,"refundDay":"2019-05-18"},{"interest":143.75,"countRefundMoney":560.42,"thisMoney":416.67,"refundDay":"2019-06-17"},{"interest":137.5,"countRefundMoney":554.17,"thisMoney":416.67,"refundDay":"2019-07-17"},{"interest":131.25,"countRefundMoney":547.92,"thisMoney":416.67,"refundDay":"2019-08-16"},{"interest":125,"countRefundMoney":541.67,"thisMoney":416.67,"refundDay":"2019-09-15"},{"interest":118.75,"countRefundMoney":535.42,"thisMoney":416.67,"refundDay":"2019-10-15"},{"interest":112.5,"countRefundMoney":529.17,"thisMoney":416.67,"refundDay":"2019-11-14"},{"interest":106.25,"countRefundMoney":522.92,"thisMoney":416.67,"refundDay":"2019-12-14"},{"interest":100,"countRefundMoney":516.67,"thisMoney":416.67,"refundDay":"2020-01-13"},{"interest":93.75,"countRefundMoney":510.42,"thisMoney":416.67,"refundDay":"2020-02-12"},{"interest":87.5,"countRefundMoney":504.17,"thisMoney":416.67,"refundDay":"2020-03-13"},{"interest":81.25,"countRefundMoney":497.92,"thisMoney":416.67,"refundDay":"2020-04-12"},{"interest":75,"countRefundMoney":491.67,"thisMoney":416.67,"refundDay":"2020-05-12"},{"interest":68.75,"countRefundMoney":485.42,"thisMoney":416.67,"refundDay":"2020-06-11"},{"interest":62.5,"countRefundMoney":479.17,"thisMoney":416.67,"refundDay":"2020-07-11"},{"interest":56.25,"countRefundMoney":472.92,"thisMoney":416.67,"refundDay":"2020-08-10"},{"interest":50,"countRefundMoney":466.67,"thisMoney":416.67,"refundDay":"2020-09-09"},{"interest":43.75,"countRefundMoney":460.42,"thisMoney":416.67,"refundDay":"2020-10-09"},{"interest":37.5,"countRefundMoney":454.17,"thisMoney":416.67,"refundDay":"2020-11-08"},{"interest":31.25,"countRefundMoney":447.92,"thisMoney":416.67,"refundDay":"2020-12-08"},{"interest":25,"countRefundMoney":441.67,"thisMoney":416.67,"refundDay":"2021-01-07"},{"interest":18.75,"countRefundMoney":435.42,"thisMoney":416.67,"refundDay":"2021-02-06"},{"interest":12.5,"countRefundMoney":429.17,"thisMoney":416.67,"refundDay":"2021-03-08"},{"interest":6.25,"countRefundMoney":422.92,"thisMoney":416.67,"refundDay":"2021-04-07"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<RefundPlanDetailBean> refundPlanDetail;

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

    public List<RefundPlanDetailBean> getRefundPlanDetail() {
        return refundPlanDetail;
    }

    public void setRefundPlanDetail(List<RefundPlanDetailBean> refundPlanDetail) {
        this.refundPlanDetail = refundPlanDetail;
    }

    public static class RefundPlanDetailBean implements Parcelable {
        /**
         * interest : 150
         * countRefundMoney : 566.59
         * thisMoney : 416.59
         * refundDay : 2019-05-18
         */

        public double interest;
        public double countRefundMoney;
        public double thisMoney;
        public String refundDay;

        protected RefundPlanDetailBean(Parcel in) {
            interest = in.readDouble();
            countRefundMoney = in.readDouble();
            thisMoney = in.readDouble();
            refundDay = in.readString();
        }

        public static final Creator<RefundPlanDetailBean> CREATOR = new Creator<RefundPlanDetailBean>() {
            @Override
            public RefundPlanDetailBean createFromParcel(Parcel in) {
                return new RefundPlanDetailBean(in);
            }

            @Override
            public RefundPlanDetailBean[] newArray(int size) {
                return new RefundPlanDetailBean[size];
            }
        };

//        public int getInterest() {
//            return interest;
//        }

        public void setInterest(int interest) {
            this.interest = interest;
        }

        public double getCountRefundMoney() {
            return countRefundMoney;
        }

        public void setCountRefundMoney(double countRefundMoney) {
            this.countRefundMoney = countRefundMoney;
        }

        public double getThisMoney() {
            return thisMoney;
        }

        public void setThisMoney(double thisMoney) {
            this.thisMoney = thisMoney;
        }

        public String getRefundDay() {
            return refundDay;
        }

//        public void setRefundDay(String refundDay) {
//            this.refundDay = refundDay;
//        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(interest);
            dest.writeDouble(countRefundMoney);
            dest.writeDouble(thisMoney);
            dest.writeString(refundDay);
        }
    }
}
