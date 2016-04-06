package com.ukiy.fastemplet.beans;

import org.litepal.crud.DataSupport;

/**
 * Created by UKIY on 2016/4/6.
 */
public class Bean extends DataSupport {
    private String name;
    private String phone;

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
