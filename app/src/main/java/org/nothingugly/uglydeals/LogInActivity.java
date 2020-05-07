package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;

public class LogInActivity extends AppCompatActivity {

    //Initiating EditText components
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    //Initiating Button components
    private Button buttonLoginLogin;
    private Button buttonLoginRegister;

    private TextView textViewRegister;
    private TextView textViewLoginResetPassword;

    private ProgressBar progressBarLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        //Assigning the EditText to the View components
        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);

        //Assigning the Buttons to the View Components
        buttonLoginLogin = (Button) findViewById(R.id.buttonLoginLogin);
        buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);

        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        textViewLoginResetPassword = (TextView) findViewById(R.id.textViewLoginResetPassword);

        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

        //When Login button is clicked
        buttonLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextLoginEmail.getText().toString();
                String password = editTextLoginPassword.getText().toString();

                //If the bars are filled
                if ((!TextUtils.isEmpty(email)) && !TextUtils.isEmpty(password)) {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    buttonLoginLogin.setEnabled(false);
                    buttonLoginRegister.setEnabled(false);

                    buttonLoginLogin.setVisibility(View.INVISIBLE);
                    buttonLoginRegister.setVisibility(View.INVISIBLE);

                    textViewRegister.setVisibility(View.INVISIBLE);
                    textViewLoginResetPassword.setVisibility(View.INVISIBLE);

                    //Firebase Authentication in progress
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        //When the user is signed in.
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendToMain();
                            }

                            //If user is not authenitcated
                            else {
                                String error = task.getException().getMessage();
                                Toast.makeText(LogInActivity.this, error, Toast.LENGTH_LONG).show();

                                progressBarLogin.setVisibility(View.INVISIBLE);
                                buttonLoginLogin.setEnabled(true);
                                buttonLoginRegister.setEnabled(true);

                                buttonLoginLogin.setVisibility(View.VISIBLE);
                                buttonLoginRegister.setVisibility(View.VISIBLE);

                                textViewRegister.setVisibility(View.VISIBLE);
                                textViewLoginResetPassword.setVisibility(View.VISIBLE);

                            }


                        }
                    });
                }

            }
        });

        textViewLoginResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToPasswordReset();
            }
        });


        //When Register button is clicked
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    //Whenever this activity is started
    @Override
    protected void onStart() {
        super.onStart();

        //Takes the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If there is a user logged in, Go to MainAtivity.
        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(this, JobPortActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToPasswordReset() {
        Intent passwordReset = new Intent(this, PasswordResetActivity.class);
        startActivity(passwordReset);
        finish();
    }
}
