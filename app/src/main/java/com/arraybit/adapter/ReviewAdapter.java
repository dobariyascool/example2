package com.arraybit.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.ReviewMaster;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    View view;
    Context context;
    ArrayList<ReviewMaster> alReviewMaster;
    ReviewListener objReviewListener;
    LayoutInflater layoutInflater;
    double totalReview;

    public ReviewAdapter(Context context, ArrayList<ReviewMaster> alReviewMaster, ReviewListener objReviewListener) {
        this.context = context;
        this.alReviewMaster = alReviewMaster;
        this.layoutInflater = LayoutInflater.from(context);
        this.objReviewListener = objReviewListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = layoutInflater.inflate(R.layout.row_review, parent, false);
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
        SetAverageReview(alReviewMaster);
    }

    private void SetAverageReview(ArrayList<ReviewMaster> alReviewMaster){
        totalReview = 0;
        for(ReviewMaster reviewMaster : alReviewMaster){
            totalReview = totalReview + reviewMaster.getStarRating();
        }
        totalReview = totalReview/alReviewMaster.size();
        if(totalReview > 0) {
            objReviewListener.AverageReview(Globals.dfWithOnePrecision.format(totalReview), String.valueOf(alReviewMaster.size()));
        }
    }

    @Override
    public int getItemCount() {
        return alReviewMaster.size();
    }

    public interface ReviewListener {
        void AverageReview(String average,String totalUser);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        RatingBar rtbReview;
        TextView txtReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            rtbReview = (RatingBar) itemView.findViewById(R.id.rtbReview);
            txtReview = (TextView) itemView.findViewById(R.id.txtReview);

            LayerDrawable stars = (LayerDrawable) rtbReview.getProgressDrawable();
            //2 means fullyselected,1 means partiallyselected,0 means notselected
            stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
