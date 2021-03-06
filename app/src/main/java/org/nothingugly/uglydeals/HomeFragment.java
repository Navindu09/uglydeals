package org.nothingugly.uglydeals;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView featuredRecyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private List<Deal> featuredList;
    private List<Deal> dealList1;
    private List<Deal> dealList2;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private SwipeRefreshLayout swipeRefereshLayout;

    private FeaturedDealRecyclerAdapter featuredDealRecyclerAdapter;
    private DealRecyclerAdapter dealRecyclerAdapter1;
    private DealRecyclerAdapter dealRecyclerAdapter2;
    //private FlashDealRecyclerAdapter flashDealRecyclerAdapter;

    private TextView textViewHeaderFeatured;
    private TextView textViewHeaderNearMe;
    private TextView textViewHeaderAll;

    private Long points;
    private String userId;

    private Button buttonPoints;

    private ProgressBar progressBarHomeFragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();
            this.userId = userId;
            Log.d(TAG, "onCreateView: *************" + userId);
        } else {
            sendToLogin();
            Log.d(TAG, "onCreateView: No user found, sending to login");
        }


        /*try{
            String uId = mAuth.getCurrentUser().getUid();
            userId = uId;
        } catch (NullPointerException e){
            Log.d(TAG, "onCreateView: " + e);

        }
*/
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBarHomeFragment = (ProgressBar) view.findViewById(R.id.progressBarHomeFragment);
        progressBarHomeFragment.setVisibility(View.VISIBLE);

        //Initialising the list of deals
        featuredList = new ArrayList<>();
        dealList1 = new ArrayList<>();
        dealList2 = new ArrayList<>();

       // swipeRefereshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        textViewHeaderFeatured = (TextView) view.findViewById(R.id.textViewHeaderFeatured);
        textViewHeaderNearMe = (TextView) view.findViewById(R.id.textViewHeaderNearMe);
        textViewHeaderAll = (TextView) view.findViewById(R.id.textViewHeaderAll);

        buttonPoints = (Button) view.findViewById(R.id.buttonPoints);


        AdView adView = (AdView) view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity().getBaseContext(),
                "ca-app-pub-9409818967408705/7852596485");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

       /* swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {

                                                         updateDealLists();
                                                         swipeRefereshLayout.setRefreshing(false);
                                                     }
                                                 }

        );*/


        textViewHeaderAll.setVisibility(View.INVISIBLE);
        textViewHeaderFeatured.setVisibility(View.INVISIBLE);
        textViewHeaderNearMe.setVisibility(View.INVISIBLE);

        //Mapping the layout component
        featuredRecyclerView = view.findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        //Instantiating the recycler adapters with the relevant lists. See RecyclerAdapter.Constructor
        featuredDealRecyclerAdapter = new FeaturedDealRecyclerAdapter(featuredList);
        dealRecyclerAdapter1 = new DealRecyclerAdapter(dealList1);
        dealRecyclerAdapter2 = new DealRecyclerAdapter(dealList2);


        featuredRecyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);


        featuredRecyclerView.setAdapter(featuredDealRecyclerAdapter);
        recyclerView1.setAdapter(dealRecyclerAdapter1);
        recyclerView2.setAdapter(dealRecyclerAdapter2);


        //Update the number of points shown to the user
        try {
            mFirestore.collection("customers").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    points = (Long) documentSnapshot.get("points");

                    try {

                        buttonPoints.setText(points.toString());

                    } catch (Exception e) {
                        Log.d(TAG, "onComplete: " + e);
                    }

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: " + e);
        }


        buttonPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToPointsActivity();
            }
        });


        //Initialising the list of deals
        updateDealLists();


        //REturning view type object
        return view;


    }

    public void updateDealLists() {

        featuredList.clear();
        dealList1.clear();
        dealList2.clear();

        updatesFeaturedList();

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("deals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {


                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Deal deal = doc.toObject(Deal.class);

                        try {
                            if (deal.getActive()) {
                                Log.d(TAG, " Active Deal:  " + deal.getId());

                                if (deal.getCategory() == 1) {
                                    dealList1.add(deal);
                                    dealRecyclerAdapter1.notifyDataSetChanged();
                                }

                                dealList2.add(deal);
                                dealRecyclerAdapter2.notifyDataSetChanged();


                            } else {
                                // Log.d(TAG, " Deal not Active:  " + deal.getId());
                                deal = null;

                            }

                        } catch (Exception e) {
                            Log.e(TAG, "updateDealList / onCreateView / DealId :  " + deal.getId(), e);
                        }
                    }


                    progressBarHomeFragment.setVisibility(View.INVISIBLE);
                    textViewHeaderNearMe.setVisibility(View.VISIBLE);
                    textViewHeaderFeatured.setVisibility(View.VISIBLE);
                    textViewHeaderAll.setVisibility(View.VISIBLE);


                }
            }
        });


    }

    public void sendToPointsActivity() {
        Intent pointsIntent = new Intent(getContext(), PointsActivity.class);
        startActivity(pointsIntent);
    }

    public void sendToLogin() {
        Intent loginIntent = new Intent(getContext(), LogInActivity.class);
        startActivity(loginIntent);

    }

    //This methods takes the add the last five deals on UD and shows it as a featuredItem
    public void updatesFeaturedList() {

        final Map<Date, String> dateAndIdMap = new HashMap<>();
        final ArrayList<String> lastFiveDeals = new ArrayList<>();
        final ArrayList<Deal> dealList = new ArrayList<>();

        //Gets all the deals that are active
        try {
            mFirestore.collection("deals").whereEqualTo("active", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    //For each deal takes the dealTimeStamp and ID then put it in a Map
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        Date dealTimestamp = (Date) doc.getDocument().get("dealTimeStamp");
                        String id = (String) doc.getDocument().get("id");

                        dateAndIdMap.put(dealTimestamp, id);
                    }

                    /*for (Date d : dateAndIdMap.keySet()) {
                        System.out.println(d.toString());
                    }*/

                    //Initialise a Tree map
                    Map<Date, String> sortedMap = new TreeMap<>(Collections.reverseOrder());

                    //Put all the sorted entries in to the Map. This sorts it against the keys of the map
                    sortedMap.putAll(dateAndIdMap);


                    int count = 0;
                    for (Map.Entry<Date, String> entry : sortedMap.entrySet()) {
                        //System.out.println(entry.getValue());
                        if (count >= 5) break;

                        //Add all the deals to an arrayList that holds only ID's of the deal
                        lastFiveDeals.add(entry.getValue());
                        System.out.println("*************13" + entry.getKey());
                        count++;
                    }

                    /*for (String i : lastFiveDeals) {
                        System.out.println("*****" + i);
                    }*/


                    //Uses the String id arraylist and fetches each Deal from the database
                    try {
                        for (int i = 0; i < 5; i++) {
                            mFirestore.collection("deals").document(lastFiveDeals.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot doc = task.getResult();

                                    //Convert the Document to a Deal Object
                                    Deal deal = doc.toObject(Deal.class);
                                    //Adds the deal to the FeaturedList
                                    featuredList.add(deal);

                                    featuredDealRecyclerAdapter.notifyDataSetChanged();

                                }
                            });
                        }
                    } catch (IndexOutOfBoundsException indexE) {
                        Log.d(TAG, "onEvent: " + indexE);
                    }


                }
            });
        } catch (Exception e) {
            Log.d(TAG, "updatesFeaturedList: ]" + e);
        }


    }

}
