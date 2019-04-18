package org.nothingugly.uglydeals;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    //Initiating variables
    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterConfirmPassword;


    private Button buttonRegisterRegister;
    private Button getButtonRegisterLogin;

    private ProgressBar progressBarRegister;

    //Intiating Firebase Authentication.
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Getting the current date/Time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date ();
        final String currentTime = sdf.format(date);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //Mapping components to variables
        editTextRegisterEmail= (EditText) findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword= (EditText) findViewById(R.id.editTextRegisterPassword);
        editTextRegisterConfirmPassword = (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
        buttonRegisterRegister = (Button) findViewById(R.id.buttonRegisterRegister);
        getButtonRegisterLogin= (Button) findViewById(R.id.buttonRegisterLogin);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);



        //When the login button is clicked
        getButtonRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });


        //When the Register Button is pressed
        buttonRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting values from editTexts
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                String confirmPassword = editTextRegisterConfirmPassword.getText().toString();

                //If all the fields are filled
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword))
                {
                    //Are the passwords matching?
                    if(confirmPassword.equals(password)) {

                        //Show the progressbar
                        progressBarRegister.setVisibility(View.VISIBLE);

                        //Set buttons in invisible
                        buttonRegisterRegister.setVisibility(View.INVISIBLE);
                        getButtonRegisterLogin.setVisibility(View.INVISIBLE);

                        //Execute firebase creat user with retrieved email and password. Setting on complete listener to see if registration is complete
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {

                                //If task is complete, Make the Document
                                if (task.isSuccessful()) {


                                    String userID = mAuth.getUid();
                                    String userEmail = mAuth.getCurrentUser().getEmail();

                                    Map<String, Object> userMap = new HashMap<>();

                                    userMap.put("email", userEmail);
                                    userMap.put("isPhoneVerified", false);
                                    userMap.put("DateOfRegistration", currentTime);
                                    userMap.put("isFreeTrailUsed", false);


                                    //Create the document for the user. Added set on complete listener to wait for writing document.


                                    mFirestore.collection("customers").document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {


                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {



                                                //If document written successfully, send a verification email to email address, and signs the user out. Add an on conplete listener to see if the email was sent successfully
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        //If Email sent successfully, log out and send to main. Make toast to verify email.
                                                        if (task.isSuccessful()) {
                                                            //If email sent successfully, send toast message and sign out user. ACCOUNT IS CREATED
                                                            Toast.makeText(RegisterActivity.this, "Account created. Please verify your email and log in!", Toast.LENGTH_LONG).show();
                                                            mAuth.signOut();
                                                            sendToMain();
                                                        }
                                                    }
                                                });

                                            } else {

                                                //Delete the user, since the account was created but then document wasnt
                                                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "OOPS! Something went wrong please try again", Toast.LENGTH_LONG).show();
                                                            sendToMain();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });


                                }




                                //Else give the user the error message
                                else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    editTextRegisterEmail.setText("");
                                    editTextRegisterPassword.setText("");
                                    editTextRegisterConfirmPassword.setText("");

                                    //Make the progress bar invisible
                                    progressBarRegister.setVisibility(View.INVISIBLE);
                                    //Set buttons in to visible
                                    buttonRegisterRegister.setVisibility(View.VISIBLE);
                                    getButtonRegisterLogin.setVisibility(View.VISIBLE);
                                }


                            }

                        });
                    }

                    //If Password and ConfirmPassword dont match, let the user know
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Password and Confirm Password should match",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    //Whenever registerActivity is opened
    @Override
    protected void onStart() {
        super.onStart();

        //Checks if there is a user logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If yes, send to their main page
        if (currentUser != null)
        {
            sendToMain();
        }
    }


    //Simply close the Register Activity and Start MainActivity.
    private void sendToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    //Send the activity to login .
    private void sendToLogin() {
        Intent loginIntent = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
