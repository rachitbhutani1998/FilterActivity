package com.cafedroid.facilityfilter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cafedroid.facilityfilter.CustomRadioButton;
import com.cafedroid.facilityfilter.MainActivity;
import com.cafedroid.facilityfilter.R;
import com.cafedroid.facilityfilter.model.Facility;
import com.cafedroid.facilityfilter.model.Option;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

    private RealmList<Facility> mFacilities;
    private Context mContext;
    private ArrayList<Integer> exclusionList;
    private MainActivity.OnOptionSelected callback;
    private Realm realm;

    public FacilityAdapter(Context context) {
        this.mContext = context;
        realm = Realm.getDefaultInstance();
    }

    public void setFacilities(RealmList<Facility> mFacilities) {
        this.mFacilities = mFacilities;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FacilityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.facility_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder facilityViewHolder, int pos) {
        Facility currentFacility = mFacilities.get(pos);
        facilityViewHolder.facilityNameTv.setText(currentFacility.getName());
        facilityViewHolder.optionRadioGroup.removeAllViews();
        for (Option option :
                currentFacility.getOptions()) {
            CustomRadioButton button = new CustomRadioButton(mContext);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            realm.beginTransaction();
            if (currentFacility.getSelectedOptionId() != null && currentFacility.getSelectedOptionId().equals(option.get_id()))
                option.setChecked(true);
            else option.setChecked(false);
            realm.commitTransaction();
            button.setOption(option);
            button.setOnClickListener(v -> {
                realm.beginTransaction();
                button.setChecked(option.isChecked());
                Log.e("ADAPTER", "onBindViewHolder: " + button.isChecked());
                if (currentFacility.getSelectedOptionId() != null)
                    callback.removeOption(currentFacility.getSelectedOptionId());
                callback.optionChecked(button.getId());
                currentFacility.setSelectionId(button.getId());
                realm.commitTransaction();
            });

            if (exclusionList == null || !exclusionList.contains(option.get_id())) {
                facilityViewHolder.facilityNameTv.setVisibility(View.VISIBLE);
                facilityViewHolder.optionRadioGroup.addView(button);
            }
            if (facilityViewHolder.optionRadioGroup.getChildCount()==0)
                facilityViewHolder.facilityNameTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mFacilities == null ? 0 : mFacilities.size();
    }

    public void setExclusions(ArrayList<Integer> exclusions) {
        this.exclusionList = exclusions;
    }

    public void setCallback(MainActivity.OnOptionSelected optionSelected) {
        this.callback = optionSelected;
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.facility_name_tv)
        TextView facilityNameTv;
        @BindView(R.id.options_radio_group)
        RadioGroup optionRadioGroup;

        FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
