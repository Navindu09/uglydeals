package org.nothingugly.uglydeals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneSetupActivity extends AppCompatActivity {

    private EditText  editTextPhoneSetupPhone;
    private EditText  editTextPhoneSetupCode;


    private Button buttonPhoneSetupGetCode;
    private Button buttonPhoneLogout;

    private FirebaseAuth mAuth;
    AuthCredential credential = PhoneAuthProvider.getCredential("","");

   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_setup);

        editTextPhoneSetupPhone = (EditText) findViewById(R.id.editTextPhoneSetupPhone);
        editTextPhoneSetupCode= (EditText) findViewById(R.id.editTextPhoneSetupPhoneCode);

        buttonPhoneSetupGetCode= (Button) findViewById(R.id.buttonPhoneSetupGetCode);
        buttonPhoneLogout = (Button) findViewById(R.id.buttongPhoneLogout);

        mAuth = FirebaseAuth.getInstance();




        // When GetCode button is pressed
        buttonPhoneSetupGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //PhoneEditText disabled
                editTextPhoneSetupCode.setEnabled(false);
                //set
                //buttonPhoneSetupGetCode.setVisibility(View.INVISIBLE);
                String phoneNumber = editTextPhoneSetupPhone.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        PhoneSetupActivity.this,
                        mCallBacks);

            }
        });







        buttonPhoneLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                sendToLogin();

            }
        });




        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }


        };

    }

    //Send to LoginActivity
    private void sendToLogin()
    {
        Intent loginintent= new Intent(this ,LogInActivity.class);
        startActivity(loginintent);
        finish();
    }

}
