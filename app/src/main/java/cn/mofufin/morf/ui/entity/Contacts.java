package cn.mofufin.morf.ui.entity;

import java.io.Serializable;

public class Contacts implements Serializable {
    public String name;
    public String phone;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
