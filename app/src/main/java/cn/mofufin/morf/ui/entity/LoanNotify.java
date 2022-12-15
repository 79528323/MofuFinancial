package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LoanNotify {


    /**
     * result_Msg : 查询成功
     * informList : [{"informContent":"sadsadsacxz21930821093821098rdifohfkjnskjfnsdmcmxnvkjsdoiueoiwqwqewq","informDate":"2019-04-23 09:27:35"},{"informContent":"ldslksajlkdjsalkjdlksadas","informDate":"2019-04-23 09:27:17"},{"informContent":"sadsadaaaaaaaaaaaaaasadcxzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz","informDate":"2019-04-22 14:19:46"},{"informContent":"sadsadaaaaaaaaaaaaaa","informDate":"2019-04-22 14:19:37"},{"informContent":"16513213213213213","informDate":"2019-04-22 14:19:25"},{"informContent":"abcsdasdsada","informDate":"2019-04-15 16:55:14"},{"informContent":"1321320.02132156","informDate":"2019-04-15 16:03:12"}]
     * result_Code : 0
     */

    public String result_Msg;
    public int result_Code;
    public List<InformListBean> informList;


    public static class InformListBean implements Parcelable {
        /**
         * informContent : sadsadsacxz21930821093821098rdifohfkjnskjfnsdmcmxnvkjsdoiueoiwqwqewq
         * informDate : 2019-04-23 09:27:35
         */

        public String informContent;
        public String informDate;

        protected InformListBean(Parcel in) {
            informContent = in.readString();
            informDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(informContent);
            dest.writeString(informDate);
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
    }
}
