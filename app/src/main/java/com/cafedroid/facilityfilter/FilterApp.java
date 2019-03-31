package com.cafedroid.facilityfilter;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import io.realm.Realm;

public class FilterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
        Realm.init(this);
    }
}
