package com.ja.miproyecto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private List<DoctorsProfile> listData;
    private List<String> emaildata;
    private Context mContext;
    private String name;
    private String email;
    private String speciality;

    public DoctorsAdapter(List<DoctorsProfile> listData, List<String> emaildata, Context mContext) {
        this.listData = listData;
        this.emaildata=emaildata;
        this.mContext=mContext;
    }

    public void filterList(ArrayList<DoctorsProfile> filterdNames) {
        this.listData = filterdNames;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(v -> {
            DoctorsProfile d = listData.get(viewHolder.getAbsoluteAdapterPosition());
            name = d.getName();
            email = emaildata.get(viewHolder.getAbsoluteAdapterPosition());
            speciality = d.getType();
            Intent intent=new Intent(view.getContext(), PatientDisplayDoctor.class);
            intent.putExtra("Doctor's Name",name);
            intent.putExtra("Email",email);
            intent.putExtra("Speciality",speciality);
            view.getContext().startActivity(intent);
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsAdapter.ViewHolder holder, int position) {
        DoctorsProfile ld=listData.get(position);

        holder.name.setText(ld.getName());
        holder.emailid.setText(emaildata.get(position));
        holder.spec.setText(ld.getType());
        if(ld.getDoc_pic()!=null){
        Picasso.get().load(ld.getDoc_pic().getUrl()).into(holder.doc_image);}
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,emailid,spec;
        ImageView doc_image;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nameTextView);
            emailid=(TextView) itemView.findViewById(R.id.emailTextView);
            spec=(TextView) itemView.findViewById(R.id.specialityTextView);
            doc_image=(ImageView) itemView.findViewById(R.id.imageView_doc);
        }
    }
}
