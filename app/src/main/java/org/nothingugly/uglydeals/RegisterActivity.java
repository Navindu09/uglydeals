package org.nothingugly.uglydeals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

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
                    if(confirmPassword.equals(password))
                    {
                        //Show the progressbar
                        progressBarRegister.setVisibility(View.VISIBLE);

                        //Execute firebase creat user with retrieved email and password. Setting on complete listener to see if registration is complete
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //If task is complete send the user to their Main Activity
                                if(task.isSuccessful())
                                {
                                    sendToMain();
                                }

                                //Else give the user the error message
                                else
                                {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }

                                //Make the progress bar invisible
                                progressBarRegister.setVisibility(View.INVISIBLE);

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



        //Setting transition animation.
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

    //When back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        sendToLogin();


    }
}
