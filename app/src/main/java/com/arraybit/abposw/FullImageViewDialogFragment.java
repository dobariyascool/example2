package com.arraybit.abposw;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.BusinessGalleryTran;
import com.squareup.picasso.Picasso;


public class FullImageViewDialogFragment extends Fragment {

    BusinessGalleryTran objBusinessGalleryTran;
    String transitionName;

    public FullImageViewDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.tran_move));
            setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
            //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_image_view_dialog, container, false);

        final ImageView ivFullGalleryImage = (ImageView) view.findViewById(R.id.ivFullGalleryImage);
        //ivFullGalleryImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in));
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable("BusinessGallery") != null) {
            objBusinessGalleryTran = bundle.getParcelable("BusinessGallery");
            transitionName = bundle.getString("Element");

            Picasso.with(getActivity()).load(objBusinessGalleryTran.getXLImagePhysicalName()).into(ivFullGalleryImage);

        }

        ivFullGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            view.setTransitionName(transitionName);
        }
        return view;
    }

}
