package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ExChanged implements Parcelable{

    /**
     * data : {"category":"USD","refreshTime":"17:03:12","spotPurchasePrice ":"692.31","USDollar":0,"reason":"The purchase price of usd spot exchange was successfully obtained."}
     * bool : true
     * stateCode : 10000
     * message : Request Success!
     */

    private DataBean data;
    private boolean bool;
    private int stateCode;
    private String message;

    protected ExChanged(Parcel in) {
        data = in.readParcelable(DataBean.class.getClassLoader());
        bool = in.readByte() != 0;
        stateCode = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeByte((byte) (bool ? 1 : 0));
        dest.writeInt(stateCode);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExChanged> CREATOR = new Creator<ExChanged>() {
        @Override
        public ExChanged createFromParcel(Parcel in) {
            return new ExChanged(in);
        }

        @Override
        public ExChanged[] newArray(int size) {
            return new ExChanged[size];
        }
    };

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Parcelable{
        /**
         * category : USD
         * refreshTime : 17:03:12
         * spotPurchasePrice  : 692.31
         * USDollar : 0
         * reason : The purchase price of usd spot exchange was successfully obtained.
         */

        public String category;
        public String refreshTime;
        public String spotPurchasePrice;
        public double USDollar;
        public double HKDollar;
        public String reason;

        protected DataBean(Parcel in) {
            category = in.readString();
            refreshTime = in.readString();
            spotPurchasePrice = in.readString();
            USDollar = in.readDouble();
            HKDollar = in.readDouble();
            reason = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(category);
            dest.writeString(refreshTime);
            dest.writeString(spotPurchasePrice);
            dest.writeDouble(USDollar);
            dest.writeDouble(HKDollar);
            dest.writeString(reason);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRefreshTime() {
            return refreshTime;
        }

        public void setRefreshTime(String refreshTime) {
            this.refreshTime = refreshTime;
        }

        public String getSpotPurchasePrice() {
            return spotPurchasePrice;
        }

        public void setSpotPurchasePrice(String spotPurchasePrice) {
            this.spotPurchasePrice = spotPurchasePrice;
        }

        public double getUSDollar() {
            return USDollar;
        }

        public void setUSDollar(double USDollar) {
            this.USDollar = USDollar;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
