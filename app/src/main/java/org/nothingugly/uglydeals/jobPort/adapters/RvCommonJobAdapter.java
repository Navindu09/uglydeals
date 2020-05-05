package org.nothingugly.uglydeals.jobPort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvCommonJobAdapter extends RecyclerView.Adapter<RvCommonJobAdapter.JobHoler> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CommonJobsModel> jobsModelArrayList;

    public RvCommonJobAdapter(Context context, ArrayList<CommonJobsModel> jobsModels) {
        this.context = context;
        this.jobsModelArrayList = jobsModels;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public JobHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.single_item, viewGroup, false);
        return new JobHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHoler jobHoler, int i) {
        jobHoler.textViewItemName.setText(jobsModelArrayList.get(i).getSkills());
    }

    @Override
    public int getItemCount() {
        return jobsModelArrayList.size();
    }

    public class JobHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.itemImage)
        ImageView itemImage;
        @BindView(R.id.textViewItemName)
        TextView textViewItemName;
        @BindView(R.id.textViewItemRestaurantName)
        TextView textViewItemRestaurantName;
        @BindView(R.id.viewIsFeaturedBanner)
        View viewIsFeaturedBanner;
        @BindView(R.id.textViewFeaturedBanner)
        TextView textViewFeaturedBanner;
        @BindView(R.id.itemCardView)
        CardView itemCardView;

        public JobHoler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
