package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CoinDetail implements Parcelable{

    /**
     * result_Msg : 查询成功
     * totalIncome : 172
     * detailList : [{"mrNature":0,"mrDate":"2018-11-15 17:27:47","mrNumber":83,"mrGetType":0},{"mrNature":0,"mrDate":"2018-11-15 15:33:09","mrNumber":24,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-15 15:05:10","mrNumber":20,"mrGetType":0},{"mrNature":0,"mrDate":"2018-11-15 14:18:43","mrNumber":7,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-15 14:08:25","mrNumber":3,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-15 14:05:56","mrNumber":3,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-15 14:03:31","mrNumber":27,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-15 11:50:13","mrNumber":40,"mrGetType":0},{"mrNature":0,"mrDate":"2018-11-14 19:50:57","mrNumber":15,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:49:21","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:47:31","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:46:02","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:43:49","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:42:23","mrNumber":5,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:40:26","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:38:33","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:36:47","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:35:21","mrNumber":15,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:33:32","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:32:28","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 19:16:48","mrNumber":29,"mrGetType":0},{"mrNature":0,"mrDate":"2018-11-14 17:54:20","mrNumber":19,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:52:28","mrNumber":15,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:51:04","mrNumber":12,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:49:46","mrNumber":12,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:48:49","mrNumber":9,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:47:30","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:46:25","mrNumber":16,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:45:20","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:44:19","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:43:23","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:41:55","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:40:03","mrNumber":6,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:38:53","mrNumber":5,"mrGetType":2},{"mrNature":0,"mrDate":"2018-11-14 17:38:01","mrNumber":5,"mrGetType":2}]
     * result_Code : 0
     * totalExpend : 0
     */

    public String result_Msg;
    public int totalIncome;
    public int result_Code;
    public int totalExpend;
    public List<DetailListBean> detailList;

    protected CoinDetail(Parcel in) {
        result_Msg = in.readString();
        totalIncome = in.readInt();
        result_Code = in.readInt();
        totalExpend = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result_Msg);
        dest.writeInt(totalIncome);
        dest.writeInt(result_Code);
        dest.writeInt(totalExpend);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoinDetail> CREATOR = new Creator<CoinDetail>() {
        @Override
        public CoinDetail createFromParcel(Parcel in) {
            return new CoinDetail(in);
        }

        @Override
        public CoinDetail[] newArray(int size) {
            return new CoinDetail[size];
        }
    };

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public int getTotalExpend() {
        return totalExpend;
    }

    public void setTotalExpend(int totalExpend) {
        this.totalExpend = totalExpend;
    }

    public List<DetailListBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailListBean> detailList) {
        this.detailList = detailList;
    }

    public static class DetailListBean implements Parcelable{
        /**
         * mrNature : 0
         * mrDate : 2018-11-15 17:27:47
         * mrNumber : 83
         * mrGetType : 0
         */

        public int mrNature;
        public String mrDate;
        public int mrNumber;
        public int mrGetType;

        protected DetailListBean(Parcel in) {
            mrNature = in.readInt();
            mrDate = in.readString();
            mrNumber = in.readInt();
            mrGetType = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mrNature);
            dest.writeString(mrDate);
            dest.writeInt(mrNumber);
            dest.writeInt(mrGetType);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DetailListBean> CREATOR = new Creator<DetailListBean>() {
            @Override
            public DetailListBean createFromParcel(Parcel in) {
                return new DetailListBean(in);
            }

            @Override
            public DetailListBean[] newArray(int size) {
                return new DetailListBean[size];
            }
        };

        public int getMrNature() {
            return mrNature;
        }

        public void setMrNature(int mrNature) {
            this.mrNature = mrNature;
        }

        public String getMrDate() {
            return mrDate;
        }

        public void setMrDate(String mrDate) {
            this.mrDate = mrDate;
        }

        public int getMrNumber() {
            return mrNumber;
        }

        public void setMrNumber(int mrNumber) {
            this.mrNumber = mrNumber;
        }

        public int getMrGetType() {
            return mrGetType;
        }

        public void setMrGetType(int mrGetType) {
            this.mrGetType = mrGetType;
        }
    }
}
