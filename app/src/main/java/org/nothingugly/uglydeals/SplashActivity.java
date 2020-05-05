package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private static final String TAG = "SplashScreenActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //currentDate = new Date();



        //Initialise Firebase app
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


    }

    //When ever MainActivity is started. Do these Validations : 1. user already logged in?
    //  2. Email verfied?
    //  3. Is phone Verified?
    //  4. Has a subscription?
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Running Splash ");
        //Retrieve the current logged in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If not user is logged in
        if (currentUser == null)

        //Open loginActivity to login
        {
            sendToLogin();
        }

        //If a user is logged in
        else {
            //If logged in user is not email verified.
            if (!currentUser.isEmailVerified()) {
                Toast.makeText(SplashActivity.this, "Verify your email and log back in", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                sendToLogin();
            } else {
                sendToMain();

            }
        }

          /*  //Checks if the document exists.
            DocumentReference docIdRef = mFirestore.collection("customers").document(currentUser.getUid());
            //Getting that document and addOnCompleteListener
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                //When complete recieves a DocumentSnapShot as the result
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    //If task is successful
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            if (document.get("isPhoneVerified").equals(false)) {
                                mAuth.signOut();
                                sendToPhoneSetup();
                            }
                        }
                    }
                }
            });}*/
    }





    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(this, LogInActivity.class);
        startActivity(loginintent);
        finish();
    }

    private void sendToMain(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
