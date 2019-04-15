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

public class LogInActivity extends AppCompatActivity {

    //Initiating EditText components
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    //Initiating Button components
    private Button buttonLoginLogin;
    private Button buttonLoginRegister;

    private ProgressBar progressBarLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        //Assigning the EditText to the View components
        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword= (EditText) findViewById(R.id.editTextLoginPassword);

        //Assigning the Buttons to the View Components
        buttonLoginLogin = (Button) findViewById(R.id.buttonLoginLogin);
        buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);

        progressBarLogin  =  (ProgressBar) findViewById(R.id.progressBarLogin);

        //When Login button is clicked
        buttonLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextLoginEmail.getText().toString();
                String password = editTextLoginPassword.getText().toString();

                if((!TextUtils.isEmpty(email)) && !TextUtils.isEmpty(password))
                {
                    progressBarLogin.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                sendToMain();
                            }

                            else
                            {
                                String error = task.getException().getMessage();
                                Toast.makeText(LogInActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                            progressBarLogin.setVisibility(View.INVISIBLE);



                        }
                    });
                }

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
        if (currentUser != null)
        {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
