package org.nothingugly.uglydeals;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Button buttonLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise Firebase app
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);


        //Logout Button pressed
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieves current user and signs out.

                mAuth.signOut();
                sendToLogin();
            }
        });
    }

    //When ever MainActivity is started. Do these Validations : 1. user already logged in?
                                                            //  2. Email verfied?
                                                            //  3. Is phone Verified?
                                                            //  4. Has a subscription?
    @Override
    protected void onStart() {
        super.onStart();

        //Retrieve the current logged in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If not user is logged in
        if(currentUser == null)

        //Open loginActivity to login
        {
            sendToLogin();
        }

        //If a user is logged in
        else {

            //If logged in user is not email verified.
            if (!currentUser.isEmailVerified())
            {
                Toast.makeText(MainActivity.this, "Verify your email and log back in", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                sendToLogin();

            }

            //Checks if the document exists.
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
            });
        }


    }

    //Send to LoginActivity
    private void sendToLogin()
    {
        Intent loginintent= new Intent(this ,LogInActivity.class);
        startActivity(loginintent);
        finish();
    }

    //Send to LoginActivity
    private void sendToPhoneSetup()
    {
        Intent phoneSetupIntent = new Intent(this , PhoneSetupActivity.class);
        startActivity(phoneSetupIntent);
        finish();
    }


}
