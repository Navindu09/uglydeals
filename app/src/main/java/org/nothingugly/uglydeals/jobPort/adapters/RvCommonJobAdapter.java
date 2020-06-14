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
import org.nothingugly.uglydeals.jobPort.fragments.JobHomeFragment;
import org.nothingugly.uglydeals.jobPort.interfaces.RvClickInterface;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvCommonJobAdapter extends RecyclerView.Adapter<RvCommonJobAdapter.JobHoler> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CommonJobsModel> jobsModelArrayList;
    private RvClickInterface rvClickInterface;

    public RvCommonJobAdapter(Context context, ArrayList<CommonJobsModel> jobsModels) {
        this.context = context;
        this.jobsModelArrayList = jobsModels;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public JobHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.job_single_item, viewGroup, false);
        return new JobHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHoler jobHoler, int i) {
        jobHoler.textViewItemName.setText(jobsModelArrayList.get(i).getTitle());
        jobHoler.textViewItemRestaurantName.setText(jobsModelArrayList.get(i).getLocation());
        jobHoler.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvClickInterface != null) {
                    rvClickInterface.onItemClick(jobsModelArrayList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobsModelArrayList.size();
    }

    public void setListener(RvClickInterface listener) {
        this.rvClickInterface = listener;
    }

    public class JobHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView itemImage;
        @BindView(R.id.tv_item_name)
        TextView textViewItemName;
        @BindView(R.id.tv_item_name_second)
        TextView textViewItemRestaurantName;
        @BindView(R.id.card_view)
        CardView itemCardView;

        public JobHoler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
