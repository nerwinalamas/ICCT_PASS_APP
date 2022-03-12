package com.example.icctpassapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.ScanStudents;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private ArrayList<ScanStudents> scanStudents;

    public ClassAdapter(ArrayList<ScanStudents> scanStudents) {
        this.scanStudents = scanStudents;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ScanStudents scan = scanStudents.get(position);
        User user = scan.getUser();

        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return scanStudents.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail, tvCourse;

        public ClassViewHolder(@NonNull View v) {
            super(v);
           tvName = v.findViewById(R.id.tv_name);
           tvEmail = v.findViewById(R.id.tv_email);
           tvCourse = v.findViewById(R.id.tv_course);
        }

        public void bind(User user){
            tvName.setText("Name: " + user.getFullName());
            tvEmail.setText("Email: " + user.getEmail());
            tvCourse.setText("Course: " + user.getCourse());
        }
    }
}
