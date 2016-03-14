package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public boolean isItemAnimate = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    int previousPosition;
    ItemMaster objItemMaster;

    public ItemAdapter(Context context, ArrayList<ItemMaster> alItemMaster) {
        this.context = context;
        this.alItemMaster = alItemMaster;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_category_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        objItemMaster = alItemMaster.get(position);

        holder.txtItemName.setText(objItemMaster.getItemName());
        if (objItemMaster.getShortDescription().equals("")) {
            holder.txtItemDescription.setVisibility(View.GONE);
        } else {
            holder.txtItemDescription.setVisibility(View.VISIBLE);
            holder.txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        if (!objItemMaster.getMd_ImagePhysicalName().equals("")) {
            Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(holder.ivItem);
        }
        holder.txtItemPrice.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getRate()));

        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public void ItemDataChanged(ArrayList<ItemMaster> result) {
        alItemMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemName, txtItemDescription, txtItemPrice;
        ImageView ivItem;
        CardView cvItem;
        Button btnAdd;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemDescription = (TextView) itemView.findViewById(R.id.txtItemDescription);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);

            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
        }
    }
}
