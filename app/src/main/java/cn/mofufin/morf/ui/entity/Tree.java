package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Tree implements Parcelable{


    /**
     * data : {"reason":"您有气无力的踹下5个摩富币,还需苦练腿法","coin":5}
     * bool : true
     * stateCode : 10000
     * message : Request Success!
     */

    public DataBean data;
    public boolean bool;
    public int stateCode;
    public String message;

    protected Tree(Parcel in) {
        bool = in.readByte() != 0;
        stateCode = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (bool ? 1 : 0));
        dest.writeInt(stateCode);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tree> CREATOR = new Creator<Tree>() {
        @Override
        public Tree createFromParcel(Parcel in) {
            return new Tree(in);
        }

        @Override
        public Tree[] newArray(int size) {
            return new Tree[size];
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

    public static class DataBean {
        /**
         * reason : 您有气无力的踹下5个摩富币,还需苦练腿法
         * coin : 5
         */

        private String reason;
        private int coin;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }
    }
}
