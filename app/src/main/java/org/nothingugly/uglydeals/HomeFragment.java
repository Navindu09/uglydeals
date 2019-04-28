package org.nothingugly.uglydeals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private List<Deal> dealList;
    private List<Deal> dealList1;
    private List<Deal> dealList2;

    private FirebaseFirestore mFirestore;

    private FeaturedDealRecyclerAdapter dealRecyclerAdapter;
    private DealRecyclerAdapter dealRecyclerAdapter1;
    private DealRecyclerAdapter dealRecyclerAdapter2;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //Initialising the list of deals
        dealList = new ArrayList<>();
        dealList1 = new ArrayList<>();
        dealList2 = new ArrayList<>();


        //Mapping the layout component
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView2);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        dealRecyclerAdapter = new FeaturedDealRecyclerAdapter(dealList);
        dealRecyclerAdapter1 = new DealRecyclerAdapter(dealList1);
        dealRecyclerAdapter2 = new DealRecyclerAdapter(dealList2);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        recyclerView.setAdapter(dealRecyclerAdapter);
        recyclerView1.setAdapter(dealRecyclerAdapter1);
        recyclerView2.setAdapter(dealRecyclerAdapter2);



        //Initialising the list of deals
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("deals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                {
                    if (doc.getType() == DocumentChange.Type.ADDED)
                    {
                        Deal deal = doc.getDocument().toObject(Deal.class);
                        dealList.add(deal);
                        dealList1.add(deal);
                        dealList2.add(deal);

                        dealRecyclerAdapter.notifyDataSetChanged();
                        dealRecyclerAdapter1.notifyDataSetChanged();
                        dealRecyclerAdapter2.notifyDataSetChanged();




                    }
                }

            }
        });



        //REturning view type object
        return view;



    }

}