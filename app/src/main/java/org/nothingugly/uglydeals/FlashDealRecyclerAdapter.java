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

public class FlashDealRecyclerAdapter extends RecyclerView.Adapter<FlashDealRecyclerAdapter.ViewHolder>
{
    public List<Deal> dealList;
    public FirebaseFirestore mFirestore;

    private static final String TAG = "FlashRecyclerAdapter";
    public Context context;

    public FlashDealRecyclerAdapter(List<Deal >dealList){

        this.dealList = dealList;

    }


    @NonNull
    @Override
    public FlashDealRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.featured_item,viewGroup,false);
        mFirestore = FirebaseFirestore.getInstance();
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FlashDealRecyclerAdapter.ViewHolder viewHolder, int i) {
        //Retrieve name of the deal
        String  name = dealList.get(i).getName();
        //Set the name of the deal to the viewholder
        viewHolder.setName(name);

        //Retrieve the image URL of the current deal at index
        String imageURL = dealList.get(i).getDealPhoto();
        //Set the URL to the viewHolder
        viewHolder.setImage(imageURL);


        //Taking the restaurant ID from the deal
        String restaurantId = dealList.get(i).getPartnerID();

        final String dealId = dealList.get(i).getId();


        try{
        //Getting the corresponding document for the partner ID
        DocumentReference temp = mFirestore.collection("partners").document(restaurantId);

        //Get Document Snapshot
        temp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                //If document snap recieved successfully.
                if (task.isSuccessful()){
                    DocumentSnapshot restaurantDocument = task.getResult();
                    if(restaurantDocument.exists()){
                        String restaurantName = restaurantDocument.get("name").toString();

                        //Set the name on the view holder
                       // viewHolder.setRestaurantName(restaurantName);

                       String restaurantLogo = (String) restaurantDocument.get("restaurantLogo").toString();
                        viewHolder.setRestaurantImage(restaurantLogo);

                    }

                }
            }
        });

        } catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: ",e );
        }

        try {

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedItemActivity.class);
                intent.putExtra("dealId",dealId);
                context.startActivity(intent);
            }
        });

        } catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: ",e );
        }



    }

    @Override
    public int getItemCount() {
        return dealList.size();
    }



    //Class of the view holder, so that the information taken from the class can then be added to the Single Item layout. By using the below class methods.
    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView name;
        private ImageView image;
        private ImageView restImage;
        //private TextView restaurantName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setName(String nametext){

            name = mView.findViewById(R.id.textViewFeaturedName);
            name.setText(nametext);
        }

        public void setImage(String imageURL) {
            image = mView.findViewById(R.id.featuredImageView);
            Glide.with(context).load(imageURL).into(image);

        }

        public void setRestaurantImage(String restaurantImageURL) {
            restImage = mView.findViewById(R.id.restaurantImage);
            Glide.with(context).load(restaurantImageURL).into(restImage);

        }

      /*  public void setRestaurantName(String restName){
            restaurantName = mView.findViewById(R.id.textViewFeaturedRestaurantName);
            restaurantName.setText(restName);
        }*/
    }
}
