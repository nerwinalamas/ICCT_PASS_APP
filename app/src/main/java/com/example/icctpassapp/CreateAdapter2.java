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

public class CreateAdapter2 extends RecyclerView.Adapter<CreateAdapter2.CreateViewHolder2> {
    ArrayList<CreateItem2> createItems2;
    Context context2;

    public CreateAdapter2(Context context2, ArrayList<CreateItem2> createItems2) {
        this.createItems2 = createItems2;
        this.context2 = context2;
    }

    public static class CreateViewHolder2 extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventLocation;
        TextView time;

        public CreateViewHolder2(@NonNull View createView2) {
            super(createView2);
            eventName = itemView.findViewById(R.id.event_name);
            eventLocation = itemView.findViewById(R.id.event_location);
            time = itemView.findViewById(R.id.time);
        }
    }

    @NonNull
    @Override
    public CreateViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View createView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_item2,parent,false);

        return new CreateViewHolder2(createView2);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateViewHolder2 holder, int position) {

        holder.eventName.setText(createItems2.get(position).getEventName());
        holder.eventLocation.setText(createItems2.get(position).getEventLocation());
        holder.time.setText(createItems2.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return createItems2.size();
    }
}

