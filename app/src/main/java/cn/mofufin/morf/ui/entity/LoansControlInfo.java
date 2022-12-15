package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class LoansControlInfo {
    public String result_Msg;
    public int result_Code;
    public ControlInfo controlInfo;


    public static class ControlInfo implements Parcelable {
        /**
         * //本人文本资料提交状态: 0:已提交,1:未提交,2:未通过
         */
        public int selfTextState;
        public int selfImgState;//本人图片资料提交状态: 0:已提交,1:未提交,2:未通过
        public int selfPrivateImgState;//本人私营业主资料提交状态: 0:已提交,1:未提交,2:未通过,3:本人不是私营业主无需提交
        public int spouseTextState;//配偶文本资料提交状态: 0:已提交,1:未提交,2:未通过,3:本人无配偶信息无需提交
        public int spouseImgState;//配偶图片资料提交状态：0:已提交,1:未提交,2:未通过,3:本人无配偶信息无需提交
        public int friendTextState;//亲朋文本资料提交状态: 0:已提交,1:未提交,2:未通过
        public int businessImgState;//业务图片资料提交状态: 0:已提交,1:未提交,2:未通过


        protected ControlInfo(Parcel in) {
            selfTextState = in.readInt();
            selfImgState = in.readInt();
            selfPrivateImgState = in.readInt();
            spouseTextState = in.readInt();
            spouseImgState = in.readInt();
            friendTextState = in.readInt();
            businessImgState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(selfTextState);
            dest.writeInt(selfImgState);
            dest.writeInt(selfPrivateImgState);
            dest.writeInt(spouseTextState);
            dest.writeInt(spouseImgState);
            dest.writeInt(friendTextState);
            dest.writeInt(businessImgState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ControlInfo> CREATOR = new Creator<ControlInfo>() {
            @Override
            public ControlInfo createFromParcel(Parcel in) {
                return new ControlInfo(in);
            }

            @Override
            public ControlInfo[] newArray(int size) {
                return new ControlInfo[size];
            }
        };
    }
}
