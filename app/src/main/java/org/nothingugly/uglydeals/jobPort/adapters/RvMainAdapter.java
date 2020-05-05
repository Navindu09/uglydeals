package org.nothingugly.uglydeals.jobPort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.nothingugly.uglydeals.R;

public class RvMainAdapter extends RecyclerView.Adapter<RvMainAdapter.RvHolder> {
    private Context context;
    private LayoutInflater layoutInflater;

    public RvMainAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.featured_item, viewGroup, false);
        return new RvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder rvHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class RvHolder extends RecyclerView.ViewHolder {
        public RvHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
