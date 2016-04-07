package com.arraybit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.OfferMaster;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    public boolean isItemAnimate = false;
    View view;
    Context context;
    ArrayList<OfferMaster> alOfferMaster;
    int previousPosition;

    public OfferAdapter(Context context, ArrayList<OfferMaster> result) {
        this.context = context;
        alOfferMaster = result;
    }


    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OfferViewHolder holder, int position) {
        OfferMaster objOfferMaster = alOfferMaster.get(position);
        holder.txtOfferTitle.setText(objOfferMaster.getOfferTitle());
        if (objOfferMaster.getOfferContent().equals("null") || objOfferMaster.getOfferContent().equals("")) {
            holder.txtOfferContent.setVisibility(View.GONE);
        } else {
            holder.txtOfferContent.setVisibility(View.VISIBLE);
            holder.txtOfferContent.setText(objOfferMaster.getOfferContent());
        }
        holder.txtOfferExpiredDate.setText(objOfferMaster.getToDate());
        if (!objOfferMaster.getImagePhysicalName().equals("")) {
            Picasso.with(holder.ivOffer.getContext()).load(objOfferMaster.getImagePhysicalName()).into(holder.ivOffer);
        }

        //holder animation
        if (isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return alOfferMaster.size();
    }

    public void OffersDataChanged(ArrayList<OfferMaster> result) {
        alOfferMaster.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView txtOfferTitle, txtOfferContent,txtOfferExpiredDate;
        ImageView ivOffer;
        CardView cvOffer;
        LinearLayout titleLayout;

        public OfferViewHolder(View itemView) {
            super(itemView);

            titleLayout = (LinearLayout) itemView.findViewById(R.id.titleLayout);

            cvOffer = (CardView) itemView.findViewById(R.id.cvOffer);

            ivOffer = (ImageView) itemView.findViewById(R.id.ivOffer);

            txtOfferTitle = (TextView) itemView.findViewById(R.id.txtOfferTitle);
            txtOfferContent = (TextView) itemView.findViewById(R.id.txtOfferContent);
            txtOfferExpiredDate=(TextView)itemView.findViewById(R.id.txtOfferExpiredDate);

            cvOffer.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RtlHardcoded")
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
