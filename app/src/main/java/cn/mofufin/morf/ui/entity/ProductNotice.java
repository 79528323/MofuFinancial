package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductNotice implements Parcelable {


    /**
     * result_Msg : 查询成功
     * informList : [{"informMessage":"摩服金服测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知","infoDate":"2019-01-13 17:42:38"},{"informMessage":"摩服金服测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知","infoDate":"2019-01-13 17:42:37"},{"informMessage":"摩服金服测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知","infoDate":"2019-01-13 17:42:34"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<InformListBean> informList;

    protected ProductNotice(Parcel in) {
        result_Msg = in.readString();
        result_Code = in.readInt();
    }

    public static final Creator<ProductNotice> CREATOR = new Creator<ProductNotice>() {
        @Override
        public ProductNotice createFromParcel(Parcel in) {
            return new ProductNotice(in);
        }

        @Override
        public ProductNotice[] newArray(int size) {
            return new ProductNotice[size];
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

    public List<InformListBean> getInformList() {
        return informList;
    }

    public void setInformList(List<InformListBean> informList) {
        this.informList = informList;
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

    public static class InformListBean implements Parcelable{
        /**
         * informMessage : 摩服金服测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知测试通知
         * infoDate : 2019-01-13 17:42:38
         */

        private String informMessage;
        private String infoDate;

        protected InformListBean(Parcel in) {
            informMessage = in.readString();
            infoDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(informMessage);
            dest.writeString(infoDate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<InformListBean> CREATOR = new Creator<InformListBean>() {
            @Override
            public InformListBean createFromParcel(Parcel in) {
                return new InformListBean(in);
            }

            @Override
            public InformListBean[] newArray(int size) {
                return new InformListBean[size];
            }
        };

        public String getInformMessage() {
            return informMessage;
        }

        public void setInformMessage(String informMessage) {
            this.informMessage = informMessage;
        }

        public String getInfoDate() {
            return infoDate;
        }

        public void setInfoDate(String infoDate) {
            this.infoDate = infoDate;
        }
    }
}
