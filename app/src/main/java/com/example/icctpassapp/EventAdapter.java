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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context4;
    private ArrayList id_event, Type_event, Content_event;

    public EventAdapter(Context context4, ArrayList id_event, ArrayList Type_event, ArrayList Content_event) {
        this.context4 = context4;
        this.id_event = id_event;
        this.Type_event = Type_event;
        this.Content_event = Content_event;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context4);
        View view = inflater.inflate(R.layout.activity_event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.id_event.setText(String.valueOf(id_event.get(position)));
        holder.Type_event.setText(String.valueOf(Type_event.get(position)));
        holder.Content_event.setText(String.valueOf(Content_event.get(position)));
        Linkify.addLinks(holder.Type_event, Linkify.ALL);
    }

    @Override
    public int getItemCount() {
        return id_event.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView id_event, Type_event, Content_event;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            id_event = itemView.findViewById(R.id.id_event);
            Type_event = itemView.findViewById(R.id.type_event);
            Content_event = itemView.findViewById(R.id.content_event);
        }
    }
}
