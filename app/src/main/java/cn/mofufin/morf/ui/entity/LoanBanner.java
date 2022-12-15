package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.jar.Pack200;

public class LoanBanner {


    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * imgList : [{"imgAddress":"upload/loans/carousel/image20190428142412.jpg","hrefAddress":"http://www.mofubill.com"},{"imgAddress":"upload/loans/carousel/image20190428092049.jpg","hrefAddress":""}]
     */

    public String result_Msg;
    public int result_Code;
    public List<ImgListBean> imgList;


    public static class ImgListBean implements Parcelable {
        /**
         * imgAddress : upload/loans/carousel/image20190428142412.jpg
         * hrefAddress : http://www.mofubill.com
         */

        public String imgAddress;
        public String hrefAddress;

        protected ImgListBean(Parcel in) {
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

        public static final Creator<ImgListBean> CREATOR = new Creator<ImgListBean>() {
            @Override
            public ImgListBean createFromParcel(Parcel in) {
                return new ImgListBean(in);
            }

            @Override
            public ImgListBean[] newArray(int size) {
                return new ImgListBean[size];
            }
        };
    }
}
