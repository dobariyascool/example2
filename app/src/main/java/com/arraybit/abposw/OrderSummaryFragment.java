package com.arraybit.abposw;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;

@SuppressWarnings("ConstantConditions")
public class OrderSummaryFragment extends Fragment implements View.OnClickListener{


    public OrderSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        //app_bar
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
            app_bar.setTitle(getActivity().getResources().getString(R.string.title_order_summary_fragment));
        }
        //end

        setHasOptionsMenu(true);

        Button btnAddMore = (Button)view.findViewById(R.id.btnAddMore);

        btnAddMore.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAddMore){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ShowMessage", false);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
