package org.nothingugly.uglydeals;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {

    private Button buttonVerifyEmail;
    private Button buttonVerifyEmailResend;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        buttonVerifyEmail = findViewById(R.id.buttonVerifyEmail);
        buttonVerifyEmailResend = findViewById(R.id.buttonVerifyEmailResend);
        mAuth = FirebaseAuth.getInstance();

        //Close the verifyEmailActivity
        buttonVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogIn();

            }
        });

        //Resend Email verification
        buttonVerifyEmailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send the email verfication
                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Show dialog box
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyEmailActivity.this);
                        builder.setTitle("Email Verification Sent!");
                        builder.setMessage("An email verification mail has been sent to your email. Please log into your email and follow the link on the verification mail");

                        //If yes
                        builder.setPositiveButton(
                                "Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                });
            }
        });


    }

    //Send to the loginActivity
    public void sendToLogIn() {
        Intent sendToLogIn = new Intent(this, LogInActivity.class);
        startActivity(sendToLogIn);
        finish();
    }

}
