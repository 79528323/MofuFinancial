package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Address implements Parcelable {

    /**
     * data : {"addressInfo":[{"takePersonPhone":"13760832152","takePersonName":"李荣浩","addressNumber":886028,"takeAddress":"中国台湾省"}],"reason":"查询成功"}
     * bool : true
     * stateCode : 10000
     * message : Request Success!
     */

    public DataBean data;
    public boolean bool;
    public int stateCode;
    public String message;

    protected Address(Parcel in) {
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

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
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
         * addressInfo : [{"takePersonPhone":"13760832152","takePersonName":"李荣浩","addressNumber":886028,"takeAddress":"中国台湾省"}]
         * reason : 查询成功
         */

        public String reason;
        public List<AddressInfoBean> addressInfo;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<AddressInfoBean> getAddressInfo() {
            return addressInfo;
        }

        public void setAddressInfo(List<AddressInfoBean> addressInfo) {
            this.addressInfo = addressInfo;
        }

        public static class AddressInfoBean implements Parcelable{
            /**
             * takePersonPhone : 13760832152
             * takePersonName : 李荣浩
             * addressNumber : 886028
             * takeAddress : 中国台湾省
             */

            public String takePersonPhone;
            public String takePersonName;
            public int addressNumber;
            public String takeAddress;

            public AddressInfoBean() {
            }

            protected AddressInfoBean(Parcel in) {
                takePersonPhone = in.readString();
                takePersonName = in.readString();
                addressNumber = in.readInt();
                takeAddress = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(takePersonPhone);
                dest.writeString(takePersonName);
                dest.writeInt(addressNumber);
                dest.writeString(takeAddress);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<AddressInfoBean> CREATOR = new Creator<AddressInfoBean>() {
                @Override
                public AddressInfoBean createFromParcel(Parcel in) {
                    return new AddressInfoBean(in);
                }

                @Override
                public AddressInfoBean[] newArray(int size) {
                    return new AddressInfoBean[size];
                }
            };

            public String getTakePersonPhone() {
                return takePersonPhone;
            }

            public void setTakePersonPhone(String takePersonPhone) {
                this.takePersonPhone = takePersonPhone;
            }

            public String getTakePersonName() {
                return takePersonName;
            }

            public void setTakePersonName(String takePersonName) {
                this.takePersonName = takePersonName;
            }

            public int getAddressNumber() {
                return addressNumber;
            }

            public void setAddressNumber(int addressNumber) {
                this.addressNumber = addressNumber;
            }

            public String getTakeAddress() {
                return takeAddress;
            }

            public void setTakeAddress(String takeAddress) {
                this.takeAddress = takeAddress;
            }
        }
    }
}
