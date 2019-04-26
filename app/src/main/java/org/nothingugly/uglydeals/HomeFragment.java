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
    private List<Deal> dealList;

    private FirebaseFirestore mFirestore;

    private DealRecyclerAdapter dealRecyclerAdapter;


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

        //Mapping the layout component
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        dealRecyclerAdapter = new DealRecyclerAdapter(dealList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dealRecyclerAdapter);


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

                        dealRecyclerAdapter.notifyDataSetChanged();
                    }
                }

            }
        });



        //REturning view type object
        return view;



    }

}
