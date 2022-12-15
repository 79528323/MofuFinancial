package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MposArea {

    /**
     * result_Msg : 查询成功
     * areaList : [{"areaName":"北京","latitude":"39.90","longitude":"116.40"},{"areaName":"天津","latitude":"39.12","longitude":"117.20"},{"areaName":"济南","latitude":"36.67","longitude":"116.98"},{"areaName":"青岛","latitude":"36.07","longitude":"120.38"},{"areaName":"昆明","latitude":"25.05","longitude":"102.72"},{"areaName":"成都","latitude":"30.67","longitude":"104.07"},{"areaName":"西宁","latitude":"36.62","longitude":"101.78"},{"areaName":"深圳","latitude":"22.55","longitude":"114.05"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<AreaListBean> areaList;

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

    public List<AreaListBean> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaListBean> areaList) {
        this.areaList = areaList;
    }

    public static class AreaListBean implements Parcelable {
        /**
         * areaName : 北京
         * latitude : 39.90
         * longitude : 116.40
         */

        public String areaName;
        public String latitude;
        public String longitude;

        public AreaListBean(String areaName, String latitude, String longitude) {
            this.areaName = areaName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        protected AreaListBean(Parcel in) {
            areaName = in.readString();
            latitude = in.readString();
            longitude = in.readString();
        }

        public static final Creator<AreaListBean> CREATOR = new Creator<AreaListBean>() {
            @Override
            public AreaListBean createFromParcel(Parcel in) {
                return new AreaListBean(in);
            }

            @Override
            public AreaListBean[] newArray(int size) {
                return new AreaListBean[size];
            }
        };

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(areaName);
            dest.writeString(latitude);
            dest.writeString(longitude);
        }
    }
}
