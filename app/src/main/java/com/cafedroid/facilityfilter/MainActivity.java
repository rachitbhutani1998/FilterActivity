package com.cafedroid.facilityfilter;

import android.os.Bundle;
import android.widget.Toast;

import com.cafedroid.facilityfilter.adapter.FacilityAdapter;
import com.cafedroid.facilityfilter.model.Facility;
import com.cafedroid.facilityfilter.model.Option;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements FilterView {

    @BindView(R.id.facilities_rv)
    RecyclerView mRecyclerView;

    FilterPresenter mPresenter;

    FacilityAdapter mAdapter;

    OnOptionSelected optionSelected;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        mPresenter = new FilterPresenterImpl(this);
        mAdapter = new FacilityAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        optionSelected = new OnOptionSelected() {
            @Override
            public void optionChecked(int optId) {
                mPresenter.updateFilters(optId);
            }
            @Override
            public void removeOption(int selectedOptionId) {
                mPresenter.removeExclusions(selectedOptionId);
            }
        };
        mAdapter.setCallback(optionSelected);
        mPresenter.attachView(this,realm);
    }


    @Override
    public void updateFacilities(RealmList<Facility> facilities) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setFacilities(facilities);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setExclusions(ArrayList<Integer> exclusions) {
        mAdapter.setExclusions(exclusions);
        mAdapter.notifyDataSetChanged();
    }

    public interface OnOptionSelected {
        void optionChecked(int optId);

        void removeOption(int selectedOptionId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
