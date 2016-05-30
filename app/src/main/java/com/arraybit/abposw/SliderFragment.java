package com.arraybit.abposw;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.global.Globals;
import com.arraybit.modal.BannerMaster;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

public class SliderFragment extends Fragment {

    BannerMaster objBannerMaster;

    public static SliderFragment createInstance(BannerMaster objBannerMaster) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("BannerMaster", objBannerMaster);
        SliderFragment fragment = new SliderFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        ImageView ivGalleryImage = (ImageView) view.findViewById(R.id.ivGalleryImage);
        TextView txtBannerTitle = (TextView) view.findViewById(R.id.txtBannerTitle);
        TextView txtBannerDescription = (TextView) view.findViewById(R.id.txtBannerDescription);

        Bundle arguments = getArguments();
        if (arguments != null) {
            objBannerMaster = arguments.getParcelable("BannerMaster");
            if (objBannerMaster != null && objBannerMaster.getLGImageName() != null && !objBannerMaster.getLGImageName().equals("")) {
                ivGalleryImage.setTag(objBannerMaster.getBannerMasterId());
                Picasso.with(getActivity()).load(objBannerMaster.getLGImageName()).into(ivGalleryImage);
                if (objBannerMaster.getBannerTitle() != null && !objBannerMaster.getBannerTitle().equals("")) {
                    txtBannerTitle.setText(objBannerMaster.getBannerTitle());
                }
                if (objBannerMaster.getBannerDescription() != null && !objBannerMaster.getBannerDescription().equals("")) {
                    txtBannerDescription.setText(objBannerMaster.getBannerDescription());
                }
            }
        }

        ivGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objBannerMaster.getType() != 0 && objBannerMaster.getID() != 0) {
                    if(objBannerMaster.getType() == Globals.BannerType.Item.getValue()) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("isBannerClick", true);
                        intent.putExtra("ItemMasterId", objBannerMaster.getID());
                        getActivity().startActivityForResult(intent, 0);
                    }else if(objBannerMaster.getType() == Globals.BannerType.Offer.getValue()){
                        Intent intent = new Intent(getActivity(),OfferDetailActivity.class);
                        intent.putExtra("OfferMasterId",objBannerMaster.getID());
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

}
