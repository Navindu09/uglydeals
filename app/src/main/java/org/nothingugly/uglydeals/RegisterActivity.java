package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //Initiating variables
    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterConfirmPassword;
    private TextView textViewRegisterLogin;
    private EditText editTextRegisterName;
    private Spinner spinnerWhere;
    private TextView textViewButtonLogin;

    private boolean whereSelected;


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
        final Date date = new Date();


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        whereSelected = false;

        //Set up the Dropdown  menu
        Spinner dropdown = (Spinner) findViewById(R.id.spinnerWhere);
        String[] items = new String[]{"How do you know about us?", "Restaurant", "Facebook", "Instagram", "YouTube", "Friend", "Poster", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        //Mapping components to variables
        editTextRegisterEmail = (EditText) findViewById(R.id.editTextProfileEmail);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextProfileName);
        editTextRegisterConfirmPassword = (EditText) findViewById(R.id.editTextProfileOrganisation);
        buttonRegisterRegister = (Button) findViewById(R.id.buttonProfileSave);
        getButtonRegisterLogin = (Button) findViewById(R.id.buttonRegisterLogin);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarProfile);
        textViewRegisterLogin = (TextView) findViewById(R.id.textViewRegisterLogin);
        editTextRegisterName = (EditText) findViewById(R.id.editTextProfilePhone);
        spinnerWhere = (Spinner) findViewById(R.id.spinnerWhere);
        textViewButtonLogin = (TextView) findViewById(R.id.textViewButtonLogin);

        String selectedSpinnerItem = spinnerWhere.getSelectedItem().toString();

        //When the login button is clicked
        textViewButtonLogin.setOnClickListener(new View.OnClickListener() {
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
                final String name = editTextRegisterName.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                String confirmPassword = editTextRegisterConfirmPassword.getText().toString();
                final String heardFrom = spinnerWhere.getSelectedItem().toString();

                //If all the fields are filled
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && !TextUtils.isEmpty(name) && spinnerWhere.getSelectedItemPosition() != 0) {
                    //Are the passwords matching?
                    if (confirmPassword.equals(password)) {

                        //Show the progressbar
                        progressBarRegister.setVisibility(View.VISIBLE);

                        //Set buttons in invisible
                        buttonRegisterRegister.setVisibility(View.INVISIBLE);
                        textViewButtonLogin.setVisibility(View.INVISIBLE);
                        textViewRegisterLogin.setVisibility(View.INVISIBLE);



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
                                    userMap.put("name", name);
                                    userMap.put("points", 0);
                                    userMap.put("hearFrom", heardFrom);
                                    userMap.put("isPhoneVerified", true);
                                    userMap.put("DateOfRegistration", date);
                                    userMap.put("mobile", "");
                                    userMap.put("degree", "");
                                    userMap.put("organisation", "");
                                    userMap.put("occupation", "");


                                    //Create the document for the user. Added set on complete listener to wait for writing document.

                                    mFirestore.collection("customers").document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {


                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                //If document written successfully, send a verification email to email address, and signs the user out. Add an on conplete listener to see if the email was sent successfully
                                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        //If Email sent successfully, log out and send to main. Send to verify email page.
                                                        if (task.isSuccessful()) {
                                                            sendToVerifyEmail();
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
                                    editTextRegisterName.setText("");


                                    //Make the progress bar invisible
                                    progressBarRegister.setVisibility(View.INVISIBLE);

                                    //Set buttons in to visible
                                    buttonRegisterRegister.setVisibility(View.VISIBLE);
                                    getButtonRegisterLogin.setVisibility(View.VISIBLE);
                                    textViewRegisterLogin.setVisibility(View.VISIBLE);
                                    spinnerWhere.setVisibility(View.INVISIBLE);
                                }
                            }

                        });

                        //If Password and ConfirmPassword don't match, let the user know
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password and Confirm Password should match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please make sure all the fields are completed", Toast.LENGTH_LONG).show();
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
        if (currentUser != null) {
            sendToMain();
        }
    }


    // Register Activity and Start MainActivity.
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

    private void sendToVerifyEmail() {
        Intent verifyEmailIntent = new Intent(this, VerifyEmailActivity.class);
        startActivity(verifyEmailIntent);
        finish();
    }


}
