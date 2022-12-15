package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Coin implements Parcelable{


    /**
     * result_Msg : 查询成功
     * currentMoBi : 389
     * moBiList : [{"phone":"18620573888","totalMoney":3636,"merchantName":"潘勇辉"},{"phone":"15815824459","totalMoney":389,"merchantName":"李奕浩"},{"phone":"18065239989","totalMoney":263,"merchantName":"黄美珠"},{"phone":"13570520105","totalMoney":200,"merchantName":"刘科良"},{"phone":"17666529449","totalMoney":114,"merchantName":"文浩"},{"phone":"13730880668","totalMoney":54,"merchantName":"杨开"},{"phone":"13528904596","totalMoney":52,"merchantName":"李韶"},{"phone":"15188910676","totalMoney":50,"merchantName":"赵玲苛"},{"phone":"13725567735","totalMoney":33,"merchantName":"黄惠宁"},{"phone":"18773682877","totalMoney":16,"merchantName":"贺吉平"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int currentMoBi;
    public int result_Code;
    public Record record;
    public List<MoBiListBean> moBiList;


    protected Coin(Parcel in) {
        result_Msg = in.readString();
        currentMoBi = in.readInt();
        result_Code = in.readInt();
        record = in.readParcelable(Record.class.getClassLoader());
        moBiList = in.createTypedArrayList(MoBiListBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(currentMoBi);
        dest.writeInt(result_Code);
        dest.writeParcelable(record, flags);
        dest.writeTypedList(moBiList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coin> CREATOR = new Creator<Coin>() {
        @Override
        public Coin createFromParcel(Parcel in) {
            return new Coin(in);
        }

        @Override
        public Coin[] newArray(int size) {
            return new Coin[size];
        }
    };

    public static class Record implements Parcelable{
        public int mrNumber;
        public int mrGetType;
        public String merchantName;


        protected Record(Parcel in) {
            mrNumber = in.readInt();
            mrGetType = in.readInt();
            merchantName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mrNumber);
            dest.writeInt(mrGetType);
            dest.writeString(merchantName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Record> CREATOR = new Creator<Record>() {
            @Override
            public Record createFromParcel(Parcel in) {
                return new Record(in);
            }

            @Override
            public Record[] newArray(int size) {
                return new Record[size];
            }
        };
    }

    public static class MoBiListBean implements Parcelable{
        /**
         * phone : 18620573888
         * totalMoney : 3636
         * merchantName : 潘勇辉
         */

        public String phone;
        public int totalMoney;
        public String merchantName;
        public int historyTotal;

        protected MoBiListBean(Parcel in) {
            phone = in.readString();
            totalMoney = in.readInt();
            merchantName = in.readString();
            historyTotal = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(phone);
            dest.writeInt(totalMoney);
            dest.writeString(merchantName);
            dest.writeInt(historyTotal);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MoBiListBean> CREATOR = new Creator<MoBiListBean>() {
            @Override
            public MoBiListBean createFromParcel(Parcel in) {
                return new MoBiListBean(in);
            }

            @Override
            public MoBiListBean[] newArray(int size) {
                return new MoBiListBean[size];
            }
        };

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(int totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }
    }

}
