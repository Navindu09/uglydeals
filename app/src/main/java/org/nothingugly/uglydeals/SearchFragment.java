package org.nothingugly.uglydeals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

                String searchText = editTextSearchSearch.getText().toString();


               mFirestore.collection("partners").whereEqualTo("name",searchText).addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                       for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges())
                       {
                           if (documentChange.getType() == DocumentChange.Type.ADDED)
                           {
                               DocumentSnapshot doc = documentChange.getDocument();
                               String id = doc.getId();

                               mFirestore.collection("deals").whereEqualTo("partnerID",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                       for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                                            if(documentChange.getType() == DocumentChange.Type.ADDED){

                                                Deal deal = documentChange.getDocument().toObject(Deal.class);

                                                searchedDeals.add(deal);

                                                searchRecyclerAdapter.notifyDataSetChanged();
                                            }
                                       }
                                   }
                               });
                           }
                       }
                   }
               });

            }
        });
        return view;
    }

}
