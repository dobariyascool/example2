package com.arraybit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.arraybit.abposw.R;
import com.arraybit.modal.ReviewMaster;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    View view;
    Context context;
    ArrayList<ReviewMaster> alReviewMaster;

    public ReviewAdapter(Context context, ArrayList<ReviewMaster> alReviewMaster) {
        this.context = context;
        this.alReviewMaster = alReviewMaster;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewMaster objReviewMaster = alReviewMaster.get(position);

        holder.rtbReview.setRating((float) objReviewMaster.getStarRating());
        holder.txtReview.setText(objReviewMaster.getReview());
    }

    public void ReviewDataChanged(ArrayList<ReviewMaster> result) {
        alReviewMaster.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alReviewMaster.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        RatingBar rtbReview;
        TextView txtReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            rtbReview = (RatingBar) itemView.findViewById(R.id.rtbReview);
            txtReview = (TextView) itemView.findViewById(R.id.txtReview);
        }
    }
}
