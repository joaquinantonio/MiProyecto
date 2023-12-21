package com.ja.miproyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class DoctorViewSlotsAdapter extends RecyclerView.Adapter<DoctorViewSlotsAdapter.ViewHolder>{
    private List<DataSnapshot> slots_list;
    private Context mContext;
    private List<String> date_slot;
    private List<Integer> count;

    public DoctorViewSlotsAdapter(Context mContext, List<DataSnapshot> slots_list, List<String> date_slot, List<Integer> count) {
        this.slots_list = slots_list;
        this.mContext = mContext;
        this.date_slot = date_slot;
        this.count = count;
    }

    @NonNull
    @Override
    public DoctorViewSlotsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_view_slot_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewSlotsAdapter.ViewHolder holder, int position) {
        String slots_time = slots_list.get(position).getKey();
        DoctorSlotsRV doctor_slots_rv = new DoctorSlotsRV(date_slot.get(position), slots_time, count.get(position));
        holder.date.setText("Date: " + doctor_slots_rv.getDate());
        holder.time.setText("Time: " + doctor_slots_rv.getTime());
        holder.slots.setText("Slots Booked: "+doctor_slots_rv.getSlots_booked());
    }

    @Override
    public int getItemCount() {
        return slots_list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,time,slots;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.timeTextView);
            slots = itemView.findViewById(R.id.slots_view);
        }
    }
}

