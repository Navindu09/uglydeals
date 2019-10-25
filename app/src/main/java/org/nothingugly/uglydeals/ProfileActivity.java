package org.nothingugly.uglydeals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private EditText editTextProfileEmail;
    private EditText editTextProfilePhone;
    private EditText editTextProfileName;
    private EditText editTextProfileSchool;
    private EditText editTextProfileAreaOfStudy;

    private Button buttonProfileSave;
    private ProgressBar progressBarProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        editTextProfileEmail = (EditText) findViewById(R.id.editTextProfileEmail);
        editTextProfilePhone = (EditText) findViewById(R.id.editTextProfilePhone);
        ;
        editTextProfileName = (EditText) findViewById(R.id.editTextProfileName);
        ;
        editTextProfileSchool = (EditText) findViewById(R.id.editTextProfileSchool);
        ;
        editTextProfileAreaOfStudy = (EditText) findViewById(R.id.editTextProfileAreaOfStudy);

        buttonProfileSave = (Button) findViewById(R.id.buttonProfileSave);
        progressBarProfile = (ProgressBar) findViewById(R.id.progressBarProfile);

       /* buttonProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map <String, Object> profileMap = new HashMap<>();


            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            //send to Log in
        }
        String currentUserUid = mAuth.getCurrentUser().getUid().toString();


            mFirestore.collection("customers").document(currentUserUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        editTextProfileEmail.setText(doc.get("email").toString());
                        editTextProfileName.setText(doc.get("name").toString());

                    } else {
                        Toast.makeText(ProfileActivity.this, "Document Error: Please contact Ugly Deals Support for help", Toast.LENGTH_LONG).show();

                    }

                }
            });


    }
}
