package com.arraybit.abposw;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.OrderAdapter;
import com.arraybit.global.Globals;
import com.arraybit.modal.OrderMaster;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class YourOrderFragment extends Fragment {

    RecyclerView rvOrder;
    LinearLayout errorlayout;
    LinearLayoutManager linearLayoutManager;
    OrderAdapter OrderAdapter;
    ArrayList<OrderMaster>alOrderMaster;
    ProgressDialog progressDialog = new ProgressDialog();

    public YourOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_order, container, false);

        android.support.v7.widget.Toolbar app_bar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_your_order));
        setHasOptionsMenu(true);


        rvOrder = (RecyclerView) view.findViewById(R.id.rvOrder);
       /* headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);*/
        errorlayout = (LinearLayout) view.findViewById(R.id.errorLayout);
      /*  headerLayout.setVisibility(View.GONE);*/
        linearLayoutManager = new LinearLayoutManager(getActivity());

        OrderAdapter = new OrderAdapter(alOrderMaster,getActivity());
        rvOrder.setAdapter(OrderAdapter);
        rvOrder.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    private void RequestOrderMasterOrderItem(){
        progressDialog.show(getActivity().getSupportFragmentManager(),"");

    }
}
