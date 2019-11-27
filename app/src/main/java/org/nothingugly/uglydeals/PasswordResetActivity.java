package org.nothingugly.uglydeals;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordResetAct";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private TextView textViewText;
    private EditText editTextPasswordResetEmail;
    private Button buttonPasswordResetConfirm;
    private ProgressBar progressBarResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        textViewText = (TextView) findViewById(R.id.textViewText);
        editTextPasswordResetEmail = (EditText) findViewById(R.id.editTextPasswordResetEmail);
        buttonPasswordResetConfirm = (Button) findViewById(R.id.buttonPasswordResetConfirm);
        progressBarResetPassword = (ProgressBar) findViewById(R.id.progressBarResetPassword);

        buttonPasswordResetConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLoading();

                String email = editTextPasswordResetEmail.getText().toString().trim();
                try {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Send to new Actvity

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PasswordResetActivity.this);
                                builder1.setTitle("Password reset email sent!");
                                builder1.setMessage("Please Check your email and follow the link to reset password");

                                builder1.setPositiveButton(
                                        "Got it!",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                setDoneLoading();
                                                sendToLogin();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PasswordResetActivity.this);
                                builder1.setTitle("Password reset failed");
                                builder1.setMessage(task.getException().getMessage());

                                builder1.setPositiveButton(
                                        "Got it!",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                setDoneLoading();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();


                            }
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "onClick: Caught Exception at ResetPassword");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PasswordResetActivity.this);
                    builder1.setTitle("Password reset failed");
                    builder1.setMessage(e.getMessage());

                    builder1.setPositiveButton(
                            "Got it!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    setDoneLoading();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }
        });

    }


    //When loading, sets
    public void setLoading() {
        buttonPasswordResetConfirm.setVisibility(View.INVISIBLE);
        progressBarResetPassword.setVisibility(View.VISIBLE);
        editTextPasswordResetEmail.setEnabled(false);
    }

    public void setDoneLoading() {
        buttonPasswordResetConfirm.setVisibility(View.VISIBLE);
        progressBarResetPassword.setVisibility(View.INVISIBLE);
        editTextPasswordResetEmail.setEnabled(true);
    }

    public void sendToLogin() {

        Intent loginIntent = new Intent(PasswordResetActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
