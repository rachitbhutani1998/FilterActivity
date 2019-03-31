package com.cafedroid.facilityfilter;

import com.cafedroid.facilityfilter.model.Facility;

import java.util.ArrayList;

import io.realm.RealmList;

interface FilterView {

    void updateFacilities(RealmList<Facility> facilities);

    void showError(String message);

    void setExclusions(ArrayList<Integer> exclusions);
}
