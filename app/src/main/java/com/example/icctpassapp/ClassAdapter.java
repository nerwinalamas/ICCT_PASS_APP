package com.example.icctpassapp;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context context3;
    private ArrayList id_class, Type_class, Content_class;

    public ClassAdapter(Context context3, ArrayList id_class, ArrayList Type_class, ArrayList Content_class) {
        this.context3 = context3;
        this.id_class = id_class;
        this.Type_class = Type_class;
        this.Content_class = Content_class;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context3);
        View view = inflater.inflate(R.layout.activity_class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.id_class.setText(String.valueOf(id_class.get(position)));
        holder.Type_class.setText(String.valueOf(Type_class.get(position)));
        holder.Content_class.setText(String.valueOf(Content_class.get(position)));
        Linkify.addLinks(holder.Type_class, Linkify.ALL);
    }

    @Override
    public int getItemCount() {
        return id_class.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView id_class, Type_class, Content_class;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            id_class = itemView.findViewById(R.id.id_class);
            Type_class = itemView.findViewById(R.id.type_class);
            Content_class = itemView.findViewById(R.id.content_class);
        }
    }
}
