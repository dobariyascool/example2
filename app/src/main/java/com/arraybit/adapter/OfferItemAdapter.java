package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.abposw.R;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class OfferItemAdapter extends RecyclerView.Adapter<OfferItemAdapter.OfferItemViewHolder> {

    View view;
    Context context;
    ArrayList<String> alString;
    String header;

    public OfferItemAdapter(Context context, ArrayList<String> result,String header) {
        this.context = context;
        alString = result;
        this.header = header;
    }

    @Override
    public OfferItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_offer_item, parent, false);
        return new OfferItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OfferItemViewHolder holder, int position) {
        if (position == 0 && !header.equals("")) {
            holder.txtHeader.setVisibility(View.VISIBLE);
            holder.txtHeader.setText(header);
        } else {
            holder.txtHeader.setVisibility(View.GONE);
        }

        holder.txtOfferItem.setText(context.getResources().getString(R.string.odaDiamond)+" "+alString.get(position));
    }

    @Override
    public int getItemCount() {
        return alString.size();
    }

    class OfferItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtHeader, txtOfferItem;

        public OfferItemViewHolder(View itemView) {
            super(itemView);

            txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);
            txtOfferItem = (TextView) itemView.findViewById(R.id.txtOfferItem);
        }
    }
}
