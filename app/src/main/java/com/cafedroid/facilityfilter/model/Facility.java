package com.cafedroid.facilityfilter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Facility extends RealmObject {

    private String name;
    private int _id;
    private RealmList<Option> options;
    private Integer selectedOptionId;

    public Facility() {
    }

    public Facility(String name, int _id, RealmList<Option> options) {
        this.name = name;
        this._id = _id;
        this.options = options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setOptions(RealmList<Option> options) {
        this.options = options;
    }

    public String getName() {
        return name;
    }


    public RealmList<Option> getOptions() {
        return options;
    }


    public static RealmList<Facility> parseJson(JSONObject response) throws JSONException {
        Realm realm = Realm.getDefaultInstance();
        RealmList<Facility> facilities = new RealmList<>();
        JSONArray facilitiesArray = response.getJSONArray("facilities");
        for (int i = 0; i < facilitiesArray.length(); i++) {
            JSONObject thisFacility = facilitiesArray.getJSONObject(i);
            String facilityName = thisFacility.getString("name");
            int _id = Integer.parseInt(thisFacility.getString("facility_id"));
            JSONArray optionsArray = thisFacility.getJSONArray("options");
            RealmList<Option> options = new RealmList<>();
            for (int j = 0; j < optionsArray.length(); j++) {
                JSONObject optionObject = optionsArray.getJSONObject(j);
                String name = optionObject.getString("name");
                String icon = optionObject.getString("icon");
                int id = Integer.parseInt(optionObject.getString("id"));
                Option option = realm.createObject(Option.class);
                option.setName(name);
                option.setIcon(icon);
                option.set_id(id);
                options.add(option);
                realm.copyToRealm(options);
            }
            Facility facility = realm.createObject(Facility.class);
            facility.set_id(_id);
            facility.setName(facilityName);
            facility.setOptions(options);
            facilities.add(facility);
        }
        return facilities;
    }

    public void setSelectionId(int option_id) {
        this.selectedOptionId = option_id;
    }

    public Integer getSelectedOptionId() {
        return this.selectedOptionId;
    }
}
