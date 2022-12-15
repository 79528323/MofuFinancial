package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import retrofit2.http.Field;

@DatabaseTable
public class SelfTextInfo{

    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * merSelfText : {"bizAddr":null,"yearSaleMarginsRate":null,"mtJobSectorCdName":"中国共产党机关负责人员","isBizEntit":"N","bizRegDtExpiry":null,"idNo":"440111198801190354","hasCreditCard":"N","dtRegistered":"2019-04-23 00:00:00","mtJobSectorCd":"10100","isBussLongEffec":null,"creditCardLines":null,"mtIndvMobileUsageStsCd":"01","employerPhone":"4008915100","email":"79528323@qq.com","hasCar":"N","employerNm":"摩富金服","mtPosHeldCdName":"银行分行行级干部","createDate":"2019-04-23 13:27:15","mtEduLvlCd":"01","qq":"79528323","bizRegNo":null,"merchantCode":"M20180827144255595","mtMaritalStsCd":"01","ratal":null,"plateNo":null,"officeCode":"S20180816230703671","iiId":2,"mtStateCdName":"北京市","isComb":null,"nationalityMtCtryCd":"国籍固定中国","mobileNo":"13760832152","isFamily":"N","debitCardCode":"6228480082936377819","mtResidenceStsCd":"01","mtCifIdTypCd":"I","mtPosHeldCd":"001","dtIssue":"2019-04-23 00:00:00","dtExpiry":null,"mtCityCd":"110100","isLongEffec":"Y","isLegalRep":null,"weChat":"wechatid","mtStateCd":"110000","yearIncAmt":"200000","currentTotal":null,"mtCityCdName":"北京市","nm":"刘伯达","mtGenderCd":"M"}
     */

    @DatabaseField
    public String result_Msg;
    @DatabaseField
    public int result_Code;
    public MerSelfTextBean merSelfText;
    public MerSpouseTextBean merSpouseText;
    public MerFriendText merFriendText;

    @DatabaseTable(tableName = "merSelfText")
    public static class MerSelfTextBean  implements Parcelable{
        /**
         * bizAddr : null
         * yearSaleMarginsRate : null
         * mtJobSectorCdName : 中国共产党机关负责人员
         * isBizEntit : N
         * bizRegDtExpiry : null
         * idNo : 440111198801190354
         * hasCreditCard : N
         * dtRegistered : 2019-04-23 00:00:00
         * mtJobSectorCd : 10100
         * isBussLongEffec : null
         * creditCardLines : null
         * mtIndvMobileUsageStsCd : 01
         * employerPhone : 4008915100
         * email : 79528323@qq.com
         * hasCar : N
         * employerNm : 摩富金服
         * mtPosHeldCdName : 银行分行行级干部
         * createDate : 2019-04-23 13:27:15
         * mtEduLvlCd : 01
         * qq : 79528323
         * bizRegNo : null
         * merchantCode : M20180827144255595
         * mtMaritalStsCd : 01
         * ratal : null
         * plateNo : null
         * officeCode : S20180816230703671
         * iiId : 2
         * mtStateCdName : 北京市
         * isComb : null
         * nationalityMtCtryCd : 国籍固定中国
         * mobileNo : 13760832152
         * isFamily : N
         * debitCardCode : 6228480082936377819
         * mtResidenceStsCd : 01
         * mtCifIdTypCd : I
         * mtPosHeldCd : 001
         * dtIssue : 2019-04-23 00:00:00
         * dtExpiry : null
         * mtCityCd : 110100
         * isLongEffec : Y
         * isLegalRep : null
         * weChat : wechatid
         * mtStateCd : 110000
         * yearIncAmt : 200000
         * currentTotal : null
         * mtCityCdName : 北京市
         * nm : 刘伯达
         * mtGenderCd : M
         */

        /**
         * UserBean实体类，存储数据库中user表中的数据
         * <p>
         * 注解：
         * DatabaseTable：通过其中的tableName属性指定数据库名称
         * DatabaseField：代表数据表中的一个字段
         * ForeignCollectionField：一对多关联，表示一个UserBean关联着多个ArticleBean（必须使用ForeignCollection集合）
         * <p>
         * 属性：
         * id：当前字段是不是id字段（一个实体类中只能设置一个id字段）
         * columnName：表示当前属性在表中代表哪个字段
         * generatedId：设置属性值在数据表中的数据是否自增
         * useGetSet：是否使用Getter/Setter方法来访问这个字段
         * canBeNull：字段是否可以为空，默认值是true
         * unique：是否唯一
         * defaultValue：设置这个字段的默认值
         */
        @DatabaseField(id = true,columnName = "merselftextbean_id")
        private int merselftextbean_id;
        @DatabaseField
        public String bizAddr;
        @DatabaseField
        public double yearSaleMarginsRate;
        @DatabaseField
        public String mtJobSectorCdName;
        @DatabaseField
        public String isBizEntit;
        @DatabaseField
        public String bizRegDtExpiry;
        @DatabaseField
        public String idNo;
        @DatabaseField
        public String hasCreditCard;
        @DatabaseField
        public String dtRegistered;
        @DatabaseField
        public String mtJobSectorCd;
        @DatabaseField
        public String isBussLongEffec;
        @DatabaseField
        public String creditCardLines;
        @DatabaseField
        public String mtIndvMobileUsageStsCd;
        @DatabaseField
        public String employerPhone;
        @DatabaseField
        public String email;
        @DatabaseField
        public String hasCar;
        @DatabaseField
        public String employerNm;
        @DatabaseField
        public String mtPosHeldCdName;
        @DatabaseField
        public String createDate;
        @DatabaseField
        public String mtEduLvlCd;
        @DatabaseField
        public String qq;
        @DatabaseField
        public String bizRegNo;
        @DatabaseField
        public String merchantCode;
        @DatabaseField
        public String mtMaritalStsCd;
        @DatabaseField
        public double ratal;
        @DatabaseField
        public String plateNo;
        @DatabaseField
        public String officeCode;
        @DatabaseField
        public int iiId;
        @DatabaseField
        public String mtStateCdName;
        @DatabaseField
        public String isComb;
        @DatabaseField
        public String nationalityMtCtryCd;
        @DatabaseField
        public String mobileNo;
        @DatabaseField
        public String isFamily;
        @DatabaseField
        public String debitCardCode;
        @DatabaseField
        public String mtResidenceStsCd;
        @DatabaseField
        public String mtCifIdTypCd;
        @DatabaseField
        public String mtPosHeldCd;
        @DatabaseField
        public String dtIssue;
        @DatabaseField
        public String dtExpiry;
        @DatabaseField
        public String mtCityCd;
        @DatabaseField
        public String isLongEffec;
        @DatabaseField
        public String isLegalRep;
        @DatabaseField
        public String weChat;
        @DatabaseField
        public String mtStateCd;
        @DatabaseField
        public String yearIncAmt;
        @DatabaseField
        public double currentTotal;
        @DatabaseField
        public String mtCityCdName;
        @DatabaseField
        public String nm;
        @DatabaseField
        public String mtGenderCd;

        public MerSelfTextBean() {
        }



        protected MerSelfTextBean(Parcel in) {
            bizAddr = in.readString();
            yearSaleMarginsRate = in.readDouble();
            mtJobSectorCdName = in.readString();
            isBizEntit = in.readString();
            bizRegDtExpiry = in.readString();
            idNo = in.readString();
            hasCreditCard = in.readString();
            dtRegistered = in.readString();
            mtJobSectorCd = in.readString();
            isBussLongEffec = in.readString();
            creditCardLines = in.readString();
            mtIndvMobileUsageStsCd = in.readString();
            employerPhone = in.readString();
            email = in.readString();
            hasCar = in.readString();
            employerNm = in.readString();
            mtPosHeldCdName = in.readString();
            createDate = in.readString();
            mtEduLvlCd = in.readString();
            qq = in.readString();
            bizRegNo = in.readString();
            merchantCode = in.readString();
            mtMaritalStsCd = in.readString();
            ratal = in.readDouble();
            plateNo = in.readString();
            officeCode = in.readString();
            iiId = in.readInt();
            mtStateCdName = in.readString();
            isComb = in.readString();
            nationalityMtCtryCd = in.readString();
            mobileNo = in.readString();
            isFamily = in.readString();
            debitCardCode = in.readString();
            mtResidenceStsCd = in.readString();
            mtCifIdTypCd = in.readString();
            mtPosHeldCd = in.readString();
            dtIssue = in.readString();
            dtExpiry = in.readString();
            mtCityCd = in.readString();
            isLongEffec = in.readString();
            isLegalRep = in.readString();
            weChat = in.readString();
            mtStateCd = in.readString();
            yearIncAmt = in.readString();
            currentTotal = in.readDouble();
            mtCityCdName = in.readString();
            nm = in.readString();
            mtGenderCd = in.readString();
        }

        public static final Creator<MerSelfTextBean> CREATOR = new Creator<MerSelfTextBean>() {
            @Override
            public MerSelfTextBean createFromParcel(Parcel in) {
                return new MerSelfTextBean(in);
            }

            @Override
            public MerSelfTextBean[] newArray(int size) {
                return new MerSelfTextBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bizAddr);
            dest.writeDouble(yearSaleMarginsRate);
            dest.writeString(mtJobSectorCdName);
            dest.writeString(isBizEntit);
            dest.writeString(bizRegDtExpiry);
            dest.writeString(idNo);
            dest.writeString(hasCreditCard);
            dest.writeString(dtRegistered);
            dest.writeString(mtJobSectorCd);
            dest.writeString(isBussLongEffec);
            dest.writeString(creditCardLines);
            dest.writeString(mtIndvMobileUsageStsCd);
            dest.writeString(employerPhone);
            dest.writeString(email);
            dest.writeString(hasCar);
            dest.writeString(employerNm);
            dest.writeString(mtPosHeldCdName);
            dest.writeString(createDate);
            dest.writeString(mtEduLvlCd);
            dest.writeString(qq);
            dest.writeString(bizRegNo);
            dest.writeString(merchantCode);
            dest.writeString(mtMaritalStsCd);
            dest.writeDouble(ratal);
            dest.writeString(plateNo);
            dest.writeString(officeCode);
            dest.writeInt(iiId);
            dest.writeString(mtStateCdName);
            dest.writeString(isComb);
            dest.writeString(nationalityMtCtryCd);
            dest.writeString(mobileNo);
            dest.writeString(isFamily);
            dest.writeString(debitCardCode);
            dest.writeString(mtResidenceStsCd);
            dest.writeString(mtCifIdTypCd);
            dest.writeString(mtPosHeldCd);
            dest.writeString(dtIssue);
            dest.writeString(dtExpiry);
            dest.writeString(mtCityCd);
            dest.writeString(isLongEffec);
            dest.writeString(isLegalRep);
            dest.writeString(weChat);
            dest.writeString(mtStateCd);
            dest.writeString(yearIncAmt);
            dest.writeDouble(currentTotal);
            dest.writeString(mtCityCdName);
            dest.writeString(nm);
            dest.writeString(mtGenderCd);
        }


        public void setMerselftextbean_id(int merselftextbean_id) {
            this.merselftextbean_id = merselftextbean_id;
        }

        public void setBizAddr(String bizAddr) {
            this.bizAddr = bizAddr;
        }

        public void setYearSaleMarginsRate(double yearSaleMarginsRate) {
            this.yearSaleMarginsRate = yearSaleMarginsRate;
        }

        public void setMtJobSectorCdName(String mtJobSectorCdName) {
            this.mtJobSectorCdName = mtJobSectorCdName;
        }

        public void setIsBizEntit(String isBizEntit) {
            this.isBizEntit = isBizEntit;
        }

        public void setBizRegDtExpiry(String bizRegDtExpiry) {
            this.bizRegDtExpiry = bizRegDtExpiry;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public void setHasCreditCard(String hasCreditCard) {
            this.hasCreditCard = hasCreditCard;
        }

        public void setDtRegistered(String dtRegistered) {
            this.dtRegistered = dtRegistered;
        }

        public void setMtJobSectorCd(String mtJobSectorCd) {
            this.mtJobSectorCd = mtJobSectorCd;
        }

        public void setIsBussLongEffec(String isBussLongEffec) {
            this.isBussLongEffec = isBussLongEffec;
        }

        public void setCreditCardLines(String creditCardLines) {
            this.creditCardLines = creditCardLines;
        }

        public void setMtIndvMobileUsageStsCd(String mtIndvMobileUsageStsCd) {
            this.mtIndvMobileUsageStsCd = mtIndvMobileUsageStsCd;
        }

        public void setEmployerPhone(String employerPhone) {
            this.employerPhone = employerPhone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setHasCar(String hasCar) {
            this.hasCar = hasCar;
        }

        public void setEmployerNm(String employerNm) {
            this.employerNm = employerNm;
        }

        public void setMtPosHeldCdName(String mtPosHeldCdName) {
            this.mtPosHeldCdName = mtPosHeldCdName;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setMtEduLvlCd(String mtEduLvlCd) {
            this.mtEduLvlCd = mtEduLvlCd;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public void setBizRegNo(String bizRegNo) {
            this.bizRegNo = bizRegNo;
        }

        public void setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
        }

        public void setMtMaritalStsCd(String mtMaritalStsCd) {
            this.mtMaritalStsCd = mtMaritalStsCd;
        }

        public void setRatal(double ratal) {
            this.ratal = ratal;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public void setOfficeCode(String officeCode) {
            this.officeCode = officeCode;
        }

        public void setIiId(int iiId) {
            this.iiId = iiId;
        }

        public void setMtStateCdName(String mtStateCdName) {
            this.mtStateCdName = mtStateCdName;
        }

        public void setIsComb(String isComb) {
            this.isComb = isComb;
        }

        public void setNationalityMtCtryCd(String nationalityMtCtryCd) {
            this.nationalityMtCtryCd = nationalityMtCtryCd;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public void setIsFamily(String isFamily) {
            this.isFamily = isFamily;
        }

        public void setDebitCardCode(String debitCardCode) {
            this.debitCardCode = debitCardCode;
        }

        public void setMtResidenceStsCd(String mtResidenceStsCd) {
            this.mtResidenceStsCd = mtResidenceStsCd;
        }

        public void setMtCifIdTypCd(String mtCifIdTypCd) {
            this.mtCifIdTypCd = mtCifIdTypCd;
        }

        public void setMtPosHeldCd(String mtPosHeldCd) {
            this.mtPosHeldCd = mtPosHeldCd;
        }

        public void setDtIssue(String dtIssue) {
            this.dtIssue = dtIssue;
        }

        public void setDtExpiry(String dtExpiry) {
            this.dtExpiry = dtExpiry;
        }

        public void setMtCityCd(String mtCityCd) {
            this.mtCityCd = mtCityCd;
        }

        public void setIsLongEffec(String isLongEffec) {
            this.isLongEffec = isLongEffec;
        }

        public void setIsLegalRep(String isLegalRep) {
            this.isLegalRep = isLegalRep;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }

        public void setMtStateCd(String mtStateCd) {
            this.mtStateCd = mtStateCd;
        }

        public void setYearIncAmt(String yearIncAmt) {
            this.yearIncAmt = yearIncAmt;
        }

        public void setCurrentTotal(double currentTotal) {
            this.currentTotal = currentTotal;
        }

        public void setMtCityCdName(String mtCityCdName) {
            this.mtCityCdName = mtCityCdName;
        }

        public void setNm(String nm) {
            this.nm = nm;
        }

        public void setMtGenderCd(String mtGenderCd) {
            this.mtGenderCd = mtGenderCd;
        }
    }

    @DatabaseTable(tableName = "merSpouseText")
    public static class MerSpouseTextBean implements Parcelable {
        /**
         * idNo : 440111198801190354
         * hasCreditCard : N
         * dtRegistered : 2019-04-24 00:00:00
         * mtCifRelCdName : 配偶
         * creditCardLines : null
         * mtIndvMobileUsageStsCd : 02
         * email : 888888@qq.com
         * hasCar : N
         * createDate : 2019-04-24 11:43:53
         * mtEduLvlCd : 01
         * qq : 8888888
         * merchantCode : M20180827144255595
         * mtCifRelCd : II001
         * mtMaritalStsCd : 01
         * plateNo :
         * officeCode : S20180816230703671
         * mtStateCdName : 安徽省
         * nationalityMtCtryCd : 国籍固定中国
         * mobileNo : 15912345678
         * isFamily : N
         * mtResidenceStsCd : 01
         * lsId : 2
         * mtCifIdTypCd : I
         * dtIssue : 2019-04-24 00:00:00
         * dtExpiry : null
         * mtCityCd : 340100
         * isLongEffec : Y
         * weChat : wechat123123
         * mtStateCd : 340000
         * yearIncAmt : 200000
         * mtCityCdName : 合肥市
         * nm : AngelBaby
         * mtGenderCd : F
         */

        @DatabaseField(id = true, columnName = "merSpouseText_id")
        public int id;
        @DatabaseField
        public String idNo;
        @DatabaseField
        public String hasCreditCard;
        @DatabaseField
        public String dtRegistered;
        @DatabaseField
        public String mtCifRelCdName;
        @DatabaseField
        public String creditCardLines;
        @DatabaseField
        public String mtIndvMobileUsageStsCd;
        @DatabaseField
        public String email;
        @DatabaseField
        public String hasCar;
        @DatabaseField
        public String createDate;
        @DatabaseField
        public String mtEduLvlCd;
        @DatabaseField
        public String qq;
        @DatabaseField
        public String merchantCode;
        @DatabaseField
        public String mtCifRelCd;
        @DatabaseField
        public String mtMaritalStsCd;
        @DatabaseField
        public String plateNo;
        @DatabaseField
        public String officeCode;
        @DatabaseField
        public String mtStateCdName;
        @DatabaseField
        public String nationalityMtCtryCd;
        @DatabaseField
        public String mobileNo;
        @DatabaseField
        public String isFamily;
        @DatabaseField
        public String mtResidenceStsCd;
        @DatabaseField
        public int lsId;
        public String mtCifIdTypCd;
        @DatabaseField
        public String dtIssue;
        @DatabaseField
        public String dtExpiry;
        @DatabaseField
        public String mtCityCd;
        @DatabaseField
        public String isLongEffec;
        @DatabaseField
        public String weChat;
        @DatabaseField
        public String mtStateCd;
        @DatabaseField
        public String yearIncAmt;
        @DatabaseField
        public String mtCityCdName;
        @DatabaseField
        public String nm;
        @DatabaseField
        public String mtGenderCd;

        public MerSpouseTextBean() {
        }

        protected MerSpouseTextBean(Parcel in) {
            idNo = in.readString();
            hasCreditCard = in.readString();
            dtRegistered = in.readString();
            mtCifRelCdName = in.readString();
            creditCardLines = in.readString();
            mtIndvMobileUsageStsCd = in.readString();
            email = in.readString();
            hasCar = in.readString();
            createDate = in.readString();
            mtEduLvlCd = in.readString();
            qq = in.readString();
            merchantCode = in.readString();
            mtCifRelCd = in.readString();
            mtMaritalStsCd = in.readString();
            plateNo = in.readString();
            officeCode = in.readString();
            mtStateCdName = in.readString();
            nationalityMtCtryCd = in.readString();
            mobileNo = in.readString();
            isFamily = in.readString();
            mtResidenceStsCd = in.readString();
            lsId = in.readInt();
            mtCifIdTypCd = in.readString();
            dtIssue = in.readString();
            dtExpiry = in.readString();
            mtCityCd = in.readString();
            isLongEffec = in.readString();
            weChat = in.readString();
            mtStateCd = in.readString();
            yearIncAmt = in.readString();
            mtCityCdName = in.readString();
            nm = in.readString();
            mtGenderCd = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(idNo);
            dest.writeString(hasCreditCard);
            dest.writeString(dtRegistered);
            dest.writeString(mtCifRelCdName);
            dest.writeString(creditCardLines);
            dest.writeString(mtIndvMobileUsageStsCd);
            dest.writeString(email);
            dest.writeString(hasCar);
            dest.writeString(createDate);
            dest.writeString(mtEduLvlCd);
            dest.writeString(qq);
            dest.writeString(merchantCode);
            dest.writeString(mtCifRelCd);
            dest.writeString(mtMaritalStsCd);
            dest.writeString(plateNo);
            dest.writeString(officeCode);
            dest.writeString(mtStateCdName);
            dest.writeString(nationalityMtCtryCd);
            dest.writeString(mobileNo);
            dest.writeString(isFamily);
            dest.writeString(mtResidenceStsCd);
            dest.writeInt(lsId);
            dest.writeString(mtCifIdTypCd);
            dest.writeString(dtIssue);
            dest.writeString(dtExpiry);
            dest.writeString(mtCityCd);
            dest.writeString(isLongEffec);
            dest.writeString(weChat);
            dest.writeString(mtStateCd);
            dest.writeString(yearIncAmt);
            dest.writeString(mtCityCdName);
            dest.writeString(nm);
            dest.writeString(mtGenderCd);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MerSpouseTextBean> CREATOR = new Creator<MerSpouseTextBean>() {
            @Override
            public MerSpouseTextBean createFromParcel(Parcel in) {
                return new MerSpouseTextBean(in);
            }

            @Override
            public MerSpouseTextBean[] newArray(int size) {
                return new MerSpouseTextBean[size];
            }
        };

    }

    @DatabaseTable(tableName = "merFriendText")
    public static class MerFriendText implements Parcelable{
        @DatabaseField(id = true, columnName = "merFriendText_id")
        public int id;
        @DatabaseField
        public String mtCifRelCd;
        @DatabaseField
        public String mtCifRelCdName;
        @DatabaseField
        public String nm;
        @DatabaseField
        public String idNo;
        @DatabaseField
        public String mtGenderCd;
        @DatabaseField
        public String mobileNo;
        @DatabaseField
        public String mtIncSourceCd;
        @DatabaseField
        public String mtIncSourceCdName;
        @DatabaseField
        public String yearIncAmt;

        public MerFriendText() {}

        protected MerFriendText(Parcel in) {
            mtCifRelCd = in.readString();
            mtCifRelCdName = in.readString();
            nm = in.readString();
            idNo = in.readString();
            mtGenderCd = in.readString();
            mobileNo = in.readString();
            mtIncSourceCd = in.readString();
            mtIncSourceCdName = in.readString();
            yearIncAmt = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mtCifRelCd);
            dest.writeString(mtCifRelCdName);
            dest.writeString(nm);
            dest.writeString(idNo);
            dest.writeString(mtGenderCd);
            dest.writeString(mobileNo);
            dest.writeString(mtIncSourceCd);
            dest.writeString(mtIncSourceCdName);
            dest.writeString(yearIncAmt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MerFriendText> CREATOR = new Creator<MerFriendText>() {
            @Override
            public MerFriendText createFromParcel(Parcel in) {
                return new MerFriendText(in);
            }

            @Override
            public MerFriendText[] newArray(int size) {
                return new MerFriendText[size];
            }
        };
    }

}
