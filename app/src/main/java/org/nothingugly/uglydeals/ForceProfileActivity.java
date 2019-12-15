package org.nothingugly.uglydeals;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ForceProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private EditText editTextProfileEmail;
    private EditText editTextProfilePhone;
    private EditText editTextProfileName;
    private EditText editTextProfileOrganisation;
    private EditText editTextProfileDegree;
    private EditText getEditTextProfileOccupation;

    private TextView textViewProfileName;

    private Button buttonProfileSave;
    private ProgressBar progressBarProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_profile);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        editTextProfileEmail = (EditText) findViewById(R.id.editTextProfileEmail);
        editTextProfilePhone = (EditText) findViewById(R.id.editTextProfilePhone);
        getEditTextProfileOccupation = (EditText) findViewById(R.id.getEditTextProfileOccupation);
        textViewProfileName = (TextView) findViewById(R.id.textViewProfileName);

        editTextProfileName = (EditText) findViewById(R.id.editTextProfileName);

        editTextProfileOrganisation = (EditText) findViewById(R.id.editTextProfileOrganisation);

        editTextProfileDegree = (EditText) findViewById(R.id.editTextProfileDegree);

        buttonProfileSave = (Button) findViewById(R.id.buttonProfileSave);
        progressBarProfile = (ProgressBar) findViewById(R.id.progressBarProfile);

        buttonProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextProfilePhone.getText().toString().equals("")
                        || editTextProfileName.getText().toString().equals("")
                        || editTextProfileOrganisation.getText().toString().equals("")
                        || editTextProfileDegree.getText().toString().equals("")
                        || getEditTextProfileOccupation.getText().toString().equals("")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForceProfileActivity.this);
                    builder1.setTitle("Empty Field");
                    builder1.setMessage("Please fill in all the details to proceed");

                    builder1.setPositiveButton(
                            "Okay!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {

                    setLoading();
                    mFirestore.collection("customers").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                String mobile = editTextProfilePhone.getText().toString();
                                String name = editTextProfileName.getText().toString();
                                String occupation = getEditTextProfileOccupation.getText().toString();
                                String organisation = editTextProfileOrganisation.getText().toString();
                                String degree = editTextProfileDegree.getText().toString();

                                Map<String, Object> profileMap = new HashMap<>();

                                profileMap.put("mobile", mobile);
                                profileMap.put("name", name);
                                profileMap.put("occupation", occupation);
                                profileMap.put("organisation", organisation);
                                profileMap.put("degree", degree);

                                doc.getReference().update(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            setDoneLoading();
                                            Log.d(TAG, "SaveButton.onClick.Success: Document Updated Successfully");
                                            Toast.makeText(ForceProfileActivity.this, "Your information has been saved!", Toast.LENGTH_SHORT).show();
                                            finish();


                                        } else {

                                            setDoneLoading();
                                            Log.e(TAG, "SaveButton.onClick.Unsuccess: Document Update failed", task.getException());

                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(ForceProfileActivity.this, "Document Error: Please contact Ugly Deals Support for help", Toast.LENGTH_LONG).show();
                                setDoneLoading();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            //send to Log in
        }
        String currentUserUid = mAuth.getCurrentUser().getUid();

        mFirestore.collection("customers").document(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    try {
                        textViewProfileName.setText(doc.getString("name"));
                        editTextProfileEmail.setText(doc.get("email").toString());
                        editTextProfileName.setText(doc.get("name").toString());
                        editTextProfileDegree.setText(doc.get("degree").toString());
                        editTextProfileOrganisation.setText(doc.get("organisation").toString());
                        editTextProfilePhone.setText(doc.get("mobile").toString());
                        getEditTextProfileOccupation.setText(doc.get("occupation").toString());

                    } catch (NullPointerException e) {

                        Log.e(TAG, "onComplete: Error getting information " + e.toString());
                        Toast.makeText(ForceProfileActivity.this, "Document Error: Please contact Ugly Deals Support for help", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(ForceProfileActivity.this, "Document Error: Please contact Ugly Deals Support for help", Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    //When loading, sets
    public void setLoading() {
        buttonProfileSave.setVisibility(View.INVISIBLE);
        progressBarProfile.setVisibility(View.VISIBLE);
        editTextProfileName.setEnabled(false);
        editTextProfileDegree.setEnabled(false);
        editTextProfileOrganisation.setEnabled(false);
        editTextProfilePhone.setEnabled(false);
        getEditTextProfileOccupation.setEnabled(false);


    }

    public void setDoneLoading() {
        buttonProfileSave.setVisibility(View.VISIBLE);
        progressBarProfile.setVisibility(View.INVISIBLE);
        editTextProfileName.setEnabled(true);
        editTextProfileDegree.setEnabled(true);
        editTextProfileOrganisation.setEnabled(true);
        editTextProfilePhone.setEnabled(true);
        getEditTextProfileOccupation.setEnabled(true);
    }


}
