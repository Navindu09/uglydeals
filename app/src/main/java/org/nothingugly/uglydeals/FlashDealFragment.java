package org.nothingugly.uglydeals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import static org.nothingugly.uglydeals.SelectedItemActivity.PERMISSION_REQUEST;

public class FlashDealFragment extends Fragment {

    private static final String TAG = "FlashDealFragment";

    private TextView textViewFlashDealPartnerName;
    private ImageView imageViewFlashDealImage;
    private TextView textViewFlashDealName;
    private TextView textViewFlashDealValidity;
    private TextView textViewFlashDealDescription;
    private TextView textViewFlashDealsTermOfUse;
    private Button buttonFlashDealRedeemButton;
    private TextView textViewFlashDealTerms;
    private ProgressBar progressBarSelectedItem;
    private TextView textViewFlashDealAlreadyUsed;

    private String dealId;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    public FlashDealFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_flash_deal_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        textViewFlashDealName = (TextView) view.findViewById(R.id.textViewFlashDealName);
        imageViewFlashDealImage = (ImageView) view.findViewById(R.id.imageViewFlashDealImage);
        textViewFlashDealPartnerName = (TextView) view.findViewById(R.id.textViewFlashDealPartnerName);
        textViewFlashDealValidity = (TextView) view.findViewById(R.id.textViewFlashDealValidity);
        textViewFlashDealDescription = (TextView) view.findViewById(R.id.textViewFlashDealDescription);
        buttonFlashDealRedeemButton = (Button) view.findViewById(R.id.buttonFlashDealRedeemButton);
        textViewFlashDealTerms = (TextView) view.findViewById(R.id.textViewFlashDealTerms);
        textViewFlashDealAlreadyUsed = (TextView) view.findViewById(R.id.textViewFlashDealAlreadyUsed);
        progressBarSelectedItem = (ProgressBar) view.findViewById(R.id.progressBarSelectedItem);
        textViewFlashDealsTermOfUse = (TextView) view.findViewById(R.id.textViewFlashDealsTermOfUse);

        setLoading();


        mFirestore.collection("flashDeals").whereEqualTo("active", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                try {
                    //Log.d(TAG, "onEvent: query size: " + queryDocumentSnapshots.size());
                    DocumentChange documentChange = queryDocumentSnapshots.getDocumentChanges().get(0);
                    FlashDeal flashDeal = documentChange.getDocument().toObject(FlashDeal.class);

                    String flashDealName = flashDeal.getName();
                    textViewFlashDealName.setText(flashDealName);

                    dealId = flashDeal.getId();

                    String flashDealPartnerName = flashDeal.getPartnerName();
                    textViewFlashDealPartnerName.setText(flashDealPartnerName);

                    long millisecondValidfrom = flashDeal.getValidFrom().getTime();
                    String validFromString = DateFormat.format("dd/MM/yyyy", new Date(millisecondValidfrom)).toString();
                    long millisecondValidTill = flashDeal.getValidTill().getTime();
                    String validTillString = DateFormat.format("dd/MM/yyyy", new Date(millisecondValidTill)).toString();
                    textViewFlashDealValidity.setText(validFromString + " to " + validTillString);

                    String flashDealDescription = flashDeal.getDescription();
                    textViewFlashDealDescription.setText(flashDealDescription);

                    String itemImageUrl = flashDeal.getDealPhoto();
                    try {
                        Glide.with(getActivity().getApplicationContext()).load(itemImageUrl).into(imageViewFlashDealImage);

                    } catch (Exception e1) {
                        Log.d(TAG, "onEvent: " + e);
                    }

                    String termsOfUse = flashDeal.getTermsOfUse();
                    textViewFlashDealTerms.setText(termsOfUse);

                    setDoneLoading();

                    isFlashDealAvailable(dealId);


                } catch (Exception e1) {
                    Log.e(TAG, "onEvent: ", e1);
                    textViewFlashDealPartnerName.setText("No Flash Deals Available");
                    textViewFlashDealPartnerName.setVisibility(View.VISIBLE);
                    progressBarSelectedItem.setVisibility(View.INVISIBLE);
                    buttonFlashDealRedeemButton.setVisibility(View.INVISIBLE);
                }

            }


        });


        buttonFlashDealRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
                }
                Intent scanIntent = new Intent(getActivity(), FlashDealScanActivity.class);
                scanIntent.putExtra("dealId", dealId);
                startActivity(scanIntent);
            }
        });


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        }
    }

    private void sendToLogin() {
        Intent loginintent = new Intent(getActivity(), LogInActivity.class);
        startActivity(loginintent);

    }

    //Hide everything when loading
    public void setLoading() {
        textViewFlashDealName.setVisibility(View.INVISIBLE);
        imageViewFlashDealImage.setVisibility(View.INVISIBLE);
        textViewFlashDealPartnerName.setVisibility(View.INVISIBLE);
        textViewFlashDealValidity.setVisibility(View.INVISIBLE);
        textViewFlashDealDescription.setVisibility(View.INVISIBLE);
        textViewFlashDealTerms.setVisibility(View.INVISIBLE);
        textViewFlashDealsTermOfUse.setVisibility(View.INVISIBLE);
        progressBarSelectedItem.setVisibility(View.VISIBLE);
        //buttonFlashDealRedeemButton.setVisibility(View.INVISIBLE);
    }

    //Show everything when loading complete
    public void setDoneLoading() {
        textViewFlashDealName.setVisibility(View.VISIBLE);
        imageViewFlashDealImage.setVisibility(View.VISIBLE);
        textViewFlashDealPartnerName.setVisibility(View.VISIBLE);
        textViewFlashDealValidity.setVisibility(View.VISIBLE);
        textViewFlashDealDescription.setVisibility(View.VISIBLE);
        textViewFlashDealTerms.setVisibility(View.VISIBLE);
        textViewFlashDealsTermOfUse.setVisibility(View.VISIBLE);

        progressBarSelectedItem.setVisibility(View.INVISIBLE);
    }

    //Whenever the FlashDeal is opened, check if this deals limit has exceeded for the day.
    private void isFlashDealAvailable(String id) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uId = currentUser.getUid();


        mFirestore.collection("customers").document(uId).collection("unavailableFlashDeals").whereEqualTo("unavailableDeal", id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                try {
                    Log.d(TAG, "onEvent: unavailableFlashDeals Query recieved SIZE: " + queryDocumentSnapshots.size());
                    if (queryDocumentSnapshots.size() > 0) {
                        buttonFlashDealRedeemButton.setEnabled(false);
                        buttonFlashDealRedeemButton.setVisibility(View.INVISIBLE);
                        textViewFlashDealAlreadyUsed.setVisibility(View.VISIBLE);

                        Log.d(TAG, "onEvent: FlashDeal is unavailable");
                    } else {
                        Log.d(TAG, "onEvent: this FlashDeal is available ");
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "isDealAvailable: ", ex);
                }
            }
        });

    }
}
