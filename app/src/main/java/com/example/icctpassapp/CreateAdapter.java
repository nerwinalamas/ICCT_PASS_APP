package com.example.icctpassapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.CreateViewHolder> {
    ArrayList <CreateItem> createItems;
    Context context;

    public CreateAdapter(Context context, ArrayList<CreateItem> createItems) {
        this.createItems = createItems;
        this.context = context;
    }

    public static class CreateViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView subjectCode;
        TextView section;

        public CreateViewHolder(@NonNull View createView) {
            super(createView);
            className = itemView.findViewById(R.id.class_name);
            subjectCode = itemView.findViewById(R.id.subject_code);
            section = itemView.findViewById(R.id.section);
        }
    }

    @NonNull
    @Override
    public CreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View createView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_item,parent,false);

        return new CreateViewHolder(createView);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateViewHolder holder, int position) {

        holder.className.setText(createItems.get(position).getClassName());
        holder.subjectCode.setText(createItems.get(position).getSubjectCode());
        holder.section.setText(createItems.get(position).getSection());
    }

    @Override
    public int getItemCount() {
        return createItems.size();
    }
}
