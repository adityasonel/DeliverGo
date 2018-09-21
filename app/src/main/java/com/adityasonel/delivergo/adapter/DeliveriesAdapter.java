package com.adityasonel.delivergo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adityasonel.delivergo.R;
import com.adityasonel.delivergo.activity.ItemLocationActivity;
import com.adityasonel.delivergo.model.DeliveryItemPOJO;
import com.adityasonel.delivergo.model.DeliveryModel;
import com.adityasonel.delivergo.ui.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DeliveriesAdapter extends RecyclerView.Adapter<DeliveriesAdapter.ItemViewHolder> {

    private List<DeliveryItemPOJO> list;
    private Context mContext;
    private Activity mActivity;

    public DeliveriesAdapter(List<DeliveryItemPOJO> list, Context mContext, Activity mActivity) {
        this.list = list;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_recycler_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final DeliveryItemPOJO model = list.get(i);

        if (!model.getDescription().equals("")) {
            itemViewHolder.itemDescription.setText(model.getDescription());
        } else {
            itemViewHolder.itemDescription.setText(mContext.getResources().getString(R.string.error_description));
        }
        if (!model.getLocation().get(0).getAddress().equals("")) {
            itemViewHolder.itemLocation.setText(model.getLocation().get(0).getAddress());
        } else {
            itemViewHolder.itemLocation.setText(mContext.getResources().getString(R.string.error_address));
        }

        if (!model.getImageUrl().equals("")) {
            Glide.with(mContext).load(model.getImageUrl()).into(itemViewHolder.itemImage);
        }

        itemViewHolder.itemMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ItemLocationActivity.class);
                intent.putExtra("description", model.getDescription());
                intent.putExtra("address", model.getLocation().get(0).getAddress());
                intent.putExtra("lat", model.getLocation().get(0).getLat());
                intent.putExtra("lng", model.getLocation().get(0).getLng());
                intent.putExtra("imageUrl", model.getImageUrl());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView itemMainView;
        CircleImageView itemImage;
        TextView itemDescription, itemLocation;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMainView = itemView.findViewById(R.id.item_main_view);
            itemImage = itemView.findViewById(R.id.item_image);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemLocation = itemView.findViewById(R.id.item_location);
        }
    }
}
