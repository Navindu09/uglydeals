package org.nothingugly.uglydeals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RedeemSuccessActivity extends AppCompatActivity {

    public static final String TAG = "RedeemSuccessActivity";

    private TextView textViewMessage;
    private TextView textViewDeal;
    private TextView textViewAt;
    private TextView textViewPartnerName;
    private TextView textViewDateTime;
    private TextView textViewNote;
    private Button buttonRedeemSuccessExit;
    private ProgressBar progressBarRedeemSuccess;


    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dealId retrived from the relevant SelectedDeal Acticity
        String dealId = null;

        //If no id is passed through, id = null
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                dealId = null;
            } else {
                dealId = (String) extras.get("dealId");
            }
        } else {
            dealId = (String) savedInstanceState.getSerializable("dealId");
        }

        setContentView(R.layout.activity_redeem_success);

        textViewMessage = (TextView) findViewById(R.id.textViewMessage) ;
        textViewDeal = (TextView) findViewById(R.id.textViewDeal);
        textViewAt = (TextView) findViewById(R.id.textViewAt);
        textViewPartnerName = (TextView) findViewById(R.id.textViewPartnerName);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
        textViewNote = (TextView) findViewById(R.id.textViewNote);
        buttonRedeemSuccessExit = (Button) findViewById(R.id.buttonRedeemSuccessExit);
        progressBarRedeemSuccess = (ProgressBar) findViewById(R.id.progressBarRedeemSuccess);

        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        textViewMessage.setVisibility(View.INVISIBLE);
        textViewDeal.setVisibility(View.INVISIBLE);
        textViewAt.setVisibility(View.INVISIBLE);
        textViewPartnerName.setVisibility(View.INVISIBLE);
        textViewDateTime.setVisibility(View.INVISIBLE);
        textViewNote.setVisibility(View.INVISIBLE);
        buttonRedeemSuccessExit.setVisibility(View.INVISIBLE);
        progressBarRedeemSuccess.setVisibility(View.VISIBLE);

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
                    textViewDeal.setText(name);


                    //String validity = deal.getValidFrom()+ " to " + deal.getValidTill();
                    // textViewSelectedActivityValidity.setText(validity);

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
                            textViewPartnerName.setText(partnerName);
                        }
                    });


                }
                textViewMessage.setVisibility(View.VISIBLE);
                textViewDeal.setVisibility(View.VISIBLE);
                textViewAt.setVisibility(View.VISIBLE);
                textViewPartnerName.setVisibility(View.VISIBLE);
                textViewDateTime.setVisibility(View.VISIBLE);
                textViewNote.setVisibility(View.VISIBLE);
                buttonRedeemSuccessExit.setVisibility(View.VISIBLE);
                progressBarRedeemSuccess.setVisibility(View.INVISIBLE);
            }


        });

        buttonRedeemSuccessExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}