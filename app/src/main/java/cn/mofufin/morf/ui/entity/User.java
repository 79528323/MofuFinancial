package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * author：created by Liubd on 2018/8/31 10:52
 * e-mail:79528323@qq.com
 */
public class User implements Parcelable {
    public final static int ORDINARY_MEMBER=1;
    public final static int GOLDEN_MEMBER=2;
    public final static int DIAMOND_MEMBER=3;
    private DataBean data;
    private boolean bool;
    private int stateCode;
    private String message;

    protected User(Parcel in) {
        bool = in.readByte() != 0;
        stateCode = in.readInt();
        message = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (bool ? 1 : 0));
        parcel.writeInt(stateCode);
        parcel.writeString(message);
    }

    public static class DataBean implements Parcelable{
        /**
         * reason : 登录成功
         * cardInfo : [{"repaymentDay":null,"accountDay":null,"cardPhone":null,"cardBankName":"邮政储蓄银行","cardDef":1,"cardCode":"6217995590002256049","cardReDate":null,"cardBackNo":null,"cardType":1}]
         * merchantInfo : {"merchantCode":"M20180808161435320","merchantImg":null,"indirectNum":0,"RMB":0,"fdMerIdentityCardName":"杜丰铭","agentName":null,"merchantState":0,"merchantName":"高大上","realName":2,"merchantPhone":"13927450890","merchantId":26,"mposFeedPart":4,"directlyNum":5,"memberType":3,"USDollar":0,"email":"dufengming2008@qq.com","HKDollar":0}
         */
        private String reason;
        private MerchantInfoBean merchantInfo;
        private List<CardInfoBean> cardInfo;

        protected DataBean(Parcel in) {
            reason = in.readString();
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

        public MerchantInfoBean getMerchantInfo() {
            return merchantInfo;
        }

        public void setMerchantInfo(MerchantInfoBean merchantInfo) {
            this.merchantInfo = merchantInfo;
        }

        public List<CardInfoBean> getCardInfo() {
            return cardInfo;
        }

        public void setCardInfo(List<CardInfoBean> cardInfo) {
            this.cardInfo = cardInfo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(reason);
        }

        @DatabaseTable
        public static class MerchantInfoBean implements Parcelable{

                    /**
                     * "merchantCode": "M20180827144255595",
                    "merchantImg": null,
                    "indirectNum": 0,
                    "RMB": 0.0,
                    "remitPayFeedPart": 1,
                    "fdMerIdentityCardName": "刘伯达",
                    "agentName": "谷云华",
                    "merchantState": 0,
                    "commssionMoney": 0.0,
                    "merchantName": "维他奶",
                    "lastLoginIP": "119.129.225.5",
                    "lastEnterDate": "2018-10-30 09:26:56",
                    "realName": 1,
                    "enterDate": "2018-10-30 10:49:42",
                    "merchantPhone": "13760832152",
                    "currentLoginIP": "119.129.225.5",
                    "merchantId": 65,
                    "mposFeedPart": 1,
                    "directlyNum": 3,
                    "memberType": 3,
                    "USDollar": 0.0,
                    "email": null,
                    "HKDollar": 0.0*/

            @DatabaseField
            public int remitPayFeedPart;//是否开通商户扫码
            @DatabaseField
            public String lastLoginIP;
            @DatabaseField
            public String lastEnterDate;
            @DatabaseField
            public String enterDate;
            @DatabaseField
            public String currentLoginIP;
            @DatabaseField
            public String merchantCode;//编号
            @DatabaseField
            public String merchantImg;//商户二维码
            @DatabaseField
            public int indirectNum;//间推人数
            @DatabaseField
            public double RMB;//人民币,主要用作余额
            @DatabaseField
            public String fdMerIdentityCardName;
            @DatabaseField
            public String agentName;
            @DatabaseField
            public int merchantState;//商户状态
            @DatabaseField
            public double commssionMoney;//返佣
            @DatabaseField
            public String merchantName;//商户名称
            @DatabaseField
            public int realName;//是否实名 0:待审核 1:审核通过 2:审核未通过 3:未上传认证信息
            @DatabaseField
            public String merchantPhone;//手机号
            @DatabaseField
            public int merchantId;
            @DatabaseField
            public int mposFeedPart;//0:进件失败 1:进件成功 2:申请进件 3:正在审核 4:未进件
            @DatabaseField
            public int directlyNum;//直推人数
            @DatabaseField
            public int memberType;//会员类型:1、普通会员 2、黄金会员 3、钻石会员
            @DatabaseField
            public double USDollar;
            @DatabaseField
            public String email;
            @DatabaseField
            public double HKDollar;
            public int rubCurrenty;//摩币
            public int treeNumber;//踹树次数
            public int ensureMoeny;//保证金账户
            public int ensureMoneyPlanState;//保证金计划状态：0未参与 1正在参与 2已完成
            public int ensureMoneyPlanPhase;//保证金计划阶段：0:未进行任何阶段,1:首月阶段,2:中月阶段,3:末月阶段,4:已完成所有阶段
            public double lockMoney;//锁定资金
            public int resetPhoneNumber;//重围账户次数
            public double cardMoney;//刷卡金
            public String merchantDateTime;//注册时间

            public MerchantInfoBean() {

            }

            public String getMerchantDateTime() {
                return merchantDateTime;
            }

            public void setMerchantDateTime(String merchantDateTime) {
                this.merchantDateTime = merchantDateTime;
            }

            public int getRemitPayFeedPart() {
                return remitPayFeedPart;
            }

            public void setRemitPayFeedPart(int remitPayFeedPart) {
                this.remitPayFeedPart = remitPayFeedPart;
            }

            public String getLastLoginIP() {
                return lastLoginIP;
            }

            public void setLastLoginIP(String lastLoginIP) {
                this.lastLoginIP = lastLoginIP;
            }

            public String getLastEnterDate() {
                return lastEnterDate;
            }

            public void setLastEnterDate(String lastEnterDate) {
                this.lastEnterDate = lastEnterDate;
            }

            public String getEnterDate() {
                return enterDate;
            }

            public void setEnterDate(String enterDate) {
                this.enterDate = enterDate;
            }

            public String getCurrentLoginIP() {
                return currentLoginIP;
            }

            public void setCurrentLoginIP(String currentLoginIP) {
                this.currentLoginIP = currentLoginIP;
            }

            protected MerchantInfoBean(Parcel in) {
                merchantCode = in.readString();
                merchantImg = in.readString();
                indirectNum = in.readInt();
                RMB = in.readDouble();
                fdMerIdentityCardName = in.readString();
                agentName = in.readString();
                merchantState = in.readInt();
                commssionMoney = in.readDouble();
                merchantName = in.readString();
                realName = in.readInt();
                merchantPhone = in.readString();
                merchantId = in.readInt();
                mposFeedPart = in.readInt();
                directlyNum = in.readInt();
                memberType = in.readInt();
                USDollar = in.readDouble();
                email = in.readString();
                HKDollar = in.readDouble();
                remitPayFeedPart = in.readInt();
                currentLoginIP = in.readString();
                lastEnterDate = in.readString();
                enterDate = in.readString();
                lastLoginIP = in.readString();
                rubCurrenty = in.readInt();
                treeNumber = in.readInt();
                ensureMoeny = in.readInt();
                ensureMoneyPlanState = in.readInt();
                ensureMoneyPlanPhase = in.readInt();
                lockMoney = in.readDouble();
                resetPhoneNumber = in.readInt();
                cardMoney = in.readDouble();
                merchantDateTime = in.readString();
            }

            public static final Creator<MerchantInfoBean> CREATOR = new Creator<MerchantInfoBean>() {
                @Override
                public MerchantInfoBean createFromParcel(Parcel in) {
                    return new MerchantInfoBean(in);
                }

                @Override
                public MerchantInfoBean[] newArray(int size) {
                    return new MerchantInfoBean[size];
                }
            };

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
            }

            public String getMerchantImg() {
                return merchantImg;
            }

            public void setMerchantImg(String merchantImg) {
                this.merchantImg = merchantImg;
            }

            public int getIndirectNum() {
                return indirectNum;
            }

            public void setIndirectNum(int indirectNum) {
                this.indirectNum = indirectNum;
            }

            public double getRMB() {
                return RMB;
            }

            public void setRMB(double RMB) {
                this.RMB = RMB;
            }

            public String getFdMerIdentityCardName() {
                return fdMerIdentityCardName;
            }

            public void setFdMerIdentityCardName(String fdMerIdentityCardName) {
                this.fdMerIdentityCardName = fdMerIdentityCardName;
            }

            public String getAgentName() {
                return agentName;
            }

            public void setAgentName(String agentName) {
                this.agentName = agentName;
            }

            public int getMerchantState() {
                return merchantState;
            }

            public void setMerchantState(int merchantState) {
                this.merchantState = merchantState;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public int getRealName() {
                return realName;
            }

            public void setRealName(int realName) {
                this.realName = realName;
            }

            public String getMerchantPhone() {
                return merchantPhone;
            }

            public void setMerchantPhone(String merchantPhone) {
                this.merchantPhone = merchantPhone;
            }

            public int getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(int merchantId) {
                this.merchantId = merchantId;
            }

            public int getMposFeedPart() {
                return mposFeedPart;
            }

            public void setMposFeedPart(int mposFeedPart) {
                this.mposFeedPart = mposFeedPart;
            }

            public int getDirectlyNum() {
                return directlyNum;
            }

            public void setDirectlyNum(int directlyNum) {
                this.directlyNum = directlyNum;
            }

            public int getMemberType() {
                return memberType;
            }

            public void setMemberType(int memberType) {
                this.memberType = memberType;
            }

            public double getUSDollar() {
                return USDollar;
            }

            public void setUSDollar(double USDollar) {
                this.USDollar = USDollar;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public double getHKDollar() {
                return HKDollar;
            }

            public void setHKDollar(double HKDollar) {
                this.HKDollar = HKDollar;
            }

            public double getCommssionMoney() {
                return commssionMoney;
            }

            public void setCommssionMoney(double commssionMoney) {
                this.commssionMoney = commssionMoney;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(merchantCode);
                parcel.writeInt(indirectNum);
                parcel.writeDouble(RMB);
                parcel.writeString(fdMerIdentityCardName);
                parcel.writeInt(merchantState);
                parcel.writeDouble(commssionMoney);
                parcel.writeString(merchantName);
                parcel.writeInt(realName);
                parcel.writeString(merchantPhone);
                parcel.writeInt(merchantId);
                parcel.writeInt(mposFeedPart);
                parcel.writeInt(directlyNum);
                parcel.writeInt(memberType);
                parcel.writeDouble(USDollar);
                parcel.writeString(email);
                parcel.writeDouble(HKDollar);
                parcel.writeString(currentLoginIP);
                parcel.writeString(lastEnterDate);
                parcel.writeString(lastLoginIP);
                parcel.writeString(enterDate);
                parcel.writeInt(remitPayFeedPart);
                parcel.writeInt(rubCurrenty);
                parcel.writeInt(treeNumber);
                parcel.writeInt(ensureMoeny);
                parcel.writeInt(ensureMoneyPlanState);
                parcel.writeInt(ensureMoneyPlanPhase);
                parcel.writeDouble(lockMoney);
                parcel.writeInt(resetPhoneNumber);
                parcel.writeDouble(cardMoney);
                parcel.writeString(merchantDateTime);
            }
        }

        @DatabaseTable
        public static class CardInfoBean implements Parcelable{
            /**
             * repaymentDay : null
             * accountDay : null
             * cardPhone : null
             * cardBankName : 邮政储蓄银行
             * cardDef : 1
             * cardCode : 6217995590002256049
             * cardReDate : null
             * cardBackNo : null
             * cardType : 1
             */
            @DatabaseField
            public String repaymentDay;
            @DatabaseField
            public String accountDay;
            @DatabaseField
            public String cardPhone;
            @DatabaseField
            public String cardBankName;
            @DatabaseField
            public int cardDef;//是否默认
            @DatabaseField
            public String cardCode;
            @DatabaseField
            public String cardReDate;
            @DatabaseField
            public String cardBackNo;
            @DatabaseField
            public int cardType;//卡类型：1、储蓄卡 2、信用卡

            public CardInfoBean() {
            }

            /**
             * "merCode": "M20180827144255595",
             "cardBankName": "平安银行",
             "cardCode": "6230580000191515449",
             "cardType": 1,
             "bankArea": "罗湖区",
             "bankCity": "深圳市",
             "number": "307584099984",
             "repaymentDay": "8",
             "accountDay": "20",
             "cardPhone": "13760832152",
             "cardDef": 1,
             "cardId": 47,
             "cardReDate": "0421",
             "cardBackNo": "180",
             "bankProvince": "广东省",
             "branchname": "平安银行股份有限公司"
             * @param in
             */


            protected CardInfoBean(Parcel in) {
                repaymentDay = in.readString();
                accountDay = in.readString();
                cardPhone = in.readString();
                cardBankName = in.readString();
                cardDef = in.readInt();
                cardCode = in.readString();
                cardReDate = in.readString();
                cardBackNo = in.readString();
                cardType = in.readInt();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(repaymentDay);
                dest.writeString(accountDay);
                dest.writeString(cardPhone);
                dest.writeString(cardBankName);
                dest.writeInt(cardDef);
                dest.writeString(cardCode);
                dest.writeString(cardReDate);
                dest.writeString(cardBackNo);
                dest.writeInt(cardType);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<CardInfoBean> CREATOR = new Creator<CardInfoBean>() {
                @Override
                public CardInfoBean createFromParcel(Parcel in) {
                    return new CardInfoBean(in);
                }

                @Override
                public CardInfoBean[] newArray(int size) {
                    return new CardInfoBean[size];
                }
            };
        }
    }
}
