package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseResult implements Parcelable{
    /**
     * result_Msg : 查询成功
     * vCreateDate : 2018-09-07 09:56:53
     * vName : app-debug.apk
     * vOfficeCode : S20180816230703671
     * result_Code : 0
     * vCode : 1.0
     * vPath : http://120.78.213.181/upload/officeapk/
     */

    public String result_Msg;
    public int result_Code;
    public String qrcodeUrl;
    public String url;
    public String sign;

    //贷款 到账银行卡
    public String debitCardCode;

    //查询用户借贷协议
    public String content;

    //TODO 理财回调
    public String fdName; //产品名称
    public String foProductBuyDate;//产品购买时间
    public double foBuyMoney;//产品购买金额
    public double fdAnnualized;//年率化
    public int fdProductDate;//投资期限(天)

    public int channelKine;//"通道种类：0:h5 1:扫码 根据此参数来决定将上方url如何打开"

    protected BaseResult(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
        qrcodeUrl = in.readString();
        url = in.readString();
        sign = in.readString();
        fdName = in.readString();
        foProductBuyDate = in.readString();
        foBuyMoney = in.readDouble();
        fdAnnualized = in.readDouble();
        fdProductDate = in.readInt();
        channelKine = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
        dest.writeString(qrcodeUrl);
        dest.writeString(url);
        dest.writeString(sign);
        dest.writeString(fdName);
        dest.writeString(foProductBuyDate);
        dest.writeDouble(foBuyMoney);
        dest.writeDouble(fdAnnualized);
        dest.writeInt(fdProductDate);
        dest.writeInt(channelKine);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseResult> CREATOR = new Creator<BaseResult>() {
        @Override
        public BaseResult createFromParcel(Parcel in) {
            return new BaseResult(in);
        }

        @Override
        public BaseResult[] newArray(int size) {
            return new BaseResult[size];
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


}
