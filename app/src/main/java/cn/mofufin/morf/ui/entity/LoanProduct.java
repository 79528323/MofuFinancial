package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LoanProduct {


    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * productList : [{"loansMinMoney":10000,"lpCode":123456,"loansInterestKind":0,"loansRete":5.0E-4,"isCommission":1,"productName":"摩富贷A","loansMaxMoney":1000000,"commissionRatio":1.0E-4,"serviceCharge":0,"loansPeriod":"24","productExplain":"dasdsadasdsadasdsadas","loansOverdue":8.0E-4,"loansInterestWay":2,"loansKind":0},{"loansMinMoney":1000,"lpCode":884455,"loansInterestKind":0,"loansRete":5.0E-4,"isCommission":1,"productName":"摩富贷C","loansMaxMoney":1000000,"commissionRatio":1.0E-4,"serviceCharge":0,"loansPeriod":"6-12","productExplain":"cxzcxzcxzcxzcxzczxczxczxcxz","loansOverdue":8.0E-4,"loansInterestWay":1,"loansKind":0},{"loansMinMoney":100,"lpCode":456789,"loansInterestKind":0,"loansRete":5.0E-4,"isCommission":1,"productName":"摩富贷B","loansMaxMoney":100000,"commissionRatio":1.0E-4,"serviceCharge":0,"loansPeriod":"1-3-6","productExplain":"dsaljdklsajldjalskdjljdlsakjdlkasjldas","loansOverdue":8.0E-4,"loansInterestWay":0,"loansKind":0}]
     */

    public String result_Msg;
    public int result_Code;
    public List<ProductListBean> productList;

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

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean implements Parcelable {
        /**
         * loansMinMoney : 10000
         * lpCode : 123456
         * loansInterestKind : 0
         * loansRete : 5.0E-4
         * isCommission : 1
         * productName : 摩富贷A
         * loansMaxMoney : 1000000
         * commissionRatio : 1.0E-4
         * serviceCharge : 0
         * loansPeriod : 24
         * productExplain : dasdsadasdsadasdsadas
         * loansOverdue : 8.0E-4
         * loansInterestWay : 2
         * loansKind : 0
         */

        public int loansMinMoney;
        public int lpCode;
        public int loansInterestKind;
        public double loansRate;
        public int isCommission;
        public String productName;
        public int loansMaxMoney;
        public double commissionRatio;
        public int serviceCharge;
        public String loansPeriod;
        public String productExplain;
        public double loansOverdue;
        public int loansInterestWay;
        public int loansKind;

        protected ProductListBean(Parcel in) {
            loansMinMoney = in.readInt();
            lpCode = in.readInt();
            loansInterestKind = in.readInt();
            loansRate = in.readDouble();
            isCommission = in.readInt();
            productName = in.readString();
            loansMaxMoney = in.readInt();
            commissionRatio = in.readDouble();
            serviceCharge = in.readInt();
            loansPeriod = in.readString();
            productExplain = in.readString();
            loansOverdue = in.readDouble();
            loansInterestWay = in.readInt();
            loansKind = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(loansMinMoney);
            dest.writeInt(lpCode);
            dest.writeInt(loansInterestKind);
            dest.writeDouble(loansRate);
            dest.writeInt(isCommission);
            dest.writeString(productName);
            dest.writeInt(loansMaxMoney);
            dest.writeDouble(commissionRatio);
            dest.writeInt(serviceCharge);
            dest.writeString(loansPeriod);
            dest.writeString(productExplain);
            dest.writeDouble(loansOverdue);
            dest.writeInt(loansInterestWay);
            dest.writeInt(loansKind);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProductListBean> CREATOR = new Creator<ProductListBean>() {
            @Override
            public ProductListBean createFromParcel(Parcel in) {
                return new ProductListBean(in);
            }

            @Override
            public ProductListBean[] newArray(int size) {
                return new ProductListBean[size];
            }
        };
    }
}
