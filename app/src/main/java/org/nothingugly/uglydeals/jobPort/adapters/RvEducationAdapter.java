package org.nothingugly.uglydeals.jobPort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.Constants;
import org.nothingugly.uglydeals.jobPort.interfaces.RemoveItemInterfaces;
import org.nothingugly.uglydeals.jobPort.models.EducationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvEducationAdapter extends RecyclerView.Adapter<RvEducationAdapter.Holder> {
    private Context context;
    private ArrayList<EducationModel> educationList;
    private RemoveItemInterfaces removeEducationInterface;

    public RvEducationAdapter(Context activity, ArrayList<EducationModel> educationList) {
        this.context = activity;
        this.educationList = educationList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.education_items, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvEducation.setText(Constants.getString(educationList.get(0).getEducation().get(position)));
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeEducationInterface != null) {
                    removeEducationInterface.removeItem(educationList.get(0).getEducation().get(position));
                    removeEducationInterface.addItem();
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (removeEducationInterface != null) {
                    removeEducationInterface.removeItem(educationList.get(0).getEducation().get(position));
                    removeEducationInterface.remove(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (educationList.get(0).getEducation().size() > 0)
            return educationList.get(0).getEducation().size();
        else return 0;
    }

    public void setListener(RemoveItemInterfaces listener) {
        this.removeEducationInterface = listener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_education)
        TextView tvEducation;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.rl_main)
        RelativeLayout rlMain;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
