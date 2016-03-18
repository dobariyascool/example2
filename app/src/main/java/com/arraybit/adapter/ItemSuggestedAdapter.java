package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.abposw.R;
import com.arraybit.modal.ItemMaster;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemSuggestedAdapter extends RecyclerView.Adapter<ItemSuggestedAdapter.ItemSuggestdViewHolder> {

    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    private LayoutInflater inflater;

    public ItemSuggestedAdapter(Context context, ArrayList<ItemMaster> alItemMaster) {
        this.context = context;
        this.alItemMaster = alItemMaster;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemSuggestdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_suggested_item, parent, false);
        return new ItemSuggestdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemSuggestdViewHolder holder, int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);

        if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
            Picasso.with(holder.ivSuggestedItem.getContext()).load(R.drawable.default_image).into(holder.ivSuggestedItem);
        } else {
            Picasso.with(holder.ivSuggestedItem.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(holder.ivSuggestedItem);
        }
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    class ItemSuggestdViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSuggestedItem;

        public ItemSuggestdViewHolder(View itemView) {
            super(itemView);

            ivSuggestedItem = (ImageView) itemView.findViewById(R.id.ivSuggestedItem);
        }
    }
}
