package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cc.ruis.lib.util.Logger;

public class ProjectResult implements Parcelable{


    /**
     * result_Msg : 创建计划成功
     * result_Code : 0
     * planDataList : {"total":{"recommendMoney":1574.92,"rpOrderCode":"HK20190719100041126999","rpConsumeTotalFee":21.92,"rcNumber":637982,"rpTotalFee":3,"rpBeginDate":"2019-07-23","rpEndDate":"2019-07-23","rpTotalMoney":2975.08,"rpConsumeTotalMoney":3000},"details":[{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.77,"deExecuteGroup":"70901695","deExecuteDate":"10:26:00","deOrderCode":"HKM20190719100041312275","deExecuteDay":"2019-07-23","dePayMoney":516},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.65,"deExecuteGroup":"70901695","deExecuteDate":"11:38:00","deOrderCode":"HKM20190719100041667856","deExecuteDay":"2019-07-23","dePayMoney":500},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.9,"deExecuteGroup":"70901695","deExecuteDate":"12:53:00","deOrderCode":"HKM20190719100041594617","deExecuteDay":"2019-07-23","dePayMoney":534},{"deType":1,"rpOrderCode":"HK20190719100041126999","dePayFee":1.5,"deExecuteGroup":"70901695","deExecuteDate":"14:15:00","deOrderCode":"HKM20190719100041534544","deExecuteDay":"2019-07-23","dePayMoney":1537.18},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.66,"deExecuteGroup":"13664706","deExecuteDate":"15:16:00","deOrderCode":"HKM20190719100041971624","deExecuteDay":"2019-07-23","dePayMoney":501},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.5,"deExecuteGroup":"13664706","deExecuteDate":"16:11:00","deOrderCode":"HKM20190719100041778492","deExecuteDay":"2019-07-23","dePayMoney":479},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.44,"deExecuteGroup":"13664706","deExecuteDate":"17:05:00","deOrderCode":"HKM20190719100041393930","deExecuteDay":"2019-07-23","dePayMoney":470},{"deType":1,"rpOrderCode":"HK20190719100041126999","dePayFee":1.5,"deExecuteGroup":"13664706","deExecuteDate":"18:35:00","deOrderCode":"HKM20190719100041188950","deExecuteDay":"2019-07-23","dePayMoney":1437.9}]}
     */

    public String result_Msg;
    public int result_Code;
    public PlanDataListBean planDataList;


    protected ProjectResult(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
        planDataList = in.readParcelable(PlanDataListBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
        dest.writeParcelable(planDataList, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProjectResult> CREATOR = new Creator<ProjectResult>() {
        @Override
        public ProjectResult createFromParcel(Parcel in) {
            return new ProjectResult(in);
        }

        @Override
        public ProjectResult[] newArray(int size) {
            return new ProjectResult[size];
        }
    };










    public static class PlanDataListBean implements Parcelable{
        /**
         * total : {"recommendMoney":1574.92,"rpOrderCode":"HK20190719100041126999","rpConsumeTotalFee":21.92,"rcNumber":637982,"rpTotalFee":3,"rpBeginDate":"2019-07-23","rpEndDate":"2019-07-23","rpTotalMoney":2975.08,"rpConsumeTotalMoney":3000}
         * details : [{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.77,"deExecuteGroup":"70901695","deExecuteDate":"10:26:00","deOrderCode":"HKM20190719100041312275","deExecuteDay":"2019-07-23","dePayMoney":516},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.65,"deExecuteGroup":"70901695","deExecuteDate":"11:38:00","deOrderCode":"HKM20190719100041667856","deExecuteDay":"2019-07-23","dePayMoney":500},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.9,"deExecuteGroup":"70901695","deExecuteDate":"12:53:00","deOrderCode":"HKM20190719100041594617","deExecuteDay":"2019-07-23","dePayMoney":534},{"deType":1,"rpOrderCode":"HK20190719100041126999","dePayFee":1.5,"deExecuteGroup":"70901695","deExecuteDate":"14:15:00","deOrderCode":"HKM20190719100041534544","deExecuteDay":"2019-07-23","dePayMoney":1537.18},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.66,"deExecuteGroup":"13664706","deExecuteDate":"15:16:00","deOrderCode":"HKM20190719100041971624","deExecuteDay":"2019-07-23","dePayMoney":501},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.5,"deExecuteGroup":"13664706","deExecuteDate":"16:11:00","deOrderCode":"HKM20190719100041778492","deExecuteDay":"2019-07-23","dePayMoney":479},{"deType":0,"rpOrderCode":"HK20190719100041126999","dePayFee":3.44,"deExecuteGroup":"13664706","deExecuteDate":"17:05:00","deOrderCode":"HKM20190719100041393930","deExecuteDay":"2019-07-23","dePayMoney":470},{"deType":1,"rpOrderCode":"HK20190719100041126999","dePayFee":1.5,"deExecuteGroup":"13664706","deExecuteDate":"18:35:00","deOrderCode":"HKM20190719100041188950","deExecuteDay":"2019-07-23","dePayMoney":1437.9}]
         */

        public TotalBean total;
        public List<DetailsBean> details;

        public List<List<DetailsBean>> detailsGrounp = new ArrayList<>();

        protected PlanDataListBean(Parcel in) {
            total = in.readParcelable(TotalBean.class.getClassLoader());
            details = in.createTypedArrayList(DetailsBean.CREATOR);
        }

        public static final Creator<PlanDataListBean> CREATOR = new Creator<PlanDataListBean>() {
            @Override
            public PlanDataListBean createFromParcel(Parcel in) {
                return new PlanDataListBean(in);
            }

            @Override
            public PlanDataListBean[] newArray(int size) {
                return new PlanDataListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(total, flags);
            dest.writeTypedList(details);
        }


        public static class TotalBean implements Parcelable {
            /**
             * recommendMoney : 1574.92
             * rpOrderCode : HK20190719100041126999
             * rpConsumeTotalFee : 21.92
             * rcNumber : 637982
             * rpTotalFee : 3
             * rpBeginDate : 2019-07-23
             * rpEndDate : 2019-07-23
             * rpTotalMoney : 2975.08
             * rpConsumeTotalMoney : 3000
             */

            public double recommendMoney;//推荐卡至少预留金额
            public String rpOrderCode;//计划订单号（执行计划时需用到）
            public double rpConsumeTotalFee;//消费总手续费
            public int rcNumber;//通道编号
            public double rpTotalFee;//还款总手续费
            public String rpBeginDate;//还款开始时间
            public String rpEndDate;//还款结束时间
            public double rpTotalMoney;//还款总金额
            public double rpConsumeTotalMoney;//消费总金额


            protected TotalBean(Parcel in) {
                recommendMoney = in.readDouble();
                rpOrderCode = in.readString();
                rpConsumeTotalFee = in.readDouble();
                rcNumber = in.readInt();
                rpTotalFee = in.readDouble();
                rpBeginDate = in.readString();
                rpEndDate = in.readString();
                rpTotalMoney = in.readDouble();
                rpConsumeTotalMoney = in.readDouble();
            }

            public static final Creator<TotalBean> CREATOR = new Creator<TotalBean>() {
                @Override
                public TotalBean createFromParcel(Parcel in) {
                    return new TotalBean(in);
                }

                @Override
                public TotalBean[] newArray(int size) {
                    return new TotalBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(recommendMoney);
                dest.writeString(rpOrderCode);
                dest.writeDouble(rpConsumeTotalFee);
                dest.writeInt(rcNumber);
                dest.writeDouble(rpTotalFee);
                dest.writeString(rpBeginDate);
                dest.writeString(rpEndDate);
                dest.writeDouble(rpTotalMoney);
                dest.writeDouble(rpConsumeTotalMoney);
            }
        }

        public static class DetailsBean implements Parcelable{
            /**
             * deType : 0
             * rpOrderCode : HK20190719100041126999
             * dePayFee : 3.77
             * deExecuteGroup : 70901695
             * deExecuteDate : 10:26:00
             * deOrderCode : HKM20190719100041312275
             * deExecuteDay : 2019-07-23
             * dePayMoney : 516
             */

            public int deType;//交易类型：0消费 1还款
            public String rpOrderCode;//所属计划订单号
            public double dePayFee;//交易手续费
            public String deExecuteGroup;//执行组(每个还款和对应的消费都为一个组)
            public String deExecuteDate;//执行时分
            public String deOrderCode;//明细订单号
            public String deExecuteDay;//执行日期
            public double dePayMoney;//交易金额

            protected DetailsBean(Parcel in) {
                deType = in.readInt();
                rpOrderCode = in.readString();
                dePayFee = in.readDouble();
                deExecuteGroup = in.readString();
                deExecuteDate = in.readString();
                deOrderCode = in.readString();
                deExecuteDay = in.readString();
                dePayMoney = in.readDouble();
            }

            public static final Creator<DetailsBean> CREATOR = new Creator<DetailsBean>() {
                @Override
                public DetailsBean createFromParcel(Parcel in) {
                    return new DetailsBean(in);
                }

                @Override
                public DetailsBean[] newArray(int size) {
                    return new DetailsBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(deType);
                dest.writeString(rpOrderCode);
                dest.writeDouble(dePayFee);
                dest.writeString(deExecuteGroup);
                dest.writeString(deExecuteDate);
                dest.writeString(deOrderCode);
                dest.writeString(deExecuteDay);
                dest.writeDouble(dePayMoney);
            }
        }

    }

}
