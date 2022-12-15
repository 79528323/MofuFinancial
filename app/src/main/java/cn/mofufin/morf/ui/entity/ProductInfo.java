package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductInfo implements Parcelable {


    /**
     * result_Msg : 查询成功
     * inCastAsset : 0
     * balance : 0
     * alreadyEarnings : 0
     * result_Code : 0
     * aboutEarnings : 0
     * Fundproduct : {"fdMinMoney":500,"fdAnnualized":0.158,"fdNumber":668186,"fdName":"新手专享","fdProductDate":31}
     * productList : [{"fdMinMoney":500,"fdAnnualized":0.158,"fdNumber":668186,"fdName":"新手专享","fdProductDate":31},{"fdMinMoney":500,"fdAnnualized":0.058,"fdNumber":130678,"fdName":"摩富801501","fdProductDate":15},{"fdMinMoney":500,"fdAnnualized":0.128,"fdNumber":288299,"fdName":"摩富803101","fdProductDate":31},{"fdMinMoney":500,"fdAnnualized":0.158,"fdNumber":111332,"fdName":"摩富0903","fdProductDate":60}]
     */

    public String result_Msg;
    public int result_Code;

    public double inCastAsset;
    public double balance;
    public double alreadyEarnings;
    public double aboutEarnings;
    public FundproductBean Fundproduct;
    public List<ProductListBean> productList;

    protected ProductInfo(Parcel in) {
        result_Msg = in.readString();
        inCastAsset = in.readDouble();
        balance = in.readDouble();
        alreadyEarnings = in.readDouble();
        result_Code = in.readInt();
        aboutEarnings = in.readDouble();
        Fundproduct = in.readParcelable(FundproductBean.class.getClassLoader());
        productList = in.createTypedArrayList(ProductListBean.CREATOR);
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeDouble(inCastAsset);
        dest.writeDouble(balance);
        dest.writeDouble(alreadyEarnings);
        dest.writeInt(result_Code);
        dest.writeDouble(aboutEarnings);
        dest.writeParcelable(Fundproduct, flags);
        dest.writeTypedList(productList);
    }

    public static class FundproductBean implements Parcelable{
        /**
         * fdMinMoney : 500
         * fdAnnualized : 0.158
         * fdNumber : 668186
         * fdName : 新手专享
         * fdProductDate : 31
         */

        public int fdMinMoney;
        public double fdAnnualized;
        public int fdNumber;
        public String fdName;
        public int fdProductDate;

        protected FundproductBean(Parcel in) {
            fdMinMoney = in.readInt();
            fdAnnualized = in.readDouble();
            fdNumber = in.readInt();
            fdName = in.readString();
            fdProductDate = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(fdMinMoney);
            dest.writeDouble(fdAnnualized);
            dest.writeInt(fdNumber);
            dest.writeString(fdName);
            dest.writeInt(fdProductDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<FundproductBean> CREATOR = new Creator<FundproductBean>() {
            @Override
            public FundproductBean createFromParcel(Parcel in) {
                return new FundproductBean(in);
            }

            @Override
            public FundproductBean[] newArray(int size) {
                return new FundproductBean[size];
            }
        };

        public int getFdMinMoney() {
            return fdMinMoney;
        }

        public void setFdMinMoney(int fdMinMoney) {
            this.fdMinMoney = fdMinMoney;
        }

        public double getFdAnnualized() {
            return fdAnnualized;
        }

        public void setFdAnnualized(double fdAnnualized) {
            this.fdAnnualized = fdAnnualized;
        }

        public int getFdNumber() {
            return fdNumber;
        }

        public void setFdNumber(int fdNumber) {
            this.fdNumber = fdNumber;
        }

        public String getFdName() {
            return fdName;
        }

        public void setFdName(String fdName) {
            this.fdName = fdName;
        }

        public int getFdProductDate() {
            return fdProductDate;
        }

        public void setFdProductDate(int fdProductDate) {
            this.fdProductDate = fdProductDate;
        }
    }

    public static class ProductListBean implements Parcelable{
        /**
         * fdMinMoney : 500
         * fdAnnualized : 0.158
         * fdNumber : 668186
         * fdName : 新手专享
         * fdProductDate : 31
         */

        public int fdMinMoney;
        public double fdAnnualized;
        public int fdNumber;
        public String fdName;
        public int fdProductDate;
        public int fdIsAdverProduct;//是否为广告产品 0:是 1:不是


        protected ProductListBean(Parcel in) {
            fdMinMoney = in.readInt();
            fdAnnualized = in.readDouble();
            fdNumber = in.readInt();
            fdName = in.readString();
            fdProductDate = in.readInt();
            fdIsAdverProduct = in.readInt();
        }

        public ProductListBean() {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(fdMinMoney);
            dest.writeDouble(fdAnnualized);
            dest.writeInt(fdNumber);
            dest.writeString(fdName);
            dest.writeInt(fdProductDate);
            dest.writeInt(fdIsAdverProduct);
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

        public int getFdMinMoney() {
            return fdMinMoney;
        }

        public void setFdMinMoney(int fdMinMoney) {
            this.fdMinMoney = fdMinMoney;
        }

        public double getFdAnnualized() {
            return fdAnnualized;
        }

        public void setFdAnnualized(double fdAnnualized) {
            this.fdAnnualized = fdAnnualized;
        }

        public int getFdNumber() {
            return fdNumber;
        }

        public void setFdNumber(int fdNumber) {
            this.fdNumber = fdNumber;
        }

        public String getFdName() {
            return fdName;
        }

        public void setFdName(String fdName) {
            this.fdName = fdName;
        }

        public int getFdProductDate() {
            return fdProductDate;
        }

        public void setFdProductDate(int fdProductDate) {
            this.fdProductDate = fdProductDate;
        }
    }
}
