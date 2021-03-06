package com.cafedroid.facilityfilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cafedroid.facilityfilter.model.Constants;
import com.cafedroid.facilityfilter.model.DbValidity;
import com.cafedroid.facilityfilter.model.Exclusion;
import com.cafedroid.facilityfilter.model.Facility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class FilterPresenterImpl implements FilterPresenter {

    private FilterView filterView;

    private RealmList<Facility> facilities;

    private ArrayList<Integer> exclusions;

    private RealmList<Exclusion> exclusionMap;

    private Realm realm;

    private Context context;

    FilterPresenterImpl(FilterView filterView) {
        this.filterView = filterView;
        this.exclusions = new ArrayList<>();
    }

    @Override
    public void loadData() {
        filterView.showLoading(true);
        realm.beginTransaction();

        DbValidity validityResult = realm.where(DbValidity.class)
                .findFirst();

        if (validityResult != null
                && System.currentTimeMillis() - validityResult.getTime() >= DbValidity.ONE_DAY_MILLIS) {
            callApi();
            return;
        }

        RealmResults<Facility> realmResults = realm.where(Facility.class)
                .findAll();

        realm.commitTransaction();
        if (realmResults != null && !realmResults.isEmpty()) {
            if (facilities == null)
                facilities = new RealmList<>();
            facilities.clear();
            realm.beginTransaction();
            for (Facility facility :
                    realmResults) {
                facility.setSelectionId(0);
            }
            realm.commitTransaction();
            facilities.addAll(realmResults);
            filterView.updateFacilities(facilities);
        } else callApi();

    }

    private void callApi() {
        if (!isNetworkAvailable()) {
            filterView.showError("Check your internet connection and try again.");
            return;
        }
        AndroidNetworking.get(Constants.API_URL)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            realm.beginTransaction();
                            DbValidity validity = realm.createObject(DbValidity.class);
                            validity.setTime(System.currentTimeMillis());
                            parseJsonForExclusions(response);
                            facilities = Facility.parseJson(response);
                            realm.commitTransaction();

                            if (facilities.isEmpty())
                                filterView.showError("No facility to display");
                            else loadData();
                        } catch (JSONException e) {
                            filterView.showError(e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        filterView.showError(anError.getLocalizedMessage());
                        anError.printStackTrace();
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }

    @SuppressLint("UseSparseArrays")
    private void parseJsonForExclusions(JSONObject response) throws JSONException {
        JSONArray exJsonArray = response.getJSONArray(Constants.RESPONSE_EXCLUSIONS);
        for (int i = 0; i < exJsonArray.length(); i++) {
            JSONArray exPair = exJsonArray.getJSONArray(i);
            Integer optionId1 = exPair.getJSONObject(0).getInt(Constants.RESPONSE_OPTION_ID);
            Integer optionId2 = exPair.getJSONObject(1).getInt(Constants.RESPONSE_OPTION_ID);

            addToMap(optionId1, optionId2);
            addToMap(optionId2, optionId1);
        }
    }

    private void addToMap(Integer opt1, Integer opt2) {
        RealmList<Integer> tempList;
        if (exclusionMap == null)
            exclusionMap = new RealmList<>();
        if (exclusionMap.contains(opt1))
            tempList = exclusionMap.get(exclusionMap.indexOf(opt1)).getValues();
        else
            tempList = new RealmList<>();
        tempList.add(opt2);
        Exclusion exclusion = realm.createObject(Exclusion.class);
        exclusion.setKey(opt1);
        exclusion.setValues(tempList);
        exclusionMap.add(exclusion);
    }

    @Override
    public void attachView(Context context) {
        realm = Realm.getDefaultInstance();
        this.context = context;
        loadData();
    }

    @Override
    public void updateFilters(Integer optId) {
        RealmResults<Exclusion> exclusionRealmResults = realm.where(Exclusion.class)
                .findAll();
        if (exclusionRealmResults.contains(optId)) {
            for (Exclusion exclusion :
                    exclusionRealmResults) {
                if (exclusion.getKey() == optId) {
                    exclusions.addAll(exclusion.getValues());
                }
            }
        }
        filterView.setExclusions(exclusions);
    }

    @Override
    public void removeExclusions(Integer selectedOptionId) {
        RealmResults<Exclusion> exclusionRealmResults = realm.where(Exclusion.class)
                .findAll();
        if (exclusionRealmResults.contains(selectedOptionId)) {
            for (Exclusion exclusion :
                    exclusionRealmResults) {
                if (exclusion.getKey() == selectedOptionId) {
                    exclusions.removeAll(exclusion.getValues());
                }
            }
        }
    }

    @Override
    public void destroy() {
        realm.close();
    }
}
