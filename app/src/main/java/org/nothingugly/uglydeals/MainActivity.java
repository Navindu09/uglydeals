package org.nothingugly.uglydeals;

import android.content.Intent;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationMenu = findViewById(R.id.bottomNavigationMenu);



        Intent intent = new Intent( this, LogInActivity.class);
        startActivity(intent);
    }
}
