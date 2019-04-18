package org.nothingugly.uglydeals;

import android.content.Intent;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
                FirebaseUser currentUser = mAuth.getCurrentUser();
                mAuth.signOut();
                sendToLogin();
            }
        });
    }

    //When ever MainActivity is started
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

        else {
            if (!currentUser.isEmailVerified())
            {
                Toast.makeText(MainActivity.this, "Verify your email and log back in", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                sendToLogin();

            }
        }


    }

    //Send to LoginActivity
    private void sendToLogin()
    {
        Intent loginintent = new Intent(this ,LogInActivity.class);
        startActivity(loginintent);
        finish();
    }


}
