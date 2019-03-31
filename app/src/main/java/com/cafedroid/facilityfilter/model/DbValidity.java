package com.cafedroid.facilityfilter.model;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class DbValidity extends RealmObject {

    public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;  // 1 day

    @Required
    private Long time;

    public DbValidity() {
    }

    @Nonnull
    public Long getTime() {
        return time;
    }

    public void setTime(@Nonnull Long time) {
        this.time = time;
    }
}
