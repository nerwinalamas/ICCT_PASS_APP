package com.example.icctpassapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Classroom;

import java.util.ArrayList;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.CreateHolder> {

    ArrayList<Classroom> classroomsArrayList;
    Context context;

    public  CreateAdapter(Context context, ArrayList<Classroom> classroomsArrayList){
        this.classroomsArrayList = classroomsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_item, parent, false);
        return new CreateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateHolder holder, int position) {
        Classroom classrooms = classroomsArrayList.get(position);
        holder.className.setText(classrooms.getName());
        holder.subjectCode.setText(classrooms.getSubjectCode());
        holder.section.setText(classrooms.getSection());
        holder.className.setOnClickListener(v -> {
            Intent i = new Intent(context, ClassActivity.class);
            i.putExtra("className", classrooms.getName());
            i.putExtra("subjectCode",classrooms.getSubjectCode());
            i.putExtra("section", classrooms.getSection());
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return classroomsArrayList.size();
    }

    class CreateHolder extends RecyclerView.ViewHolder {

        TextView className, subjectCode, section;

        public CreateHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            subjectCode = itemView.findViewById(R.id.subject_code);
            section = itemView.findViewById(R.id.section);
        }
    }
}