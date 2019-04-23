package org.nothingugly.uglydeals;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DealRecyclerAdapter extends RecyclerView.Adapter<DealRecyclerAdapter.ViewHolder>
{
    public List<Deal> dealList;


    public DealRecyclerAdapter(List<Deal >dealList){

        this.dealList = dealList;

    }


    @NonNull
    @Override
    public DealRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealRecyclerAdapter.ViewHolder viewHolder, int i) {

        String  name = dealList.get(i).getName();
        viewHolder.setName(name);
    }



    @Override
    public int getItemCount() {
        return dealList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView;
        }

        public void setName(String nametext){

            name = mView.findViewById(R.id.dealName);
            name.setText(nametext);
        }
    }
}
