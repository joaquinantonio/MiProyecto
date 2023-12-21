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
import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {
    private Context mContext;
    private List<PatientDetails> mUsers;
    private String email;
    public AdminUserAdapter(Context mContext, List<PatientDetails> mUsers, String email) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.email = email;
    }

    @NonNull
    @Override
    public AdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new AdminUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserAdapter.ViewHolder holder, int position) {
        PatientDetails detail = mUsers.get(position);
        String info = detail.getName() + " ("+detail.getPhone()+")";
        holder.username.setText(info);
        holder.profile_image.setImageResource(R.drawable.patient_dp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdminMessageActivity.class);
                intent.putExtra("phone", detail.getPhone());
                intent.putExtra("email", email);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
            profile_image = itemView.findViewById(R.id.profileImage);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }
}
