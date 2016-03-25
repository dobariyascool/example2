package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.MenuActivity;
import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ItemMaster;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public boolean isItemAnimate = false;
    boolean isTileGrid = false;
    View view;
    Context context;
    ArrayList<ItemMaster> alItemMaster;
    ItemClickListener objItemClickListener;
    int previousPosition;
    int width, height;

    public ItemAdapter(Context context, ArrayList<ItemMaster> alItemMaster, ItemClickListener objItemClickListener) {
        this.context = context;
        this.alItemMaster = alItemMaster;
        this.objItemClickListener = objItemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (MenuActivity.isViewChange) {
            if (MenuActivity.i == 1) {
                isTileGrid = false;
                view = LayoutInflater.from(context).inflate(R.layout.row_category_item_grid, parent, false);
            } else {
                isTileGrid = true;
                view = LayoutInflater.from(context).inflate(R.layout.row_category_item_tile, parent, false);
            }
        } else {
            isTileGrid = false;
            view = LayoutInflater.from(context).inflate(R.layout.row_category_item, parent, false);
        }

        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemMaster objItemMaster = alItemMaster.get(position);
        if (!isTileGrid) {
            if (objItemMaster.getMd_ImagePhysicalName().equals("null")) {
                Picasso.with(holder.ivItem.getContext()).load(R.drawable.default_image).into(holder.ivItem);
            } else {
                Picasso.with(holder.ivItem.getContext()).load(objItemMaster.getMd_ImagePhysicalName()).into(holder.ivItem);
            }
        }
        holder.txtItemName.setText(objItemMaster.getItemName());
        if (objItemMaster.getShortDescription().equals("")) {
            holder.txtItemDescription.setVisibility(View.GONE);
        } else {
            holder.txtItemDescription.setVisibility(View.VISIBLE);
            holder.txtItemDescription.setText(objItemMaster.getShortDescription());
        }
        holder.txtItemPrice.setText("Rs. " + Globals.dfWithPrecision.format(objItemMaster.getRate()));

        if (objItemMaster.getIsDineInOnly()) {
            holder.cvItem.setClickable(false);
            holder.btnAdd.setClickable(false);
            holder.btnAdd.setEnabled(false);

        } else {
            holder.cvItem.setClickable(true);
            holder.btnAdd.setClickable(true);
            holder.btnAdd.setEnabled(true);
        }

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

    public interface ItemClickListener {
        void ItemOnClick(ItemMaster objItemMaster, View view, String transitionName);

        void AddItemOnClick(ItemMaster objItemMaster);
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

            if (!isTileGrid && MenuActivity.isViewChange) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                width = displayMetrics.widthPixels / 2 - 32;
                height = displayMetrics.widthPixels / 2 - 32;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                ivItem.setLayoutParams(layoutParams);
            }

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(context, ItemModifierRemarkActivity.class);
                    //intent.putExtra("ItemMaster",alItemMaster.get(getAdapterPosition()));
                    //context.startActivity(intent);
                    objItemClickListener.AddItemOnClick(alItemMaster.get(getAdapterPosition()));
                }
            });


            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), v, v.getTransitionName());
                    } else {
                        objItemClickListener.ItemOnClick(alItemMaster.get(getAdapterPosition()), null, null);
                    }
                }
            });
        }
    }
}
