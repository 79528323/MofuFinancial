package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductDetails implements Parcelable {


    /**
     * result_Msg : 查询成功
     * recordList : []
     * result_Code : 0
     * productDetails : {"fdMinMoney":500,"fdAnnualized":0.058,"fdNumber":130678,"fdRiserestDateMsg":"15点以前购买当日计息，15点以后购买次日计息","fdTotalCirculation":1.0E9,"fdExpirreMsg":"本息退还余额","fdUserBuyNumber":100000,"fdProductCost":"购买该产品用户不得随意退出。锁定期满后，收益按照完成时摩富金服平台对应产品承诺预期收益率计算。 购买该产品的用户合同到期或中途申请退出后，平台帮助用户发起相关债权转让，债权全部转让成功即为完全退出。 ","fdProductDate":15,"fdIsRebateProduct":0,"fdResidueCirculation":0,"fdProductRisk":"摩富金服建立贷前调查、贷中审查、贷后管理等严格风控流程，将风险控制渗透到业务流程的各个环节。前中后层层把关，降低投资风险，让您投资更安心。","fdRebateRatio":1.0E-4,"fdName":"摩富801501","fdProductIntroduce":"摩富金服是知名聚合支付平台立码付旗下的理财平台，致力于为用户带来优质的移动理财投资体验。依托立码付平台强大的金融底蕴和高效的风控数据，摩富金服为用户提供更安全、更便捷、更省心的移动理财服务。","fdProductSupervise":"是摩富金服平台主动倡导，由投资人积极发起并自愿成立、实行自我管理的非营利性群众组织。旨在通过对摩富金服平台的产品、服务等实施有效监督、对借款标的进行现场检查等措施，推动平台信息公开透明、平台运营主体合法合规诚信经营，从而最终达到保护投资人合法权益的目的。"}
     */

    public String result_Msg;
    public int result_Code;
    public ProductDetailsBean productDetails;
    public List<RecordListBean> recordList;

    protected ProductDetails(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public ProductDetailsBean getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetailsBean productDetails) {
        this.productDetails = productDetails;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
    }

    public static class RecordListBean implements Parcelable{
        /**
         * foBuyMoney : 30000
         * merchantPhone : 13760832152
         * foProductBuyDate : 2019-01-23 16:48:25
         */

        public double foBuyMoney;
        public String merchantPhone;
        public String foProductBuyDate;

        protected RecordListBean(Parcel in) {
            foBuyMoney = in.readDouble();
            merchantPhone = in.readString();
            foProductBuyDate = in.readString();
        }

        public static final Creator<RecordListBean> CREATOR = new Creator<RecordListBean>() {
            @Override
            public RecordListBean createFromParcel(Parcel in) {
                return new RecordListBean(in);
            }

            @Override
            public RecordListBean[] newArray(int size) {
                return new RecordListBean[size];
            }
        };

//        public int getFoBuyMoney() {
//            return foBuyMoney;
//        }

        public void setFoBuyMoney(int foBuyMoney) {
            this.foBuyMoney = foBuyMoney;
        }

        public String getMerchantPhone() {
            return merchantPhone;
        }

        public void setMerchantPhone(String merchantPhone) {
            this.merchantPhone = merchantPhone;
        }

        public String getFoProductBuyDate() {
            return foProductBuyDate;
        }

        public void setFoProductBuyDate(String foProductBuyDate) {
            this.foProductBuyDate = foProductBuyDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(foBuyMoney);
            dest.writeString(merchantPhone);
            dest.writeString(foProductBuyDate);
        }
    }

    public static class ProductDetailsBean implements Parcelable{
        /**
         * fdMinMoney : 500
         * fdAnnualized : 0.058
         * fdNumber : 130678
         * fdRiserestDateMsg : 15点以前购买当日计息，15点以后购买次日计息
         * fdTotalCirculation : 1.0E9
         * fdExpirreMsg : 本息退还余额
         * fdUserBuyNumber : 100000
         * fdProductCost : 购买该产品用户不得随意退出。锁定期满后，收益按照完成时摩富金服平台对应产品承诺预期收益率计算。 购买该产品的用户合同到期或中途申请退出后，平台帮助用户发起相关债权转让，债权全部转让成功即为完全退出。
         * fdProductDate : 15
         * fdIsRebateProduct : 0
         * fdResidueCirculation : 0
         * fdProductRisk : 摩富金服建立贷前调查、贷中审查、贷后管理等严格风控流程，将风险控制渗透到业务流程的各个环节。前中后层层把关，降低投资风险，让您投资更安心。
         * fdRebateRatio : 1.0E-4
         * fdName : 摩富801501
         * fdProductIntroduce : 摩富金服是知名聚合支付平台立码付旗下的理财平台，致力于为用户带来优质的移动理财投资体验。依托立码付平台强大的金融底蕴和高效的风控数据，摩富金服为用户提供更安全、更便捷、更省心的移动理财服务。
         * fdProductSupervise : 是摩富金服平台主动倡导，由投资人积极发起并自愿成立、实行自我管理的非营利性群众组织。旨在通过对摩富金服平台的产品、服务等实施有效监督、对借款标的进行现场检查等措施，推动平台信息公开透明、平台运营主体合法合规诚信经营，从而最终达到保护投资人合法权益的目的。
         */

        public int fdMinMoney;
        public double fdAnnualized;
        public int fdNumber;
        public String fdRiserestDateMsg;
        public double fdTotalCirculation;
        public String fdExpirreMsg;
        public int fdUserBuyNumber;
        public String fdProductCost;
        public int fdProductDate;
        public int fdIsRebateProduct;
        public double fdResidueCirculation;
        public String fdProductRisk;
        public double fdRebateRatio;
        public String fdName;
        public String fdProductIntroduce;
        public String fdProductSupervise;

        protected ProductDetailsBean(Parcel in) {
            fdMinMoney = in.readInt();
            fdAnnualized = in.readDouble();
            fdNumber = in.readInt();
            fdRiserestDateMsg = in.readString();
            fdTotalCirculation = in.readDouble();
            fdExpirreMsg = in.readString();
            fdUserBuyNumber = in.readInt();
            fdProductCost = in.readString();
            fdProductDate = in.readInt();
            fdIsRebateProduct = in.readInt();
            fdResidueCirculation = in.readDouble();
            fdProductRisk = in.readString();
            fdRebateRatio = in.readDouble();
            fdName = in.readString();
            fdProductIntroduce = in.readString();
            fdProductSupervise = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(fdMinMoney);
            dest.writeDouble(fdAnnualized);
            dest.writeInt(fdNumber);
            dest.writeString(fdRiserestDateMsg);
            dest.writeDouble(fdTotalCirculation);
            dest.writeString(fdExpirreMsg);
            dest.writeInt(fdUserBuyNumber);
            dest.writeString(fdProductCost);
            dest.writeInt(fdProductDate);
            dest.writeInt(fdIsRebateProduct);
            dest.writeDouble(fdResidueCirculation);
            dest.writeString(fdProductRisk);
            dest.writeDouble(fdRebateRatio);
            dest.writeString(fdName);
            dest.writeString(fdProductIntroduce);
            dest.writeString(fdProductSupervise);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ProductDetailsBean> CREATOR = new Creator<ProductDetailsBean>() {
            @Override
            public ProductDetailsBean createFromParcel(Parcel in) {
                return new ProductDetailsBean(in);
            }

            @Override
            public ProductDetailsBean[] newArray(int size) {
                return new ProductDetailsBean[size];
            }
        };

        public int getFdMinMoney() {
            return fdMinMoney;
        }

        public void setFdMinMoney(int fdMinMoney) {
            this.fdMinMoney = fdMinMoney;
        }

        public double getFdAnnualized() {
            return fdAnnualized;
        }

        public void setFdAnnualized(double fdAnnualized) {
            this.fdAnnualized = fdAnnualized;
        }

        public int getFdNumber() {
            return fdNumber;
        }

        public void setFdNumber(int fdNumber) {
            this.fdNumber = fdNumber;
        }

        public String getFdRiserestDateMsg() {
            return fdRiserestDateMsg;
        }

        public void setFdRiserestDateMsg(String fdRiserestDateMsg) {
            this.fdRiserestDateMsg = fdRiserestDateMsg;
        }

        public double getFdTotalCirculation() {
            return fdTotalCirculation;
        }

        public void setFdTotalCirculation(double fdTotalCirculation) {
            this.fdTotalCirculation = fdTotalCirculation;
        }

        public String getFdExpirreMsg() {
            return fdExpirreMsg;
        }

        public void setFdExpirreMsg(String fdExpirreMsg) {
            this.fdExpirreMsg = fdExpirreMsg;
        }

        public int getFdUserBuyNumber() {
            return fdUserBuyNumber;
        }

        public void setFdUserBuyNumber(int fdUserBuyNumber) {
            this.fdUserBuyNumber = fdUserBuyNumber;
        }

        public String getFdProductCost() {
            return fdProductCost;
        }

        public void setFdProductCost(String fdProductCost) {
            this.fdProductCost = fdProductCost;
        }

        public int getFdProductDate() {
            return fdProductDate;
        }

        public void setFdProductDate(int fdProductDate) {
            this.fdProductDate = fdProductDate;
        }

        public int getFdIsRebateProduct() {
            return fdIsRebateProduct;
        }

        public void setFdIsRebateProduct(int fdIsRebateProduct) {
            this.fdIsRebateProduct = fdIsRebateProduct;
        }

//        public int getFdResidueCirculation() {
//            return fdResidueCirculation;
//        }

//        public void setFdResidueCirculation(int fdResidueCirculation) {
//            this.fdResidueCirculation = fdResidueCirculation;
//        }

        public String getFdProductRisk() {
            return fdProductRisk;
        }

        public void setFdProductRisk(String fdProductRisk) {
            this.fdProductRisk = fdProductRisk;
        }

        public double getFdRebateRatio() {
            return fdRebateRatio;
        }

        public void setFdRebateRatio(double fdRebateRatio) {
            this.fdRebateRatio = fdRebateRatio;
        }

        public String getFdName() {
            return fdName;
        }

        public void setFdName(String fdName) {
            this.fdName = fdName;
        }

        public String getFdProductIntroduce() {
            return fdProductIntroduce;
        }

        public void setFdProductIntroduce(String fdProductIntroduce) {
            this.fdProductIntroduce = fdProductIntroduce;
        }

        public String getFdProductSupervise() {
            return fdProductSupervise;
        }

        public void setFdProductSupervise(String fdProductSupervise) {
            this.fdProductSupervise = fdProductSupervise;
        }
    }
}
