package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Notifys implements Parcelable {

    /**
     * params : 5
     * way : 0
     */

    public String params;
    public String way;
    public String title;

    public Notifys() {
    }

    protected Notifys(Parcel in) {
        params = in.readString();
        way = in.readString();
        title = in.readString();
    }

    public static final Creator<Notifys> CREATOR = new Creator<Notifys>() {
        @Override
        public Notifys createFromParcel(Parcel in) {
            return new Notifys(in);
        }

        @Override
        public Notifys[] newArray(int size) {
            return new Notifys[size];
        }
    };

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(params);
        dest.writeString(way);
        dest.writeString(title);
    }
}
