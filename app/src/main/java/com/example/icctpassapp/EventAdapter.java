package com.example.icctpassapp;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.ScannedEvent;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<ScannedEvent> list;

    public EventAdapter(ArrayList<ScannedEvent> list) {
      this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        ScannedEvent e = list.get(position);
        holder.bind(e.getUser());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail, tvCourse;

        public EventViewHolder(@NonNull View v) {
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
