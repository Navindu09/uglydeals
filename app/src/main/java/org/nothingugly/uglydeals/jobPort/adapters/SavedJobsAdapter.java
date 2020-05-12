package org.nothingugly.uglydeals.jobPort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.fragments.SavedJobsFragment;
import org.nothingugly.uglydeals.jobPort.interfaces.RemoveItemInterfaces;
import org.nothingugly.uglydeals.jobPort.models.CommonJobsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedJobsAdapter extends RecyclerView.Adapter<SavedJobsAdapter.ViewHolder> {
    private ArrayList<CommonJobsModel> modelArrayList;
    private RemoveItemInterfaces removeItemInterfaces;
    private Context context;

    public SavedJobsAdapter(Context activity, ArrayList<CommonJobsModel> jobsModels) {
        this.modelArrayList = jobsModels;
        this.context = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_jobs_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(modelArrayList.get(position).getDealPhoto()).into(holder.ivProfile);
        holder.tvJobTitle.setText(modelArrayList.get(position).getTitle());
        holder.tvRemote.setText(modelArrayList.get(position).getLocation() + "");
        holder.tvPaid.setText("UnPaid");
        holder.tvExperience.setText(modelArrayList.get(position).getLevel() + "");
        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeItemInterfaces != null) {
                    removeItemInterfaces.itemClick(modelArrayList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void removeItem(int position) {
        if (removeItemInterfaces != null) {
            removeItemInterfaces.removeItem(modelArrayList.get(position).getId());
        }
        modelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, modelArrayList.size());
    }

    public void setListener(RemoveItemInterfaces listener) {
        removeItemInterfaces = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;
        @BindView(R.id.iv_profile)
        ImageView ivProfile;
        @BindView(R.id.tv_job_title)
        TextView tvJobTitle;
        @BindView(R.id.tv_remote)
        TextView tvRemote;
        @BindView(R.id.tv_paid)
        TextView tvPaid;
        @BindView(R.id.tv_experience)
        TextView tvExperience;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
