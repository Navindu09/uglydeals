package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PointsActivity extends AppCompatActivity {

    private static final String TAG = "PointsActivity";

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private TextView textViewNumberOfPoints;
    private Button buttonPointsGotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textViewNumberOfPoints = (TextView) findViewById(R.id.textViewNumberOfPoints);
        buttonPointsGotIt = (Button) findViewById(R.id.buttonPointsGotIt);


        String userId = mAuth.getCurrentUser().getUid();

        //Gets the number of points of the user and displays it on the activity
        mFirestore.collection("customers").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    try {

                        Long userPoints = (Long) documentSnapshot.get("points");
                        textViewNumberOfPoints.setText(userPoints.toString());


                    } catch (NullPointerException e) {
                        Toast.makeText(PointsActivity.this, "Please contact Ugly Deals Support regarding your points", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onComplete: ");
                    }
                }
            }
        });

        //Close the activity when button is pressed.
        buttonPointsGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        }
    }

    //Send the activity to login .
    private void sendToLogin() {
        Intent loginIntent = new Intent(PointsActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
