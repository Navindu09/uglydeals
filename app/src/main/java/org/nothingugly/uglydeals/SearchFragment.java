package org.nothingugly.uglydeals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private EditText editTextSearchSearch;
    private ImageButton imageButtonSearch;
    private RecyclerView recyclerViewSearchResults;

    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<Deal> searchedDeals;

    private FirebaseFirestore mFirestore;




    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mFirestore = FirebaseFirestore.getInstance();


        editTextSearchSearch =(EditText) view.findViewById(R.id.editTextSearchSearch);
        imageButtonSearch =  (ImageButton) view.findViewById(R.id.imageButtonSearch);
        recyclerViewSearchResults= (RecyclerView) view.findViewById(R.id.recyclerViewSearchResults);


        searchedDeals = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        searchRecyclerAdapter = new SearchRecyclerAdapter(searchedDeals);


        recyclerViewSearchResults.setLayoutManager(linearLayoutManager);
        recyclerViewSearchResults.setAdapter(searchRecyclerAdapter);


        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchedDeals.clear();
                searchRecyclerAdapter.notifyDataSetChanged();

                //Getting the search text
                final String searchText = editTextSearchSearch.getText().toString().toLowerCase();

                    try {
                        //List of partners
                    mFirestore.collection("partners").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges())
                            {
                                if (documentChange.getType() == DocumentChange.Type.ADDED)
                                {
                                    DocumentSnapshot partnerDocument = documentChange.getDocument();

                                    //Checks each partner document whether search text matches the name of the partner
                                    if (partnerDocument.get("name").toString().toLowerCase().contains(searchText)){

                                        //Get the id, of the partner if search text matched
                                        String id = partnerDocument.getId();

                                        //Search all deals to see if partnerId field matches the SearchedID
                                        mFirestore.collection(  "deals").whereEqualTo("partnerID",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                                                    if(documentChange.getType() == DocumentChange.Type.ADDED){


                                                        Deal deal = documentChange.getDocument().toObject(Deal.class);

                                                        //Log.d(TAG, "onEvent: DealId:  " +deal.getId() );

                                                        try{
                                                            if(deal.getActive())
                                                            {
                                                                 Log.d(TAG, "onEvent: Deal : "+ deal.getId() + " is active");
                                                                searchedDeals.add(deal);

                                                                searchRecyclerAdapter.notifyDataSetChanged();
                                                            }
                                                        }catch (Exception e1){

                                                            Log.e(TAG, "onEvent: DealID: " + deal.getId()  ,e1);

                                                        }

                                                    }
                                                }
                                            }
                                        });
                                    }}
                            }
                        }
                    });
                    }catch (NullPointerException e){
                        Log.e(TAG, "onClick: ", e);
                    }

            }
        });
        return view;
    }

}
