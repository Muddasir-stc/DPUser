package com.dpoint.dpointsuser.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpoint.dpointsuser.R;
import com.dpoint.dpointsuser.datasource.remote.dashboard.Data;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {
    private Context context;
    private ArrayList<Data> list;

    public NotificationAdapter(Context context, ArrayList<Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        View mView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setData(Data data){
            TextView mTitle=mView.findViewById(R.id.txtTitle);
            TextView mDesc=mView.findViewById(R.id.txtDesc);
            mTitle.setText(data.getTitle());
            mDesc.setText(data.getBody());
        }
    }

}
