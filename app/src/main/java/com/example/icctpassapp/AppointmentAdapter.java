package com.example.icctpassapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.HealthForm;
import com.example.icctpassapp.models.ScannedAppointment;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{

    private ArrayList<HealthForm> healthForms;

    public AppointmentAdapter(ArrayList<HealthForm> healthForms) {
        this.healthForms = healthForms;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_room, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        HealthForm healthForm = healthForms.get(position);
        holder.bind(healthForm);
//        holder.bind(e.getUser());
//        e.getAppointments();
//        User user = e.getUser();
//        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return healthForms.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvRoomName, tvSubName, tvTime;

        public AppointmentViewHolder(@NonNull View v) {
            super(v);
            tvRoomName = v.findViewById(R.id.tv_rname_page2);
            tvSubName = v.findViewById(R.id.tv_sname_page2);
            tvTime = v.findViewById(R.id.tv_time_page2);
        }

        public void bind(HealthForm healthForm){
            tvRoomName.setText("Name: " + healthForm.getHfName());
            tvSubName.setText("Temperature: " + healthForm.getHfTemperature());
            tvTime.setText("Purpose of Visit: " + healthForm.getHfPurposeOfVisit());
        }
    }
}
