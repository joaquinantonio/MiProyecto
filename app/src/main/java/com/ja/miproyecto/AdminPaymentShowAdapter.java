package com.ja.miproyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminPaymentShowAdapter extends RecyclerView.Adapter<AdminPaymentShowAdapter.ViewHolder> {

    private ArrayList<AdminPaymentClass> payments;

    public AdminPaymentShowAdapter(ArrayList<AdminPaymentClass> payments){
        this.payments = payments;
    }
    @NonNull
    @Override
    public AdminPaymentShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_payment_rv, parent, false);
        AdminPaymentShowAdapter.ViewHolder viewHolder = new AdminPaymentShowAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPaymentShowAdapter.ViewHolder holder, int position) {
        AdminPaymentClass p_class = payments.get(position);
        holder.name.setText(p_class.getName() + ": " +p_class.getPhone());
        holder.phone.setText("Transaction ID: "+p_class.getTransaction());
        holder.email.setText("Booked for: "+p_class.getDname());
        holder.date.setText("Date: "+p_class.getDate());
        holder.time.setText("Time: "+p_class.getTime());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void filterList(ArrayList<AdminPaymentClass> filterdNames) {
        this.payments=filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, date, time, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
