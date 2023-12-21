package com.ja.miproyecto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrescriptionPatientAdapter extends RecyclerView.Adapter<PrescriptionPatientAdapter.ViewHolder> {

    private ArrayList<GetPrescriptionDetails> get_prescription_details;
    private Context mContext;

    public PrescriptionPatientAdapter(ArrayList<GetPrescriptionDetails> get_prescription_details, Context mContext) {
        this.get_prescription_details = get_prescription_details;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_data_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrescriptionDetails obj=get_prescription_details.get(viewHolder.getAbsoluteAdapterPosition());
                if(obj.getFlag() == 0){
                    Toast.makeText(view.getContext(), "Please Submit feedback First!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(view.getContext(), Feedback.class);
                    intent1.putExtra("email",obj.getText());
                    intent1.putExtra("date",obj.getDate());
                    intent1.putExtra("time",obj.getTime());
                    intent1.putExtra("phone",obj.getPhone());
                    view.getContext().startActivity(intent1);

                }
                else {
                    Intent intent = new Intent(view.getContext(), PatientPrescription.class);
                    intent.putExtra("email", obj.getText());
                    intent.putExtra("date", obj.getDate());
                    intent.putExtra("time", obj.getTime());
                    view.getContext().startActivity(intent);
                }
            }
        });
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPrescriptionDetails ld=get_prescription_details.get(position);
        holder.pres.setText("Name: "+ ld.getDr()+"\nDate: "+ld.getDate()+"\nTime "+ld.getTime());
    }

    @Override
    public int getItemCount() {
        return get_prescription_details.size();
    }

    public void filterList(ArrayList<GetPrescriptionDetails> filterdNames) {
        this.get_prescription_details=filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pres;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pres=itemView.findViewById(R.id.prescriptionTextView);
        }
    }
}
