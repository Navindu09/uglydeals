package org.nothingugly.uglydeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Button buttonLogout;

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private Date currentDate;


    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NotificationsFragment notificationsFragment;
    private AccountFragment accountFragment;
    private FlashDealFragment flashDealFragment;

    //private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDate = new Date();


        //Initialise Firebase app
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        // Setting up Fragments
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notificationsFragment = new NotificationsFragment();
        accountFragment = new AccountFragment();
        flashDealFragment = new FlashDealFragment();


        //Setting up navigation.
        bottomNavigation = findViewById(R.id.bottomNavigation);


        //When main activity starts, the home fragment is shown
        replaceFragment(homeFragment);


        //When the bottom navigation buttons are clicked
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    //if home button clicked, replace fragment with home
                    case (R.id.bottomNavigationHome):

                        replaceFragment(homeFragment);
                        return true;
                    //if Search button clicked, replace fragment with search
                    case (R.id.bottomNavigationSearch):

                        replaceFragment(searchFragment);
                        return true;
                    //if notification button clicked, replace fragment with Notification
                   /* case (R.id.bottomNavigationNotification):

                        replaceFragment(notificationsFragment);
                        return true;*/

                    //if account button clicked, replace fragment with account
                    case (R.id.bottomNavigationAccount):

                        replaceFragment(accountFragment);
                        return true;
                    //If Flashdeal logo is clicked replace fragment with flasdeal

                    case (R.id.bottomNavigationNotificationFlashDeal):

                        replaceFragment(flashDealFragment);
                        return true;

                    default:
                        return false;

                }
            }
        });


    }

    //When ever MainActivity is started. Do these Validations :
    //  1. user already logged in?
    //  2. Email verfied?
    //  3. Is phone Verified?
    //  4. Has a subscription?
    @Override
    protected void onStart() {
        super.onStart();


        //Retrieve the current logged in user
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        //If not user is logged in
        if (currentUser == null)

        //Open loginActivity to login
        {
            sendToLogin();
        }

        //If a user is logged in, also checks if it is a customer account.
        else {
            DocumentReference docIdRef = mFirestore.collection("customers").document(currentUser.getUid());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "User is a customer!");

                            if (!currentUser.isEmailVerified()) {

                                sendToVerifyEmail();

                            } else {
                                resumeDeals();

                                //This checks if all the profile details are filled in at all times to proceed using the app
                                if (document.get("degree") == ""
                                        || document.get("mobile") == ""
                                        || document.get("name") == ""
                                        || document.get("occupation") == ""
                                        || document.get("organisation") == "") {

                                    sendToProfileActivity();
                                    Toast.makeText(MainActivity.this, "Please fill in all the fields to proceed", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } else {
                            Log.d(TAG, "User is not a customer, Logging out");
                            Toast.makeText(MainActivity.this, "The corresponding account is not a customer account. Please create a customer account", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            sendToLogin();
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });


        }

    }

    //Check if there are unavailable deals for the user. Then refresh the dates to make it available of possible.
    public void resumeDeals() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final String uId = currentUser.getUid();
        try {
            mFirestore.collection("customers").document(uId).collection("unavailableDeals").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                    try {
                        Log.d(TAG, "onEvent: Number of unavailableDeals: " + queryDocumentSnapshots.size());
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                DocumentSnapshot dealSnapshot = doc.getDocument();
                                final String documentID = dealSnapshot.getId();
                                final String dealId = (String) dealSnapshot.get("unavailableDeal"); // Not necessary
                                System.out.println(dealId);// ,,
                                Date resumeDate = (Date) dealSnapshot.get("dealResumeDate");

                                if (currentDate.after(resumeDate)) {
                                    mFirestore.collection("customers").document(uId).collection("unavailableDeals").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: " + dealId + " has been resumed and deleted from user");

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document, has not resumed", e);
                                        }
                                    });


                                } else {
                                    Log.d(TAG, "onEvent: " + dealId + " Can't be resumed");
                                }

                            }
                        }
                    } catch (NullPointerException x) {
                        Log.e(TAG, "onEvent: ", x);
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "resumeDeals: ", e);
        }
    }


    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(this, LogInActivity.class);
        startActivity(loginintent);
        //finish();
    }

    //Send to LoginActivity
    private void sendToPhoneSetup() {
        Intent phoneSetupIntent = new Intent(this, PhoneSetupActivity.class);
        startActivity(phoneSetupIntent);
        finish();
    }


    //Replacing fragment
    private void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment);
        fragmentTransaction.commit();
    }

    //Send to verifyEmail Activity
    private void sendToVerifyEmail() {
        Intent verifyEmailIntent = new Intent(this, VerifyEmailActivity.class);
        startActivity(verifyEmailIntent);
        finish();
    }

    private void sendToProfileActivity() {
        Intent ForceProfileActivity = new Intent(this, ForceProfileActivity.class);
        startActivity(ForceProfileActivity);

    }


}
