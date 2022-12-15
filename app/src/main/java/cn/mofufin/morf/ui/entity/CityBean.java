package cn.mofufin.morf.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @description: 城市
 * @Author zsj on 2016/12/17 16:53.
 */

@DatabaseTable
public class CityBean implements Serializable {
    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
    }

    public CityBean() {
    }

    //构造方法
    public CityBean(int city_id, int province_id, String code, String name, String provincecode) {
        this.city_id = city_id;
        this.province_id = province_id;
        this.code = code;
        this.name = name;
        this.provincecode = provincecode;
    }

    @DatabaseField(id = true)
    private int city_id;
    @DatabaseField
    private int province_id;
    @DatabaseField
    private String code;
    @DatabaseField
    private String name;
    @DatabaseField
    private String provincecode;
    public CityTextBean cityTextBean;

    @DatabaseTable
    public static class CityTextBean implements Parcelable {
        @DatabaseField
        private int id;
        @DatabaseField
        private String names;
        @DatabaseField
        private String sex;

        public CityTextBean() {
        }

        public CityTextBean(int id, String names, String sex) {
            this.id = id;
            this.names = names;
            this.sex = sex;
        }

        protected CityTextBean(Parcel in) {
            id = in.readInt();
            names = in.readString();
            sex = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(names);
            dest.writeString(sex);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CityTextBean> CREATOR = new Creator<CityTextBean>() {
            @Override
            public CityTextBean createFromParcel(Parcel in) {
                return new CityTextBean(in);
            }

            @Override
            public CityTextBean[] newArray(int size) {
                return new CityTextBean[size];
            }
        };

        @Override
        public String toString() {
            return "CityTextBean{" +
                    "id=" + id +
                    ", names='" + names + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }



    @Override
    public String toString() {
        return "CityBean{" +
                "city_id=" + city_id +
                ", province_id=" + province_id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", provincecode='" + provincecode + '\'' +
                '}';
    }
}
