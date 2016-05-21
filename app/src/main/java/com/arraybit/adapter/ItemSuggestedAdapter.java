package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemSuggestedAdapter extends RecyclerView.Adapter<ItemSuggestedAdapter.ItemSuggestdViewHolder> {

    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ImageViewClickListener objImageViewClickListener;

    public ItemSuggestedAdapter(Context context, ArrayList<ItemMaster> alItemMaster, ImageViewClickListener objImageViewClickListener) {
        this.context = context;
        this.alItemMaster = alItemMaster;
        this.objImageViewClickListener = objImageViewClickListener;
    }

    @Override
    public ItemSuggestdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_suggested_item, parent, false);
        return new ItemSuggestdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemSuggestdViewHolder holder, int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);
        holder.txtSuggestedName.setText(objItemMaster.getItemName());
        if (objItemMaster.getXs_ImagePhysicalName() == null || objItemMaster.getXs_ImagePhysicalName().equals("")) {
            Picasso.with(holder.ivSuggestedItem.getContext()).load(R.drawable.default_image).into(holder.ivSuggestedItem);
        } else {
            Picasso.with(holder.ivSuggestedItem.getContext()).load(objItemMaster.getXs_ImagePhysicalName()).into(holder.ivSuggestedItem);
        }
    }

    @Override
    public int getItemCount() {
        return alItemMaster.size();
    }

    public interface ImageViewClickListener {
        void ImageOnClick(ItemMaster objItemMaster, View view, String transitionName);
    }

    class ItemSuggestdViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSuggestedItem;
        TextView txtSuggestedName;
        LinearLayout suggestedItemLayout;

        public ItemSuggestdViewHolder(View itemView) {
            super(itemView);

            suggestedItemLayout = (LinearLayout)itemView.findViewById(R.id.suggestedItemLayout);

            txtSuggestedName = (TextView)itemView.findViewById(R.id.txtSuggestedName);

            ivSuggestedItem = (ImageView) itemView.findViewById(R.id.ivSuggestedItem);

            suggestedItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        objImageViewClickListener.ImageOnClick(alItemMaster.get(getAdapterPosition()), v, v.getTransitionName());
//                    } else {
                    objImageViewClickListener.ImageOnClick(alItemMaster.get(getAdapterPosition()), null, null);
                    //}
                }
            });
        }
    }
}
