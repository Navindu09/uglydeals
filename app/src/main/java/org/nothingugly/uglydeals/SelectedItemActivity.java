package org.nothingugly.uglydeals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class SelectedItemActivity extends AppCompatActivity {

    private static final String TAG = "SelectedItemActivity";

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;


    private TextView textViewSelectedItemPartnerName;
    private ImageView imageViewSelectedItemImage;
    private TextView textViewSelectedItemName;
    private TextView textViewSelectedActivityValidity;
    private TextView textViewSelectedItemDescription;
    private Button buttonSelectedItemRedeem;
    private TextView textViewSelectedItemTerms;
    private ProgressBar progressBarSelectedItem;

    private String deal;


    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dealId retrived from the relevant recycler adapter
        String dealId = null;

        //If no id is passed through, id = null
        if(savedInstanceState ==  null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                dealId = (String) extras.get("dealId");
                deal = dealId;
            }
        }else{
            dealId = (String) savedInstanceState.getSerializable("dealId");

        }

        Log.d(TAG, "onCreate: DealId retrieved : "+ dealId);


        //Setting the layout for the activity
        setContentView(R.layout.activity_selected_item);
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Mapping out all the components
        progressBarSelectedItem = (ProgressBar) findViewById(R.id.progressBarSelectedItem);
        textViewSelectedItemPartnerName = (TextView) findViewById(R.id.textViewSelectedItemPartnerName);
        imageViewSelectedItemImage = (ImageView)findViewById(R.id.imageViewSelectedItemImage);
        textViewSelectedItemName = (TextView) findViewById(R.id.textViewSelectedItemName);
        textViewSelectedActivityValidity = (TextView) findViewById(R.id.textViewSelectedActivityValidity);
        textViewSelectedItemDescription = (TextView) findViewById(R.id.textViewSelectedItemDescription);
        buttonSelectedItemRedeem = (Button) findViewById(R.id.buttonSelectedItemRedeem);
        textViewSelectedItemTerms = (TextView) findViewById(R.id.textViewSelectedItemTerms);

        //set all the
        textViewSelectedItemPartnerName.setVisibility(View.INVISIBLE);
        imageViewSelectedItemImage.setVisibility(View.INVISIBLE);
        textViewSelectedItemName.setVisibility(View.INVISIBLE);
        textViewSelectedActivityValidity .setVisibility(View.INVISIBLE);
        textViewSelectedItemDescription .setVisibility(View.INVISIBLE);
        buttonSelectedItemRedeem.setVisibility(View.INVISIBLE);
        textViewSelectedItemTerms.setVisibility(View.INVISIBLE);
        progressBarSelectedItem.setVisibility(View.VISIBLE);

        if (deal != null) {
            //Checking the deals collection for the
            mFireStore.collection("deals").document(deal).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: deal with id found");
                        DocumentSnapshot dealDocument = task.getResult();

                        //Convert the particular document into a Deal object
                        Deal deal = dealDocument.toObject(Deal.class);


                        //Set the deal name into the layout
                        String name = deal.getName();
                        textViewSelectedItemName.setText(name);

                        //Set the photo into the layout
                        String itemImageUrl = deal.getDealPhoto();
                        Glide.with(getApplicationContext()).load(itemImageUrl).into(imageViewSelectedItemImage);

                        //Set the description into the layout
                        String description = deal.getDescription();
                        textViewSelectedItemDescription.setText(description);

                        // Coverting firebase timestamp into easy date format
                        long millisecondValidfrom = deal.getValidFrom().getTime();
                        String validFromString = DateFormat.format("dd/MM/yyyy", new Date(millisecondValidfrom)).toString();

                        long millisecondValidTill = deal.getValidTill().getTime();
                        String validTillString = DateFormat.format("dd/MM/yyyy", new Date(millisecondValidTill)).toString();
                        textViewSelectedActivityValidity.setText(validFromString + " to " + validTillString);

                        //Retrieves the partner ID
                        final String partnerId = (String) deal.getPartnerID();

                        //Checks partners collection for that particular partner
                        mFireStore.collection("partners").document(partnerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                //Save the document instance
                                DocumentSnapshot partnerDocument = task.getResult();
                                String partnerName = (String) partnerDocument.get("name");

                                //Set the name of the partner in the layout
                                textViewSelectedItemPartnerName.setText(partnerName);
                            }
                        });
                    }


                    //Set components visible after data retrieval and then set progressbar invisible.
                    textViewSelectedItemPartnerName.setVisibility(View.VISIBLE);
                    imageViewSelectedItemImage.setVisibility(View.VISIBLE);
                    textViewSelectedItemName.setVisibility(View.VISIBLE);
                    textViewSelectedActivityValidity.setVisibility(View.VISIBLE);
                    textViewSelectedItemDescription.setVisibility(View.VISIBLE);
                    buttonSelectedItemRedeem.setVisibility(View.VISIBLE);
                    textViewSelectedItemTerms.setVisibility(View.VISIBLE);
                    progressBarSelectedItem.setVisibility(View.INVISIBLE);

                }

            });
        } else{
            sendToLogin();
        }



        buttonSelectedItemRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ContextCompat.checkSelfPermission(SelectedItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SelectedItemActivity.this, new String [] {Manifest.permission.CAMERA},PERMISSION_REQUEST);
                    }
                    Intent scanIntent = new Intent (SelectedItemActivity.this, ScanActivity.class);
                    scanIntent.putExtra("dealId", deal);
                    startActivity(scanIntent);
                }
            });
        }
//Checks if there is a user logged in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            sendToLogin();
        }
    }

    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(this, LogInActivity.class);
        startActivity(loginintent);
        finish();
    }

    //If the Back key is pressed, destroy the activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void sendToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}