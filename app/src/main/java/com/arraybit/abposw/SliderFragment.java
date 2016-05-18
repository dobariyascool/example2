package com.arraybit.abposw;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.BusinessGalleryTran;
import com.squareup.picasso.Picasso;

public class SliderFragment extends Fragment {

    private static final String PIC_URL = "slidepagefragment.picurl";

    public static SliderFragment createInstance(BusinessGalleryTran objBusinessGalleryTran) {
        Bundle arguments = new Bundle();
        arguments.putInt("id", objBusinessGalleryTran.getBusinessGalleryTranId());
        arguments.putString(PIC_URL, objBusinessGalleryTran.getMDImagePhysicalName());
        SliderFragment fragment = new SliderFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        ImageView ivSliderImage = (ImageView) view.findViewById(R.id.ivGalleryImage);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String url = arguments.getString(PIC_URL);
            int id = arguments.getInt(String.valueOf("id"));
            assert url != null;
            if (!url.equals("")) {
                ivSliderImage.setTag(id);
                Picasso.with(getActivity()).load(url).into(ivSliderImage);
            }
        }


        return view;
    }

}
