package com.example.tempomaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Projects> list;

    public MyAdapter(Context context, ArrayList<Projects> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Projects project = list.get(position);
        holder.projectName.setText(project.getProjectName());
        holder.description.setText(project.getDescription());
        holder.date.setText(project.getDate());
        holder.startTime.setText(project.getStartTime());
        holder.endTime.setText(project.getEndTime());
        holder.category.setText(project.getCategory());

        // Load project image using Glide
        Glide.with(context)
                .load(project.getImageUrl())
                .apply(RequestOptions.placeholderOf(R.drawable.tempo_master_logo_trasnparency)
                        .error(R.drawable.work_on_computer_employee_software_engineer_programmer_svgrepo_com))
                .into(holder.projectImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView projectName, description, date, startTime, endTime, category;
        ImageView projectImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.tvProjectName);
            description = itemView.findViewById(R.id.tvProjectDescription);
            date = itemView.findViewById(R.id.tvDate);
            startTime = itemView.findViewById(R.id.tvStartTime);
            endTime = itemView.findViewById(R.id.tvEndTime);
            category = itemView.findViewById(R.id.tvCategory);
            projectImageView = itemView.findViewById(R.id.projectImageView);
        }
    }
}
