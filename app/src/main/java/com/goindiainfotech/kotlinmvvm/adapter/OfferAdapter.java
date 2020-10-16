package com.goindiainfotech.kotlinmvvm.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.goindiainfotech.kotlinmvvm.R;
import com.goindiainfotech.kotlinmvvm.pojo.OfferPojo;

import java.util.List;


public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;


    private List<OfferPojo> offerPojoList;

    public OfferAdapter(Context context, List<OfferPojo> offerPojoList) {
        this.context = context;
        this.offerPojoList = offerPojoList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_custom_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.offer_title.setText(offerPojoList.get(position).getOffer_title());
        holder.offer_des.setText(offerPojoList.get(position).getOdder_des());
        holder.offer_price.setText("Estimated savings : "+offerPojoList.get(position).getOffer_price());


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {

        return offerPojoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView offer_title,offer_des,offer_price;


        public ViewHolder(@NonNull View view) {
            super(view);

            offer_title = (TextView) view.findViewById(R.id.offer_title);
            offer_des = (TextView) view.findViewById(R.id.offer_des);
            offer_price = (TextView) view.findViewById(R.id.offer_price);


        }
    }
}
