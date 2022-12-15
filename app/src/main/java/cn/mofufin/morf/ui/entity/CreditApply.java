package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CreditApply implements Parcelable{


    /**
     * bool : true
     * data : {"list":[{"cardName":"上海银行","cardAlias":"Shanghai","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"民生银行","cardAlias":"CMBC","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"交通银行","cardAlias":"BCOM","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"华夏银行","cardAlias":"HXB","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"兴业银行","cardAlias":"CIB","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"广发银行","cardAlias":"CGB","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"浦发银行","cardAlias":"SPDB","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"光大银行","cardAlias":"CEB","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"中信银行","cardAlias":"CITIC","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"},{"cardName":"中国银行","cardAlias":"BOC","cardUrl":"http://www.baidu.com","cardSlogan":"摩富金服申请通道"}]}
     * stateCode : 10000
     * message : Request Success!
     */

    public boolean bool;
    public DataBean data;
    public int stateCode;
    public String message;

    protected CreditApply(Parcel in) {
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

    public static final Creator<CreditApply> CREATOR = new Creator<CreditApply>() {
        @Override
        public CreditApply createFromParcel(Parcel in) {
            return new CreditApply(in);
        }

        @Override
        public CreditApply[] newArray(int size) {
            return new CreditApply[size];
        }
    };

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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
        public List<ListBean> list;

        protected DataBean(Parcel in) {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Parcelable{
            /**
             * cardName : 上海银行
             * cardAlias : Shanghai
             * cardUrl : http://www.baidu.com
             * cardSlogan : 摩富金服申请通道
             */

            public String cardName;
            public String cardAlias;
            public String cardUrl;
            public String cardSlogan;

            protected ListBean(Parcel in) {
                cardName = in.readString();
                cardAlias = in.readString();
                cardUrl = in.readString();
                cardSlogan = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(cardName);
                dest.writeString(cardAlias);
                dest.writeString(cardUrl);
                dest.writeString(cardSlogan);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel in) {
                    return new ListBean(in);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getCardAlias() {
                return cardAlias;
            }

            public void setCardAlias(String cardAlias) {
                this.cardAlias = cardAlias;
            }

            public String getCardUrl() {
                return cardUrl;
            }

            public void setCardUrl(String cardUrl) {
                this.cardUrl = cardUrl;
            }

            public String getCardSlogan() {
                return cardSlogan;
            }

            public void setCardSlogan(String cardSlogan) {
                this.cardSlogan = cardSlogan;
            }
        }
    }
}
