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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.Constants;
import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.io.IOException;
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
    @BindView(R.id.tv_level)
    TextView tvLevel;
    private CommonJobsModel commonJobsModel;
    Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String FIREBASE_CLOUD_FUNCTION_URL = "https://us-central1-ugly-deals-debug.cloudfunctions.net/sendMail";

    public SystemAnalystFragment(CommonJobsModel jobsModels) {
        // Required empty public constructor
        this.commonJobsModel = jobsModels;
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
            btnSave.setText("Unsaved");
        } else {
            btnSave.setText("Save");
        }
        tvLevel.setText(commonJobsModel.getLevel() + "");
        tvDescription.setText(commonJobsModel.getDescription() + "");
        tvRequirements.setText(commonJobsModel.getEducationRequirement() + "");
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
                    Constants.show(progressBar, getActivity());
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
                                    Constants.hide(progressBar, getActivity());
                                    Constants.showToast(getActivity(), "Job has been added to saved jobs");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Constants.hide(progressBar, getActivity());
                                    Constants.showToast(getActivity(), "Something went wrong...");
                                    Log.w("Saved", "Error writing document", e);
                                }
                            });
                } else {
                    Constants.show(progressBar, getActivity());
                    mFirestore = FirebaseFirestore.getInstance();
                    String userId = mAuth.getUid();
                    mFirestore.collection("customers").document(userId).
                            collection("savedJobs").document(commonJobsModel.getId()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btnSave.setText("Save");
                                    Constants.hide(progressBar, getActivity());
                                    Constants.showToast(getActivity(), "Job has been removed from saved jobs");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Constants.hide(progressBar, getActivity());
                                    Constants.showToast(getActivity(), "Something went wrong...");
                                    Log.w("Saved", "Error writing document", e);
                                }
                            });
                }
                break;
            case R.id.btn_apply:
                Constants.show(progressBar, getActivity());
                mFirestore = FirebaseFirestore.getInstance();
                String userId = mAuth.getUid();
                Date currentTime = Calendar.getInstance().getTime();
                Map<String, Object> data = new HashMap<>();
                data.put("id", commonJobsModel.getId());
                data.put("timeStamp", currentTime);
                mFirestore.collection("customers").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String userName = (String) documentSnapshot.get("name");
                            String contact = (String) documentSnapshot.get("mobile");
                            String emailId = (String) documentSnapshot.get("email");
                            String degree = (String) documentSnapshot.get("degree");
                            String occupation = (String) documentSnapshot.get("occupation");
                            mFirestore.collection("customers").document(userId).
                                    collection("appliedJobs").document(commonJobsModel.getId()).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            String subject = userName + ", " + commonJobsModel.getTitle() + ", " + commonJobsModel.getId();
                                            String msg = "job description: " + commonJobsModel.getDescription() + System.getProperty("line.separator") +
                                                    "job info:- " + commonJobsModel.getTitle() + System.getProperty("line.separator") +
                                                    "user profile:- " + userName + System.getProperty("line.separator") + contact + System.getProperty("line.separator") + emailId + System.getProperty("line.separator") +
                                                    "user info:- " + degree + System.getProperty("line.separator") + occupation + System.getProperty("line.separator") +
                                                    "company info:- " + commonJobsModel.getCompanyId() + System.getProperty("line.separator") +
                                                    "signature," + System.getProperty("line.separator") +
                                                    "ugly deals job";
                                            sendMailFunction(msg, subject, userName, contact, emailId);
                                            Constants.hide(progressBar, getActivity());
                                            AppliedFragment appliedFragment = new AppliedFragment();
                                            replaceFragment(appliedFragment);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Constants.hide(progressBar, getActivity());
                                    Log.w("Saved", "Error writing document", e);
                                }
                            });
                        } else {
                            Constants.hide(progressBar, getActivity());
                            Constants.showToast(getActivity(), "Something went wrong...");
                        }
                    }
                });

                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.job_container, fragment);
        fragmentTransaction.commit();
    }

    private void sendMailFunction(String msg, String subject, String userName, String contact, String email) {
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder httpBuider =
                HttpUrl.parse(FIREBASE_CLOUD_FUNCTION_URL).newBuilder();
        httpBuider.addQueryParameter("dest", "gunjanmalviya03@gmail.com");
        httpBuider.addQueryParameter("subject", subject);
        httpBuider.addQueryParameter("message", msg);
        httpBuider.addQueryParameter("jobDescription", commonJobsModel.getDescription() + "");
        httpBuider.addQueryParameter("jobInfo", commonJobsModel.getTitle());
        httpBuider.addQueryParameter("userName", userName);
        httpBuider.addQueryParameter("userEmail", contact);
        httpBuider.addQueryParameter("userContact", email);
        httpBuider.addQueryParameter("companyId", commonJobsModel.getCompanyId() + "");
        Request request = new Request.Builder().
                url(httpBuider.build()).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseBody = response.body().toString();
            }
        });
    }
}
