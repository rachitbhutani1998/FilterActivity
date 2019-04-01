package com.cafedroid.facilityfilter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cafedroid.facilityfilter.adapter.FacilityAdapter;
import com.cafedroid.facilityfilter.model.Facility;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements FilterView {

    @BindView(R.id.facilities_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.error_tv)
    TextView errorTv;
    @BindView(R.id.retry_btn)
    Button retryBtn;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;

    private FilterPresenter mPresenter;

    private FacilityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new FilterPresenterImpl(this);
        mAdapter = new FacilityAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        OnOptionSelected optionSelected = new OnOptionSelected() {
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
        mPresenter.attachView(this);

        retryBtn.setOnClickListener(v -> {
            mPresenter.loadData();
        });
    }


    @Override
    public void updateFacilities(RealmList<Facility> facilities) {
        loadingProgress.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setFacilities(facilities);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        loadingProgress.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorTv.setText(message);
    }

    @Override
    public void setExclusions(ArrayList<Integer> exclusions) {
        mAdapter.setExclusions(exclusions);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(boolean show) {
        errorView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    public interface OnOptionSelected {

        void optionChecked(int optId);

        void removeOption(int selectedOptionId);
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
