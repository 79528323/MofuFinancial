package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order implements Parcelable {
    public int result_Code;
    public String result_Msg;
    public double totalMoney;
    public int totalPenNumber;
    public double todayTotalMoney;
    public int todayTotalPenNumber;
    public int findType;
    public List<OrderDetails> detailList;
//    public List<OrderDetails> detailList;

    protected Order(Parcel in) {
        result_Code = in.readInt();
        result_Msg = in.readString();
        totalMoney = in.readDouble();
        totalPenNumber = in.readInt();
        todayTotalMoney = in.readDouble();
        todayTotalPenNumber = in.readInt();
        findType = in.readInt();
        detailList = in.createTypedArrayList(OrderDetails.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(result_Code);
        dest.writeString(result_Msg);
        dest.writeDouble(totalMoney);
        dest.writeInt(totalPenNumber);
        dest.writeDouble(todayTotalMoney);
        dest.writeInt(todayTotalPenNumber);
        dest.writeInt(findType);
        dest.writeTypedList(detailList);
    }


    public static class OrderDetails implements Parcelable{

        public String fee;
        public String resultCode;
        public String resultCodeMsg;
        public String downOrderNo;
        public String oCreateDate;
        public String debitCardName;
        public String debitCardNo;
        public String memberNo;
        public String termId;
        public String termSn;
        public String oMerchantCode;
        public String consumePan;
        public String consumeAmt;

        //扫码参数

        public String merchantCode;
        public String phone;
        public String transType;
        public String payType;
        public String ordAmt;
        public String merFee;
        public String transStat;
        public String cashOrdId;
        public String merOrdId;
        public String outTransId;
        public String partyOrderId;
        public int isTicket;
        public String ticketNumber;
        public String payRemark;

        //快捷参数
        public String tcName;
        public String amount;
        public String payBankCard;
        public String requestNo;
        public String payBank;
        public String status;
        public String genPayMoney;
        public String toBank;
        public String toBankCard;
//        public int isTicket;//使用扫码参数
        public String isTicketNumber;

        //境外收款
        public String ovOrderCode;
        public int ovPayKind;
        public String ovOrderToMoney;
        public int ovPayCurrency;
        public String ovOrderAmt;
        public int ovPayState;
        public int ovIsTicket;
        public String ovOrderFee;
        public String ovOrderMessageMoney;
        public String ovTicketNumber;
        public int ovPayWay;
        public int ovIsRemit;
        public String channelName;
        public String ovDate;
        public String ovToDate;

        protected OrderDetails(Parcel in) {
            fee = in.readString();
            resultCode = in.readString();
            resultCodeMsg = in.readString();
            downOrderNo = in.readString();
            oCreateDate = in.readString();
            debitCardName = in.readString();
            debitCardNo = in.readString();
            memberNo = in.readString();
            termId = in.readString();
            termSn = in.readString();
            oMerchantCode = in.readString();
            consumePan = in.readString();
            consumeAmt = in.readString();


            merchantCode = in.readString();
            phone = in.readString();
            transType = in.readString();
            payType = in.readString();
            ordAmt = in.readString();
            merFee = in.readString();
            transStat = in.readString();
            cashOrdId = in.readString();
            merOrdId = in.readString();
            outTransId = in.readString();
            partyOrderId = in.readString();
            isTicket = in.readInt();
            ticketNumber = in.readString();
            payRemark = in.readString();

            tcName = in.readString();
            amount = in.readString();
            payBankCard = in.readString();
            requestNo = in.readString();
            payBank = in.readString();
            status = in.readString();
            genPayMoney = in.readString();
            toBank = in.readString();
            toBankCard = in.readString();
            isTicketNumber = in.readString();

            ovOrderCode = in.readString();
            ovPayKind = in.readInt();
            ovOrderToMoney = in.readString();
            ovPayCurrency = in.readInt();
            ovOrderAmt = in.readString();
            ovPayState = in.readInt();
            ovIsTicket = in.readInt();
            ovOrderFee = in.readString();
            ovOrderMessageMoney = in.readString();
            ovTicketNumber = in.readString();
            ovPayWay = in.readInt();
            ovIsRemit = in.readInt();
            channelName = in.readString();
            ovDate = in.readString();
            ovToDate = in.readString();

        }

        public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
            @Override
            public OrderDetails createFromParcel(Parcel in) {
                return new OrderDetails(in);
            }

            @Override
            public OrderDetails[] newArray(int size) {
                return new OrderDetails[size];
            }
        };

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultCodeMsg() {
            return resultCodeMsg;
        }

        public void setResultCodeMsg(String resultCodeMsg) {
            this.resultCodeMsg = resultCodeMsg;
        }

        public String getDownOrderNo() {
            return downOrderNo;
        }

        public void setDownOrderNo(String downOrderNo) {
            this.downOrderNo = downOrderNo;
        }

        public String getOCreateDate() {
            return oCreateDate;
        }

        public void setOCreateDate(String oCreateDate) {
            this.oCreateDate = oCreateDate;
        }

        public String getDebitCardName() {
            return debitCardName;
        }

        public void setDebitCardName(String debitCardName) {
            this.debitCardName = debitCardName;
        }

        public String getDebitCardNo() {
            return debitCardNo;
        }

        public void setDebitCardNo(String debitCardNo) {
            this.debitCardNo = debitCardNo;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getTermId() {
            return termId;
        }

        public void setTermId(String termId) {
            this.termId = termId;
        }

        public String getTermSn() {
            return termSn;
        }

        public void setTermSn(String termSn) {
            this.termSn = termSn;
        }

        public String getOMerchantCode() {
            return oMerchantCode;
        }

        public void setOMerchantCode(String oMerchantCode) {
            this.oMerchantCode = oMerchantCode;
        }

        public String getConsumePan() {
            return consumePan;
        }

        public void setConsumePan(String consumePan) {
            this.consumePan = consumePan;
        }

        public String getConsumeAmt() {
            return consumeAmt;
        }

        public void setConsumeAmt(String consumeAmt) {
            this.consumeAmt = consumeAmt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(fee);
            dest.writeString(resultCode);
            dest.writeString(resultCodeMsg);
            dest.writeString(downOrderNo);
            dest.writeString(oCreateDate);
            dest.writeString(debitCardName);
            dest.writeString(debitCardNo);
            dest.writeString(memberNo);
            dest.writeString(termId);
            dest.writeString(termSn);
            dest.writeString(oMerchantCode);
            dest.writeString(consumePan);
            dest.writeString(consumeAmt);


            dest.writeString(merchantCode);
            dest.writeString(phone);
            dest.writeString(transType);
            dest.writeString(payType);
            dest.writeString(ordAmt);
            dest.writeString(merFee);
            dest.writeString(transStat);
            dest.writeString(cashOrdId);
            dest.writeString(merOrdId);
            dest.writeString(outTransId);
            dest.writeString(partyOrderId);
            dest.writeInt(isTicket);
            dest.writeString(ticketNumber);
            dest.writeString(payRemark);

            dest.writeString(tcName);
            dest.writeString(amount);
            dest.writeString(payBankCard);
            dest.writeString(requestNo);
            dest.writeString(payBank);
            dest.writeString(status);
            dest.writeString(genPayMoney);
            dest.writeString(toBank);
            dest.writeString(toBankCard);
            dest.writeString(isTicketNumber);

            dest.writeString(ovOrderCode);
            dest.writeInt(ovPayKind);
            dest.writeString(ovOrderToMoney);
            dest.writeInt(ovPayCurrency);
            dest.writeString(ovOrderAmt);
            dest.writeInt(ovPayState);
            dest.writeInt(ovIsTicket);
            dest.writeString(ovOrderFee);
            dest.writeString(ovOrderMessageMoney);
            dest.writeString(ovTicketNumber);
            dest.writeInt(ovPayWay);
            dest.writeInt(ovIsRemit);
            dest.writeString(channelName);
            dest.writeString(ovDate);
            dest.writeString(ovToDate);
        }
    }

//    public static class OrderScanDetails implements Parcelable{
//
//        /***
//         * merchantCode:编号
//         phone:手机号
//         transType:交易类型 1000(消费) 1010(预授权) 1020(预授权完成) 2000(消费撤销) 2010(预授权撤销) 2020(预授权完成撤销)
//         payType:交易方式 10微信 20支付宝 30刷卡 40银联二维码 50云闪付
//         ordAmt:交易金额
//         merFee:手续费
//         transStat:交易状态 I - 初始状态 S - 成功 F - 失败
//         cashOrdId:汇付订单号
//         merOrdId:自身系统订单号
//         outTransId:微信 支付宝订单号
//         partyOrderId:微信支付宝条码号
//         oCreateDate:订单回调时间*/
//
//        public String merchantCode;
//        public String phone;
//        public String transType;
//        public String payType;
//        public String ordAmt;
//        public String merFee;
//        public String transStat;
//        public String cashOrdId;
//        public String merOrdId;
//        public String outTransId;
//        public String partyOrderId;
//        public String oCreateDate;
//
//
//        protected OrderScanDetails(Parcel in) {
//            merchantCode = in.readString();
//            phone = in.readString();
//            transType = in.readString();
//            payType = in.readString();
//            ordAmt = in.readString();
//            merFee = in.readString();
//            transStat = in.readString();
//            cashOrdId = in.readString();
//            merOrdId = in.readString();
//            outTransId = in.readString();
//            partyOrderId = in.readString();
//            oCreateDate = in.readString();
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(merchantCode);
//            dest.writeString(phone);
//            dest.writeString(transType);
//            dest.writeString(payType);
//            dest.writeString(ordAmt);
//            dest.writeString(merFee);
//            dest.writeString(transStat);
//            dest.writeString(cashOrdId);
//            dest.writeString(merOrdId);
//            dest.writeString(outTransId);
//            dest.writeString(partyOrderId);
//            dest.writeString(oCreateDate);
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        public static final Creator<OrderScanDetails> CREATOR = new Creator<OrderScanDetails>() {
//            @Override
//            public OrderScanDetails createFromParcel(Parcel in) {
//                return new OrderScanDetails(in);
//            }
//
//            @Override
//            public OrderScanDetails[] newArray(int size) {
//                return new OrderScanDetails[size];
//            }
//        };
//    }
}
