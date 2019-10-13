package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {

    private Button buttonVerifyEmail;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        buttonVerifyEmail = findViewById(R.id.buttonVerifyEmail);
        mAuth = FirebaseAuth.getInstance();

        buttonVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogIn();

            }
        });

    }

    public void sendToLogIn(){
        Intent sendToLogIn = new Intent(this, LogInActivity.class);
        startActivity(sendToLogIn);
        finish();
    }

}
