package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SelfImgInfo implements Parcelable{


    /**
     * result_Msg : 查询成功
     * merSelfImg : {"piId":2,"imgWholePath":"D:\\ProgramFiles\\apache-tomcat-8.0.52\\webapps\\JFBank\\upload/loans/self/M20180827144255595/","incomeProve":"incomeProve.jpg","merchantCode":"M20180827144255595","creditSurvey":"creditSurvey.jpg","officeCode":"S20180816230703671","cardZ":"cardZ.jpg","identityF":"identityF.jpg","propertyProve":"propertyProve.jpg","identityS":"identityS.jpg","cardF":"cardF.jpg","identityZ":"identityZ.jpg","bankWater":"bankWater.jpg","imgUploadPath":"upload/loans/self/M20180827144255595","createDate":"2019-04-24 09:04:21"}
     * result_Code : 0
     */

    public String result_Msg;
    public MerSelfImgBean merSelfImg;
    public MerSpouseImg merSpouseImg;
    public MerSelfPrivateImg merSelfPrivateImg;
    public MerBusinessImg merBusinessImg;
    public int result_Code;

    protected SelfImgInfo(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelfImgInfo> CREATOR = new Creator<SelfImgInfo>() {
        @Override
        public SelfImgInfo createFromParcel(Parcel in) {
            return new SelfImgInfo(in);
        }

        @Override
        public SelfImgInfo[] newArray(int size) {
            return new SelfImgInfo[size];
        }
    };

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public MerSelfImgBean getMerSelfImg() {
        return merSelfImg;
    }

    public void setMerSelfImg(MerSelfImgBean merSelfImg) {
        this.merSelfImg = merSelfImg;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public static class MerSelfImgBean implements Parcelable{

        public int piId;
        public String imgWholePath;
        public String incomeProve;
        public String merchantCode;
        public String creditSurvey;
        public String officeCode;
        public String cardZ;
        public String identityF;
        public String propertyProve;
        public String identityS;
        public String cardF;
        public String identityZ;
        public String bankWater;
        public String imgUploadPath;
        public String createDate;

        protected MerSelfImgBean(Parcel in) {
            piId = in.readInt();
            imgWholePath = in.readString();
            incomeProve = in.readString();
            merchantCode = in.readString();
            creditSurvey = in.readString();
            officeCode = in.readString();
            cardZ = in.readString();
            identityF = in.readString();
            propertyProve = in.readString();
            identityS = in.readString();
            cardF = in.readString();
            identityZ = in.readString();
            bankWater = in.readString();
            imgUploadPath = in.readString();
            createDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(piId);
            dest.writeString(imgWholePath);
            dest.writeString(incomeProve);
            dest.writeString(merchantCode);
            dest.writeString(creditSurvey);
            dest.writeString(officeCode);
            dest.writeString(cardZ);
            dest.writeString(identityF);
            dest.writeString(propertyProve);
            dest.writeString(identityS);
            dest.writeString(cardF);
            dest.writeString(identityZ);
            dest.writeString(bankWater);
            dest.writeString(imgUploadPath);
            dest.writeString(createDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MerSelfImgBean> CREATOR = new Creator<MerSelfImgBean>() {
            @Override
            public MerSelfImgBean createFromParcel(Parcel in) {
                return new MerSelfImgBean(in);
            }

            @Override
            public MerSelfImgBean[] newArray(int size) {
                return new MerSelfImgBean[size];
            }
        };

        public int getPiId() {
            return piId;
        }

        public void setPiId(int piId) {
            this.piId = piId;
        }

        public String getImgWholePath() {
            return imgWholePath;
        }

        public void setImgWholePath(String imgWholePath) {
            this.imgWholePath = imgWholePath;
        }

        public String getIncomeProve() {
            return incomeProve;
        }

        public void setIncomeProve(String incomeProve) {
            this.incomeProve = incomeProve;
        }

        public String getMerchantCode() {
            return merchantCode;
        }

        public void setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
        }

        public String getCreditSurvey() {
            return creditSurvey;
        }

        public void setCreditSurvey(String creditSurvey) {
            this.creditSurvey = creditSurvey;
        }

        public String getOfficeCode() {
            return officeCode;
        }

        public void setOfficeCode(String officeCode) {
            this.officeCode = officeCode;
        }

        public String getCardZ() {
            return cardZ;
        }

        public void setCardZ(String cardZ) {
            this.cardZ = cardZ;
        }

        public String getIdentityF() {
            return identityF;
        }

        public void setIdentityF(String identityF) {
            this.identityF = identityF;
        }

        public String getPropertyProve() {
            return propertyProve;
        }

        public void setPropertyProve(String propertyProve) {
            this.propertyProve = propertyProve;
        }

        public String getIdentityS() {
            return identityS;
        }

        public void setIdentityS(String identityS) {
            this.identityS = identityS;
        }

        public String getCardF() {
            return cardF;
        }

        public void setCardF(String cardF) {
            this.cardF = cardF;
        }

        public String getIdentityZ() {
            return identityZ;
        }

        public void setIdentityZ(String identityZ) {
            this.identityZ = identityZ;
        }

        public String getBankWater() {
            return bankWater;
        }

        public void setBankWater(String bankWater) {
            this.bankWater = bankWater;
        }

        public String getImgUploadPath() {
            return imgUploadPath;
        }

        public void setImgUploadPath(String imgUploadPath) {
            this.imgUploadPath = imgUploadPath;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }


    public static class MerSpouseImg implements Parcelable{
        public String imgUploadPath;
        public String identityZ;
        public String identityF;
        public String creditSurvey;
        public String incomeProve;

        protected MerSpouseImg(Parcel in) {
            imgUploadPath = in.readString();
            identityZ = in.readString();
            identityF = in.readString();
            creditSurvey = in.readString();
            incomeProve = in.readString();
        }

        public static final Creator<MerSpouseImg> CREATOR = new Creator<MerSpouseImg>() {
            @Override
            public MerSpouseImg createFromParcel(Parcel in) {
                return new MerSpouseImg(in);
            }

            @Override
            public MerSpouseImg[] newArray(int size) {
                return new MerSpouseImg[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imgUploadPath);
            dest.writeString(identityZ);
            dest.writeString(identityF);
            dest.writeString(creditSurvey);
            dest.writeString(incomeProve);
        }
    }



    public static class MerSelfPrivateImg implements Parcelable{
        public String imgUploadPath;
        public String businessLicense;
        public String articles;
        public String capitlReport;
        public String site;
        public String bankSingle;


        protected MerSelfPrivateImg(Parcel in) {
            imgUploadPath = in.readString();
            businessLicense = in.readString();
            articles = in.readString();
            capitlReport = in.readString();
            site = in.readString();
            bankSingle = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imgUploadPath);
            dest.writeString(businessLicense);
            dest.writeString(articles);
            dest.writeString(capitlReport);
            dest.writeString(site);
            dest.writeString(bankSingle);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MerSelfPrivateImg> CREATOR = new Creator<MerSelfPrivateImg>() {
            @Override
            public MerSelfPrivateImg createFromParcel(Parcel in) {
                return new MerSelfPrivateImg(in);
            }

            @Override
            public MerSelfPrivateImg[] newArray(int size) {
                return new MerSelfPrivateImg[size];
            }
        };
    }


    public static class MerBusinessImg implements Parcelable{
        public String imgUploadPath;
        public String loansUseProve;
        public String refundSource;
        public String pact;

        protected MerBusinessImg(Parcel in) {
            imgUploadPath = in.readString();
            loansUseProve = in.readString();
            refundSource = in.readString();
            pact = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imgUploadPath);
            dest.writeString(loansUseProve);
            dest.writeString(refundSource);
            dest.writeString(pact);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MerBusinessImg> CREATOR = new Creator<MerBusinessImg>() {
            @Override
            public MerBusinessImg createFromParcel(Parcel in) {
                return new MerBusinessImg(in);
            }

            @Override
            public MerBusinessImg[] newArray(int size) {
                return new MerBusinessImg[size];
            }
        };
    }
}
