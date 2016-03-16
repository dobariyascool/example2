package com.arraybit.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arraybit.abposw.R;
import com.arraybit.global.Globals;
import com.arraybit.modal.BusinessGalleryTran;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    public boolean isItemAnimate;
    Context context;
    ArrayList<BusinessGalleryTran> alBusinessGalleryTran;
    View view;
    int previousPosition;
    ImageViewClickListener objImageViewClickListener;
    private LayoutInflater inflater;

    public GalleryAdapter(Context context, ArrayList<BusinessGalleryTran> alBusinessGalleryTran,ImageViewClickListener objImageViewClickListener) {
        this.context = context;
        this.alBusinessGalleryTran = alBusinessGalleryTran;
        inflater = LayoutInflater.from(context);
        this.objImageViewClickListener = objImageViewClickListener;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        BusinessGalleryTran current = alBusinessGalleryTran.get(position);
        if (current.getSMImagePhysicalName() != null) {
            Picasso.with(holder.ivGalleryImage.getContext()).load(current.getSMImagePhysicalName()).into(holder.ivGalleryImage);
        }

        //holder animation
        if (this.isItemAnimate) {
            if (position > previousPosition) {
                Globals.SetItemAnimator(holder);
            }
            previousPosition = position;
        }
        if(Build.VERSION.SDK_INT >=21) {
            holder.ivGalleryImage.setTransitionName("ImageTranstion" +
                    ""+current.getBusinessGalleryTranId());
        }
    }

    @Override
    public int getItemCount() {
        return alBusinessGalleryTran.size();
    }

    public void GalleryDataChanged(ArrayList<BusinessGalleryTran> result) {
        alBusinessGalleryTran.addAll(result);
        isItemAnimate = false;
        notifyDataSetChanged();
    }

    public interface ImageViewClickListener{
        void ImageOnClick(BusinessGalleryTran objBusinessGalleryTran,View view,String transitionName);
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        TextView txtGalleryTitle;
        ImageView ivGalleryImage;
        LinearLayout galleryLayout;

        public GalleryViewHolder(View itemView) {
            super(itemView);

            ivGalleryImage = (ImageView) itemView.findViewById(R.id.ivGalleryImage);

            galleryLayout  = (LinearLayout) itemView.findViewById(R.id.galleryLayout);

            ivGalleryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        objImageViewClickListener.ImageOnClick(alBusinessGalleryTran.get(getAdapterPosition()),v,v.getTransitionName());
                    }else{
                        objImageViewClickListener.ImageOnClick(alBusinessGalleryTran.get(getAdapterPosition()),null,null);
                    }
                }
            });
        }
    }
}
