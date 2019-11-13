package org.nothingugly.uglydeals;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SearchRecylcerAdapter";
    public List<Deal> dealList;
    public FirebaseFirestore mFirestore;

    public Context context;

    public SearchRecyclerAdapter(List<Deal> dealList) {

        this.dealList = dealList;

    }

    @NonNull
    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_search_item, viewGroup, false);
        mFirestore = FirebaseFirestore.getInstance();
        context = viewGroup.getContext();
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchRecyclerAdapter.ViewHolder viewHolder, int i) {

        //Retrieve name of the deal
        String name = dealList.get(i).getName();
        //Set the name of the deal to the viewholder
        viewHolder.setName(name);

        //Retrieve the image URL of the current deal at index
        String imageURL = dealList.get(i).getDealPhoto();
        //Set the URL to the viewHolder
        viewHolder.setImage(imageURL);

        //Taking the restaurant ID from the deal
        final String restaurantId = dealList.get(i).getPartnerID();

        final String dealID = dealList.get(i).getId();

        //Getting the corresponding document for the partner ID
        try {
            DocumentReference temp = mFirestore.collection("partners").document(restaurantId);

            //Get Document Snapshot
            temp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    //If document snap recieved successfully.
                    if (task.isSuccessful()) {
                        DocumentSnapshot restaurantDocument = task.getResult();
                        if (restaurantDocument.exists()) {
                            String restaurantName = restaurantDocument.get("name").toString();

                            //Set the name on the view holder
                            viewHolder.setRestaurantName(restaurantName);

                        }

                    }
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedItemActivity.class);
                intent.putExtra("dealId", dealID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dealList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView name;
        private ImageView image;
        private TextView restaurantName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setName(String nametext) {

            name = mView.findViewById(R.id.textViewSearchItemName);
            name.setText(nametext);
        }

        public void setImage(String imageURL) {
            image = mView.findViewById(R.id.imageViewSearchItem);
            Glide.with(context).load(imageURL).into(image);
        }

        public void setRestaurantName(String restName) {
            restaurantName = mView.findViewById(R.id.textViewSearchItemRestaurant);
            restaurantName.setText(restName);
        }
    }
}



