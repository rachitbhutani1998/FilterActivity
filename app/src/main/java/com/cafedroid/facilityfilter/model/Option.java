package com.cafedroid.facilityfilter.model;

import io.realm.RealmObject;

public class Option extends RealmObject {
    private String name;
    private String icon;
    private int _id;
    private boolean isChecked;

    public Option() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public int get_id() {
        return _id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
