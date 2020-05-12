package org.nothingugly.uglydeals.jobPort.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.Constants;
import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;
import org.nothingugly.uglydeals.jobPort.adapters.SavedJobsAdapter;
import org.nothingugly.uglydeals.jobPort.interfaces.RemoveItemInterfaces;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedJobsFragment extends Fragment implements RemoveItemInterfaces {

    @BindView(R.id.rv_saved_jobs)
    RecyclerView rvSavedJobs;
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<CommonJobsModel> jobsModelArrayList;
    private SavedJobsAdapter savedJobsAdapter;
    private Paint p = new Paint();
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private ArrayList<String> jobIds;
    private String fromWhere;

    public SavedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_jobs, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText("Saved");
        Constants.show(progressBar, getActivity());
        jobsModelArrayList = new ArrayList<>();
        jobIds = new ArrayList<>();
        setAdapter();
        getFromFireBase();
        return view;
    }

    private void getFromFireBase() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String userId = mAuth.getUid();
        mFirestore.collection("customers").document(userId).
                collection("savedJobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            for (QueryDocumentSnapshot item : task.getResult()) {
                                // Convert the whole Query Snapshot to a list
                                // of objects directly! No need to fetch each
                                // document.
                                String id = (String) item.get("id");
                                mFirestore.collection("jobs").document(id).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot dealDocument = task.getResult();
                                                //Convert the particular document into a Deal object
                                                CommonJobsModel commonJobsModel = dealDocument.toObject(CommonJobsModel.class);
                                                jobsModelArrayList.add(commonJobsModel);
                                                savedJobsAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                            Constants.hide(progressBar, getActivity());
                        } else {
                            Constants.hide(progressBar, getActivity());
                        }
                    }
                });
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSavedJobs.setLayoutManager(layoutManager);
        savedJobsAdapter = new SavedJobsAdapter(getActivity(), jobsModelArrayList);
        rvSavedJobs.setAdapter(savedJobsAdapter);
        savedJobsAdapter.setListener(this);
        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    savedJobsAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop() + 28, (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvSavedJobs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void removeItem(String userId) {
        Constants.show(progressBar, getActivity());
        mFirestore = FirebaseFirestore.getInstance();
        String uId = mAuth.getUid();
        mFirestore.collection("customers").document(uId).collection("savedJobs")
                .document(userId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Constants.hide(progressBar, getActivity());
                        Toast.makeText(getActivity(), "Deleted sucessfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constants.hide(progressBar, getActivity());
                        Log.w("Saved", "Error writing document", e);
                    }
                });
    }

    @Override
    public void remove(int pos) {

    }

    @Override
    public void addItem(String name) {

    }

    @Override
    public void itemClick(CommonJobsModel commonJobsModel) {
        commonJobsModel.setSaved(true);
        SystemAnalystFragment systemAnalystFragment = new SystemAnalystFragment(commonJobsModel);
        replaceFragment(systemAnalystFragment, commonJobsModel.getTitle());
    }

    public void replaceFragment(Fragment fragment, String title) {
        ((JobPortActivity) getActivity()).setToolBar();
        ((JobPortActivity) getActivity()).disableView();
        ((JobPortActivity) getActivity()).setTitle(title);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment);
        fragmentTransaction.commit();
    }
}
