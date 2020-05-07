package org.nothingugly.uglydeals.jobPort.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private CommonJobsModel commonJobsModel;
    Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

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
        //Initialise Firebase app
        //FirebaseApp.initializeApp(this);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        if (commonJobsModel.getSaved()) {
            btnSave.setText("Saved");
        }
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
                if (!commonJobsModel.getSaved()) {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirestore = FirebaseFirestore.getInstance();
                    String userId = mAuth.getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", commonJobsModel.getId());
                    mFirestore.collection("customers").document(userId).
                            collection("savedJobs").document(commonJobsModel.getId()).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btnSave.setText("Saved");
                                    progressBar.setVisibility(View.GONE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.w("Saved", "Error writing document", e);
                                }
                            });
                }
                break;
            case R.id.btn_apply:
                progressBar.setVisibility(View.VISIBLE);
                mFirestore = FirebaseFirestore.getInstance();
                String userId = mAuth.getUid();
                Date currentTime = Calendar.getInstance().getTime();
                Map<String, Object> data = new HashMap<>();
                data.put("id", commonJobsModel.getId());
                data.put("timeStamp", currentTime);
                mFirestore.collection("customers").document(userId).
                        collection("appliedJobs").document(commonJobsModel.getId()).set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ((JobPortActivity) getActivity()).setTitle("saved");
                                AppliedFragment appliedFragment = new AppliedFragment();
                                replaceFragment(appliedFragment);
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Log.w("Saved", "Error writing document", e);
                            }
                        });
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
