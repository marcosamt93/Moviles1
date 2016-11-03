package com.mamt.plumel.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mamt.plumel.Models.Service;
import com.mamt.plumel.R;
import com.mamt.plumel.view.ServiceDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by marcosmt on 02/11/2016.
 */

public class ServiceAdapterRecyclerView extends  RecyclerView.Adapter<ServiceAdapterRecyclerView.ServiceViewHolder>{

    private ArrayList<Service> services;
    private int resource;
    private Activity activity;


    public ServiceAdapterRecyclerView(ArrayList<Service> services,int resource, Activity activity) {
        this.services = services;
        this.activity = activity;
        this.resource = resource;
    }

    @Override

    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return  new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder .userNameCard.setText(service.getUsername());
        holder.addressCard.setText(service.getAddress());
        holder.dateCard.setText(service.getDate());
        Picasso.with(activity).load(service.getPicture()).into(holder.pictureCard);

        holder.pictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ServiceDetailActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView pictureCard;
        private TextView userNameCard;
        private TextView addressCard;
        private TextView dateCard;

        public ServiceViewHolder(View itemView) {
            super(itemView);

            pictureCard = (ImageView) itemView.findViewById(R.id.pictureCard);
            userNameCard = (TextView) itemView.findViewById(R.id.userNameCard);
            addressCard = (TextView)  itemView.findViewById(R.id.card_string_address);
            dateCard = (TextView) itemView.findViewById(R.id.card_service_address);
        }
    }
}
