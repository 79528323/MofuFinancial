package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Ranking implements Parcelable{


    /**
     * result_Msg : 查询成功
     * totalCommssion : 0
     * rankingList : [{"incomePersonPhone":"18818391935","incomePersonName":"林志鸿","totalMoney":10.61},{"incomePersonPhone":"15815824459","incomePersonName":"李奕浩","totalMoney":8.65},{"incomePersonPhone":"18620573888","incomePersonName":"潘勇辉","totalMoney":4.54},{"incomePersonPhone":"15814513178","incomePersonName":"庞振基","totalMoney":1.6},{"incomePersonPhone":"13790944522","incomePersonName":"谢聪","totalMoney":1.02},{"incomePersonPhone":"13528904596","incomePersonName":"李韶","totalMoney":0.27}]
     * result_Code : 0
     * commssionMoney : 0
     */

    public String result_Msg;
    public double totalCommssion;
    public int result_Code;
    public double commssionMoney;
    public List<RankingListBean> rankingList;

    protected Ranking(Parcel in) {
        result_Msg = in.readString();
        totalCommssion = in.readDouble();
        result_Code = in.readInt();
        commssionMoney = in.readDouble();
        rankingList = in.createTypedArrayList(RankingListBean.CREATOR);
    }

    public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
        @Override
        public Ranking createFromParcel(Parcel in) {
            return new Ranking(in);
        }

        @Override
        public Ranking[] newArray(int size) {
            return new Ranking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeDouble(totalCommssion);
        dest.writeInt(result_Code);
        dest.writeDouble(commssionMoney);
        dest.writeTypedList(rankingList);
    }

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public double getTotalCommssion() {
        return totalCommssion;
    }

    public void setTotalCommssion(double totalCommssion) {
        this.totalCommssion = totalCommssion;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public double getCommssionMoney() {
        return commssionMoney;
    }

    public void setCommssionMoney(double commssionMoney) {
        this.commssionMoney = commssionMoney;
    }

    public List<RankingListBean> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<RankingListBean> rankingList) {
        this.rankingList = rankingList;
    }

    public static class RankingListBean implements Parcelable{
        /**
         * incomePersonPhone : 18818391935
         * incomePersonName : 林志鸿
         * totalMoney : 10.61
         */

        public String incomePersonPhone;
        public String incomePersonName;
        public double totalMoney;
        public double historyTotal;

        public String getIncomePersonPhone() {
            return incomePersonPhone;
        }

        public void setIncomePersonPhone(String incomePersonPhone) {
            this.incomePersonPhone = incomePersonPhone;
        }

        public String getIncomePersonName() {
            return incomePersonName;
        }

        public void setIncomePersonName(String incomePersonName) {
            this.incomePersonName = incomePersonName;
        }

        public double getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(double totalMoney) {
            this.totalMoney = totalMoney;
        }

        protected RankingListBean(Parcel in) {
            incomePersonPhone = in.readString();
            incomePersonName = in.readString();
            totalMoney = in.readDouble();
            historyTotal = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(incomePersonPhone);
            dest.writeString(incomePersonName);
            dest.writeDouble(totalMoney);
            dest.writeDouble(historyTotal);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RankingListBean> CREATOR = new Creator<RankingListBean>() {
            @Override
            public RankingListBean createFromParcel(Parcel in) {
                return new RankingListBean(in);
            }

            @Override
            public RankingListBean[] newArray(int size) {
                return new RankingListBean[size];
            }
        };
    }
}
