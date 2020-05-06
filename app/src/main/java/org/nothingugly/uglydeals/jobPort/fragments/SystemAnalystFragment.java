package org.nothingugly.uglydeals.jobPort.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemAnalystFragment extends Fragment {

    @BindView(R.id.iv_frame)
    ImageView ivFrame;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_requirements)
    TextView tvRequirements;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_paid)
    TextView tvPaid;
    @BindView(R.id.tv_experience)
    TextView tvExperience;
    private CommonJobsModel commonJobsModel;
    Unbinder unbinder;

    public SystemAnalystFragment(CommonJobsModel jobsModels) {
        // Required empty public constructor
        this.commonJobsModel = jobsModels;
    }

    public SystemAnalystFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system_analyst, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvDescription.setText(commonJobsModel.getDescription());
        tvRequirements.setText(commonJobsModel.getEducationRequirement());
        tvLocation.setText("Location: " + commonJobsModel.getLocation());
        tvType.setText("" + commonJobsModel.getType());
        tvPaid.setText("Paid: Tk 10,000");
        tvExperience.setText("" + commonJobsModel.getExperience());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_save, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                btnSave.setText("Saved");
                break;
            case R.id.btn_apply:
                AppliedFragment appliedFragment = new AppliedFragment();
                replaceFragment(appliedFragment);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
