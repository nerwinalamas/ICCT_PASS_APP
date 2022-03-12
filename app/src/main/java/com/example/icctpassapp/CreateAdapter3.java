package com.example.icctpassapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Appointments;

import java.util.ArrayList;

public class CreateAdapter3 extends RecyclerView.Adapter<CreateAdapter3.CreateViewHolder3>{

    private ArrayList<Appointments> appointmentsList;

    private CreateAdapter3.OnItemClickListener onItemClickListener3;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener3(CreateAdapter3.OnItemClickListener onItemClickListener3) {
        this.onItemClickListener3 = onItemClickListener3;
    }

    CreateAdapter3(ArrayList<Appointments> appointmentsList){
        this.appointmentsList = appointmentsList;
    }

    @NonNull
    @Override
    public CreateAdapter3.CreateViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_create_room_item, parent, false);
        return new CreateAdapter3.CreateViewHolder3(view, onItemClickListener3);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreateAdapter3.CreateViewHolder3 holder, final int position) {
        Appointments appointments = appointmentsList.get(position);
        holder.id.setText(appointments.getAppointmentId());
        holder.roomName.setText(appointments.getApRoomName());
        holder.subName.setText(appointments.getApSubName());
        holder.time.setText(appointments.getApTime());
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    class CreateViewHolder3 extends RecyclerView.ViewHolder {

        TextView id, roomName, subName, time;

        CreateViewHolder3(@NonNull View itemView, CreateAdapter3.OnItemClickListener onItemClickListener3) {
            super(itemView);
            id = itemView.findViewById(R.id.id_room);
            roomName = itemView.findViewById(R.id.ap_room_name);
            subName = itemView.findViewById(R.id.ap_sub_name);
            time = itemView.findViewById(R.id.ap_time);
            itemView.setOnClickListener(v -> onItemClickListener3.onClick(getAdapterPosition()));
        }
    }
}
