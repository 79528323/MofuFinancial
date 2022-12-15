package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * author：created by Administrator on 2018/8/29 17
 * e-mail:79528323@qq.com
 */
public class GeneralResponse {
    public boolean bool;
    public int stateCode;
    public String message;
    public DataBean data;
    public String reason;
    /**
     * data : {"rc":[{"imgAddress":"upload/officeImg/20190218112510.jpg","hrefAddress":"http://www.baidu.com"},{"imgAddress":"upload/officeImg/20190218112045.jpg","hrefAddress":""}],"reason":"查询成功"}
     */

//    @SerializedName("data")
//    private DataBean dataX;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean{
        public String reason;
        public String smsCode;
        public dataMap dataMap;
        public List<GeneralResponse.DataBean.RcBean> rc;//轮播图


        public static class dataMap implements Parcelable{
            /**
             * "cardType2": "贷记卡",
             "bin": "625080",
             "bankname": "广州农村商业银行",
             "cardType1": "广州农村商业银行",
             "bankcode": "65055810"
             */

            public String cardType2;
            public String bin;
            public String bankname;
            public String cardType1;
            public String bankcode;


            protected dataMap(Parcel in) {
                cardType2 = in.readString();
                bin = in.readString();
                bankname = in.readString();
                cardType1 = in.readString();
                bankcode = in.readString();
//                rc = in.createTypedArrayList(RcBean.CREATOR);
            }

            public static final Creator<dataMap> CREATOR = new Creator<dataMap>() {
                @Override
                public dataMap createFromParcel(Parcel in) {
                    return new dataMap(in);
                }

                @Override
                public dataMap[] newArray(int size) {
                    return new dataMap[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(cardType2);
                dest.writeString(bin);
                dest.writeString(bankname);
                dest.writeString(cardType1);
                dest.writeString(bankcode);
//                dest.writeTypedList(rc);
            }
        }


        public static class RcBean implements Parcelable{
            /**
             * imgAddress : upload/officeImg/20190218112510.jpg
             * hrefAddress : http://www.baidu.com
             */

            public String imgAddress;//图片地址
            public String hrefAddress;//跳转链接

            protected RcBean(Parcel in) {
                imgAddress = in.readString();
                hrefAddress = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(imgAddress);
                dest.writeString(hrefAddress);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<RcBean> CREATOR = new Creator<RcBean>() {
                @Override
                public RcBean createFromParcel(Parcel in) {
                    return new RcBean(in);
                }

                @Override
                public RcBean[] newArray(int size) {
                    return new RcBean[size];
                }
            };
        }
    }

//    public static class DataBean {
//        /**
//         * rc : [{"imgAddress":"upload/officeImg/20190218112510.jpg","hrefAddress":"http://www.baidu.com"},{"imgAddress":"upload/officeImg/20190218112045.jpg","hrefAddress":""}]
//         * reason : 查询成功
//         */
//
//        @SerializedName("reason")
//        private String reasonX;
//
//        public String getReasonX() {
//            return reasonX;
//        }
//
//        public void setReasonX(String reasonX) {
//            this.reasonX = reasonX;
//        }
//
//        public List<RcBean> getRc() {
//            return rc;
//        }
//
//        public void setRc(List<RcBean> rc) {
//            this.rc = rc;
//        }
//
//
//    }
}
