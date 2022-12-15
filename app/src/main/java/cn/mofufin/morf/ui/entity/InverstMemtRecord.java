package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class InverstMemtRecord implements Parcelable{


    /**
     * result_Msg : 查询成功
     * thenTotalMoney : 100001.12
     * recordList : [{"foBuyMoney":10000.87,"fdAnnualized":0.058,"foOrderState":0,"fdNumber":130678,"foOrderMsg":"来源余额,资金明细订单号：MX20190124165445975177","foIsRebate":0,"fdUserBuyNumber":100000,"fdProductDate":15,"foProductBuyDate":"2019-01-24 16:54:45","foIsRemit":1,"foProductRansomDate":"2019-02-08 16:54:45","foTicketNumber":"","foExpireMoney":10023.12,"foIsUseTicket":1,"foEarnings":22.25,"foOrderCode":"LC20190124165445556730","fdName":"摩富801501"},{"foBuyMoney":10000.25,"fdAnnualized":0.058,"foOrderState":0,"fdNumber":130678,"foOrderMsg":"来源余额,资金明细订单号：MX20190124165347613639","foIsRebate":0,"fdUserBuyNumber":100000,"fdProductDate":15,"foProductBuyDate":"2019-01-24 16:53:47","foIsRemit":1,"foProductRansomDate":"2019-02-08 16:53:47","foTicketNumber":"","foExpireMoney":10022.5,"foIsUseTicket":1,"foEarnings":22.25,"foOrderCode":"LC20190124165347575823","fdName":"摩富801501"},{"foBuyMoney":10000,"fdAnnualized":0.158,"foOrderState":0,"fdNumber":111332,"foOrderMsg":"来源余额,资金明细订单号：MX20190124093908368453","foIsRebate":0,"fdUserBuyNumber":100000,"fdProductDate":60,"foProductBuyDate":"2019-01-24 09:39:08","foIsRemit":1,"foProductRansomDate":"2019-03-25 09:39:08","foTicketNumber":"","foExpireMoney":10259.73,"foIsUseTicket":1,"foEarnings":259.73,"foOrderCode":"LC20190124093908435327","fdName":"摩富0903"},{"foBuyMoney":10000,"fdAnnualized":0.158,"foOrderState":0,"fdNumber":668186,"foOrderMsg":"来源余额,资金明细订单号：MX20190123165353488077","foIsRebate":0,"fdUserBuyNumber":1,"fdProductDate":31,"foProductBuyDate":"2019-01-23 16:53:53","foIsRemit":1,"foProductRansomDate":"2019-02-23 16:53:53","foTicketNumber":"","foExpireMoney":10129.86,"foIsUseTicket":1,"foEarnings":129.86,"foOrderCode":"LC20190123165353289758","fdName":"新手专享"},{"foBuyMoney":30000,"fdAnnualized":0.058,"foOrderState":0,"fdNumber":130678,"foOrderMsg":"来源余额,资金明细订单号：MX20190123164825662916","foIsRebate":0,"fdUserBuyNumber":100000,"fdProductDate":15,"foProductBuyDate":"2019-01-23 16:48:25","foIsRemit":1,"foProductRansomDate":"2019-02-07 16:48:25","foTicketNumber":"","foExpireMoney":30066.74,"foIsUseTicket":1,"foEarnings":66.74,"foOrderCode":"LC20190123164825772477","fdName":"摩富801501"},{"foBuyMoney":10000,"fdAnnualized":0.128,"foOrderState":0,"fdNumber":288299,"foOrderMsg":"来源余额,资金明细订单号：MX20190123164404286744","foIsRebate":0,"fdUserBuyNumber":1000000,"fdProductDate":31,"foProductBuyDate":"2019-01-23 16:44:04","foIsRemit":1,"foProductRansomDate":"2019-02-23 16:44:04","foTicketNumber":"","foExpireMoney":10105.21,"foIsUseTicket":1,"foEarnings":105.21,"foOrderCode":"LC20190123164404264257","fdName":"摩富803101"},{"foBuyMoney":20000,"fdAnnualized":0.058,"foOrderState":0,"fdNumber":130678,"foOrderMsg":"来源余额,资金明细订单号：MX20190123155913448520","foIsRebate":0,"fdUserBuyNumber":100000,"fdProductDate":15,"foProductBuyDate":"2019-01-23 15:59:13","foIsRemit":1,"foProductRansomDate":"2019-02-07 15:59:13","foTicketNumber":"","foExpireMoney":20044.49,"foIsUseTicket":1,"foEarnings":44.49,"foOrderCode":"LC20190123155913536401","fdName":"摩富801501"}]
     * thenTotalPenNumber : 7
     * result_Code : 0
     * redeemTotalPenNumber : 0
     * redeemTotalMoney : 0
     */

    public String result_Msg;
    public double thenTotalMoney;
    public int thenTotalPenNumber;
    public int result_Code;
    public int redeemTotalPenNumber;
    public double redeemTotalMoney;
    public List<RecordListBean> recordList;

    protected InverstMemtRecord(Parcel in) {
        result_Msg = in.readString();
        thenTotalMoney = in.readDouble();
        thenTotalPenNumber = in.readInt();
        result_Code = in.readInt();
        redeemTotalPenNumber = in.readInt();
        redeemTotalMoney = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeDouble(thenTotalMoney);
        dest.writeInt(thenTotalPenNumber);
        dest.writeInt(result_Code);
        dest.writeInt(redeemTotalPenNumber);
        dest.writeDouble(redeemTotalMoney);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InverstMemtRecord> CREATOR = new Creator<InverstMemtRecord>() {
        @Override
        public InverstMemtRecord createFromParcel(Parcel in) {
            return new InverstMemtRecord(in);
        }

        @Override
        public InverstMemtRecord[] newArray(int size) {
            return new InverstMemtRecord[size];
        }
    };

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public double getThenTotalMoney() {
        return thenTotalMoney;
    }

    public void setThenTotalMoney(double thenTotalMoney) {
        this.thenTotalMoney = thenTotalMoney;
    }

    public int getThenTotalPenNumber() {
        return thenTotalPenNumber;
    }

    public void setThenTotalPenNumber(int thenTotalPenNumber) {
        this.thenTotalPenNumber = thenTotalPenNumber;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public int getRedeemTotalPenNumber() {
        return redeemTotalPenNumber;
    }

    public void setRedeemTotalPenNumber(int redeemTotalPenNumber) {
        this.redeemTotalPenNumber = redeemTotalPenNumber;
    }

//    public int getRedeemTotalMoney() {
//        return redeemTotalMoney;
//    }

//    public void setRedeemTotalMoney(int redeemTotalMoney) {
//        this.redeemTotalMoney = redeemTotalMoney;
//    }

    public List<RecordListBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListBean> recordList) {
        this.recordList = recordList;
    }

    public static class RecordListBean implements Parcelable{
        /**
         * foBuyMoney : 10000.87
         * fdAnnualized : 0.058
         * foOrderState : 0
         * fdNumber : 130678
         * foOrderMsg : 来源余额,资金明细订单号：MX20190124165445975177
         * foIsRebate : 0
         * fdUserBuyNumber : 100000
         * fdProductDate : 15
         * foProductBuyDate : 2019-01-24 16:54:45
         * foIsRemit : 1
         * foProductRansomDate : 2019-02-08 16:54:45
         * foTicketNumber :
         * foExpireMoney : 10023.12
         * foIsUseTicket : 1
         * foEarnings : 22.25
         * foOrderCode : LC20190124165445556730
         * fdName : 摩富801501
         */

        public double foBuyMoney;
        public double fdAnnualized;
        public int foOrderState;
        public int fdNumber;
        public String foOrderMsg;
        public int foIsRebate;
        public int fdUserBuyNumber;
        public int fdProductDate;
        public String foProductBuyDate;
        public int foIsRemit;
        public String foProductRansomDate;
        public String foTicketNumber;
        public double foExpireMoney;
        public int foIsUseTicket;
        public double foEarnings;
        public String foOrderCode;
        public String fdName;

        protected RecordListBean(Parcel in) {
            foBuyMoney = in.readDouble();
            fdAnnualized = in.readDouble();
            foOrderState = in.readInt();
            fdNumber = in.readInt();
            foOrderMsg = in.readString();
            foIsRebate = in.readInt();
            fdUserBuyNumber = in.readInt();
            fdProductDate = in.readInt();
            foProductBuyDate = in.readString();
            foIsRemit = in.readInt();
            foProductRansomDate = in.readString();
            foTicketNumber = in.readString();
            foExpireMoney = in.readDouble();
            foIsUseTicket = in.readInt();
            foEarnings = in.readDouble();
            foOrderCode = in.readString();
            fdName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(foBuyMoney);
            dest.writeDouble(fdAnnualized);
            dest.writeInt(foOrderState);
            dest.writeInt(fdNumber);
            dest.writeString(foOrderMsg);
            dest.writeInt(foIsRebate);
            dest.writeInt(fdUserBuyNumber);
            dest.writeInt(fdProductDate);
            dest.writeString(foProductBuyDate);
            dest.writeInt(foIsRemit);
            dest.writeString(foProductRansomDate);
            dest.writeString(foTicketNumber);
            dest.writeDouble(foExpireMoney);
            dest.writeInt(foIsUseTicket);
            dest.writeDouble(foEarnings);
            dest.writeString(foOrderCode);
            dest.writeString(fdName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RecordListBean> CREATOR = new Creator<RecordListBean>() {
            @Override
            public RecordListBean createFromParcel(Parcel in) {
                return new RecordListBean(in);
            }

            @Override
            public RecordListBean[] newArray(int size) {
                return new RecordListBean[size];
            }
        };

        public double getFoBuyMoney() {
            return foBuyMoney;
        }

        public void setFoBuyMoney(double foBuyMoney) {
            this.foBuyMoney = foBuyMoney;
        }

        public double getFdAnnualized() {
            return fdAnnualized;
        }

        public void setFdAnnualized(double fdAnnualized) {
            this.fdAnnualized = fdAnnualized;
        }

        public int getFoOrderState() {
            return foOrderState;
        }

        public void setFoOrderState(int foOrderState) {
            this.foOrderState = foOrderState;
        }

        public int getFdNumber() {
            return fdNumber;
        }

        public void setFdNumber(int fdNumber) {
            this.fdNumber = fdNumber;
        }

        public String getFoOrderMsg() {
            return foOrderMsg;
        }

        public void setFoOrderMsg(String foOrderMsg) {
            this.foOrderMsg = foOrderMsg;
        }

        public int getFoIsRebate() {
            return foIsRebate;
        }

        public void setFoIsRebate(int foIsRebate) {
            this.foIsRebate = foIsRebate;
        }

        public int getFdUserBuyNumber() {
            return fdUserBuyNumber;
        }

        public void setFdUserBuyNumber(int fdUserBuyNumber) {
            this.fdUserBuyNumber = fdUserBuyNumber;
        }

        public int getFdProductDate() {
            return fdProductDate;
        }

        public void setFdProductDate(int fdProductDate) {
            this.fdProductDate = fdProductDate;
        }

        public String getFoProductBuyDate() {
            return foProductBuyDate;
        }

        public void setFoProductBuyDate(String foProductBuyDate) {
            this.foProductBuyDate = foProductBuyDate;
        }

        public int getFoIsRemit() {
            return foIsRemit;
        }

        public void setFoIsRemit(int foIsRemit) {
            this.foIsRemit = foIsRemit;
        }

        public String getFoProductRansomDate() {
            return foProductRansomDate;
        }

        public void setFoProductRansomDate(String foProductRansomDate) {
            this.foProductRansomDate = foProductRansomDate;
        }

        public String getFoTicketNumber() {
            return foTicketNumber;
        }

        public void setFoTicketNumber(String foTicketNumber) {
            this.foTicketNumber = foTicketNumber;
        }

        public double getFoExpireMoney() {
            return foExpireMoney;
        }

        public void setFoExpireMoney(double foExpireMoney) {
            this.foExpireMoney = foExpireMoney;
        }

        public int getFoIsUseTicket() {
            return foIsUseTicket;
        }

        public void setFoIsUseTicket(int foIsUseTicket) {
            this.foIsUseTicket = foIsUseTicket;
        }

        public double getFoEarnings() {
            return foEarnings;
        }

        public void setFoEarnings(double foEarnings) {
            this.foEarnings = foEarnings;
        }

        public String getFoOrderCode() {
            return foOrderCode;
        }

        public void setFoOrderCode(String foOrderCode) {
            this.foOrderCode = foOrderCode;
        }

        public String getFdName() {
            return fdName;
        }

        public void setFdName(String fdName) {
            this.fdName = fdName;
        }
    }
}
