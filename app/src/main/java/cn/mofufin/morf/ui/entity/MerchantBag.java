package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MerchantBag implements Parcelable{

    /**
     * bool : true
     * data : {"reason":"查询成功","list":[{"addCardBagDate":"2018-12-24 16:09:03","gdGoodsDenomination":1,"gdGoodsBranchType":5,"mcbValidDateBegin":"2018-12-24 16:09:03","gdGoodsType":0,"gdGoodsName":"代付减免券","gdGoodsUseCondition":100,"mcbGoodsCode":"993779","mcbValidDateEnd":"2018-12-27 16:09:03","mcbGoodsState":0,"gdGoodsExplain":"使用于余额代付提现,减免一元手续费.","mcbGoodsNumber":"465BF5CC4F2640E0B77EE97FA6BC5AFF"},{"addCardBagDate":"2018-12-24 15:57:22","gdGoodsDenomination":0.005,"gdGoodsBranchType":3,"mcbValidDateBegin":"2018-12-24 15:57:22","gdGoodsType":0,"gdGoodsName":"境外减免券","gdGoodsUseCondition":200,"mcbGoodsCode":"766685","mcbValidDateEnd":"2018-12-27 15:57:22","mcbGoodsState":0,"gdGoodsExplain":"该商品用于境外交易减免手续费0.5%","mcbGoodsNumber":"6002145E073D41B6908FCDD74654FC6D"}]}
     * stateCode : 10000
     * message : Request Success!
     */

    public boolean bool;
    public DataBean data;
    public int stateCode;
    public String message;

    protected MerchantBag(Parcel in) {
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

    public static final Creator<MerchantBag> CREATOR = new Creator<MerchantBag>() {
        @Override
        public MerchantBag createFromParcel(Parcel in) {
            return new MerchantBag(in);
        }

        @Override
        public MerchantBag[] newArray(int size) {
            return new MerchantBag[size];
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
        /**
         * reason : 查询成功
         * list : [{"addCardBagDate":"2018-12-24 16:09:03","gdGoodsDenomination":1,"gdGoodsBranchType":5,"mcbValidDateBegin":"2018-12-24 16:09:03","gdGoodsType":0,"gdGoodsName":"代付减免券","gdGoodsUseCondition":100,"mcbGoodsCode":"993779","mcbValidDateEnd":"2018-12-27 16:09:03","mcbGoodsState":0,"gdGoodsExplain":"使用于余额代付提现,减免一元手续费.","mcbGoodsNumber":"465BF5CC4F2640E0B77EE97FA6BC5AFF"},{"addCardBagDate":"2018-12-24 15:57:22","gdGoodsDenomination":0.005,"gdGoodsBranchType":3,"mcbValidDateBegin":"2018-12-24 15:57:22","gdGoodsType":0,"gdGoodsName":"境外减免券","gdGoodsUseCondition":200,"mcbGoodsCode":"766685","mcbValidDateEnd":"2018-12-27 15:57:22","mcbGoodsState":0,"gdGoodsExplain":"该商品用于境外交易减免手续费0.5%","mcbGoodsNumber":"6002145E073D41B6908FCDD74654FC6D"}]
         */

        private String reason;
        private List<ListBean> list;

        protected DataBean(Parcel in) {
            reason = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Parcelable{
            /**
             * addCardBagDate : 2018-12-24 16:09:03
             * gdGoodsDenomination : 1
             * gdGoodsBranchType : 5
             * mcbValidDateBegin : 2018-12-24 16:09:03
             * gdGoodsType : 0
             * gdGoodsName : 代付减免券
             * gdGoodsUseCondition : 100
             * mcbGoodsCode : 993779
             * mcbValidDateEnd : 2018-12-27 16:09:03
             * mcbGoodsState : 0
             * gdGoodsExplain : 使用于余额代付提现,减免一元手续费.
             * mcbGoodsNumber : 465BF5CC4F2640E0B77EE97FA6BC5AFF
             */

            public String addCardBagDate;
            public double gdGoodsDenomination;
            public int gdGoodsBranchType;
            public String mcbValidDateBegin;
            public int gdGoodsType;
            public String gdGoodsName;
            public int gdGoodsUseCondition;
            public String mcbGoodsCode;
            public String mcbValidDateEnd;
            public int mcbGoodsState;
            public String gdGoodsExplain;
            public String mcbGoodsNumber;

            protected ListBean(Parcel in) {
                addCardBagDate = in.readString();
                gdGoodsDenomination = in.readDouble();
                gdGoodsBranchType = in.readInt();
                mcbValidDateBegin = in.readString();
                gdGoodsType = in.readInt();
                gdGoodsName = in.readString();
                gdGoodsUseCondition = in.readInt();
                mcbGoodsCode = in.readString();
                mcbValidDateEnd = in.readString();
                mcbGoodsState = in.readInt();
                gdGoodsExplain = in.readString();
                mcbGoodsNumber = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(addCardBagDate);
                dest.writeDouble(gdGoodsDenomination);
                dest.writeInt(gdGoodsBranchType);
                dest.writeString(mcbValidDateBegin);
                dest.writeInt(gdGoodsType);
                dest.writeString(gdGoodsName);
                dest.writeInt(gdGoodsUseCondition);
                dest.writeString(mcbGoodsCode);
                dest.writeString(mcbValidDateEnd);
                dest.writeInt(mcbGoodsState);
                dest.writeString(gdGoodsExplain);
                dest.writeString(mcbGoodsNumber);
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

            public String getAddCardBagDate() {
                return addCardBagDate;
            }

            public void setAddCardBagDate(String addCardBagDate) {
                this.addCardBagDate = addCardBagDate;
            }

//            public int getGdGoodsDenomination() {
//                return gdGoodsDenomination;
//            }

//            public void setGdGoodsDenomination(int gdGoodsDenomination) {
//                this.gdGoodsDenomination = gdGoodsDenomination;
//            }

            public int getGdGoodsBranchType() {
                return gdGoodsBranchType;
            }

            public void setGdGoodsBranchType(int gdGoodsBranchType) {
                this.gdGoodsBranchType = gdGoodsBranchType;
            }

            public String getMcbValidDateBegin() {
                return mcbValidDateBegin;
            }

            public void setMcbValidDateBegin(String mcbValidDateBegin) {
                this.mcbValidDateBegin = mcbValidDateBegin;
            }

            public int getGdGoodsType() {
                return gdGoodsType;
            }

            public void setGdGoodsType(int gdGoodsType) {
                this.gdGoodsType = gdGoodsType;
            }

            public String getGdGoodsName() {
                return gdGoodsName;
            }

            public void setGdGoodsName(String gdGoodsName) {
                this.gdGoodsName = gdGoodsName;
            }

            public int getGdGoodsUseCondition() {
                return gdGoodsUseCondition;
            }

            public void setGdGoodsUseCondition(int gdGoodsUseCondition) {
                this.gdGoodsUseCondition = gdGoodsUseCondition;
            }

            public String getMcbGoodsCode() {
                return mcbGoodsCode;
            }

            public void setMcbGoodsCode(String mcbGoodsCode) {
                this.mcbGoodsCode = mcbGoodsCode;
            }

            public String getMcbValidDateEnd() {
                return mcbValidDateEnd;
            }

            public void setMcbValidDateEnd(String mcbValidDateEnd) {
                this.mcbValidDateEnd = mcbValidDateEnd;
            }

            public int getMcbGoodsState() {
                return mcbGoodsState;
            }

            public void setMcbGoodsState(int mcbGoodsState) {
                this.mcbGoodsState = mcbGoodsState;
            }

            public String getGdGoodsExplain() {
                return gdGoodsExplain;
            }

            public void setGdGoodsExplain(String gdGoodsExplain) {
                this.gdGoodsExplain = gdGoodsExplain;
            }

            public String getMcbGoodsNumber() {
                return mcbGoodsNumber;
            }

            public void setMcbGoodsNumber(String mcbGoodsNumber) {
                this.mcbGoodsNumber = mcbGoodsNumber;
            }
        }
    }
}
