package com.cafedroid.facilityfilter;

import android.content.Context;

import com.cafedroid.facilityfilter.model.Facility;
import com.cafedroid.facilityfilter.model.Option;

import io.realm.Realm;

public interface FilterPresenter {

    void loadData();

    void attachView(Context context);

    void updateFilters(Integer optId);

    void removeExclusions(Integer selectedOptionId);

    void destroy();
}
