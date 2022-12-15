package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Coupons implements Parcelable{


    /**
     * bool : true
     * data : {"reason":"查询成功","list":[{"gdGoodsDenomination":0.005,"gdGoodsName":"境外减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:02:19","gdGoodsBranchType":3,"gdGoodsType":0,"gdGoodsCode":"766685","gdGoodsUseCondition":200,"gdGoodsExplain":"该商品用于境外交易减免手续费0.5%","gdGoodsMoney":1888,"gdGoodsRestrict":100},{"gdGoodsDenomination":1,"gdGoodsName":"快捷返现券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:01:13","gdGoodsBranchType":10,"gdGoodsType":0,"gdGoodsCode":"955981","gdGoodsUseCondition":10000,"gdGoodsExplain":"该商品用于快捷交易返现","gdGoodsMoney":500,"gdGoodsRestrict":100},{"gdGoodsDenomination":2.0E-4,"gdGoodsName":"快捷减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:00:06","gdGoodsBranchType":1,"gdGoodsType":0,"gdGoodsCode":"863762","gdGoodsUseCondition":10000,"gdGoodsExplain":"该商品用于快捷交易减免费率0.0002","gdGoodsMoney":1000,"gdGoodsRestrict":100},{"gdGoodsDenomination":88.88,"gdGoodsName":"钻石会员券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:49:53","gdGoodsBranchType":15,"gdGoodsType":0,"gdGoodsCode":"585460","gdGoodsUseCondition":1,"gdGoodsExplain":"该商品用于普通会员或金牌会员升级钻石会员","gdGoodsMoney":8888,"gdGoodsRestrict":1},{"gdGoodsDenomination":38.88,"gdGoodsName":"金牌会员券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:48:23","gdGoodsBranchType":14,"gdGoodsType":0,"gdGoodsCode":"551292","gdGoodsUseCondition":1,"gdGoodsExplain":"该商品用于普通会员用于升级金牌会员","gdGoodsMoney":3888,"gdGoodsRestrict":1},{"gdGoodsDenomination":1,"gdGoodsName":"代付减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:45:48","gdGoodsBranchType":5,"gdGoodsType":0,"gdGoodsCode":"993779","gdGoodsUseCondition":100,"gdGoodsExplain":"使用于余额代付提现,减免一元手续费.","gdGoodsMoney":500,"gdGoodsRestrict":100}]}
     * stateCode : 10000
     * message : Request Success!
     */

    public boolean bool;
    public DataBean data;
    public int stateCode;
    public String message;

    protected Coupons(Parcel in) {
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

    public static final Creator<Coupons> CREATOR = new Creator<Coupons>() {
        @Override
        public Coupons createFromParcel(Parcel in) {
            return new Coupons(in);
        }

        @Override
        public Coupons[] newArray(int size) {
            return new Coupons[size];
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

    public static class DataBean {
        /**
         * reason : 查询成功
         * list : [{"gdGoodsDenomination":0.005,"gdGoodsName":"境外减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:02:19","gdGoodsBranchType":3,"gdGoodsType":0,"gdGoodsCode":"766685","gdGoodsUseCondition":200,"gdGoodsExplain":"该商品用于境外交易减免手续费0.5%","gdGoodsMoney":1888,"gdGoodsRestrict":100},{"gdGoodsDenomination":1,"gdGoodsName":"快捷返现券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:01:13","gdGoodsBranchType":10,"gdGoodsType":0,"gdGoodsCode":"955981","gdGoodsUseCondition":10000,"gdGoodsExplain":"该商品用于快捷交易返现","gdGoodsMoney":500,"gdGoodsRestrict":100},{"gdGoodsDenomination":2.0E-4,"gdGoodsName":"快捷减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 23:00:06","gdGoodsBranchType":1,"gdGoodsType":0,"gdGoodsCode":"863762","gdGoodsUseCondition":10000,"gdGoodsExplain":"该商品用于快捷交易减免费率0.0002","gdGoodsMoney":1000,"gdGoodsRestrict":100},{"gdGoodsDenomination":88.88,"gdGoodsName":"钻石会员券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:49:53","gdGoodsBranchType":15,"gdGoodsType":0,"gdGoodsCode":"585460","gdGoodsUseCondition":1,"gdGoodsExplain":"该商品用于普通会员或金牌会员升级钻石会员","gdGoodsMoney":8888,"gdGoodsRestrict":1},{"gdGoodsDenomination":38.88,"gdGoodsName":"金牌会员券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:48:23","gdGoodsBranchType":14,"gdGoodsType":0,"gdGoodsCode":"551292","gdGoodsUseCondition":1,"gdGoodsExplain":"该商品用于普通会员用于升级金牌会员","gdGoodsMoney":3888,"gdGoodsRestrict":1},{"gdGoodsDenomination":1,"gdGoodsName":"代付减免券","gdGoodsState":0,"gdGoodsUseDate":3,"gdGoodsNumber":10000,"gdDate":"2018-12-19 22:45:48","gdGoodsBranchType":5,"gdGoodsType":0,"gdGoodsCode":"993779","gdGoodsUseCondition":100,"gdGoodsExplain":"使用于余额代付提现,减免一元手续费.","gdGoodsMoney":500,"gdGoodsRestrict":100}]
         */

        private String reason;
        private List<ListBean> list;

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
             * gdGoodsDenomination : 0.005
             * gdGoodsName : 境外减免券
             * gdGoodsState : 0
             * gdGoodsUseDate : 3
             * gdGoodsNumber : 10000
             * gdDate : 2018-12-19 23:02:19
             * gdGoodsBranchType : 3
             * gdGoodsType : 0
             * gdGoodsCode : 766685
             * gdGoodsUseCondition : 200
             * gdGoodsExplain : 该商品用于境外交易减免手续费0.5%
             * gdGoodsMoney : 1888
             * gdGoodsRestrict : 100
             */

            public double gdGoodsDenomination;//商品面额
            public String gdGoodsName;//商品名称
            public int gdGoodsState;//商品状态 0:正在发行 1:停止发行 2:已售完
            public int gdGoodsUseDate;//使用期限(天)
            public int gdGoodsNumber;//商品发行数量
            public String gdDate;//商品发行时间
            //'商品分支类型： 虚拟类：0:理财加息券,1:快捷优惠券,2:还款优惠券,3:境外快捷优惠券,4:话费券,5:代付券,6:Mpos优惠券 7:通用返现券
//    8:境外返现券 9:扫码返现券 10:快捷返现券 11:理财返现券 12:还款返现券 13:Mpos返现券 14:金牌会员券 15:钻石会员券
//            实物商品(30起):30:Mpos机
            public int gdGoodsBranchType;
            public int gdGoodsType;//商品类型 0：虚拟商品 1：实物商品
            public String gdGoodsCode;//商品代号
            public int gdGoodsUseCondition;//商品使用条件 交易金额满多少可使用
            public String gdGoodsExplain;//商品说明
            public int gdGoodsMoney;//商品兑换所需摩币
            public int gdGoodsRestrict;//商品每人限兑数量
            public boolean isTitle =false;
            public String titleName;
            public boolean AbNormalGoods = false;

            public ListBean(boolean isTitle,String titleName,boolean AbNormalGoods) {
                this.isTitle = true;
                this.titleName = titleName;
                this.AbNormalGoods = AbNormalGoods;
            }

            public void setAbNormalGoods(boolean abNormalGoods) {
                AbNormalGoods = abNormalGoods;
            }

            protected ListBean(Parcel in) {
                gdGoodsDenomination = in.readDouble();
                gdGoodsName = in.readString();
                gdGoodsState = in.readInt();
                gdGoodsUseDate = in.readInt();
                gdGoodsNumber = in.readInt();
                gdDate = in.readString();
                gdGoodsBranchType = in.readInt();
                gdGoodsType = in.readInt();
                gdGoodsCode = in.readString();
                gdGoodsUseCondition = in.readInt();
                gdGoodsExplain = in.readString();
                gdGoodsMoney = in.readInt();
                gdGoodsRestrict = in.readInt();
                isTitle = in.readByte() != 0;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(gdGoodsDenomination);
                dest.writeString(gdGoodsName);
                dest.writeInt(gdGoodsState);
                dest.writeInt(gdGoodsUseDate);
                dest.writeInt(gdGoodsNumber);
                dest.writeString(gdDate);
                dest.writeInt(gdGoodsBranchType);
                dest.writeInt(gdGoodsType);
                dest.writeString(gdGoodsCode);
                dest.writeInt(gdGoodsUseCondition);
                dest.writeString(gdGoodsExplain);
                dest.writeInt(gdGoodsMoney);
                dest.writeInt(gdGoodsRestrict);
                dest.writeByte((byte) (isTitle ? 1 : 0));
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

            public double getGdGoodsDenomination() {
                return gdGoodsDenomination;
            }

            public void setGdGoodsDenomination(double gdGoodsDenomination) {
                this.gdGoodsDenomination = gdGoodsDenomination;
            }

            public boolean isTitle() {
                return isTitle;
            }

            public void setTitle(boolean title) {
                isTitle = title;
            }

            public String getGdGoodsName() {
                return gdGoodsName;
            }

            public void setGdGoodsName(String gdGoodsName) {
                this.gdGoodsName = gdGoodsName;
            }

            public int getGdGoodsState() {
                return gdGoodsState;
            }

            public void setGdGoodsState(int gdGoodsState) {
                this.gdGoodsState = gdGoodsState;
            }

            public int getGdGoodsUseDate() {
                return gdGoodsUseDate;
            }

            public void setGdGoodsUseDate(int gdGoodsUseDate) {
                this.gdGoodsUseDate = gdGoodsUseDate;
            }

            public int getGdGoodsNumber() {
                return gdGoodsNumber;
            }

            public void setGdGoodsNumber(int gdGoodsNumber) {
                this.gdGoodsNumber = gdGoodsNumber;
            }

            public String getGdDate() {
                return gdDate;
            }

            public void setGdDate(String gdDate) {
                this.gdDate = gdDate;
            }

            public int getGdGoodsBranchType() {
                return gdGoodsBranchType;
            }

            public void setGdGoodsBranchType(int gdGoodsBranchType) {
                this.gdGoodsBranchType = gdGoodsBranchType;
            }

            public int getGdGoodsType() {
                return gdGoodsType;
            }

            public void setGdGoodsType(int gdGoodsType) {
                this.gdGoodsType = gdGoodsType;
            }

            public String getGdGoodsCode() {
                return gdGoodsCode;
            }

            public void setGdGoodsCode(String gdGoodsCode) {
                this.gdGoodsCode = gdGoodsCode;
            }

            public int getGdGoodsUseCondition() {
                return gdGoodsUseCondition;
            }

            public void setGdGoodsUseCondition(int gdGoodsUseCondition) {
                this.gdGoodsUseCondition = gdGoodsUseCondition;
            }

            public String getGdGoodsExplain() {
                return gdGoodsExplain;
            }

            public void setGdGoodsExplain(String gdGoodsExplain) {
                this.gdGoodsExplain = gdGoodsExplain;
            }

            public int getGdGoodsMoney() {
                return gdGoodsMoney;
            }

            public void setGdGoodsMoney(int gdGoodsMoney) {
                this.gdGoodsMoney = gdGoodsMoney;
            }

            public int getGdGoodsRestrict() {
                return gdGoodsRestrict;
            }

            public void setGdGoodsRestrict(int gdGoodsRestrict) {
                this.gdGoodsRestrict = gdGoodsRestrict;
            }

        }
    }
}
