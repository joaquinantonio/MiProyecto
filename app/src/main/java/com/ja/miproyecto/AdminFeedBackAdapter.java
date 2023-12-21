package com.ja.miproyecto;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminFeedBackAdapter extends RecyclerView.Adapter<AdminFeedBackAdapter.ViewHolder> {
    private List<String> email,name,date,time,phone;

    public AdminFeedBackAdapter(List<String> email, List<String> name, List<String> date, List<String> time, List<String> phone){
        this.email = email;
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.time = time;
    }

    @NonNull
    @Override
    public AdminFeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_feedback_rv, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AdminShowFeedBack.class);
                intent.putExtra("name", name.get(viewHolder.getAbsoluteAdapterPosition()));
                intent.putExtra("email", email.get(viewHolder.getAbsoluteAdapterPosition()));
                intent.putExtra("phone", phone.get(viewHolder.getAbsoluteAdapterPosition()));
                intent.putExtra("time", time.get(viewHolder.getAbsoluteAdapterPosition()));
                intent.putExtra("date", date.get(viewHolder.getAbsoluteAdapterPosition()));
                view.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFeedBackAdapter.ViewHolder holder, int position) {
        holder.name_view.setText("Doctor's Name: "+name.get(position));
        holder.phone_view.setText("Patient's Num: "+phone.get(position));
        holder.date_view.setText("Date: "+ date.get(position));
        holder.time_view.setText("Time: "+ time.get(position));
    }

    @Override
    public int getItemCount() {
        return phone.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_view,phone_view,date_view,time_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_view = (TextView)itemView.findViewById(R.id.name);
            phone_view = (TextView) itemView.findViewById(R.id.phone);
            date_view = (TextView) itemView.findViewById(R.id.date);
            time_view = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
