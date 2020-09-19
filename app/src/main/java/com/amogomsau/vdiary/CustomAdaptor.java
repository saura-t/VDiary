package com.amogomsau.vdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder> {

    private Context context;
    private ArrayList entry_id, entry_title, entry_date, entry_location;

    CustomAdaptor(Context context, ArrayList entry_id, ArrayList entry_title, ArrayList entry_date, ArrayList entry_location) {
        this.context = context;
        this.entry_id = entry_id;
        this.entry_title = entry_title;
        this.entry_date = entry_date;
        this.entry_location = entry_location;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.entry_id.setText(String.valueOf(entry_id.get(position)));
        holder.entry_title.setText(String.valueOf(entry_title.get(position)));
        holder.entry_date.setText(String.valueOf(entry_date.get(position)));
        holder.entry_location.setText(String.valueOf(entry_location.get(position)));
    }

    @Override
    public int getItemCount() {
        return entry_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView entry_id, entry_title, entry_date, entry_location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            entry_id = itemView.findViewById(R.id.entry_id_txt);
            entry_title = itemView.findViewById(R.id.entry_title_txt);
            entry_date = itemView.findViewById(R.id.entry_date_txt);
            entry_location = itemView.findViewById(R.id.entry_location_txt);
        }
    }
}
