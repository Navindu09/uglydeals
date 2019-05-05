package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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


public class SelectedItemActivity extends AppCompatActivity {

    private static final String TAG = "SelectedItemActivity";

    private TextView textViewSelectedItemPartnerName;
    private ImageView imageViewSelectedItemImage;
    private TextView textViewSelectedItemName;
    private TextView textViewSelectedActivityValidity;
    private TextView textViewSelectedItemDescription;
    private Button buttonSelectedItemRedeem;
    private TextView textViewSelectedItemTerms;
    private ProgressBar progressBarSelectedItem;

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
                dealId = null;
            } else {
                dealId = (String) extras.get("dealId");
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

            //Checking the deals collection for the
            mFireStore.collection("deals").document(dealId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

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



                        //String validity = deal.getValidFrom()+ " to " + deal.getValidTill();
                       // textViewSelectedActivityValidity.setText(validity);

                        //Retrieves the partner ID
                        final String partnerId = (String) deal.getPartnerID();

                        //Checks partners collection for that particular partner
                        mFireStore.collection("partners").document(partnerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                DocumentSnapshot partnerDocument = task.getResult();
                                String partnerName = (String) partnerDocument.get("name");

                                //Set the name of the partner in the layout
                                textViewSelectedItemPartnerName.setText(partnerName);

                            }
                        });
                    }

                    textViewSelectedItemPartnerName.setVisibility(View.VISIBLE);
                    imageViewSelectedItemImage.setVisibility(View.VISIBLE);
                    textViewSelectedItemName.setVisibility(View.VISIBLE);
                    textViewSelectedActivityValidity .setVisibility(View.VISIBLE);
                    textViewSelectedItemDescription .setVisibility(View.VISIBLE);
                    buttonSelectedItemRedeem.setVisibility(View.VISIBLE);
                    textViewSelectedItemTerms.setVisibility(View.VISIBLE);
                    progressBarSelectedItem.setVisibility(View.INVISIBLE);

                }



            });



        }

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
}