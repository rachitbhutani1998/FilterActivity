package com.cafedroid.facilityfilter.model;

import androidx.annotation.Nullable;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Exclusion extends RealmObject {

    private int key;
    private RealmList<Integer> values;

    public Exclusion() {
    }

    public Exclusion(int opt1) {
        this.key = opt1;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public RealmList<Integer> getValues() {
        return values;
    }

    public void setValues(RealmList<Integer> values) {
        this.values = values;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Integer)
            return obj.equals(this.getKey());
        if (!(obj instanceof Exclusion))
            return false;
        return ((Exclusion) obj).getKey() == this.getKey();
    }
}
