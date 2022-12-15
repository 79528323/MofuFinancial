package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BalanceDetail implements Parcelable{

    public String result_Msg;
    public int result_Code;
    public double expend;//查询时间区间总支出
    public double income;//查询时间区间总收入
    public List<DetailLists> detailList;


    public static class DetailLists implements Parcelable{
        public String cdCode;// 订单号
        public int cdAccount;//交易账户  0:人民币账户 1:美金账户 2:港币账户
        public int cdType;//交易类型 0:收入 1:支出
        public int cdWay;//交易方式 0:返佣 1:理财 2:违章 3:提现 4:支付宝 5:微信 6:快捷支付 7:境外转入 8:转账 9:官方经账
                            //  （100全部只是用于查询时返回是没有的）
        public int cdState;//交易状态 0:成功 1:失败 2:审核中
        public int cdCurrency;//交易币种 0:人民币 1:美金 2:港币
        public double cdMoney;//交易金额
        public double cdFee; //交易手续费/汇率
        public String cdExplain;// 交易说明
        public String cdCreateDate;//记录时间


        protected DetailLists(Parcel in) {
            cdCode = in.readString();
            cdAccount = in.readInt();
            cdType = in.readInt();
            cdWay = in.readInt();
            cdState = in.readInt();
            cdCurrency = in.readInt();
            cdMoney = in.readDouble();
            cdFee = in.readDouble();
            cdExplain = in.readString();
            cdCreateDate = in.readString();
        }

        public static final Creator<DetailLists> CREATOR = new Creator<DetailLists>() {
            @Override
            public DetailLists createFromParcel(Parcel in) {
                return new DetailLists(in);
            }

            @Override
            public DetailLists[] newArray(int size) {
                return new DetailLists[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(cdCode);
            dest.writeInt(cdAccount);
            dest.writeInt(cdType);
            dest.writeInt(cdWay);
            dest.writeInt(cdState);
            dest.writeInt(cdCurrency);
            dest.writeDouble(cdMoney);
            dest.writeDouble(cdFee);
            dest.writeString(cdExplain);
            dest.writeString(cdCreateDate);
        }
    }


    protected BalanceDetail(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
        expend = in.readDouble();
        income = in.readDouble();
        detailList = in.createTypedArrayList(DetailLists.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(result_Code);
        dest.writeDouble(expend);
        dest.writeDouble(income);
        dest.writeTypedList(detailList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BalanceDetail> CREATOR = new Creator<BalanceDetail>() {
        @Override
        public BalanceDetail createFromParcel(Parcel in) {
            return new BalanceDetail(in);
        }

        @Override
        public BalanceDetail[] newArray(int size) {
            return new BalanceDetail[size];
        }
    };
}
