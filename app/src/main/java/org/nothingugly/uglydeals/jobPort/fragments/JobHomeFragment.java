package org.nothingugly.uglydeals.jobPort.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.nothingugly.uglydeals.Deal;
import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.adapters.RvCommonJobAdapter;
import org.nothingugly.uglydeals.jobPort.adapters.RvMainAdapter;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobHomeFragment extends Fragment {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.rv_all_jobs)
    RecyclerView rvAllJobs;
    Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String userId;
    private ArrayList<CommonJobsModel> jobsModelArrayList;

    public JobHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        //Initialise Firebase app
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        checkUser();
        getFromFireBase();
        setMainAdapter();
        setRecommendedAdapter();
        setAllJobsAdapter();
        return view;
    }

    private void getFromFireBase() {
        jobsModelArrayList = new ArrayList<>();
        mFirestore.collection("jobs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        CommonJobsModel commonJobsModel = doc.toObject(CommonJobsModel.class);
                        jobsModelArrayList.add(commonJobsModel);
                    }
                } else {

                }
            }
        });
    }

    private void checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();
            this.userId = userId;
        } else {
//            sendToLogin();
        }
    }

    private void setAllJobsAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvAllJobs.setLayoutManager(layoutManager);
        RvCommonJobAdapter rvCommonJobAdapter = new RvCommonJobAdapter(getActivity(),jobsModelArrayList);
        rvAllJobs.setAdapter(rvCommonJobAdapter);
    }

    private void setRecommendedAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvRecommended.setLayoutManager(layoutManager);
        RvCommonJobAdapter rvCommonJobAdapter = new RvCommonJobAdapter(getActivity(), jobsModelArrayList);
        rvRecommended.setAdapter(rvCommonJobAdapter);
    }

    private void setMainAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMain.setLayoutManager(layoutManager);
        RvMainAdapter rvMainAdapter = new RvMainAdapter(getActivity());
        rvMain.setAdapter(rvMainAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
