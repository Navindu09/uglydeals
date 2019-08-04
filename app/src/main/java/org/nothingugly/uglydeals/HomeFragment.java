package org.nothingugly.uglydeals;


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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private DiscreteScrollView featuredRecyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private List<Deal> featuredList;
    private List<Deal> dealList1;
    private List<Deal> dealList2;

    private FirebaseFirestore mFirestore;

    private SwipeRefreshLayout swipeRefereshLayout;

    private FeaturedDealRecyclerAdapter featuredDealRecyclerAdapter;
    private DealRecyclerAdapter dealRecyclerAdapter1;
    private DealRecyclerAdapter dealRecyclerAdapter2;

    private TextView textViewHeaderFeatured;
    private TextView textViewHeaderNearMe;
    private TextView textViewHeaderAll;

    private ProgressBar progressBarHomeFragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBarHomeFragment = (ProgressBar) view.findViewById(R.id.progressBarHomeFragment);
        progressBarHomeFragment.setVisibility(View.VISIBLE);

        //Initialising the list of deals
        featuredList = new ArrayList<>();
        dealList1 = new ArrayList<>();
        dealList2 = new ArrayList<>();

        swipeRefereshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

        textViewHeaderFeatured = (TextView) view.findViewById(R.id.textViewHeaderFeatured);
        textViewHeaderNearMe = (TextView) view.findViewById(R.id.textViewHeaderNearMe);
        textViewHeaderAll = (TextView) view.findViewById(R.id.textViewHeaderAll);


        AdView adView = (AdView) view.findViewById(R.id.adView);

        MobileAds.initialize(getActivity().getBaseContext(),
                "ca-app-pub-9409818967408705/7852596485");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateDealLists();
                swipeRefereshLayout.setRefreshing(false);
            }
        }

        );


        textViewHeaderAll.setVisibility(View.INVISIBLE);
        textViewHeaderFeatured.setVisibility(View.INVISIBLE);
        textViewHeaderNearMe.setVisibility(View.INVISIBLE);

        //Mapping the layout component
        featuredRecyclerView =  view.findViewById(R.id.featuredRecyclerView);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        featuredDealRecyclerAdapter = new FeaturedDealRecyclerAdapter(featuredList);
        dealRecyclerAdapter1 = new DealRecyclerAdapter(dealList1);
        dealRecyclerAdapter2 = new DealRecyclerAdapter(dealList2);


        //featuredRecyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        featuredRecyclerView.setAdapter(featuredDealRecyclerAdapter);
        recyclerView1.setAdapter(dealRecyclerAdapter1);
        recyclerView2.setAdapter(dealRecyclerAdapter2);

        //Initialising the list of deals
       updateDealLists();


        //REturning view type object
        return view;



    }

    public void updateDealLists(){

        featuredList.clear();
        dealList1.clear();
        dealList2.clear();

        mFirestore = FirebaseFirestore.getInstance();

            mFirestore.collection("deals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isComplete()){


                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Deal deal = doc.toObject(Deal.class);

                               try{
                                if(deal.getActive()){
                                    Log.d(TAG, " Active Deal:  " + deal.getId());
                                    if (deal.getMainAd() ) {
                                        featuredList.add(deal);
                                        featuredDealRecyclerAdapter.notifyDataSetChanged();

                                    }

                                    dealList1.add(deal);
                                    dealList2.add(deal);


                                    dealRecyclerAdapter1.notifyDataSetChanged();
                                    dealRecyclerAdapter2.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, " Deal not Active:  " + deal.getId());
                                    deal = null;

                                }
                                /* else {
                                    Log.d(TAG, deal.getId() + " is not active", task.getException());
                                            
                                }*/
                               } catch (Exception e){
                                   Log.e(TAG, "updateDealList / onCreateView / DealId :  " + deal.getId() , e );
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

}
