package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.adapter.GalleryAdapter;
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.BusinessGalleryTran;
import com.arraybit.parser.BusinessGalleryJSONParser;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


public class GalleryFragment extends Fragment implements BusinessGalleryJSONParser.BusinessGalleryRequestListener, GalleryAdapter.ImageViewClickListener {

    static ArrayList<BusinessGalleryTran> lstBusinessGalleryTran;
    ProgressDialog progressDialog = new ProgressDialog();
    RecyclerView rvGallery;
    TextView txtMsg;
    GalleryAdapter adapter;
    GridLayoutManager gridLayoutManager;
    int currentPage = 1;

    public GalleryFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.tran_move));
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
            setAllowEnterTransitionOverlap(false);
            setAllowReturnTransitionOverlap(false);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        rvGallery.setVisibility(View.GONE);

        if (Service.CheckNet(getActivity())) {
            currentPage = 1;
            RequestBusinessGallery();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        rvGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                adapter.isItemAnimate = true;
            }
        });

        rvGallery.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                adapter.isItemAnimate = true;
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        RequestBusinessGallery();
                    } else {
                        Globals.ShowSnackBar(rvGallery, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
        return view;
    }


    @Override
    public void BusinessGalleryResponse(ArrayList<BusinessGalleryTran> alBusinessGalleryTran) {
        if (currentPage > 3) {
            progressDialog.dismiss();
        }
        lstBusinessGalleryTran = alBusinessGalleryTran;
        SetGalleryRecyclerView(lstBusinessGalleryTran);
    }

    @Override
    public void ImageOnClick(BusinessGalleryTran objBusinessGalleryTran, View view, String transitionName) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        FullImageViewDialogFragment fullViewDialogFragment = new FullImageViewDialogFragment();
        if (Build.VERSION.SDK_INT >= 21) {

            //fullViewDialogFragment.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
            //fullViewDialogFragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));

            Bundle bundle = new Bundle();
            bundle.putParcelable("BusinessGallery", objBusinessGalleryTran);
            bundle.putString("Element", transitionName);
            fullViewDialogFragment.setArguments(bundle);

            //fragmentTransaction.addSharedElement(view,transitionName);
            //fullViewDialogFragment.show(fragmentTransaction, "");
            //fragmentTransaction.commit();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        }
        //fragmentTransaction.replace(android.R.id.content, fullViewDialogFragment, "fullViewDialogFragment");
        fragmentTransaction.replace(R.id.hotelProfileFragment, fullViewDialogFragment, "fullViewDialogFragment");
        fragmentTransaction.addToBackStack("fullViewDialogFragment");
        fragmentTransaction.addSharedElement(view, transitionName);
        fragmentTransaction.commit();
    }

    //region Private Methods
    private void RequestBusinessGallery() {
        if (progressDialog.isAdded()) {
            progressDialog.dismiss();
        }
        if (currentPage > 3) {
            progressDialog.show(getFragmentManager(), "");
        }
        BusinessGalleryJSONParser objBusinessGalleryJSONParser = new BusinessGalleryJSONParser();
        objBusinessGalleryJSONParser.SelectAllBusinessGalleryTran(this, getActivity(), String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId));
    }

    private void SetGalleryRecyclerView(ArrayList<BusinessGalleryTran> lstBusinessGalleryTran) {
        if (lstBusinessGalleryTran == null) {
            if (currentPage == 1) {
                txtMsg.setVisibility(View.GONE);
                txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
            }
        } else if (lstBusinessGalleryTran.size() == 0) {
            if (currentPage == 1) {
                txtMsg.setVisibility(View.GONE);
                txtMsg.setText(getResources().getString(R.string.MsgSelectFail));
            }
        } else {
            rvGallery.setVisibility(View.VISIBLE);
            txtMsg.setVisibility(View.GONE);
            if (currentPage > 1) {
                adapter.GalleryDataChanged(lstBusinessGalleryTran);
                return;
            } else if (lstBusinessGalleryTran.size() < 10) {
                currentPage += 1;
            }
            adapter = new GalleryAdapter(getActivity(), lstBusinessGalleryTran, this);
            rvGallery.setAdapter(adapter);
            rvGallery.setLayoutManager(gridLayoutManager);
        }
    }
    //endregion

}
