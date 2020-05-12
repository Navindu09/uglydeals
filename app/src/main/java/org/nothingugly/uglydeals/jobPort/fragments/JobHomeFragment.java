package org.nothingugly.uglydeals.jobPort.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import org.nothingugly.uglydeals.jobPort.activity.Constants;
import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;
import org.nothingugly.uglydeals.jobPort.adapters.RvCommonJobAdapter;
import org.nothingugly.uglydeals.jobPort.adapters.RvMainAdapter;
import org.nothingugly.uglydeals.jobPort.interfaces.RvClickInterface;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobHomeFragment extends Fragment implements RvClickInterface {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.rv_all_jobs)
    RecyclerView rvAllJobs;
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBarItem;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String userId;
    private ArrayList<CommonJobsModel> jobsModelArrayList;
    private RvCommonJobAdapter rvRecommendedJobAdapter;
    private RvCommonJobAdapter rvAllJobAdapter;

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
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        jobsModelArrayList = new ArrayList<>();
        checkUser();
        getFromFireBase();
        setRecommendedAdapter();
        setAllJobsAdapter();
        setMainAdapter();
        return view;
    }

    private void hide() {
        Constants.hide(getActivity());
        progressBarItem.setVisibility(View.GONE);
    }

    private void show() {
        Constants.showProgessBar(getActivity());
        progressBarItem.setVisibility(View.VISIBLE);
    }

    private void getFromFireBase() {
        show();
        mFirestore = FirebaseFirestore.getInstance();
        ArrayList<Deal> deals = new ArrayList<>();
        mFirestore.collection("jobs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Convert the whole Query Snapshot to a list
                        // of objects directly! No need to fetch each
                        // document.
                        CommonJobsModel commonJobsModel = document.toObject(CommonJobsModel.class);
                        // Add all to your list
                        jobsModelArrayList.add(commonJobsModel);
                        rvAllJobAdapter.notifyDataSetChanged();
                        rvRecommendedJobAdapter.notifyDataSetChanged();
                    }
                    hide();
                } else {
                    hide();
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
        rvAllJobAdapter = new RvCommonJobAdapter(getActivity(), jobsModelArrayList);
        rvAllJobAdapter.setListener(this);
        rvAllJobs.setAdapter(rvAllJobAdapter);
    }

    private void setRecommendedAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvRecommended.setLayoutManager(layoutManager);
        rvRecommendedJobAdapter = new RvCommonJobAdapter(getActivity(), jobsModelArrayList);
        rvRecommendedJobAdapter.setListener(this);
        rvRecommended.setAdapter(rvRecommendedJobAdapter);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(CommonJobsModel jobsModels) {
        jobsModels.setSaved(false);
        SystemAnalystFragment systemAnalystFragment = new SystemAnalystFragment(jobsModels);
        replaceFragment(systemAnalystFragment, jobsModels.getTitle());
    }

    public void replaceFragment(Fragment fragment, String title) {
        ((JobPortActivity) getActivity()).disableView();
        ((JobPortActivity) getActivity()).setTitle(title);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
