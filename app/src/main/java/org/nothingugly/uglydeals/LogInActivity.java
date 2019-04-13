package org.nothingugly.uglydeals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    //Initiating EditText components
    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    //Initiaing Button components
    private Button buttonLoginLogin;
    private Button buttonLoginRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Assigning the EditText to the View components
        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword= (EditText) findViewById(R.id.editTextLoginPassword);

        //Assigning the Buttons to the View Components
        buttonLoginLogin = (Button) findViewById(R.id.buttonLoginLogin);
        buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);

        //When Register button is clicked
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });



    }

}
