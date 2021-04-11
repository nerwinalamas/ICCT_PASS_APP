package com.example.icctpassapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CreateAdapter2 extends RecyclerView.Adapter<CreateAdapter2.CreateViewHolder2> {

    private Context context2;
    private ArrayList id, eventName, eventLocation, time;

    private OnItemClickListener onItemClickListener2;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener2(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener2 = onItemClickListener2;
    }

    CreateAdapter2(Context context2,ArrayList id, ArrayList eventName, ArrayList eventLocation, ArrayList time){
        this.context2 = context2;
        this.id = id;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.time = time;
    }

    @NonNull
    @Override
    public CreateViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context2);
        View view = inflater.inflate(R.layout.activity_create_item2, parent, false);
        return new CreateViewHolder2(view, onItemClickListener2);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreateViewHolder2 holder, final int position) {
        holder.id.setText(String.valueOf(id.get(position)));
        holder.eventName.setText(String.valueOf(eventName.get(position)));
        holder.eventLocation.setText(String.valueOf(eventLocation.get(position)));
        holder.time.setText(String.valueOf(time.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class CreateViewHolder2 extends RecyclerView.ViewHolder {

        TextView eventName, eventLocation, time, id;

        CreateViewHolder2(@NonNull View itemView, OnItemClickListener onItemClickListener2) {
            super(itemView);
            id = itemView.findViewById(R.id.id_class);
            eventName = itemView.findViewById(R.id.event_name);
            eventLocation = itemView.findViewById(R.id.event_location);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(v -> onItemClickListener2.onClick(getAdapterPosition()));
        }
    }
}
